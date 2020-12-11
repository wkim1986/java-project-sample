import log.Logger;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;

public class MemberManager {
    private final static String REGEX_ID = "^[a-zA-Z][a-zA-Z0-9_]{3,11}$";
    private final static String REGEX_PW = "^(?=.*[a-zA-Z0-9])(?=\\S+$).{1,15}$";
    private final static String REGEX_NAME = "^.{2,15}$";
    private final static String REGEX_BIRTH = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$";


    private static boolean authPassword(String format) {
        System.out.println();
        Scanner sc = new Scanner(System.in);

        System.out.printf(format + ": ", "Password");
        String pw = sc.nextLine();

        if (Login.shared().getUser().authenticate(pw))
            return true;
        else {
            Logger.fail("비밀번호가 올바르지 않습니다.");
            return false;
        }
    }

    public static void login() {
        System.out.println();
        Scanner sc = new Scanner(System.in);

        String f = "%-10s";
        String[] fields = new String[]{"UserId", "Password"};
        String[] values = new String[fields.length];

        for (int i = 0; i < fields.length; ++i) {
            System.out.printf(f + ": ", fields[i]);
            values[i] = sc.nextLine();
        }

        Login.shared().login(values[0], values[1]);
    }

    public static void logout() {
        Login.shared().logout();
    }

    public static void register() {
        System.out.println();
        Scanner sc = new Scanner(System.in);

        String f = "%-10s";
        String[] fields = {"UserId", "Password", "Name", "Birth"};
        String[] values = new String[fields.length];
        Function<String, Boolean>[] constraints = new Function[]{
                str -> Pattern.matches(REGEX_ID, (CharSequence) str),
                str -> Pattern.matches(REGEX_PW, (CharSequence) str),
                str -> Pattern.matches(REGEX_NAME, (CharSequence) str),
                str -> Pattern.matches(REGEX_BIRTH, (CharSequence) str)
        };

        for (int i = 0; i < fields.length; ++i) {
            System.out.printf(f + ": ", fields[i]);
            values[i] = sc.nextLine();
            if (!constraints[i].apply(values[i])) --i;
        }

        User user = new User(values[0], values[1], values[2], values[3]);

        UserDB db = UserDB.shared();
        if (db.select(user.getId()) != null) Logger.fail("사용할 수 없는 아이디입니다.");
        else if (db.insert(user.getId(), user)) Logger.success("회원 가입에 성공하였습니다.");
        else Logger.fail("회원 가입에 실패하였습니다.");
    }

    public static void withdraw() {
        if (!authPassword("%-10s")) return;

        if (UserDB.shared().delete(Login.shared().getUser().getId())) {
            Login.shared().logout();
            Logger.success("회원 탈퇴 처리되었습니다.");
        } else
            Logger.fail("회원 탈퇴에 실패하였습니다.");
    }

    public static void changePassword() {
        String f = "%-15s";
        if (!authPassword(f)) return;

        User user = Login.shared().getUser();

        String pw;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.printf(f + ": ", "New password");
            pw = sc.nextLine();
        } while (!Pattern.matches(REGEX_PW, pw));

        user.changePassword(pw);
        if (UserDB.shared().update(user.getId(), user))
            Logger.success("비밀번호가 변경되었습니다.");
        else {
            user.changePassword(pw);
            Logger.fail("비밀번호 변경에 실패하였습니다.");
        }
    }

    public static void selectMember() {
        System.out.println();
        Scanner sc = new Scanner(System.in);

        String f = "%-10s";
        System.out.printf(f + ": ", "UserId");
        String userId = sc.nextLine();

        User user = UserDB.shared().select(userId);

        if (user == null) Logger.fail("회원 정보를 찾을 수 없었습니다.");
        else System.out.println("\n" + user);
    }

    public static void selectMembers() {
        List<User> users = UserDB.shared().select();
        if (users.size() == 0) Logger.fail("회원이 없습니다.");
        else users.forEach(user -> System.out.println("\n" + user.toString()));
    }
}
