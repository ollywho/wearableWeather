package edu.brown.cs.wearableWeather;

public abstract class Weather {

  private final static int SUNNY = 6;
  private final static double RAIN = 0.3;

  public Weather() {

  }

  public static int getTempRange(int temp) {
    if (temp > Constants.hotTemp) {
      return Constants.HOT;
    }
    if (temp > Constants.warmTemp) {
      return Constants.WARM;
    }
    if (temp > Constants.comfortableTemp) {
      return Constants.COMFORTABLE;
    }
    if (temp > Constants.coolTemp) {
      return Constants.COOL;
    }
    if (temp > Constants.coldTemp) {
      return Constants.COLD;
    }
    return Constants.FRIGID;
  }

  public static boolean isRaining(double precip) {
    return precip >= RAIN;
  }

  public static boolean isSunny(int uv) {
    return uv >= SUNNY;
  }

  public static boolean isFreezing(int temp) {
    return temp <= Constants.FREEZINGTEMP;
  }

}
