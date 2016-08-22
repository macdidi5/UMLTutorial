import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CalculatorGUI {
  private JButton btn0, btn1, btn2, btn3, btn4;
  private JButton btn5, btn6, btn7, btn8, btn9;
  private JButton btnEqual, btnPlus, btnMinus;
  private JButton btnPeriod, btnMult, btnDiv;
  private JPanel buttonPane;
  private JPanel calculatorPane;
  private SwingScreen screen;
  private Calculator calculator;
  private boolean isReady;

  public CalculatorGUI() {
    calculator = new Calculator();
    isReady = true;
    screen = new CalculatorScreen("0.0", JLabel.RIGHT);
    btn0 = new JButton("0");
    btn1 = new JButton("1");
    btn2 = new JButton("2");
    btn3 = new JButton("3");
    btn4 = new JButton("4");
    btn5 = new JButton("5");
    btn6 = new JButton("6");
    btn7 = new JButton("7");
    btn8 = new JButton("8");
    btn9 = new JButton("9");
    btnEqual = new JButton("=");
    btnPlus = new JButton("+");
    btnMinus = new JButton("-");
    btnMult = new JButton("*");
    btnDiv = new JButton("/");
    btnPeriod = new JButton(".");
    buttonPane = new JPanel();
    calculatorPane = new JPanel();
  }

  public JPanel getCalculatorPane() {
    buttonPane.setLayout(new GridLayout(4, 4));

    buttonPane.add(btn7);
    buttonPane.add(btn8);
    buttonPane.add(btn9);
    buttonPane.add(btnDiv);
    buttonPane.add(btn4);
    buttonPane.add(btn5);
    buttonPane.add(btn6);
    buttonPane.add(btnMult);
    buttonPane.add(btn1);
    buttonPane.add(btn2);
    buttonPane.add(btn3);
    buttonPane.add(btnMinus);
    buttonPane.add(btn0);
    buttonPane.add(btnPeriod);
    buttonPane.add(btnEqual);
    buttonPane.add(btnPlus);

    OperationHanlder operationHanlder = new OperationHanlder();
    NonOperationHanlder nonOperationHanlder = new NonOperationHanlder();

    btn0.addActionListener(nonOperationHanlder);
    btn1.addActionListener(nonOperationHanlder);
    btn2.addActionListener(nonOperationHanlder);
    btn3.addActionListener(nonOperationHanlder);
    btn4.addActionListener(nonOperationHanlder);
    btn5.addActionListener(nonOperationHanlder);
    btn6.addActionListener(nonOperationHanlder);
    btn7.addActionListener(nonOperationHanlder);
    btn8.addActionListener(nonOperationHanlder);
    btn9.addActionListener(nonOperationHanlder);
    btnPeriod.addActionListener(nonOperationHanlder);
    btnPlus.addActionListener(operationHanlder);
    btnMinus.addActionListener(operationHanlder);
    btnMult.addActionListener(operationHanlder);
    btnDiv.addActionListener(operationHanlder);
    btnEqual.addActionListener(operationHanlder);

    calculatorPane.setLayout(new BorderLayout());
    calculatorPane.add(screen, BorderLayout.NORTH);
    calculatorPane.add(buttonPane, BorderLayout.CENTER);

    return calculatorPane;
  }

  private class OperationHanlder implements ActionListener {
    public void actionPerformed(ActionEvent event) {
      char  operator = event.getActionCommand().charAt(0);
      String result = "";
      switch  (operator) {
      case '+':
        result = calculator.opAdd(screen.getScreen());
        break;
      case '-':
        result = calculator.opSubtract(screen.getScreen());
        break;
      case '*':
        result = calculator.opMultiply(screen.getScreen());
        break;
      case '/':
        result = calculator.opDivide(screen.getScreen());
        break;
      case '=':
        result = calculator.opEquals(screen.getScreen());
        break;
      }
      screen.display(result);
      isReady = true;
    }
  }

  private class NonOperationHanlder implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      if (isReady) {
        screen.display(ae.getActionCommand());
        isReady = false;
      } else {
        screen.display(screen.getScreen() + 
          ae.getActionCommand().charAt(0));
      }
    }
  }
}
