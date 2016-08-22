package booking.server;

import booking.db.*;
import booking.client.*;

import java.rmi.*;
import java.rmi.server.*;

/**
* Implemention of RMI, Data local methods are wrapped with 
* remote methods for remote client.
*/
public class DBRMIServerImpl 
  extends UnicastRemoteObject implements DBMain {

  private final static LockManager LOCK_MANAGER = new LockManager();
  private static Data data;
  private static final String UNEXPECTED = 
    "DBRMIServerImpl - Unexpected database access problem: ";
  
  /**
  * The constructor creates the database instance.
  *
  * @param dbname The database file path & name
  * @exception BookingException Thrown if any error occur.
  * @exception RemoteException Thrown if remote error occur.
  */
  public DBRMIServerImpl(String dbname) 
    throws RemoteException, BookingException {

    super();
    data = new Data(dbname);
  }
    
  /**
  * Gets a requested record from the database based on record number.
  *
  * @param recNo The number of the record to read (first record is 1).
  * @return String array for the record or null if the record has been 
  *  marked for deletion.
  * @exception RecordNotFoundException Thrown if database file cannot 
  *  be accessed.
  * @exception RemoteException Thrown if remote error occur.
  */
  public String [] read(int recNo) 
    throws RecordNotFoundException, RemoteException {
    return data.read(recNo);
  }

  /**
  * Modifies the fields of a record.
  *
  * @param recNo The number of the record.
  * @param data The new value, field n appears in data[n]
  * @exception RecordNotFoundException Thrown if database file cannot 
  *  be accessed.
  * @exception RemoteException Thrown if remote error occur.
  */
  public void update(int recNo, String [] data) 
    throws RecordNotFoundException, RemoteException {

    try {
      if (isLocked(recNo)) {
        throw new RemoteException(
          "Record is locked by another user, try again later.");
      } else {
        lock(recNo);
        this.data.update(recNo, data);
        unlock(recNo);
      }
    } catch (Exception ex) {
      throw new RemoteException(ex.getMessage());
    }
  }

  /**
  * Deletes a record, making the record number and associated disk
  * storage available for reuse. 
  *
  * @param recNo The number of the record.
  * @exception RecordNotFoundException Thrown if database file cannot 
  *  be accessed.
  * @exception RemoteException Thrown if remote error occur.
  */
  public void delete(int recNo) 
    throws RecordNotFoundException, RemoteException {

    try {
      if (isLocked(recNo)) {
        throw new RemoteException(
          "Record is locked by another user, try again later.");
      } else {
        lock(recNo);
        this.data.delete(recNo);
        unlock(recNo);
      }
    } catch (Exception ex) {
      throw new RemoteException(ex.getMessage());
    }
  }

  /**
  * This method searches the database for match the specified
  * criteria.
  *
  * @param criteria A null value in criteria[n] matches any field
  *  value.  A non-null  value in criteria[n] matches any field 
  *  value that begins with criteria[n].
  * @return int array The matching record number.
  * @exception RecordNotFoundException Thrown when can not find 
  *  any match record.
  * @exception RemoteException Thrown if remote error occur.
  */
  public int [] find(String [] criteria) 
    throws RecordNotFoundException, RemoteException {
    return data.find(criteria);
  }

  /**
  * Creates a new record in the database (possibly reusing a
  * deleted entry)
  *
  * @param data The value of the record.
  * @return int The record number of the new record.
  * @exception DuplicateKeyException thrown if duplicate key occure.
  * @exception RemoteException Thrown if remote error occur.
  */
  public int create(String [] data) 
    throws DuplicateKeyException, RemoteException {
    return this.data.create(data);
  }

  /**
  * Locks a record so that it can only be updated or deleted 
  * by this client.
  *
  * @param recNo The record number.
  * @exception RecordNotFoundException Thrown if any error occur.
  * @exception RemoteException Thrown if remote error occur.
  */
  public void lock(int recNo) 
    throws RecordNotFoundException, RemoteException {

    try {
      LOCK_MANAGER.lock(recNo, getClientHost());
    } catch (Exception ex) {
      throw new RemoteException(ex.getMessage());
    }
  }
  
  /**
  * Releases the lock on a record.
  *
  * @param recNo The record number.
  * @exception RecordNotFoundException Thrown if any error occur.
  * @exception RemoteException Thrown if remote error occur.
  */
  public void unlock(int recNo) 
    throws RecordNotFoundException, RemoteException {

    try {
      LOCK_MANAGER.unlock(recNo, getClientHost());
    } catch (Exception ex) {
      throw new RemoteException(ex.getMessage());
    }
  }

  /**
  * Determines if a record is currenly locked. Returns true if the
  * record is locked, false otherwise.
  *
  * @param recNo The record number.
  * @return boolean Return true if record is locked by another user.
  * @exception RecordNotFoundException Thrown if any error occur.
  * @exception RemoteException Thrown if remote error occur.
  */
  public boolean isLocked(int recNo) 
    throws RecordNotFoundException, RemoteException {

    boolean result = false;
    try {
      result = LOCK_MANAGER.isLocked(recNo, getClientHost());
    } catch (Exception ex) {
      throw new RemoteException(ex.getMessage());
    }

    return result;
  }

  /**
  * Booking model state change listener registration methods.
  *
  * @param sv The suncertify view object.
  * @exception BookingException Thrown if MVC error occur.
  * @exception RemoteException Thrown if remote error occur.
  */
  public void addChangeListener(BookingView bv) 
    throws BookingException, RemoteException {
    data.addChangeListener(bv);
  }
}
