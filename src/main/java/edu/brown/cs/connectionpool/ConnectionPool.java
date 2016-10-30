package edu.brown.cs.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * ConnectionPool that keeps track of a pool of connections to a Database and
 * hands them out to users. The ConnectionPool implements AutoCloseable, which
 * means we can use it in try with resources blocks of code.
 */
public class ConnectionPool implements AutoCloseable {
  private String url = null;
  // @foff
  private HashMap<Connection, Integer> myConnections = new HashMap<Connection, Integer>();

  // @fon
  /**
   * Constructor for a ConnectionPool. Takes in the URL of the database we're
   * managing connections to.
   * @param myURL A String that represents the URL to the database we're
   *          connecting to.
   */
  public ConnectionPool(String myURL) {
    try {
      Class.forName("org.sqlite.JDBC");
      setDatabase(myURL);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets the URL of the database that we're currently connected to. If we
   * change the URL, it closes all the connections and wipes the HashMap.
   * @param myURL A String that represents the URL of the database we're
   *          connecting to.
   */
  public void setDatabase(String myURL) {
    this.url = "jdbc:sqlite:" + myURL;
    this.close();
    this.myConnections = new HashMap<Connection, Integer>();
  }

  /**
   * Returns a connection from the ConnectionPool. Checks the HashMap of
   * connections for a connection that currently isn't being used and returns
   * it. If no connections are open, the ConnectionPool opens another one.
   * @return A Connection to the Database that ConnectionPool is connected to.
   */
  public Connection getConnection() {
    try {
      for (Map.Entry<Connection, Integer> entry : myConnections.entrySet()) {
        if (!entry.getKey().isClosed() && entry.getValue() == 1) {
          myConnections.put(entry.getKey(), 0);
          return entry.getKey();
        }
      }
      Connection conn = DriverManager.getConnection(url);
      try (Statement state = conn.createStatement()) {
        state.executeUpdate("PRAGMA foreign_keys = ON;");
        myConnections.put(conn, 0);
      }
      return conn;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Sets a connection from the connection pool back to open. Doesn't close the
   * connection! It keeps the connection open and simply makes it available to
   * be returned.
   * @param conn A Connection that represents the connection we're making
   *          available again.
   */
  public void destroy(Connection conn) {
    if (myConnections.containsKey(conn)) {
      myConnections.put(conn, 1);
    }
  }

  @Override
  public void close() {
    for (Connection c : myConnections.keySet()) {
      try {
        c.close();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
