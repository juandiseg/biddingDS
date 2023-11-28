
import java.util.HashMap;

public class UserManager {
    private static HashMap<String, User> users = new HashMap<>();

    public static User addUser(String username, String email, String password, char userType) {
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

    public static boolean usernamePresent(String username) {
        return users.get(username) != null;
    }

    public static char validateUser(String username, String password) {
        User temp = users.get(username);
        if (temp != null && temp.getPassword().equals(password)) {
            return temp.getUserType();
        }
        return 'E';
    }

    public static boolean validateUser(User user, char type) {
        User temp = users.get(user.getUsername());
        return user.equals(temp) && user.getUserType() == type;
    }

}