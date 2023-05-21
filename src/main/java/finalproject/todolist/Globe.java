package finalproject.todolist;

import java.util.HashMap;
public class Globe {
    private HashMap<String,Object> globeList;

    public Object get(String key) {
        return globeList.get(key);
    }

    public void add(String key,Object object) {
        globeList.put(key, object);
    }

    public void remove(String key) {
        globeList.remove(key);
    }

    public void clear() {
        globeList.clear();
    }

    private static Globe instance;

    private Globe() {
        globeList = new HashMap<>();
    }

    public static Globe getInstance() {
        if (instance == null)
            instance = new Globe();
        return instance;
    }
}