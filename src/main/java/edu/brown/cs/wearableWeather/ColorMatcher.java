package edu.brown.cs.wearableWeather;

import java.util.HashMap;

/**
 * ColorMatcher class containing an internal hashmap for comparing garments' colors.
 * @author ohu
 *
 */
public class ColorMatcher {

  private final static int NEUTRAL = 0;
  private final static int BOLD = 1;

  private HashMap<String, Integer> matcher;

  /**
   * ColorMatcher constructor.
   */
  public ColorMatcher() {
    matcher = new HashMap<String, Integer>();

    matcher.put("black", NEUTRAL);
    matcher.put("navy", NEUTRAL);
    matcher.put("blue", NEUTRAL);
    matcher.put("brown", NEUTRAL);
    matcher.put("gray", NEUTRAL);
    matcher.put("tan", NEUTRAL);
    matcher.put("white", NEUTRAL);

    matcher.put("green", BOLD);
    matcher.put("burgundy", BOLD);
    matcher.put("orange", BOLD);
    matcher.put("pink", BOLD);
    matcher.put("purple", BOLD);
    matcher.put("yellow", BOLD);
    matcher.put("red", BOLD);
  }

  /**
   * Compares two garments to see if their colors can be paired together.
   * @param g1 a garment
   * @param g2 another garment to match with
   * @return true if the garments can be paired together, false otherwise
   */
  public boolean matches(Garment g1, Garment g2) {
    if (matcher.get(g1.getColor()) == NEUTRAL
        || matcher.get(g2.getColor()) == NEUTRAL) {
      return true;
    }
    return false;

  }

}
