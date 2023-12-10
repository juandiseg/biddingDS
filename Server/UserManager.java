
import java.util.ArrayList;
import java.util.HashMap;

public class UserManager {

    private final HashMap<String, User> users = ServerReplication.getUserState();
    private ArrayList<MethodCaller> RPCallsToMake = new ArrayList<>();
    private final String className = "UserManager";

    // ACTIONS

    public User logIn(String username, String password, char type) {
        User temp = getUser(username);
        if (validateCredentials(temp, username, password) == type) {
            return temp;
        }
        return null;
    }

    public User signUp(boolean makeCall, String username, String email, String password, char userType) {
        if (makeCall) {
            Object[] args = { false, username, email, password, userType };
            RPCallsToMake.add(new MethodCaller(className, "signUp", args));
        }
        User temp = new User(username, email, password, userType);
        if (isEmailTaken(email) || users.get(username) != null) {
            return null;
        }
        users.put(username, temp);
        return temp;
    }

    // GETTERS

    public User getUser(String username) {
        return users.get(username);
    }

    // CHECKS

    public boolean doesUsernameExist(String username) {
        return users.get(username) != null;
    }

    private boolean isEmailTaken(String email) {
        for (String tempKey : users.keySet()) {
            if (users.get(tempKey).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    private char validateCredentials(User temp, String username, String password) {
        if (temp != null && temp.getPassword().equals(password)) {
            return temp.getUserType();
        }
        return 'E';
    }

    // REMOTE PROCEDURE CALLS

    public ArrayList<MethodCaller> getRPCallsToMake() {
        return RPCallsToMake;
    }

    public void emptyRPCallsToMake() {
        RPCallsToMake.clear();
    }

}