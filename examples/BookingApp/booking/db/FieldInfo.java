package booking.db;

import java.io.*;

/**
* This class embodies the name of a field and the maximum width 
* that it may have.
*/
public class FieldInfo implements Serializable {
  private String name;
  private int length;

  /**
  * This constructs an initialized FieldInfo object.
  *
  * @param name - the name of the field.
  * @param length - the length of the field.
  */
  public FieldInfo(String name, int length) {
    this.name = name;
    this.length = length;
  }

  /**
  * This method returns the name of the field.
  *
  * @return String The name of the field.
  */
  public String getName() {
    return name;
  }

  /**
  * This method returns the length of the field.
  *
  * @return int The length of the field.
  */
  public int getLength() {
    return length;
  }
}