public class TestThermos {
  public static void main(String[] args) {
    HotWaterContainer h = new HotWaterContainer(2);
    CoolWaterContainer c = new CoolWaterContainer(50);
    ThermosGui g = new ThermosGui();
    Thermos t = new Thermos(h, c, g);
  }
}
