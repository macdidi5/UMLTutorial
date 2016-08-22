public abstract class WaterContainer {
  private int capacity;
  private boolean isReady;
  private boolean isBrewing;
  private boolean isHeating;
  static final int MAX_CAPACITY = 50;
  static final int MIN_CAPACITY = 10;

  public WaterContainer(int capacity) {
    if (capacity <= MAX_CAPACITY) {
      this.capacity = capacity;
    } else {
      this.capacity = MAX_CAPACITY;
    }

    isReady = true;
    isBrewing = false;
    isHeating = false;
  }

  public int getCapacity() {
    return capacity;
  }

  public boolean isFull() {
    return (capacity == MAX_CAPACITY);
  }

  public boolean lowCapacity() {
    return (capacity < MIN_CAPACITY);
  }

  public synchronized void addWater(int water) {
    if ((water + capacity) < MAX_CAPACITY) {
      capacity += water;
    } else {
      capacity = MAX_CAPACITY;
    }
  }

  public boolean isReady() {
    return isReady;
  }

  public void setIsReady(boolean isReady) {
    this.isReady = isReady;
  }

  public boolean isBrewing() {
    return isBrewing;
  }

  public void setIsBrewing(boolean isBrewing) {
    this.isBrewing = isBrewing;
  }

  public boolean isHeating() {
    return isHeating;
  }

  public void setIsHeating(boolean isHeating) {
    this.isHeating = isHeating;
  }

  public synchronized int useWater(int water) {
    if (capacity >= water) {
      capacity -= water;
      return water;
    } else {
      isReady = false;
      return 0;
    }
  }

  public abstract int getTemperature();
  public abstract void reBrew();
}
