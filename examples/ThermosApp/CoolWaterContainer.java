public class CoolWaterContainer extends WaterContainer {
  public CoolWaterContainer(int capacity) {
    super(capacity);
  }

  public int getTemperature() {
    return 28;
  }
  
  public void reBrew() {
    // Do nothing
  }
}
