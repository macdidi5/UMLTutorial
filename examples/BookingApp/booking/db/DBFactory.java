package booking.db;

import java.rmi.*;
import java.rmi.registry.*;

/**
* Provides local or remote database connections.
*/
public class DBFactory {
  private static final String dbFile = "db-2x1.db";
  private static final String UNEXPECTED = 
    "DBFactory - Unexpected database access problem: ";
  
  /**
  * Return DBMain object, open data file in current working directory.
  *
  * @return Instance of database.
  * @exception BookingException Thrown if cannot open data file.
  */
  public DBMain getDB() throws BookingException {
    return new Data(dbFile);
  }
  
  /**
  * Open specify local data file and return DBMain object.
  *
  * @param dbFile The data file.
  * @return Instance of database.
  * @exception BookingException Thrown if cannot open data file.
  */
  public DBMain getDB(String dbFile) throws BookingException {
    return new Data(dbFile);
  }
  
  /**
  * Lookup remote rmi server and return DBMain object.
  *
  * @param host The rmi server.
  * @param port The rmi service port number.
  * @param serviceName The rmi service name.
  * @return Instance of database.
  * @exception BookingException Thrown if cannot open data connection.
  */
  public DBMain getDB(String host, String port, String serviceName) 
    throws BookingException {

    String lookupString = "rmi://" + host + ":" + port +
                          "/" + serviceName;
    try {
      return (DBMain)Naming.lookup(lookupString);
    } catch (Exception ex) {
      throw new BookingException(lookupString + "\n" + 
                                    UNEXPECTED + "\n" + ex);
    }
  }
}
