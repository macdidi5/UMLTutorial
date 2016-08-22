import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ChatClient {
  private JFrame frame;
  private JPanel panel;
  private JTextArea incoming;
  private JTextField outGoing;
  private JTextField nickName;
  private JButton sendButton;
  private JScrollPane scroll;
  private BufferedReader reader;
  private PrintWriter writer;
  private Socket sock;
  private String remoteHost = "localhost";
  private int portNumber = 8000;

  public static void main(String[] args) {
    new ChatClient().go();
  }

  private void go() {
    getConnection();
    IncomingReader incomingReader = new IncomingReader();
    new Thread(incomingReader).start();
    constructGUI();
  }

  private void getConnection() {
    try {
      sock = new Socket(remoteHost, portNumber);
      reader = new BufferedReader(
        new InputStreamReader(sock.getInputStream()));
      writer = new PrintWriter(sock.getOutputStream());
    } catch (ConnectException ce) {
      System.out.println("Remote server is not ready.");
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(0);
    }
  }

  private void constructGUI() {
    frame = new JFrame("Chat client");
    panel = new JPanel();
    incoming = new JTextArea(15, 50);
    incoming.setLineWrap(true);
    incoming.setWrapStyleWord(true);
    incoming.setEditable(false);
    scroll = new JScrollPane(incoming, 
      ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    outGoing = new JTextField(20);
    nickName = new JTextField("default", 10);
    sendButton = new JButton("Send");
    sendButton.addActionListener(new SendButtonListener());
    outGoing.addKeyListener(new TextFieldListener());
    panel.add(scroll);
    panel.add(new JLabel("Nick:"));
    panel.add(nickName);
    panel.add(outGoing);
    panel.add(sendButton);
    frame.getContentPane().add(BorderLayout.CENTER, panel);
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private class SendButtonListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      sendMessage(nickName.getText() + ": " + 
        outGoing.getText());
    }
  }

  private void appendMessage(String message) {
    incoming.append(message);
  }

  private class TextFieldListener implements KeyListener {
    public void keyPressed(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e) {
      if (e.getKeyChar() == '\n') {
        sendMessage(nickName.getText() + ": " + 
          outGoing.getText());
      }
    }
  }

  private void sendMessage(String message) {
    try {
      writer.println(message);
      writer.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
    outGoing.setText("");
    outGoing.requestFocus();
  }

  private class IncomingReader implements Runnable {
    public void run() {
      String message;
      try {
        while ((message = reader.readLine()) != null) {
          appendMessage(message + "\n");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
