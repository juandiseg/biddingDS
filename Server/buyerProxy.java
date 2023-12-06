import java.rmi.RemoteException;
import java.security.cert.Certificate;

public class buyerProxy implements iBuyer {

    @Override
    public Double normalAuctionBid(int auctionID, User user, Double bidAmount) throws RemoteException {
        return BasicAuctionManager.bid(auctionID, user, bidAmount);
    }

    @Override
    public String getBasicAuctionStatus(User user, int auctionID) throws RemoteException {
        return BasicAuctionManager.getStatusBuyer(user, auctionID);
    }

    @Override
    public String getBasicAuctionsDisplay(int itemID) throws RemoteException {
        return BasicAuctionManager.getDisplay(itemID);
    }

    @Override
    public String getItemsDisplay() throws RemoteException {
        return BasicAuctionManager.getItemsDisplay();
    }

    @Override
    public User logIn(String username, String password) throws RemoteException {
        return UserManager.logIn(username, password, 'B');
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return UserManager.signUp(username, email, password, 'B');
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
        return UserManager.doesUsernameExist(username);
    }

    @Override
    public String getReversedBasicAuctionsDisplay(int itemID) throws RemoteException {
        return BasicAuctionManager.getReversedDisplay(itemID);
    }

    @Override
    public String getDoubleAuctionsDisplay(User user) throws RemoteException {
        return DoubleAuctionManager.getDoubleAuctionsDisplayBuyers(user);
    }

    @Override
    public boolean doubleAuctionBid(int auctionID, User user, double bidAmount) throws RemoteException {
        return DoubleAuctionManager.bid(auctionID, user, bidAmount);
    }

    @Override
    public String getDoubleAuctionResolution(User user, int auctionID) throws RemoteException {
        return DoubleAuctionManager.getResolutionBuyer(user, auctionID);
    }

}
