package booking.client;

import booking.db.DBMain;
import booking.db.BookingException;

/**
* This class provides controller of the MVC.
*/
public class BookingController {
  private DBMain data;
  private BookingView bv;

  /**
  * Creates new BookingController.
  *
  * @param data The model of the MVC.
  * @param bv The view of the MVC.
  * @exception BookingException Thrown if any error occur.
  */
  public BookingController(DBMain data, BookingView bv) 
    throws BookingException {
    this.data = data;
    this.bv = bv;

    try {
      bv.setUserGestureListener(this);
    } catch (Exception ex) {
      throw new BookingException(
        "Exception occure in BookingController constructor.");
    }
  }

  /**
  * Create data user gesture callback method called by
  * the suncertify view in response to the create data
  * button click on the GUI or equivalent user interface
  * action  - create the data in the model
  *
  * @param data String array represent new data.
  */
  public void handleAddGesture(String data[]) 
    throws BookingException {

    try {
      System.out.println("BookingController: handleAddGesture");
      this.data.create(data);
    } catch (Exception ex) {
      throw new BookingException(ex.getMessage());
    }
  }

  /**
  * Update data user gesture callback method called by
  * the suncertify view in response to the update data
  * button click on the GUI or equivalent user interface
  * action  - update the data in the model
  *
  * @param recNo Record number.
  * @param data String array represent new data.
  */
  public void handleUpdateGesture(int recNo, String data[]) 
    throws BookingException {

    try {
      System.out.println("BookingController: handleUpdateGesture: " + recNo);
      this.data.update(recNo, data);
    } catch (Exception ex) {
      throw new BookingException(ex.getMessage());
    }
  }

  /**
  * Delete data user gesture callback method called by the suncertify 
  * view in response to the delete data button click on the GUI or 
  * equivalent user interface action  - delete the data in the model
  *
  * @param recNo Record number.
  */
  public void handleDeleteGesture(int recNo) 
    throws BookingException {

    try {
      System.out.println("BookingController: handleDeleteGesture: " + recNo);
      this.data.delete(recNo);
    } catch (Exception ex) {
      throw new BookingException(ex.getMessage());
    }
  }
}
