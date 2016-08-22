import java.util.List;
import java.util.ArrayList;

public class MyMap {
  private List keyList = new ArrayList();
  private List valueList = new ArrayList();

  public void add(Object key, Object value) {
    if (keyList.size() == 0 || !keyList.contains(key)) {
      keyList.add(key);
      valueList.add(value);
    }
  }

  public Object get(Object key) {
    Object result;
    int index = 0;

    if ((index = keyList.indexOf(key)) != -1) {
      result = valueList.get(index);
    } else {
      result = null;
    }

    return result;
  }

  public Object remove(Object key) {
    Object result;
    int index = 0;

    if ((index = keyList.indexOf(key)) != -1) {
      keyList.remove(index);
      result = valueList.remove(index);
    } else {
      result = null;
    }

    return result;
  }
}
