package edu.brown.cs.wearableWeather;

/**
 * Class for bottoms which knows about its parameters.
 * @author ohu
 *
 */
public class Bottom implements Garment {

  private int id;
  private String name;
  private String weight;
  private String color;
  private int formality;
  private int user;
  private String type;

  /**
   * Bottom constructor.
   * @param id of the bottom in the database
   * @param name of the bottom
   * @param weight short or long
   * @param color of the bottom
   * @param formality from 1-5 of the bottom
   * @param user the bottom belongs to
   */
  public Bottom(int id, String name, String weight, String color,
      int formality, int user) {
    this.id = id;
    this.name = name;
    this.weight = weight;
    this.color = color;
    this.formality = formality;
    this.user = user;
    this.type = "bottom";
  }

  /**
   * Returns the ID of the bottom.
   * @return id of bottom
   */
  public int getID() {
    return id;
  }

  /**
   * Returns the name of the bottom.
   * @return name of bottom
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the weight of the bottom.
   * @return short or long
   */
  public String getWeight() {
    return weight;
  }

  /**
   * Returns the color of the bottom.
   * @return color of bottom
   */
  public String getColor() {
    return color;
  }

  /**
   * Returns the formality of the bottom.
   * @return formality 1-5 of the bottom
   */
  public int getFormality() {
    return formality;
  }

  /**
   * Returns the unique user_id that the bottom belongs to.
   * @return user_id that bottom belongs to
   */
  public int getUserID() {
    return user;
  }

  /**
   * Returns type of the garment.
   * @return bottom
   */
  public String getType() {
    return type;
  }

}
