import java.awt.*;
import javax.swing.*;

public class MyCalculator {
  public static void main(String args[]) {
    CalculatorGUI cg = new CalculatorGUI();
    JFrame frame = new JFrame("Calculator");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(250, 250);
    frame.getContentPane().add(
      cg.getCalculatorPane(), BorderLayout.CENTER);
    frame.setVisible (true);
  }
}
