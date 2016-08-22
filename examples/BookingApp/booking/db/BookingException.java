package booking.db;

/**
* This class defines the database exception used
* in this application to indicate application wrong.
*/

public class BookingException extends Exception {
  /**
  * Creates new <code>BookingException</code> without detail message.
  */
  public BookingException() {
    super();
  }

  /**
  * Constructs an <code>BookingException</code> with the specified
  * detail message.
  *
  * @param msg the detail message.
  */
  public BookingException(String msg) {
    super(msg);
  }
}
