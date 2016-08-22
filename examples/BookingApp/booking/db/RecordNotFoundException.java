package booking.db;

/**
* This class defines the database exception used
* in this application to indicate can not find
* match record.
*/

public class RecordNotFoundException extends BookingException {
  /**
  * Creates new <code>RecordNotFoundException</code> without detail message.
  */
  public RecordNotFoundException() {
    super("Record not found.");
  }
  
  /**
  * Constructs an <code>RecordNotFoundException</code> with the specified
  * detail message.
  *
  * @param msg the detail message.
  */
  public RecordNotFoundException(String msg) {
    super(msg);
  }
}
