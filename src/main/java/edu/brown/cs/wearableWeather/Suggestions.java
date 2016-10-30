package edu.brown.cs.wearableWeather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Suggestions {

  private DbQueryer dbq;
  private int user;
  private ColorMatcher color;

  public Suggestions(int user, DbQueryer dbq) {
    this.dbq = dbq;
    this.user = user;
    color = new ColorMatcher();
  }

  public List<Top> getTops(int temp, double precip, int formality) {
    int tempRange = Weather.getTempRange(temp);
    boolean waterproof = false;
    List<Top> tops;
    if (!Weather.isFreezing(temp) && tempRange <= Constants.COOL
        && Weather.isRaining(precip)) {
      waterproof = true;
    }
    if (tempRange == Constants.HOT) {
      tops = dbq.queryTops(user, "base", "short", waterproof, formality);
    } else if (tempRange == Constants.WARM) {
      // query database for short base
      tops = dbq.queryTops(user, "base", "short", waterproof, formality);
    } else if (tempRange == Constants.COMFORTABLE) {
      // query database for long base
      tops = dbq.queryTops(user, "base", "long", waterproof, formality);
    } else if (tempRange == Constants.COOL) {
      if (waterproof) {
        // query for light waterproof outer
        tops = dbq.queryTops(user, "outer", "light", waterproof, formality);
      } else {
        // query database for mid
        tops = dbq.queryTops(user, "mid", "mid", waterproof, formality);
      }

    } else if (tempRange == Constants.COLD) {
      // query database for light outer
      tops = dbq.queryTops(user, "outer", "light", waterproof, formality);
    } else {
      tops = dbq.queryTops(user, "outer", "heavy", waterproof, formality); // heavy
      // outer
    }
    Collections.shuffle(tops);
    return tops;
  }

  public List<Bottom> getBottoms(int temp, int formality) {
    List<Bottom> bottoms;
    if (Weather.getTempRange(temp) == Constants.HOT && formality <= 3) {
      // query database for shorts
      bottoms = dbq.queryBottoms(user, "short", 0);
    } else {
      bottoms = dbq.queryBottoms(user, "long", formality); // pants
    }
    Collections.shuffle(bottoms);
    return bottoms;
  }

  public List<Garment[]> getOutfits(int temp, double precip, int formality) {
    List<Top> tops = this.getTops(temp, precip, formality);
    List<Bottom> bottoms = this.getBottoms(temp, formality);
    List<Garment[]> outfits = new ArrayList<Garment[]>();
    if (tops.isEmpty()) { // dumb query if specific query yields no results
      tops = this.getTops(temp, 0.0, 0);
      Collections.sort(tops, new FormalityComparator(formality));
    }
    if (bottoms.isEmpty()) { // dumb query
      bottoms = this.getBottoms(temp, 0);
      Collections.sort(bottoms, new FormalityComparator(formality));
    }
    for (Top t : tops) {
      for (Bottom b : bottoms) {
        if (color.matches(t, b)) {
          Garment[] garment = new Garment[2];
          garment[0] = t;
          garment[1] = b;
          outfits.add(garment);
        }
      }
    }
    if (outfits.size() == 0) { // no color matching if user doesn't have proper
                               // outfits
      for (Top t : tops) {
        for (Bottom b : bottoms) {
          Garment[] garment = new Garment[2];
          garment[0] = t;
          garment[1] = b;
          outfits.add(garment);
        }
      }
    }
    return outfits;
  }

}