
import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.rmi.Remote;

public interface iUser extends Remote {
    public User logInUsername(String username, String password) throws RemoteException;

    public User signUp(String username, String email, String password) throws RemoteException;

    public boolean doesUsernameExist(String username) throws RemoteException;

    public String checkDoubleAuctionResolution(int auctionID, User user) throws RemoteException;

    public byte[] signDocument(byte[] dataToSign) throws Exception;

    public Certificate exportCertificate() throws Exception;

}
