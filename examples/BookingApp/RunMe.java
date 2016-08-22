import booking.client.*;
import booking.db.*;
import booking.server.*;

import java.util.Properties;
import java.io.FileInputStream;

/**
* Main program.  Depend on command line arguments, it can start rmi 
* service, remote GUI client or run in standalone mode.
*/
public class RunMe {
  /**
  * The standard start entry.
  *
  * @param args String array, take zero or one command line argument:
  * <li>zero : network client.</li>
  * <li>"server" : create rmi service.</li>
  * <li>"alone" : standalone mode.</li>
  */
  public static void main(String args[]) {
    args = new String[]{"alone"};
    int argLen = args.length;

    // MVC component
    BookingGui gui;        // view
    BookingController sc;  // controller
    DBMain data;              // model

    // Default configuration
    String dbFile = "booking.db";
    String remoteHost = "localhost";
    String rmiPortNumber = "1234";
    String rmiServiceName = "DatabaseService";

    Properties p = new Properties();

    // Read configuration from suncertify.properties
    try {
      p.load(new FileInputStream("booking.properties"));
      dbFile = p.getProperty("dbFile");
      remoteHost = p.getProperty("remoteHost");
      rmiPortNumber = p.getProperty("rmiPortNumber");
      rmiServiceName = p.getProperty("rmiServiceName");
    } catch (Exception ex) {
      System.out.println("Read properties error, use default configuration");
    }

    try {
      if (argLen == 0) {
        // No argument, remote client
        data = new DBFactory().getDB(
          remoteHost, rmiPortNumber, rmiServiceName);
        gui = new BookingGui(data);
        sc = new BookingController(data, gui);
      } else if (argLen == 1) {
        // One argument
        // "server" for remote server, "along" for single mode
        if (args[0].equals("server")) {
          DBRMIServer db_rmi = 
            new DBRMIServer(dbFile, remoteHost, 
                            rmiPortNumber, rmiServiceName);
        } else if (args[0].equals("alone")) {
          data = new DBFactory().getDB(dbFile);
          gui = new BookingGui(data);
          sc = new BookingController(data, gui);
        }
      } else {
        // Show usage
        System.out.println("Usage:");
        System.out.println("  Remote client:");
        System.out.println("    java -jar runme.jar");
        System.out.println("  Remote server:");
        System.out.println("    java -jar runme.jar server");
        System.out.println("  Standalone:");
        System.out.println("    java -jar runme.jar along");
      }
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }
}
