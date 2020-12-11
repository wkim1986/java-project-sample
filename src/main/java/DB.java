import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DB<T> {
    private final String PATH;

    private Map<String, T> map;

    protected DB(String path) {
        PATH = path;
        init();
    }

    private void init() {
        try (FileReader fr = new FileReader(PATH)) {
            Gson gson = new GsonBuilder().create();
            JsonReader reader = gson.newJsonReader(fr);

            map = gson.fromJson(reader, getTypeToken());

            reader.close();
        } catch (IOException ignored) {
        }
    }

    protected abstract Type getTypeToken();

    public List<T> select() {
        return new ArrayList<>(map.values());
    }

    public T select(String key) {
        return map.get(key);
    }

    public boolean insert(String key, T obj) {
        return update(key, obj);
    }

    public boolean update(String key, T obj) {
        map.put(key, obj);
        return write();
    }

    public boolean delete(String key) {
        T obj = map.remove(key);
        boolean result = write();
        if (!result) map.put(key, obj);
        return result;
    }

    private boolean write() {
        try (
                FileWriter fw = new FileWriter(PATH);
                BufferedWriter bw = new BufferedWriter(fw)
        ) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonWriter writer = gson.newJsonWriter(bw);

            Type type = new TypeToken<Map<String, T>>() {
            }.getType();
            gson.toJson(map, type, writer);

            writer.flush();
            writer.close();

            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}
