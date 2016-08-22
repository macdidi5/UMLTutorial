package booking.db;

import booking.client.BookingView;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
* Supplied interface. 
* Extends java.rmi.Remote for rmi approach.
* Add addChangeListener method for apply MVC design pattern.
*/
public interface DBMain extends Remote {
  // Reads a record from the file. Returns an array where each
  // element is a record value.
  public String [] read(int recNo) throws RecordNotFoundException, RemoteException;
  // Modifies the fields of a record. The new value for field n 
  // appears in data[n].
  public void update(int recNo, String [] data)
  throws RecordNotFoundException, RemoteException;
  // Deletes a record, making the record number and associated disk
  // storage available for reuse. 
  public void delete(int recNo) throws RecordNotFoundException, RemoteException;
  // Returns an array of record numbers that match the specified
  // criteria. Field n in the database file is described by
  // criteria[n]. A null value in criteria[n] matches any field
  // value. A non-null  value in criteria[n] matches any field
  // value that begins with criteria[n]. (For example, "Fred"
  // matches "Fred" or "Freddy".)
  public int [] find(String [] criteria) throws RecordNotFoundException, RemoteException;
  // Creates a new record in the database (possibly reusing a
  // deleted entry). Inserts the given data, and returns the record
  // number of the new record.
  public int create(String [] data) throws DuplicateKeyException, RemoteException;
  // Locks a record so that it can only be updated or deleted by this client.
  // If the specified record is already locked, the current thread gives up
  // the CPU and consumes no CPU cycles until the record is unlocked.
  public void lock(int recNo) throws RecordNotFoundException, RemoteException;
  // Releases the lock on a record. 
  public void unlock(int recNo) throws RecordNotFoundException, RemoteException;
  // Determines if a record is currenly locked. Returns true if the
  // record is locked, false otherwise.
  public boolean isLocked(int recNo) throws RecordNotFoundException, RemoteException;
  // Adds requestor to the list of objects to be notified when an
  // object model alters state.
  public void addChangeListener(BookingView sv) throws BookingException, RemoteException;
}
