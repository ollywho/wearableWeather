package edu.brown.cs.wearableWeather;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WeatherTest {

  @Test
  public void tempRange() {
    assertTrue(Weather.getTempRange(80) == Constants.HOT);
    assertTrue(Weather.getTempRange(75) == Constants.WARM);
    assertTrue(Weather.getTempRange(69) == Constants.COMFORTABLE);
    assertTrue(Weather.getTempRange(57) == Constants.COOL);
    assertTrue(Weather.getTempRange(50) == Constants.COLD);
    assertTrue(Weather.getTempRange(30) == Constants.FRIGID);
  }

  @Test
  public void raining() {
    assertTrue(Weather.isRaining(0.5));
    assertTrue(!Weather.isRaining(0.1));
  }

  @Test
  public void sunny() {
    assertTrue(Weather.isSunny(7));
    assertTrue(!Weather.isSunny(5));
  }

  @Test
  public void freezing() {
    assertTrue(Weather.isFreezing(30));
    assertTrue(!Weather.isFreezing(33));
  }

}
