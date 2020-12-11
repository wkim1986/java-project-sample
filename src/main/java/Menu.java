import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import log.ConsoleColors;
import log.Logger;
import patterns.IObserver;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

public class Menu implements IObserver {
    private final static String MENU_FILE_PATH = Path.of("jsons", "menu.json").toString();
    private final static String MEMBER_FILE_PATH = Path.of("jsons", "member.json").toString();

    private static class Instance {
        private static final Menu instance = new Menu();
    }

    public static Menu shared() {
        return Menu.Instance.instance;
    }


    private final static Map<String, Object> returnMenu = new HashMap<>();
    static {
        returnMenu.put("Class", "Menu");
        returnMenu.put("Method", "_return");
    }


    private Menu() {
        init(MENU_FILE_PATH);
        Login.shared().attach(this);
    }

    private Map<String, ?> menus;

    private void init(String path) {
        try (FileReader fr = new FileReader(path)) {
            Gson gson = new GsonBuilder().create();
            JsonReader reader = gson.newJsonReader(fr);
            menus = gson.fromJson(reader, LinkedHashMap.class);
            reader.close();
        } catch (IOException ignored) {
        }
    }

    public void exec() {
        exec(menus, Login.shared().isLogin() ? "Member menu" : "Not-member menu");
    }

    private void exec(Map<String, ?> menu, String title) {
        List<String> keys = new ArrayList<>(menu.keySet());
        print(keys, title);
        int n = getNumber(keys.size());

        String key = keys.get(n);
        Map<String, Object> m = (Map<String, Object>) menu.get(key);
        if (m.containsKey("Class")) {
            String cls = m.get("Class").toString();
            String method = m.get("Method").toString();

            try {
                Class.forName(cls).getDeclaredMethod(method).invoke(null);
            } catch (Exception e) {
                Logger.error(e.toString());
            }
        } else {
            m.put("처음으로 돌아가기", returnMenu);
            exec(m, key);
        }
    }

    private void print(List<String> list, String title) {
        Logger.println(ConsoleColors.BLACK_BOLD, String.format("\n[%s]", title));
        IntStream.range(0, list.size()).forEach(i -> {
            String key = list.get(i);
            System.out.printf("%d: %s\n", i + 1, key);
        });
    }

    private int getNumber(int len) {
        Scanner sc = new Scanner(System.in);

        int N;
        while (true) {
            try {
                Logger.print(ConsoleColors.CYAN, "입력: ");
                N = sc.nextInt();
                if (1 <= N && N <= len) break;
            } catch (Exception ignore) {
            } finally {
                sc.nextLine();
            }
        }

        return N - 1;
    }


    @Override
    public void update() {
        init(Login.shared().isLogin() ? MEMBER_FILE_PATH : MENU_FILE_PATH);
    }


    public static void _return() {
        Menu.shared().exec();
    }

    public static void _exit() {
        System.exit(0);
    }
}
