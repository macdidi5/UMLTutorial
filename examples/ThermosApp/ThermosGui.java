import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ThermosGui {
  private JFrame frame;
  private Container container;
  private JPanel infoPanel, btnPanel, statusPanel;
  private JLabel hot, cool, capacity, temperature;
  private JTextField hotCapacity, coolCapacity, hotTemperature;
  private JButton addCoolWater, reBrew, coolToHot, useHotWater;
  private JCheckBox lowHot, lowCool, brewing, heating;

  public ThermosGui() {
    buildGui();
  }

  private void buildGui() {
    frame = new JFrame("Java熱水瓶");
    statusPanel = new JPanel();
    lowHot = new JCheckBox("熱水太少了");
    lowCool = new JCheckBox("冷水太少了");
    brewing = new JCheckBox("煮沸中...");
    heating = new JCheckBox("冷水加到熱水中...");
    statusPanel.setLayout(new FlowLayout());
    statusPanel.add(lowHot);
    statusPanel.add(lowCool);
    statusPanel.add(brewing);
    statusPanel.add(heating);
    
    infoPanel = new JPanel();
    hot = new JLabel("熱");
    cool = new JLabel("冷");
    capacity = new JLabel("容量");
    temperature = new JLabel("溫度");
    hotCapacity = new JTextField("0", 10);
    coolCapacity = new JTextField("0", 10);
    hotTemperature = new JTextField("0", 10);
    infoPanel.setLayout(new GridLayout(3, 3));
    infoPanel.add(new JLabel(""));
    infoPanel.add(hot);
    infoPanel.add(cool);
    infoPanel.add(capacity);
    infoPanel.add(hotCapacity);
    infoPanel.add(coolCapacity);
    infoPanel.add(temperature);
    infoPanel.add(hotTemperature);
    infoPanel.add(new JLabel(""));
    
    btnPanel = new JPanel();
    addCoolWater = new JButton("加冷水");
    reBrew = new JButton("再沸騰");
    coolToHot = new JButton("冷水加到熱水");
    useHotWater = new JButton("使用熱水");
    btnPanel.setLayout(new FlowLayout());
    btnPanel.add(addCoolWater);
    btnPanel.add(reBrew);
    btnPanel.add(coolToHot);
    btnPanel.add(useHotWater);
    
    container = frame.getContentPane();
    container.add(statusPanel, BorderLayout.NORTH);
    container.add(infoPanel, BorderLayout.CENTER);
    container.add(btnPanel, BorderLayout.SOUTH);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void showInformation(int hotCapacity, int coolCapacity, 
                         int hotTemperature) {
    this.hotCapacity.setText(Integer.toString(hotCapacity));
    this.coolCapacity.setText(Integer.toString(coolCapacity));
    this.hotTemperature.setText(Integer.toString(hotTemperature));
  }

  public void showStatus(boolean status[]) {
    lowHot.setSelected(status[0]);
    lowCool.setSelected(status[1]);
    brewing.setSelected(status[2]);
    heating.setSelected(status[3]);
  }
  
  public void addListener(ActionListener a[]) {
    addCoolWater.addActionListener(a[0]);
    reBrew.addActionListener(a[1]);
    coolToHot.addActionListener(a[2]);
    useHotWater.addActionListener(a[3]);
  }
}
