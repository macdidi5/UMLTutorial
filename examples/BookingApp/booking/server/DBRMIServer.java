package booking.server;

import booking.db.*;

import java.net.*;
import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

/**
* Provides RMI service for remote client.
*/
public class DBRMIServer {
  /**
  * The constructor creates the RMI server.
  *
  * @param dbFile The database file.
  * @param remoteHost The rmi server.
  * @param rmiPortNumber The rmi port number.
  * @param rmiServiceName The rmi service name.
  */
  public DBRMIServer(String dbFile, String remoteHost,
                     String rmiPortNumber, String rmiServiceName) {
    try {
      Registry myRegistry = 
        LocateRegistry.createRegistry(Integer.parseInt(rmiPortNumber));
      DBMain dbServer = new DBRMIServerImpl(dbFile);
      String urlString = "rmi://" + remoteHost + ":" + rmiPortNumber + 
                         "/" + rmiServiceName;
      myRegistry.rebind(rmiServiceName, dbServer);
      System.out.println("Server started, waiting for client requests.");
    } catch(Exception ex) {
      System.out.println(ex);
    }
  }
}
