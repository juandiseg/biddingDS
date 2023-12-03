
import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.rmi.Remote;

/*  Functionalities common to all users.*/
public interface iUser extends Remote {

    // LOG-IN & SIGN-UP PROCESS.
    public User logIn(String username, String password) throws RemoteException;

    public User signUp(String username, String email, String password) throws RemoteException;

    public boolean doesUsernameExist(String username) throws RemoteException;

    // DIGITAL SIGNATURE
    public byte[] signDocument(byte[] dataToSign) throws Exception;

    public Certificate exportCertificate() throws Exception;

    // BROWSING / DISPLAY
    public String getBasicAuctionStatus(User user, int auctionID) throws RemoteException;

    public String getDoubleAuctionsDisplay(User user) throws RemoteException;

    public String getDoubleAuctionResolution(int auctionID, User user) throws RemoteException;
}
