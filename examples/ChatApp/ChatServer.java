import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

public class ChatServer {
  private JFrame frame;
  private JPanel panel;
  private JTextArea messageLog;
  private JScrollPane scroll;

  private ArrayList clients;
  private ServerSocket serverSocket;
  
  private int portNumber = 8000;
  private int userCount = 0;
  
  public static void main(String[] args) {
    new ChatServer().go();
  }

  public void go() {
    constructGUI();
    clients = new ArrayList();

    try {
      serverSocket = new ServerSocket(portNumber);

      while (true) {
        Socket clientSocket = serverSocket.accept();
        messageLog.append("Client " + (++userCount) + " add in.\n");
        PrintWriter writer = 
          new PrintWriter(clientSocket.getOutputStream());
        clients.add(writer);
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        new Thread(clientHandler).start();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void constructGUI() {
    frame = new JFrame("Chat server");
    panel = new JPanel();
    messageLog = new JTextArea(6, 20);
    messageLog.setLineWrap(true);
    messageLog.setWrapStyleWord(true);
    messageLog.setEditable(false);
    scroll = new JScrollPane(messageLog, 
      ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    panel.add(BorderLayout.CENTER, scroll);
    frame.getContentPane().add(BorderLayout.CENTER, panel);
    frame.setSize(300, 180);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private void tellEveryClient(String message) throws IOException {
    Iterator i = clients.iterator();
    while (i.hasNext()) {
      PrintWriter printWriter = (PrintWriter)i.next();
      printWriter.println(message);
      printWriter.flush();
    }
  }

  private class ClientHandler implements Runnable {
    private BufferedReader reader;
    private Socket socket;

    public ClientHandler(Socket clientSocket) {
      try {
        socket = clientSocket;
        InputStreamReader inputStreamReader = 
          new InputStreamReader(socket.getInputStream());
        reader = new BufferedReader(inputStreamReader);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public void run() {
      String message;

      try {
        while ((message = reader.readLine()) != null) {
          tellEveryClient(message);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
