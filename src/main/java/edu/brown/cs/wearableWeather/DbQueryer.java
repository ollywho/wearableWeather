package edu.brown.cs.wearableWeather;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.brown.cs.connectionpool.ConnectionPool;

/**
 * DbQueryer class which contains the SQLite to query the database for specific tops, bottoms, user temperature thresholds.
 * @author ohu
 *
 */
public class DbQueryer {

  private ConnectionPool myConnectionPool;

  /**
   * DbQueryer constructor.
   * @param connectionPool a pool of connections to the database
   */
  public DbQueryer(ConnectionPool connectionPool) {
    myConnectionPool = connectionPool;
  }

  /**
   * Queries the database for tops.
   * @param user to query for
   * @param layer to query for
   * @param weight to query for
   * @param waterproof if waterproof tops are desired
   * @param formality to query for
   * @return a list of tops
   */
  public List<Top> queryTops(int user, String layer, String weight,
      boolean waterproof, int formality) {
    List<Top> tops = new ArrayList<>();
    Connection conn = myConnectionPool.getConnection();
    String water = "";
    String formal = " AND formality < 5;";
    if (waterproof) {
      water = " AND waterproof = 1";
    }
    if (formality != 0) {
      formal = " AND formality = " + Integer.toString(formality) + ";";
    }
    try (PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM clothes WHERE user_id = ? AND type = 'top' AND layer = ? AND weight = ?"
            + water + formal)) {
      ps.setInt(1, user);
      ps.setString(2, layer);
      ps.setString(3, weight);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt(1);
          String name = rs.getString(2);
          String color = rs.getString(7);
          int dressiness = rs.getInt(8);
          tops.add(new Top(id, name, layer, weight, waterproof, color,
              dressiness, user));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return tops;
  }

  /**
   * Queries the database for bottoms.
   * @param user to query for
   * @param weight to query for
   * @param formality to query for
   * @return a list of bottoms
   */
  public List<Bottom> queryBottoms(int user, String weight, int formality) {
    List<Bottom> bottoms = new ArrayList<>();
    Connection conn = myConnectionPool.getConnection();
    String formal = " AND formality < 5;";
    if (formality != 0) {
      formal = " AND formality > " + Integer.toString(formality - 2)
          + " AND formality < " + Integer.toString(formality + 2) + ";";
    }
    try (PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM clothes WHERE user_id = ? AND type = 'bottom' AND weight = ?"
            + formal)) {
      ps.setInt(1, user);
      ps.setString(2, weight);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt(1);
          String name = rs.getString(2);
          String color = rs.getString(7);
          int dressiness = rs.getInt(8);
          bottoms.add(new Bottom(id, name, weight, color, dressiness, user));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return bottoms;
  }

  /**
   * Queries the database for user temperature thresholds.
   * @param id of user to query for
   * @return list containing user's temperature thresholds
   */
  public List<Integer> queryThresholds(int id) {
    List<Integer> thresholds = new ArrayList<>();
    Connection conn = myConnectionPool.getConnection();
    try (PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM users WHERE id = ?;")) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        rs.next();
        for (int col = 4; col < 9; col++) {
          thresholds.add(rs.getInt(col));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    Collections.sort(thresholds);
    return thresholds;
  }

  /**
   * Returns all clothing belonging to a specific user.
   * @param user to query for
   * @return list of all garments belonging to specified user
   */
  public List<Garment> getAllClothes(int user) {
    List<Garment> clothes = new ArrayList<>();
    Connection conn = myConnectionPool.getConnection();
    try (PreparedStatement ps = conn
        .prepareStatement("SELECT * FROM clothes WHERE user_id = ?;")) {
      ps.setInt(1, user);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int id = rs.getInt(1);
          String name = rs.getString(2);
          String type = rs.getString(3);
          String layer = rs.getString(4);
          String weight = rs.getString(5);
          boolean waterproof = (rs.getInt(6) != 0);
          String color = rs.getString(7);
          int formality = rs.getInt(8);
          int userId = rs.getInt(9);
          if (type.equals("top")) {
            clothes.add(new Top(id, name, layer, weight, waterproof, color,
                formality, userId));
          } else if (type.equals("bottom")) {
            clothes
                .add(new Bottom(id, name, weight, color, formality, userId));
          }
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return clothes;
  }
}
