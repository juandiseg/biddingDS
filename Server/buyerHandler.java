
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;

public class buyerHandler implements iBuyer {

    @Override
    public Double normalAuctionBid(int auctionID, User user, Double bidAmount) throws RemoteException {
        return NormalAuctionManager.bid(auctionID, user, bidAmount);
    }

    @Override
    public String checkAuctionStatus(User user, int auctionID) throws RemoteException {
        return NormalAuctionManager.checkAuctionStatusBuyer(user, auctionID);
    }

    @Override
    public String getAuctionsDisplay(int itemID) throws RemoteException {
        return NormalAuctionManager.getAuctionsDisplay(itemID);
    }

    @Override
    public String getItemsDisplay() throws RemoteException {
        return NormalAuctionManager.getItemsDisplay();
    }

    @Override
    public User logInUsername(String username, String password) throws RemoteException {
        if (UserManager.validateUserByUsername(username, password) == 'B')
            return UserManager.getUser(username);
        return null;
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return UserManager.addUser(username, email, password, 'B');
    }

    @Override
    public byte[] signDocument(byte[] dataToSign) throws Exception {
        return KeyManager.signFile(dataToSign);
    }

    @Override
    public Certificate exportCertificate() throws Exception {
        return KeyManager.exportCertificate();
    }

    @Override
    public boolean doesUsernameExist(String username) throws RemoteException {
        return UserManager.usernamePresent(username);
    }

    @Override
    public String viewReverseAuction(int itemID) throws RemoteException {
        return NormalAuctionManager.viewReverseAuction(itemID);
    }

    @Override
    public String viewDoubleAuctions(User user) throws RemoteException {
        return DoubleAuctionManager.viewDoubleAuctionsBuyers(user);
    }

    @Override
    public boolean doubleAuctionBid(int auctionID, User user, double bidAmount) throws RemoteException {
        return DoubleAuctionManager.bid(auctionID, user, bidAmount);
    }

    @Override
    public String checkDoubleAuctionResolution(int auctionID, User user) throws RemoteException {
        return DoubleAuctionManager.getResolutionBuyer(auctionID, user);
    }

}
