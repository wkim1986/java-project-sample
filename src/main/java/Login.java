import log.Logger;
import patterns.Subject;

public class Login extends Subject {
    private static class Instance {
        private static final Login instance = new Login();
    }

    public static Login shared() {
        return Instance.instance;
    }


    private User user;

    private Login() {
    }


    public boolean isLogin() {
        return user != null;
    }

    public User getUser() {
        if (user == null)
            Logger.fail("로그인되어 있지 않습니다.");
        return user;
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean login(String userId, String password) {
        try {
            User user = UserDB.shared().select(userId);
            if (user == null)
                throw new Exception("아이디가 올바르지 않습니다.");

            if (!user.authenticate(password))
                throw new Exception("비밀번호가 올바르지 않습니다.");

            this.user = user;
            notifyObservers();

            Logger.success(String.format("%s (%s)님 환영합니다.", user.getName(), user.getId()));
            return true;
        } catch (Exception e) {
            Logger.error(e.getLocalizedMessage());
            return false;
        }
    }

    public void logout() {
        user = null;
        notifyObservers();
    }
}
