import java.util.HashMap;

public class userManager {
    private static HashMap<String, user> users = new HashMap<>();

    public static int addUser(String username, String password, String userType) {
        user temp = new user(username, password, userType);
        users.put(username, temp);
        return temp.getUserID();
    }

    public static String validateUser(String username, String password) {
        user temp = users.get(username);
        if (temp.getPassword().equals(password)) {
            return temp.getUserType();
        }
        return "ERR";
    }

}

class user {
    private static int lastID = 0;
    private final int userID;
    private final String username;
    private String password;
    private String userType;

    public user(String username, String password, String userType) {
        user.lastID++;
        this.userID = user.lastID;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserType() {
        return this.userType;
    }

}
