public class HotWaterContainer extends WaterContainer {
  private int temperature;
  static final int MIN_TEMPERATURE = 80;
  static final int MAX_TEMPERATURE = 100;
  
  public HotWaterContainer(int capacity) {
    super(capacity);
    temperature = MAX_TEMPERATURE;
    new Thread(new DropTemperature()).start();
  }

  public int getTemperature() {
    return temperature;
  }
  
  public void reBrew() {
    new Thread(new ReBrew()).start();
  }

  public synchronized void addWater(int water) {
    temperature = averageTemp(super.getCapacity(), temperature,
                              water, 100);
    super.addWater(water);
  }
  
  private int averageTemp(int oldCap, int oldTemp,
                          int newCap, int newTemp) {
    int result = 0;
    result = ((oldCap * oldTemp) + (newCap * newTemp))
             / (oldCap + newCap);
    return result;
  }

  public class ReBrew implements Runnable {
    public void run() {
      setIsReady(false);
      setIsBrewing(true);

      for (; temperature < MAX_TEMPERATURE; ) {
        try {
          Thread.sleep(100);
          temperature++;
        } catch (Exception ex) {
          //
        }
      }

      setIsReady(true);
      setIsBrewing(false);
    }
  }

  public class DropTemperature implements Runnable {
    public void run() {
      while (true) {
        try {
          Thread.sleep(1000);
          temperature--;
        } catch (Exception ex) {
          //
        }

        if (temperature < MIN_TEMPERATURE) {
          reBrew();
        }
      }
    }
  }
}
