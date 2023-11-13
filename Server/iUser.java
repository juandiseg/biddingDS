
import java.rmi.RemoteException;
import java.rmi.Remote;

public interface iUser extends Remote {
    public User logInUsername(String username, String password) throws RemoteException;

    public User signUp(String username, String email, String password) throws RemoteException;

    public boolean doesUsernameExist(String username) throws RemoteException;
}
