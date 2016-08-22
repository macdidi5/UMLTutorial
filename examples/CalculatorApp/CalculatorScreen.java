public class CalculatorScreen extends SwingScreen {
  public CalculatorScreen(String s) {
    super(s);
  }

  public CalculatorScreen(String s, int align) {
    super(s, align);
  }

  public void display(String s) {
    setText(s);
  }

  public String getScreen() {
    return getText();
  }
}
