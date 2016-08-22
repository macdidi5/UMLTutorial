public class BTreeMap {
  BTreeMapNode topNode = null;

  public void add(Comparable key, Object value) {
    if (topNode == null) {
      topNode = new BTreeMapNode(key, value);
    } else {
      topNode.add(key, value);
    }
  }

  public Object get(Comparable key) {
    Object result = null;

    if (topNode != null) {
      result = topNode.find(key);
    }

    return result;
  }

  public static void main(String args[]) {
    BTreeMap btm = new BTreeMap();
    btm.add("5", "Five");  
    btm.add("8", "Eight");  
    btm.add("9", "Eight");
    btm.add("2", "Eight");  
    btm.add("32", "Eight");

    System.out.println(btm.get("9"));
  }
}
