
import java.util.HashMap;
import java.util.LinkedList;

public class UserManager {

    private static final HashMap<String, User> users = ServerReplication.getUserState();
    private static final HashMap<String, User> unsynchronized = new HashMap<String, User>();

    public static User signUp(String username, String email, String password, char userType) {
        User temp = new User(username, email, password, userType);
        if (emailTaken(email) || users.get(username) != null) {
            return null;
        }
        users.put(username, temp);
        unsynchronized.put(temp.getUsername(), temp);
        return temp;
    }

    public static LinkedList<User> getSystemUsers() {
        LinkedList<User> temp = new LinkedList<>(users.values());
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

    public static synchronized HashMap<String, User> getUnsyncronizedUsers() {
        return UserManager.unsynchronized;
    }

    public static synchronized void cleanUnsynchronizedUsers() {
        unsynchronized.clear();
    }

    public synchronized static void importUnsyncronizedUsers(LinkedList<User> temp) {
        for (User user : temp) {
            users.put(user.getUsername(), user);
        }
    }

    public synchronized static void importUnsyncronizedUsers(HashMap<String, User> temp) {
        users.putAll(temp);
    }

    public static User logIn(String username, String password, char type) {
        User temp = UserManager.getUser(username);
        if (UserManager.validateCredentials(temp, username, password) == type) {
            return temp;
        }
        return null;
    }

    private static char validateCredentials(User temp, String username, String password) {
        if (temp != null && temp.getPassword().equals(password)) {
            return temp.getUserType();
        }
        return 'E';
    }

}