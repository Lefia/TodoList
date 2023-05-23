package finalproject.todolist;

import java.util.HashMap;
public class Globe {
    private final HashMap<String,Object> globeList;

    public Object get(String key) {
        return globeList.get(key);
    }

    public void put(String key, Object object) {
        globeList.put(key, object);
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