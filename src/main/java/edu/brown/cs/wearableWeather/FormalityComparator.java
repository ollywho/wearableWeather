package edu.brown.cs.wearableWeather;

import java.util.Comparator;

/**
 * Comparator that sorts a list of garments based on how close they are to the desired formality level.
 * @author ohu
 *
 */
public class FormalityComparator implements Comparator<Garment> {

  private int formality;

  /**
   * FormalityComparator constructor.
   * @param formality desired formality level 1-5
   */
  public FormalityComparator(int formality) {
    this.formality = formality;
  }

  @Override
  public int compare(Garment g1, Garment g2) {
    if (Math.abs(g1.getFormality() - formality) < Math.abs(g2.getFormality()
        - formality)) {
      return -1;
    } else if (Math.abs(g1.getFormality() - formality) > Math.abs(g2
        .getFormality() - formality)) {
      return 1;
    }
    return 0;
  }

}
