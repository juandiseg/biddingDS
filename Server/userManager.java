
import org.jgroups.blocks.ReplicatedHashMap;

public class UserManager {
    private static ReplicatedHashMap<String, User> users = ServerReplication.userMap;

    public static User signUp(String username, String email, String password, char userType) {
        User temp = new User(username, email, password, userType);
        if (emailTaken(email) || users.get(username) != null) {
            return null;
        }
        users._put(username, temp);
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