
public class User implements java.io.Serializable {
    private final String username;
    private final String email;
    private final char userType;
    private String password;

    public User(String username, String email, String password, char userType) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public char getUserType() {
        return this.userType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User temp = (User) obj;
            return temp.username.equals(this.username) && temp.email.equals(this.email)
                    && temp.password.equals(this.password)
                    && temp.userType == temp.userType;
        }
        return false;
    }

}