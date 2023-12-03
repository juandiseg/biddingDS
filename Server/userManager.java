
import java.util.HashMap;

public class UserManager {
    private static HashMap<String, User> users = new HashMap<>();

    public static User signUp(String username, String email, String password, char userType) {
        User temp = new User(username, email, password, userType);
        if (emailTaken(email) || users.get(username) != null) {
            return null;
        }
        users.put(username, temp);
        return temp;
    }

    private static boolean emailTaken(String email) {
        for (String tempKey : users.keySet()) {
            if (users.get(tempKey).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public static User getUser(String username) {
        return users.get(username);
    }

    public static boolean doesUsernameExist(String username) {
        return users.get(username) != null;
    }

    public static User logIn(String username, String password, char type) {
        if (UserManager.validateCredentials(username, password) == 'B')
            return UserManager.getUser(username);
        return null;
    }

    private static char validateCredentials(String username, String password) {
        User temp = users.get(username);
        if (temp != null && temp.getPassword().equals(password)) {
            return temp.getUserType();
        }
        return 'E';
    }

}