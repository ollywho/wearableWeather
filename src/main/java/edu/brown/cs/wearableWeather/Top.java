package edu.brown.cs.wearableWeather;

public class Top implements Garment {

  private int id;
  private String name;
  private String layer;
  private String weight;
  private boolean waterproof;
  private String color;
  private int formality;
  private int user;
  private String type;

  public Top(int id, String name, String layer, String weight,
      boolean waterproof, String color, int formality, int user) {
    this.id = id;
    this.name = name;
    this.layer = layer;
    this.weight = weight;
    this.waterproof = waterproof;
    this.color = color;
    this.formality = formality;
    this.user = user;
    this.type = "top";
  }

  public String getName() {
    return name;
  }

  public String getWeight() {
    return weight;
  }

  public int getUserID() {
    return user;
  }

  public int getID() {
    return id;
  }

  public String getLayer() {
    return layer;
  }

  public String getColor() {
    return color;
  }

  public int getFormality() {
    return formality;
  }

  public boolean isWaterproof() {
    return waterproof;
  }

  public String getType() {
    return type;
  }

}
