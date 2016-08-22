public class ObjectCount {
  private int count = 0;
  private static int objectSerial = 0;

  public ObjectCount() {
    objectSerial++;
    count = objectSerial;
  }

  public String toString() {
    return "ObjectCount: " + count;
  }

  public static void main(String args[]) {
    System.out.println(new ObjectCount());
    System.out.println(new ObjectCount());
    System.out.println(new ObjectCount());
  }
}
