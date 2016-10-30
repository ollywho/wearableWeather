package edu.brown.cs.wearableWeather;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.connectionpool.ConnectionPool;
import freemarker.template.Configuration;

public class Main {

  private String[] args;
  private ConnectionPool myConnectionPool;
  private String db;
  private DbQueryer dbq;
  private DbUpdater dbu;
  private int user;
  private static Suggestions sugg;

  private final static Gson GSON = new Gson();

  /**
   * Main run when program starts.
   * @param args arguments
   */
  private Main(String[] args) {
    this.args = args;
  }

  /**
   * Creates new main class when program executes.
   * @param args arguments
   * @throws SQLException
   */
  public static void main(String[] args) throws SQLException {
    new Main(args).run();
  }

  /**
   * Run when program executes
   * @throws SQLException
   */
  private void run() throws SQLException {
    OptionParser parser = new OptionParser();
    parser.accepts("cli");

    parser.allowsUnrecognizedOptions();
    OptionSet options = parser.parse(args);
    @SuppressWarnings("unchecked")
    List<String> nonOpts = (List<String>) options.nonOptionArguments();
    db = nonOpts.get(0);
    System.out.println(db);
    try (ConnectionPool myConnections = new ConnectionPool(db)) {
      this.myConnectionPool = myConnections;
    }
    dbq = new DbQueryer(myConnectionPool);
    dbu = new DbUpdater(myConnectionPool);
    if (options.has("cli")) { // run command-line interface
      String username = nonOpts.get(1);
      String password = nonOpts.get(2);
      if (dbu.checkLogin(username, password)) { // check login
        user = dbu.getUserID(username);
        runCLI();
      } else {
        System.err.println("Incorrect username and/or password");
        System.exit(1);
      }
    } else { //run GUI by default
      runSparkServer();
    }
  }

  private void runCLI() throws SQLException {
    Constants.fillThresholds(user, dbq);
    System.out.println("User #" + String.valueOf(user)
        + " loaded with thresholds: " + Constants.coldTemp + ","
        + Constants.coolTemp + "," + Constants.comfortableTemp + ","
        + Constants.warmTemp + "," + Constants.hotTemp);
    System.out.println("Ready:");
    Suggestions suggestions = new Suggestions(user, dbq);
    BufferedReader inputScan;
    List<Garment[]> outfits;
    String[] inputList;
    try {
      inputScan = new BufferedReader(
          new InputStreamReader(System.in, "UTF-8"));
      String input;
      try {
        // begin scanning input
        input = inputScan.readLine();
        // parameters for REPL loop
        while (input != null && !input.equals("") && !input.equals("\n")) {
          inputList = input.split(" ");
          if (inputList[0].equals("add")) { // add garment to database
            try {
              dbu.addGarment(inputList[1], inputList[2], inputList[3],
                  inputList[4], Integer.valueOf(inputList[5]), inputList[6],
                  Integer.valueOf(inputList[7]), user);
              System.out.println("Garment added to wardrobe");
            } catch (NumberFormatException e) {
              e.printStackTrace();
            } catch (SQLException e) {
              System.err.println("ERROR: Could not add garment to wardrobe");
            }
          } else if (inputList[0].equals("remove")) { // remove garment from database
            try {
              dbu.removeGarment(Integer.valueOf(inputList[1]), user);
              System.out.println("Garment removed from wardrobe");
            } catch (NumberFormatException e) {
              e.printStackTrace();
            } catch (SQLException e) {
              System.err
                  .println("ERROR: Could not remove garment from wardrobe");
            }
          } else if (inputList[0].equals("newuser")) { // add a new user to the database
            if (dbu.usernameTaken(inputList[1])) {
              System.out.println("Username taken");
            } else {
              dbu.addUser(inputList[1], inputList[2]);
              System.out.println("User added");
            }
          } else if (inputList[0].equals("removeuser")) { // remove a user from the database
            dbu.removeUser(user, inputList[1]);
          } else if (inputList[0].equals("switch")) {
            if (!dbu.checkLogin(inputList[1], inputList[2])) {
              System.out.println("Incorrect username and/or password");
            } else {
              user = dbu.getUserID(inputList[1]);
              suggestions = new Suggestions(user, dbq);
              System.out.println("Logged in as " + inputList[1]);
            }
          } else if (inputList[0].equals("update")) { // update user temperature thresholds
            dbu.changeThresholds(user, Integer.valueOf(inputList[1]));
            System.out.println("Database updated");
          } else if (inputList[0].equals("edit")) { // edit a garment in the database
            try {
              dbu.updateGarment(Integer.valueOf(inputList[1]), inputList[2],
                  inputList[3], inputList[4], inputList[5],
                  Integer.valueOf(inputList[6]), inputList[7],
                  Integer.valueOf(inputList[8]), user);
              System.out.println("Garment edited");
            } catch (NumberFormatException e) {
              e.printStackTrace();
            } catch (SQLException e) {
              System.err.println("ERROR: Could not edit garment");
            }
          } else { // get clothing suggestions
            int temp = Integer.parseInt(inputList[0]);
            double precip = Double.parseDouble(inputList[1]);
            int formality = Integer.parseInt(inputList[2]);
            outfits = suggestions.getOutfits(temp, precip, formality);
            System.out.println("Outfits:");
            for (Garment[] g : outfits) {
              System.out.println(g[0].getName() + " + " + g[1].getName());
            }
          }
          // read next line
          input = inputScan.readLine();
        }
      } catch (IOException e) {
        System.out.println("ERROR: IO Exception");
      } catch (NullPointerException e) {
        System.exit(1);
      }
    } catch (UnsupportedEncodingException e1) {
      System.out.println("ERROR: InputStream unsupported encoding to UTF-8");
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer() {
    Spark.externalStaticFileLocation("src/main/resources/static");
    Spark.exception(Exception.class, new ExceptionPrinter());

    FreeMarkerEngine freeMarker = createEngine();
    // Setup Spark Routes
    Spark.get("/login", new LoginHandler(), freeMarker);
    Spark.post("/checklogin", new CheckLoginHandler());
    Spark.post("/newuser", new NewUserHandler());
    Spark.get("/home", new HomeHandler(), freeMarker);
    Spark.post("/feedback", new FeedbackHandler());
    Spark.get("/wardrobe", new WardHandler(), freeMarker);
    Spark.post("/suggestion", new SuggHandler());
    Spark.post("/add", new AddHandler());
    Spark.post("/remove", new RemoveHandler());
    Spark.get("/garments", new GarmentsHandler());

  }

  private class LoginHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      Map<String, Object> variables = ImmutableMap.of("title",
          "Wearable Weather");
      return new ModelAndView(variables, "login.ftl");
    }

  }

  private class NewUserHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      boolean nameTaken = false;
      try {
        nameTaken = dbu.usernameTaken(username);
      } catch (SQLException e) {
        System.err.println("ERROR: checking username failed");
        e.printStackTrace();
      }
      if (!nameTaken) {
        try {
          dbu.addUser(username, password);
          user = dbu.getUserID(username);
        } catch (SQLException e) {
          System.err.println("ERROR: could not add new user");
          e.printStackTrace();
        }
        Constants.resetThresholds();
        sugg = new Suggestions(user, dbq);
      }
      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("result", nameTaken).build();
      return GSON.toJson(variables);
    }

  }

  private class CheckLoginHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      String username = qm.value("username");
      String password = qm.value("password");
      boolean valid = false;
      try {
        valid = dbu.checkLogin(username, password);
      } catch (SQLException e) {
        System.err.println("ERROR: Login failed");
        e.printStackTrace();
      }
      if (valid) {
        try {
          user = dbu.getUserID(username);
          Constants.fillThresholds(user, dbq);
          sugg = new Suggestions(user, dbq);
        } catch (SQLException e) {
          System.err.println("ERROR: Retrieving user ID failed");
          e.printStackTrace();
        }
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("is_valid", valid).build();
      return GSON.toJson(variables);
    }

  }

  /**
   * @author rwdodd
   */
  private class HomeHandler implements TemplateViewRoute {
    /*
     * (non-Javadoc)
     * @see spark.TemplateViewRoute#handle(spark.Request, spark.Response)
     */
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Wearable Weather");
      return new ModelAndView(variables, "home.ftl");

    }
  }

  private class FeedbackHandler implements Route {

    @Override
    public Object handle(Request req, Response res) {
      QueryParamsMap qm = req.queryMap();
      int feedback = Integer.parseInt(qm.value("feedback"));
      try {
        dbu.changeThresholds(user, feedback);
      } catch (SQLException e) {
        System.err.println("ERROR: updating thresholds failed");
        e.printStackTrace();
      }

      return null;
    }

  }

  /**
   * @author rwdodd
   */
  private class WardHandler implements TemplateViewRoute {
    /*
     * (non-Javadoc)
     * @see spark.TemplateViewRoute#handle(spark.Request, spark.Response)
     */
    @Override
    public ModelAndView handle(Request req, Response res) {
      Map<String, Object> variables = ImmutableMap.of("title",
          "Wearable Weather");
      return new ModelAndView(variables, "wardrobe.ftl");

    }
  }

  /**
   * @author rwdodd
   */
  private class GarmentsHandler implements Route {
    /*
     * (non-Javadoc)
     * @see spark.TemplateViewRoute#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request req, final Response res) {

      List<Garment> garb = dbq.getAllClothes(user);

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("garb", garb).build();

      return GSON.toJson(variables);

    }
  }

  /**
   * @author rwdodd
   */
  private class SuggHandler implements Route {
    /*
     * (non-Javadoc)
     * @see spark.TemplateViewRoute#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request req, final Response res) {
      System.out.println("received sugg data");
      QueryParamsMap qm = req.queryMap();

      Integer uv = Integer.parseInt(qm.value("uv"));
      Integer high = Integer.parseInt(qm.value("high"));
      Double precip = Double.parseDouble(qm.value("precip"));
      int formality = Integer.parseInt(qm.value("formality"));
      System.out.println("UV: " + uv);
      System.out.println("High: " + high);
      System.out.println("Precip: " + precip);

      List<Garment[]> garb = sugg.getOutfits(high, precip, formality);

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("garb", garb).build();

      return GSON.toJson(variables);

    }
  }

  /**
   * @author rwdodd
   */
  private class AddHandler implements Route {
    /*
     * (non-Javadoc)
     * @see spark.TemplateViewRoute#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request req, final Response res) {
      System.out.println("received sugg data");
      QueryParamsMap qm = req.queryMap();

      String name = qm.value("name");
      String type = qm.value("type");
      String layer = qm.value("layer");
      String weight = qm.value("weight");
      Integer waterproof = Integer.parseInt(qm.value("waterproof"));
      String color = qm.value("color");
      Integer formality = Integer.parseInt(qm.value("formality"));
      boolean is_success = false;
      try {
        dbu.addGarment(name, type, layer, weight, waterproof, color,
            formality, user);
        is_success = true;
      } catch (SQLException e) {
        e.printStackTrace();
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("is_success", is_success).build();

      return GSON.toJson(variables);

    }
  }

  /**
   * @author rwdodd
   */
  private class RemoveHandler implements Route {
    /*
     * (non-Javadoc)
     * @see spark.TemplateViewRoute#handle(spark.Request, spark.Response)
     */
    @Override
    public Object handle(final Request req, final Response res) {
      System.out.println("received sugg data");
      QueryParamsMap qm = req.queryMap();

      Integer id = Integer.parseInt(qm.value("id"));

      boolean is_success = false;

      try {
        dbu.removeGarment(id, user);
        is_success = true;
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      Map<String, Object> variables = new ImmutableMap.Builder<String, Object>()
          .put("is_success", is_success).build();

      return GSON.toJson(variables);

    }
  }

  /**
   * @author rwdodd
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    private final Integer STATUS = 500;

    /*
     * (non-Javadoc)
     * @see spark.ExceptionHandler#handle(java.lang.Exception, spark.Request,
     * spark.Response)
     */
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(STATUS);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }

}
