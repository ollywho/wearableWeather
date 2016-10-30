package edu.brown.cs.wearableWeather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.brown.cs.connectionpool.ConnectionPool;

/**
 * DbUpdater class used to update the database.
 * @author ohu
 *
 */
public class DbUpdater {

  private ConnectionPool myConnectionPool;
  private Connection conn;

  /**
   * DbUpdater contructor.
   * @param connectionPool a pool of connections to the database
   */
  public DbUpdater(ConnectionPool connectionPool) {
    myConnectionPool = connectionPool;
    conn = myConnectionPool.getConnection();
  }

  /**
   * Adds a garment to the database.
   * @param name of garment
   * @param type top or bottom
   * @param layer base, mid, or outer for tops
   * @param weight of garment
   * @param waterproof whether garment is waterproof
   * @param color of garment
   * @param formality 1-5 of garment
   * @param user garment belongs to
   * @throws SQLException
   */
  public void addGarment(String name, String type, String layer,
      String weight, int waterproof, String color, int formality, int user)
      throws SQLException {
    String schema = "SELECT MAX(id) FROM clothes;";
    PreparedStatement ps = conn.prepareStatement(schema);
    ResultSet rs = ps.executeQuery();
    int newId = rs.getInt(1) + 1;
    ps = conn
        .prepareStatement("INSERT INTO clothes VALUES(?,?,?,?,?,?,?,?,?);");
    ps.setInt(1, newId);
    ps.setString(2, name);
    ps.setString(3, type);
    ps.setString(4, layer);
    ps.setString(5, weight);
    ps.setInt(6, waterproof);
    ps.setString(7, color);
    ps.setInt(8, formality);
    ps.setInt(9, user);
    ps.executeUpdate();
  }

  /**
   * Edits an existing garment in the database.
   * @param id of garment
   * @param name to set to
   * @param type to set to
   * @param layer to set to
   * @param weight to set to
   * @param waterproof to set to
   * @param color to set to
   * @param formality 1-5 to set to
   * @param user garment belongs to
   * @throws SQLException
   */
  public void updateGarment(int id, String name, String type, String layer,
      String weight, int waterproof, String color, int formality, int user)
      throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("UPDATE clothes SET name = ?,type = ?,layer = ?,weight = ?,waterproof = ?,color = ?,formality = ? WHERE user_id = ? AND id = ?;");
    ps.setString(1, name);
    ps.setString(2, type);
    ps.setString(3, layer);
    ps.setString(4, weight);
    ps.setInt(5, waterproof);
    ps.setString(6, color);
    ps.setInt(7, formality);
    ps.setInt(8, user);
    ps.setInt(9, id);
    ps.executeUpdate();
  }

  /**
   * Removes a garment from the database.
   * @param id of the garment
   * @param user that the garment belongs to
   * @throws SQLException
   */
  public void removeGarment(int id, int user) throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("DELETE FROM clothes WHERE id = ? AND user_id = ?;");
    ps.setInt(1, id);
    ps.setInt(2, user);
    ps.executeUpdate();
  }

  /**
   * Adds a user to the database.
   * @param username of new user
   * @param password of new user
   * @throws SQLException
   */
  public void addUser(String username, String password) throws SQLException {
    Constants.resetThresholds();
    String schema = "SELECT MAX(id) FROM users;";
    PreparedStatement ps = conn.prepareStatement(schema);
    ResultSet rs = ps.executeQuery();
    int id = rs.getInt(1) + 1;
    ps = conn.prepareStatement("INSERT INTO users VALUES(?,?,?,?,?,?,?,?);");
    ps.setInt(1, id);
    ps.setString(2, username);
    ps.setString(3, password);
    ps.setInt(4, Constants.coldTemp);
    ps.setInt(5, Constants.coolTemp);
    ps.setInt(6, Constants.comfortableTemp);
    ps.setInt(7, Constants.warmTemp);
    ps.setInt(8, Constants.hotTemp);
    ps.executeUpdate();
  }

  /**
   * Removes a user from the database.
   * @param id of user to remove
   * @param password of user to remove
   * @throws SQLException
   */
  public void removeUser(int id, String password) throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("DELETE FROM users WHERE id = ? AND password = ?;");
    ps.setInt(1, id);
    ps.setString(2, password);
    ps.executeUpdate();
  }

  /**
   * Checks if a username is already taken.
   * @param username to check for
   * @return true if username is taken, false otherwise
   * @throws SQLException
   */
  public boolean usernameTaken(String username) throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM users WHERE username = ?;");
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }

  /**
   * Checks the validity of a username/password combination.
   * @param username entered
   * @param password entered
   * @return true if username and password are correct, false otherwise
   * @throws SQLException
   */
  public boolean checkLogin(String username, String password)
      throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?;");
    ps.setString(1, username);
    ps.setString(2, password);
    ResultSet rs = ps.executeQuery();
    return rs.next();
  }

  /**
   * Returns a user's unique id.
   * @param username to query for
   * @return id of user
   * @throws SQLException
   */
  public int getUserID(String username) throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM users WHERE username = ?;");
    ps.setString(1, username);
    ResultSet rs = ps.executeQuery();
    return rs.getInt(1);
  }

  /**
   * Changes the temperature thresholds of a given user.
   * @param userId to update
   * @param direction 1 to move threholds down, 0 to move threholds up
   * @throws SQLException
   */
  public void changeThresholds(int userId, int direction) throws SQLException {
    PreparedStatement ps = conn
        .prepareStatement("UPDATE users SET cold = ?,cool = ?,comfortable = ?,warm = ?,hot = ? WHERE id = ?");
    if (direction == 1 && Constants.coldTemp > Constants.TEMPFLOOR) {
      // too hot, move thresholds down
      Constants.coldTemp = Constants.coldTemp - 2;
      Constants.coolTemp = Constants.coolTemp - 2;
      Constants.comfortableTemp = Constants.comfortableTemp - 2;
      Constants.warmTemp = Constants.warmTemp - 2;
      Constants.hotTemp = Constants.hotTemp - 2;
    } else if (direction == 0 && Constants.hotTemp < Constants.TEMPCEILING) {
      // too cold, move thresholds up
      Constants.coldTemp = Constants.coldTemp + 2;
      Constants.coolTemp = Constants.coolTemp + 2;
      Constants.comfortableTemp = Constants.comfortableTemp + 2;
      Constants.warmTemp = Constants.warmTemp + 2;
      Constants.hotTemp = Constants.hotTemp + 2;
    }
    ps.setInt(1, Constants.coldTemp);
    ps.setInt(2, Constants.coolTemp);
    ps.setInt(3, Constants.comfortableTemp);
    ps.setInt(4, Constants.warmTemp);
    ps.setInt(5, Constants.hotTemp);
    ps.setInt(6, userId);
    ps.executeUpdate();
  }
}
