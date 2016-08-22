package booking.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

import java.util.*;
import java.io.Serializable;

import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;

import booking.db.*;

/**
* This class provides client GUIs and view of the MVC.
*/
public class BookingGui 
  extends UnicastRemoteObject 
  implements BookingView, Serializable {
  
  // MVC
  private DBMain data;
  private BookingController bc;

  // GUI components
  private JFrame frame;
  private Container contentPane;
  private JPanel uPan = new JPanel();
  private JPanel lPan = new JPanel();
  private JPanel functionPan = new JPanel();
  private JButton helpBt = new JButton("Help");
  private JButton seaBt = new JButton("Search");
  private JButton addBt = new JButton("Add");
  private JButton updBt = new JButton("Update");
  private JButton delBt = new JButton("Delete");
  private JButton bookBt = new JButton("Booking");
  private JButton exitBt = new JButton("Exit");
  private JPanel subconPan = new JPanel();
  private JLabel recNoLb = new JLabel("Record number");
  private JLabel nameLb = new JLabel("Subcontractor name");
  private JLabel locationLb = new JLabel("City");
  private JLabel specialtiesLb = new JLabel("Types of work performed");
  private JLabel sizeLb = new JLabel("Number of staff");
  private JLabel rateLb = new JLabel("Hourly charge");
  private JLabel ownerLb = new JLabel("Owner");
  private JTextField recNoTf = new JTextField();
  private JTextField nameTf = new JTextField();
  private JTextField locationTf = new JTextField();
  private JTextField specialtiesTf = new JTextField();
  private JTextField sizeTf = new JTextField();
  private JTextField rateTf = new JTextField();
  private JTextField ownerTf = new JTextField();
  private JPanel tablePan = new JPanel();
  private String tableHeaders[] = 
    {"Subcontractor name", "City", "Types of work performed", 
    "Number of staff", "Hourly charge", "Owner", "Record number"};
  private JTable table;
  private JScrollPane tableSp; 
  private DefaultTableModel tableModel; 
  private JPanel statusPan = new JPanel();

  // Status bar
  private JLabel statusLb = new JLabel("Status", SwingConstants.CENTER);
  private JTextField statusTf= new JTextField();
  
  /**
  * This constructor take a model object and construct user interface.
  *
  * @param db The model object.
  * @exception RemoteException Thrown if any error occur.
  */
  public BookingGui(DBMain data) throws RemoteException {
    try {
      this.data = data;
      this.data.addChangeListener(this);
      buildDisplay();
      addListeners();
    } catch (Exception ex) {
      throw new RemoteException(ex.getMessage());
    }
  }

  // Private methods
  
  private void buildDisplay(){
    frame = new JFrame("Subcontractor Tool");
    buildFunctionPanel();
    buildSubconPanel();
    buildTablePanel();
    buildStatusPanel();

    uPan.setLayout(new BorderLayout());
    uPan.add(functionPan, BorderLayout.NORTH);
    uPan.add(subconPan, BorderLayout.CENTER);

    lPan.setLayout(new BorderLayout());
    lPan.add(tablePan, BorderLayout.CENTER);
    lPan.add(statusPan, BorderLayout.SOUTH);
    
    contentPane = frame.getContentPane();
    contentPane.setLayout(new BorderLayout());
    contentPane.add(uPan, BorderLayout.NORTH);
    contentPane.add(lPan, BorderLayout.CENTER);
    
    frame.pack();
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.setVisible(true);
  }
  
  private void buildFunctionPanel() {
    functionPan.setLayout(new FlowLayout());
    functionPan.add(helpBt);
    functionPan.add(seaBt);
    functionPan.add(addBt);
    functionPan.add(updBt);
    functionPan.add(delBt);
    functionPan.add(bookBt);
    functionPan.add(exitBt);
  }
    
  private void buildSubconPanel() {
    ownerTf.setEditable(false);
    recNoTf.setEditable(false);
    subconPan.setLayout(new GridLayout(7,2));
    subconPan.add(recNoLb);
    subconPan.add(recNoTf);
    subconPan.add(nameLb);
    subconPan.add(nameTf);
    subconPan.add(locationLb);
    subconPan.add(locationTf);
    subconPan.add(specialtiesLb);
    subconPan.add(specialtiesTf);
    subconPan.add(sizeLb);
    subconPan.add(sizeTf);
    subconPan.add(rateLb);
    subconPan.add(rateTf);
    subconPan.add(ownerLb);
    subconPan.add(ownerTf);
  }
    
  private void buildTablePanel() {
    tablePan.setLayout(new BorderLayout());
    tableModel = new DefaultTableModel(tableHeaders, 10);
    table = new JTable(tableModel);
    tableSp = new JScrollPane(table);
    tablePan.add(tableSp, BorderLayout.CENTER);
    Dimension dim = new Dimension(500, 150);
    table.setPreferredScrollableViewportSize(dim);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    ListSelectionModel rowSM = table.getSelectionModel();
    rowSM.addListSelectionListener(new ChooseRecord(rowSM));
  }
  
  private void buildStatusPanel() {
    statusPan.setLayout(new BorderLayout());
    statusPan.add(statusLb, BorderLayout.NORTH);
    statusPan.add(statusTf, BorderLayout.CENTER);
  }

  private void addListeners() {
    helpBt.addActionListener(new HelpAction());
    seaBt.addActionListener(new SearchAction());
    addBt.addActionListener(new AddAction());
    updBt.addActionListener(new UpdateAction());
    delBt.addActionListener(new DeleteAction());
    bookBt.addActionListener(new BookingAction());
    exitBt.addActionListener(new ExitAction());
  }

  private void clearSubconPanel() {
    recNoTf.setText("");
    nameTf.setText("");
    locationTf.setText("");
    specialtiesTf.setText("");
    sizeTf.setText("");
    rateTf.setText("");
    ownerTf.setText("");
  }

  /**
  * Refresh field data on subcontractor panel.
  *
  * @param record The String array of record.
  */
  public void refreshSubconPanel(String record[]){
    nameTf.setText(record[0]);
    locationTf.setText(record[1]);
    specialtiesTf.setText(record[2]);
    sizeTf.setText(record[3]);
    rateTf.setText(record[4]);
    ownerTf.setText(record[5]);
  }

  /**
  * Refresh table data on table panel.
  *
  * @param records The String two array of records.
  */
  public void refreshTablePanel(String records[][]) {
    tableModel.setDataVector(records, tableHeaders);
  }

  /**
  * Refresh table record on table panel.
  *
  * @param record The String array of record.
  */
  public void refreshTableRecord(int recNo, String record[]) {
    int rowCount = tableModel.getRowCount();
    String recNoStr = String.valueOf(recNo);

    for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
      String tableRecNo = (String)tableModel.getValueAt(rowIndex, 6);
      
      if (recNoStr.equals(tableRecNo)) {
        for (int i = 0; i < record.length; i++) {
          tableModel.setValueAt(record[i], rowIndex, i);
        }
        break;
      }
    }
  }

  /**
  * Update status message.
  *
  * @param msg The status message string.
  */
  public void updateStatus(String msg) {
    statusTf.setText(msg);
  }
  
  /**
  * Get record data on subcontractor panel.
  *
  * @return The String array of record.
  */
  public String[] getRecordOnPan() {
    String result[] = new String[6];
    result[0] = nameTf.getText();
    result[1] = locationTf.getText();
    result[2] = specialtiesTf.getText();
    result[3] = sizeTf.getText();
    result[4] = rateTf.getText();
    result[5] = ownerTf.getText();

    return result;
  }

  /**
  * Adds requester to the list of objects to be notified of user 
  * gestures entered through a user interface such as a GUI.
  *
  * @param bc The controller object.
  */
  public void setUserGestureListener(BookingController bc)
    throws BookingException, RemoteException {
    this.bc = bc;
  }

  /**
  * Callback method to handle data state change notification
  * from the suncertify model.
  *
  * @param recNo Record number.
  * @param data String array represent new data.
  */
  public void handleDataChange(Integer recNo, String data[])
    throws BookingException {
    System.out.println("BookingGui: handleDataChange: " + recNo);
    
    // Determine subcontractor panel refresh
    String panelData[] = getRecordOnPan();
    if (panelData[0].equals(data[0]) && panelData[1].equals(data[1])) {
      refreshSubconPanel(data);
    }

    refreshTableRecord(recNo.intValue(), data);
  }
  
  /**
  * This inner class Search the database.
  */
  public class SearchAction implements ActionListener {
    /**
    * User click Search button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      String record[] = getRecordOnPan();
      try {
        int match[] = data.find(record);
        String records[][] = new String[match.length][7];
        
        for (int i = 0; i < match.length; i++) {
          String rec[] = data.read(match[i]);
          System.arraycopy(rec, 0, records[i], 0, rec.length);
          records[i][6] = String.valueOf(match[i]);
        }

        refreshTablePanel(records);
        updateStatus(match.length + " records matched.");
      } catch (Exception ex) {
        updateStatus(ex.getMessage());
      }
    }
  }

  /**
  * This inner class add new record to database.
  */
  public class AddAction implements ActionListener {
    /**
    * User click Add button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      String record[] = getRecordOnPan();
      // Avoid exist owner value
      record[5] = "";

      try {
        bc.handleAddGesture(record);
        updateStatus("Record added.");
      } catch (Exception ex) {
        updateStatus(ex.getMessage());
      }
    }
  }  // End of AddAction
  
  /**
  * This inner class update record to database.
  */
  public class UpdateAction implements ActionListener {
    /**
    * User click Update button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      String record[] = getRecordOnPan();
      String searchKey[] = new String[6];
      System.arraycopy(record, 0, searchKey, 0, 2);
      searchKey[0] = searchKey[0].trim();
      searchKey[1] = searchKey[1].trim();

      try {
        int match[] = data.find(searchKey);

        if (match.length > 1) {
          updateStatus("More than one match record.");
          return;
        } else if (match.length == 0) {
          updateStatus("Record not found.");
          return;
        }

        bc.handleUpdateGesture(match[0], record);
        updateStatus("Record updated."); 
      } catch (Exception ex) {
        updateStatus(ex.getMessage());
      }
    }
  }  // End of UpdateAction

  /**
  * This inner class delete record to database.
  */
  public class DeleteAction implements ActionListener {
    /**
    * User click Delete button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      String record[] = getRecordOnPan();
      try {
        int match[] = data.find(record);
        if (match.length == 1) {
          bc.handleDeleteGesture(match[0]);
          clearSubconPanel();
          updateStatus("Record deleted.");
        } else {
          updateStatus("More than one match record.");
        }
      } catch (Exception ex) {
        updateStatus(ex.getMessage());
      }
    }
  }  // End of DeleteAction

  /**
  * This inner class write the database.
  */
  public class BookingAction implements ActionListener {
    /**
    * User click Booking button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      JTextField ownerTf;
      String mes;

      String record[] = getRecordOnPan();

      try {
        int match[] = data.find(record);
        
        if (match.length > 1) {
          updateStatus("More than one match record.");
          return;
        } else if (match.length == 0) {
          updateStatus("Record not found.");
          return;
        }
        
        ownerTf = new JTextField();
        ownerTf.setText(record[5]);
        mes = "Enter customer id";
        
        int result = JOptionPane.showOptionDialog(
          frame, new Object[] {mes, ownerTf},
          "Booking", JOptionPane.OK_CANCEL_OPTION,
          JOptionPane.PLAIN_MESSAGE,
          null, null, null);

        if (result == JOptionPane.OK_OPTION) {
          record[5] = ownerTf.getText();
          bc.handleUpdateGesture(match[0], record);
          updateStatus("Subcontractor booked.");
        }
      } catch (Exception ex) {
        updateStatus(ex.getMessage());
      }
    }
  }  // End of BookingAction 

  /**
  * This inner class exit application.
  */
  public class ExitAction implements ActionListener {
    /**
    * User click exit button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }
  } // End of ExitAction

  /**
  * This inner class show user help dialog.
  */
  public class HelpAction implements ActionListener {
    /**
    * User click help button.
    *
    * @param e The ActionEvent object
    */
    public void actionPerformed(ActionEvent e) {
      JTextPane pane = new JTextPane();
      JScrollPane scroller = new JScrollPane();
      scroller.getViewport().add(pane);
      JDialog dialog = new JDialog();
      dialog.getContentPane().add(scroller);
      dialog.pack();
      dialog.setSize(800, 500);
      dialog.setVisible(true);
      String htmlDocument = "userguide.html";
      
      try {
        java.net.URL url = 
          new java.net.URL("file:docs" + 
                           System.getProperty("file.separator") + 
                           htmlDocument);
        pane.setPage(url);
      }
      catch(Exception ex) {
        updateStatus(ex.getMessage());
      }
    }
  } // End of HelpAction

  /**
  * This inner class implements ListSelectionListener and handles
  * the selection of JTable cell. Once a cell is selected, 
  * record is automatically filled in the constractor panel.
  */
  public class ChooseRecord implements ListSelectionListener {
    ListSelectionModel model;
    String record[] = new String[7];
    
    /**
     * Sets the ListSelectionModel
     *
     * @param lsm The ListSelectionModel object
     */
    public ChooseRecord(ListSelectionModel lsm) {
      model = lsm;
    }
    
    /**
    * Invokes when a different line is selected and sets values to 
    * constractor panel. If successful, a choose record message
    * shown in status area. If failed, no message is shown.
    *
    * @param lse The ListSelectionEvent object
    */
    public void valueChanged(ListSelectionEvent lse) {
      if(!lse.getValueIsAdjusting()) {
        int rowIndex = model.getMinSelectionIndex();
        
        if(rowIndex == -1) {
          return ;
        }

        for (int i = 0; i < record.length; i++) {
          record[i] = 
            (String)tableModel.getValueAt(rowIndex, i);
        }

        nameTf.setText(record[0]);
        locationTf.setText(record[1]);
        specialtiesTf.setText(record[2]);
        sizeTf.setText(record[3]);
        rateTf.setText(record[4]);
        ownerTf.setText(record[5]);
        recNoTf.setText(record[6]);
        updateStatus("Choose record.");
      }
    }
  } // End of ChooseRecord
} 