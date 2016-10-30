package edu.brown.cs.wearableWeather;

import java.util.List;

/**
 * Constants class used throughout the algorithm.
 * @author ohu
 *
 */
public abstract class Constants {

  public final static int FRIGID = 0;
  public final static int COLD = 1;
  public final static int COOL = 2;
  public final static int COMFORTABLE = 3;
  public final static int WARM = 4;
  public final static int HOT = 5;

  public static int hotTemp = 78;
  public static int warmTemp = 69;
  public static int comfortableTemp = 61;
  public static int coolTemp = 53;
  public static int coldTemp = 45;
  public final static int FREEZINGTEMP = 32;
  
  public final static int TEMPFLOOR = 37;
  public final static int TEMPCEILING = 86;

  /**
   * Fills the threshold temperatures from the user database.
   * @param id of the user to query
   * @param db the database querier used to query the database
   */
  public static void fillThresholds(int id, DbQueryer db) {
    List<Integer> thresholds = db.queryThresholds(id);
    Constants.coldTemp = thresholds.get(0);
    Constants.coolTemp = thresholds.get(1);
    Constants.comfortableTemp = thresholds.get(2);
    Constants.warmTemp = thresholds.get(3);
    Constants.hotTemp = thresholds.get(4);
  }

  /**
   * Resets the thresholds to the default values.
   */
  public static void resetThresholds() {
    Constants.coldTemp = 45;
    Constants.coolTemp = 53;
    Constants.comfortableTemp = 61;
    Constants.warmTemp = 69;
    Constants.hotTemp = 78;
  }

}
