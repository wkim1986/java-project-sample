package log;

import java.lang.reflect.Method;

public class Logger {
    public static boolean COLOR = false;

    public static void print(String color, String str) {
        if (COLOR) System.out.printf("%s%s%s", color, str, ConsoleColors.RESET);
        else System.out.print(str);
    }

    public static void println(String color, String str) {
        print(color, str + "\n");
    }

    public static String toString(String[] props, String format, Object obj) {
        StringBuilder sb = new StringBuilder();
        for (String prop : props) {
            try {
                Method m = obj.getClass().getDeclaredMethod("get" + prop);
                sb.append(String.format("\n" + format + ": %s", prop, m.invoke(obj)));
            } catch (Exception ignored) {}
        }

        return sb.toString().replaceFirst("\n", "");
    }

    public static void success(String str) {
        println(ConsoleColors.PURPLE, str);
    }

    public static void fail(String str) {
        println(ConsoleColors.RED, str);
    }

    public static void error(String str) {
        println(ConsoleColors.RED, str);
    }
}
