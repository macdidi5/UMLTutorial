package booking.client;

import booking.db.BookingException;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* The interface of view in MVC.
*/
public interface BookingView extends Remote {
  // Adds requester to the list of objects to be notified of user 
  // gestures entered through a user interface such as a GUI.
  void setUserGestureListener(BookingController sc)
    throws BookingException, RemoteException;

  // Callback method to handle data state change notification
  // from the suncertify model
  void handleDataChange(Integer recNo, String data[])
    throws BookingException, RemoteException;

  // Update status message.
  void updateStatus(String msg) 
    throws RemoteException;
}
