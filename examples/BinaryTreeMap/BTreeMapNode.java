public class BTreeMapNode {
  private Comparable key;
  private Object value;
  private BTreeMapNode nodes[] = new BTreeMapNode[2];

  public BTreeMapNode(Comparable key, Object value) {
    this.key = key;
    this.value = value;
  }

  public Object find(Comparable key) {
    System.out.println("Find");
    if (this.key.compareTo(key) == 0) {
      return this.value;
    } else {
      return findSubNodeForKey(selectSubNode(key), key);
    }
  }

  private int selectSubNode(Comparable key) {
    int result = 0;

    if (key.compareTo(this.key) >= 0){
      result = 1;
    }

    return result;
  }

  private Object findSubNodeForKey(int node, Comparable key) {
    if (nodes[node] == null) {
      return null;
    } else {
      return nodes[node].find(key);
    }
  }

  public void add(Comparable key, Object value) {
    if (key.compareTo(this.key) == 0) {
      this.value = value;
    } else {
      addSubNode(selectSubNode(key), key, value);
    }
  }

  private void addSubNode(int node, Comparable key, Object value) {
    if (nodes[node] == null) {
      nodes[node] = new BTreeMapNode(key, value);
    } else {
      nodes[node].add(key, value);
    }
  }
}
