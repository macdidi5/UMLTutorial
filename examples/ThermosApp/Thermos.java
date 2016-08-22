import java.awt.event.*;

public class Thermos {
  private WaterContainer hot;
  private WaterContainer cool;
  private ThermosGui gui;

  public Thermos(WaterContainer hot, 
                 WaterContainer cool,
                 ThermosGui gui) {
    this.hot = hot;
    this.cool = cool;
    this.gui = gui;

    gui.addListener(new ActionListener[] {
      new AddCoolWaterListener(),
      new ReBrewListener(),
      new CoolToHotListener(),
      new UseHotWaterListener()
    });

    new Thread(new RefreshInfoPanel()).start();
  }

  public void addCoolWater() {
    cool.addWater(CoolWaterContainer.MAX_CAPACITY);
  }

  public void coolToHot() {
    new Thread(new CoolToHot()).start();
  }
    
  public class CoolToHot implements Runnable {
    public void run() {
      cool.setIsHeating(true);
      while (cool.isReady() && !hot.isFull()) {
        int water = cool.useWater(1);
        hot.addWater(Heater.brew(water));
      }
      cool.setIsHeating(false);
    }
  }

  public class RefreshInfoPanel implements Runnable {
    public void run() {
      boolean status[] = new boolean[4];

      while (true) {
        try {
          Thread.sleep(200);
        } catch (Exception ex) {
        
        }
        gui.showInformation(hot.getCapacity(), 
                            cool.getCapacity(), 
                            hot.getTemperature());

        status[0] = hot.lowCapacity();
        status[1] = cool.lowCapacity();
        status[2] = hot.isBrewing();
        status[3] = cool.isHeating();
        gui.showStatus(status);
      }
    }
  }

  public class AddCoolWaterListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      addCoolWater();
    }
  }

  public class ReBrewListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      hot.reBrew();
    }
  }

  public class CoolToHotListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      coolToHot();
    }
  }

  public class UseHotWaterListener implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      hot.useWater(1);
    }
  }
}
