package todo.list.api.controller.account.helper;

public class PasswordHelper {
    public static String createSalt() {
        int random = (int) (Math.random() * 999999999L) + 1;
        return Integer.toString(random);
    }
}
