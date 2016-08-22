package booking.db;

import booking.server.*;

import java.util.HashMap;
import java.util.Map;


/**
* This class eparate concurrent issue in Data class.
*/
public class LockManager {
  private final Map lockedRecords = new HashMap();

  /**
  * Locks a record.
  *
  * @param recNo The record number.
  * @param client The client host name.
  * @exception InterruptedException Thrown if interrupted occure.
  */
  synchronized public void lock(int recNo, Object client) 
    throws BookingException {
    Integer record = new Integer(recNo);
    Object oldClient = lockedRecords.get(record);
    
    System.out.println("Process LockManager lock()");

    try {
      if ((lockedRecords.containsKey(record)) &&
          (!oldClient.equals(client))) {
        throw new BookingException(
          "Record is locked by another user, try again later.");
      }

      lockedRecords.put(record, client);
    } catch(Exception ex) {
      throw new BookingException(ex.getMessage());
    }
  }
  
  /**
  * Examine record lock by another user.
  *
  * @param recNo The record number.
  * @param client The client host name.
  */
  synchronized public boolean isLocked(int recNo, Object client) {
    boolean isLocked = false;
    Integer record = new Integer(recNo);
    Object oldClient = lockedRecords.get(record);
    
    System.out.println("Process LockManager isLocked()");

    if(oldClient == null) {
      lockedRecords.remove(record);
    } else if (!oldClient.equals(client)) {
      isLocked = true;
    }

    return isLocked;
  }
  
  /**
  * Unlocks a record.
  *
  * @param recNo The record number.
  * @param client The client host name.
  */
  synchronized public void unlock(int recNo, Object client) {
    Integer record = new Integer(recNo);
    Object oldClient = lockedRecords.get(record);
    
    System.out.println("Process LockManager unlock()");

    if ((oldClient != null) && (oldClient.equals(client))) {
      lockedRecords.remove(record);
    }
  }
}
