import log.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class User {
    private final String id;
    private int password;

    private final String name;
    private final String birth;

    public User(String id, String password, String name, String birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        changePassword(password);
    }

    @Override
    public String toString() {
        return Logger.toString(new String[] { "Id", "Name", "Birth", "Age" }, "%-10s", this);
    }

    public String getId() {
        return id;
    }

    public int getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public int getAge() {
        LocalDate date = LocalDate.parse(birth, DateTimeFormatter.ofPattern("yyyyMMdd"));
        return (int) ChronoUnit.YEARS.between(date, LocalDate.now()) + 1;
    }

    public void changePassword(String password) {
        this.password = password.hashCode();
    }

    public boolean authenticate(String password) {
        return this.password == password.hashCode();
    }
}
