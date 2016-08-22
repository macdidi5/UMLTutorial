import javax.swing.*;

public abstract class SwingScreen extends JLabel implements Screen {
  public SwingScreen(String s) {
    super(s);
  }

  public SwingScreen(String s, int align) {
    super(s, align);
  }
}
