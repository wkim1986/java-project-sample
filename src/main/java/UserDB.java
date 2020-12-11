import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Map;

public class UserDB extends DB<User> {
    private final static String PATH = Path.of("jsons", "user.json").toString();

    private UserDB() {
        super(PATH);
    }

    private static class Instance {
        private static final UserDB instance = new UserDB();
    }

    public static UserDB shared() {
        return Instance.instance;
    }

    protected Type getTypeToken() {
        return new TypeToken<Map<String, User>>() {}.getType();
    }
}
