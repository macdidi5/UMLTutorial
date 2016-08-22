package booking.db;

/**
* This class defines the database exception used
* in this application to indicate duplicate key
* when append record.
*/

public class DuplicateKeyException extends BookingException {
  /**
  * Creates new <code>DuplicateKeyException</code> without detail message.
  */
  public DuplicateKeyException() {
    super("Duplicate Key.");
  }

  /**
  * Constructs an <code>DuplicateKeyException</code> with the specified
  * detail message.
  *
  * @param msg the detail message.
  */
  public DuplicateKeyException(String msg) {
    super(msg);
  }
}
