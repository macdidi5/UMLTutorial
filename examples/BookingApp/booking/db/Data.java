package booking.db;

import booking.client.*;

import java.io.*;
import java.util.*;

/**
* This class provides the database services.
*/
public class Data implements DBMain, Serializable {
  private static final byte LIVE_RECORD = 0;
  private static final byte DELETED_RECORD = 1;
  private static final String UNEXPECTED =
    "Data: Unexpected database access problem - \n";
  private int headerLen = 4 + 4 + 2;
  private final int MAGIC = 513;
  private RandomAccessFile db;
  private int recordLen;
  private int fieldCount;
  private FieldInfo fieldInfo[];

  // Store BookingView object
  private ArrayList changeListeners = new ArrayList(10);

  /**
  * This constructor opens an existing database given the name
  * of the disk file containing it.
  *
  * @param dbname The name of the database file to open.
  * @exception BookingException
  */
  public Data(String dbName) throws BookingException {
    try {
      File f = new File(dbName);
      if (f.exists() && f.canRead() && f.canWrite()) {
        db = new RandomAccessFile(f, "rw");
        
        if (db.readInt() != MAGIC) {
          throw new BookingException(
          "Data: corrupted database file - " + dbName);
        }

        recordLen = db.readInt();  // value is 182
        fieldCount = db.readShort();  // value is 6
        fieldInfo = new FieldInfo[fieldCount];

        for (int count = 0; count < fieldCount; count++) {
          headerLen += 2;
          int fieldNameLen = db.readShort();
          headerLen += fieldNameLen;
          byte temp[] = new byte[fieldNameLen];
          db.read(temp);
          int fieldLen = db.readShort();
          fieldInfo[count] = new FieldInfo(new String(temp), fieldLen);
          headerLen += 2;
        }
        // headerLen is 70
      } else {
        throw new BookingException("Data: Non-existant or " +
                                      "inaccessible file" + dbName);
      }
    } catch (Exception e) {
      throw new BookingException(UNEXPECTED + e);
    }
  }

  /**
  * Adds requestor to the list of objects to be notified when an
  * object model alters state.
  *
  * @param sv The suncertify view object.
  */
  public void addChangeListener(BookingView bv)
    throws BookingException {
    changeListeners.add(bv);
  }

  /**
  * This method notifies all registered BookingView listeners
  * that data has changed.
  */
  private void fireModelChangeEvent(Integer recNo, String data[]) 
    throws BookingException {
    BookingView sv;
    
    for (int i=0; i<changeListeners.size(); i++) {
      try {    
        sv = (BookingView)changeListeners.get(i);
        sv.handleDataChange(recNo, data);
      } catch (Exception ex) {
        throw new BookingException("Exception occur in fireModelChangeEvent.");
      }
    }
  }

  /**
  * Closes the database, flushing any outstanding
  * writes at the same time.
  */
  public synchronized void close() {
    try {
      db.close();
    } catch (IOException iex) {
      System.out.println(iex);
    }

    db = null;
  }
  
  /**
  * Ensure close the database.
  */
  protected void finalize() {
    if (db != null) {
      close();
    }
  }

  /**
  * Gets a requested record from the database based on record number.
  *
  * @param recNo The number of the record to read (first record is 1).
  * @return String array for the record or null if the record has been 
  *  marked for deletion.
  * @exception RecordNotFoundException Thrown if database file cannot 
  *  be accessed.
  */
  public synchronized String [] read(int recNo) 
    throws RecordNotFoundException {
    try {
      if (recNo < 1) {
        throw new RecordNotFoundException(
          "Record number must be greater than 0");
      }

      seek(recNo);

      String records[] = readRecord();
      if (records == null) {
         throw new RecordNotFoundException("Record " + recNo + " not found!");
      }

      return records;
    } catch(Exception e) {
      throw new RecordNotFoundException("Record " + recNo + " not found!");
    }
  }

  /**
  * Modifies the fields of a record.
  *
  * @param recNo The number of the record.
  * @param data The new value, field n appears in data[n]
  * @exception RecordNotFoundException Thrown if database file cannot 
  *  be accessed.
  */
  public synchronized void update(int recNo, String [] data) 
    throws RecordNotFoundException {
    try {
      seek(recNo);
      writeRecord(data);
      fireModelChangeEvent(new Integer(recNo), data);
    } catch (Exception e) {
      throw new RecordNotFoundException(UNEXPECTED + e);
    }
  }
  
  /**
  * Deletes a record, making the record number and associated disk
  * storage available for reuse. 
  *
  * @param recNo The number of the record.
  * @exception RecordNotFoundException Thrown if database file cannot 
  *  be accessed.
  */
  public synchronized void delete(int recNo) 
    throws RecordNotFoundException {
    try {
      seek(recNo);
      db.write(DELETED_RECORD);
      fireModelChangeEvent(new Integer(recNo), 
                           new String[] {"-- deleted --", 
                                         "", "", "", "", ""});
    } catch (Exception e) {
      throw new RecordNotFoundException(UNEXPECTED + e);
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
  */
  public int [] find(String [] criteria) 
    throws RecordNotFoundException {
    int result [] = null;
    ArrayList aList = new ArrayList();
    String values [] = null;
    int recordCount = 0;
    
    try {
      recordCount = getRecordCount();

      for (int count = 1; count <= recordCount; count++) {
        seek(count);
        values = readRecord();

        if (values != null) {
          boolean isMatch = true;
          
          for (int i = 0; i < fieldCount; i++) {
            if (criteria[i] != null) {
              if (!values[i].startsWith(criteria[i])) {
                isMatch = false;
                break;
              }
            }
          }

          if (isMatch) {
            aList.add(new Integer(count));
          }
        }
      }
    } catch (Exception ex) {
      throw new RecordNotFoundException();
    }

    if (aList.size() > 0) {
      result = new int[aList.size()];
      for (int i = 0; i < result.length; i++) {
        result[i] = ((Integer)aList.get(i)).intValue();
      }
    } else {
      throw new RecordNotFoundException();
    }

    return result;
  }
  
  /**
  * Creates a new record in the database (possibly reusing a
  * deleted entry)
  *
  * @param data The value of the record.
  * @return int The record number of the new record.
  * @exception DuplicateKeyException thrown if duplicate key occure.
  */
  public synchronized int create(String [] data) 
    throws DuplicateKeyException {
    
    String searchKey[] = new String[6];
    System.arraycopy(data, 0, searchKey, 0, 2);
    searchKey[0] = searchKey[0].trim();
    searchKey[1] = searchKey[1].trim();

    int writeRecNo = 0;
    int recordCount = 0;

    // Check duplicate key
    try {
      if (find(searchKey).length > 0) {
        throw new DuplicateKeyException(
          "Attempt to create a duplicate key record.");
      }
    } catch (RecordNotFoundException enfe) {
      // do nothing
    } catch (Exception ex) {
      throw new DuplicateKeyException(UNEXPECTED + ex);
    }

    try {
      // Search deleted record
      recordCount = getRecordCount();

      if (recordCount > 0) {
        for (int i = 1; i <= recordCount; i++) {
          if (!isValidRecord(i)) {
            writeRecNo = i;
            break;
          }
        }
      }

      if (writeRecNo == 0) {
        writeRecNo = recordCount + 1;
      }

      seek(writeRecNo);
      writeRecord(data);
      
      fireModelChangeEvent(new Integer(writeRecNo), data);
    } catch (Exception e) {
      throw new DuplicateKeyException(UNEXPECTED + e);
    }

    return writeRecNo;
  }
  
  /**
  * Locks a record so that it can only be updated or deleted 
  * by this client.
  *
  * @param recNo The record number.
  */
  public synchronized void lock(int recNo) throws RecordNotFoundException {
    // do nothing in local mode
  }
  
  /**
  * Releases the lock on a record.
  *
  * @param recNo The record number.
  */
  public synchronized void unlock(int recNo) throws RecordNotFoundException {
    // do nothing in local mode
  }
  
  /**
  * Determines if a record is currenly locked. Returns true if the
  * record is locked, false otherwise.
  *
  * @param recNo The record number.
  * @return boolean Return true if record is locked by another user.
  */
  public synchronized boolean isLocked(int recNo) throws RecordNotFoundException {
    // Always return false in local mode
    return false;
  }
  
  /**
  * Moves the current database record pointer to the specified record.
  *
  * @param recno The record number to position the cursor.
  * @exception IOException If the record position is invalid.
  */
  private synchronized void seek(int recNo) 
    throws BookingException {
    try {
      db.seek(headerLen + ((recordLen + 1) * (recNo - 1)));
    } catch (Exception e) {
      throw new BookingException(UNEXPECTED + e);
    }
  }

  /**
  * Reads a record from the current cursor position of the underlying 
  * random access file.
  *
  * @return The array of strings that make up a database record.
  *         Null value if record is marked deleted.
  * @exception BookingException Generated if any unexpected
  *  exception occur.
  */
  private synchronized String[] readRecord() 
    throws BookingException {
    int offset = 1;
    String [] rv = null;
    byte [] buffer = new byte[recordLen + 1];

    try {
      db.read(buffer);

      if (buffer[0] == LIVE_RECORD) {
        rv = new String[fieldCount];
        for (int i = 0; i < fieldCount; i++) {
          rv[i] = new String(buffer, offset, fieldInfo[i].getLength());
          offset += fieldInfo[i].getLength();
        }
      } else {
        rv = null;
      }
    } catch (Exception e) {
      throw new BookingException(UNEXPECTED + e);
    }
    
    return rv;
  }

  /**
  * Writes a record to the database using the current location of 
  * the underlying random access file.
  *
  * @param newData An array of strings in the database specified order.
  * @exception BookingException Generated if any unexpected
  *  exception occur.
  */
  private synchronized void writeRecord(String[] newData) 
    throws BookingException {
    if ((newData == null) || (newData.length != fieldCount)) {
      throw new BookingException(
        "Data: Wrong number of fields in writeRecord() " +
        newData.length + " given, " + fieldCount + " required");
    }

    int size, space, toCopy;
    byte [] buffer = new byte[recordLen + 1];
    buffer[0] = LIVE_RECORD;
    int offset = 1;

    for (int i = 0; i < fieldCount; i++) {
      space = fieldInfo[i].getLength();
      String s = rPad(newData[i], space);
      byte temp[] = s.getBytes();
      System.arraycopy(temp, 0, buffer, offset, space);
      offset += space;
    }

    try {
      db.write(buffer);
    } catch (Exception e) {
      throw new BookingException(UNEXPECTED + e);
    }
  }

  /**
  * Pad space to right side of String. 
  *
  * @param s String object.
  * @param len The total length of String.
  */
  private String rPad(String s, int len) {
    String result = s;
    if (s.length() < len) {
      int spaceLen = len - s.length();

      for (int i = 0; i < spaceLen; i++) {
        result = result + " ";
      }
    }

    return result;
  }

  /**
  * Return the record count of the database.
  *
  * @return The count of record.
  * @exception BookingException generated if any unexpected
  *  exception occur.
  */
  public int getRecordCount() throws BookingException {

    long dbFileLen = 0;
    int result = 0;
    
    try {
      dbFileLen = db.length();  
    } catch (Exception e) {
      throw new BookingException(UNEXPECTED + e);
    }
    
    result = (int)((dbFileLen - 70) / (recordLen + 1));
    return result;
  }

  /**
  * Test the recNo record is valid(not deleted).
  *
  * @param recNo The record number.
  * @return The Boolean value, true mean record is valid, false mean
  *  record is deleted.
  * @exception BookingException generated if any unexpected
  *  exception occur.
  */
  public boolean isValidRecord(int recNo) throws BookingException {
    boolean result = false;

    try {
      seek(recNo);
      byte isValid = db.readByte();

      result = (isValid == LIVE_RECORD);
    } catch (Exception e) {
      throw new BookingException(UNEXPECTED + e);
    }

    return result;
  }

  /**
  * Return field count.
  *
  * @return Field count.
  */
  public int getFieldCount() {
    return fieldCount;
  }
}
