import java.rmi.RemoteException;
import java.security.cert.Certificate;

public class buyerProxy implements iBuyer {

    private BasicAuctionManager basicAuctionMan;
    private DoubleAuctionManager doubleAuctionMan;
    private UserManager userMan;
    private KeyManager keyMan;

    public buyerProxy(BasicAuctionManager basicAuctionMan, DoubleAuctionManager doubleAuctionMan,
            UserManager userMan,
            KeyManager keyMan) {
        this.basicAuctionMan = basicAuctionMan;
        this.doubleAuctionMan = doubleAuctionMan;
        this.userMan = userMan;
        this.keyMan = keyMan;
    }

    @Override
    public Double normalAuctionBid(int auctionID, User user, Double bidAmount)
            throws RemoteException {
        return basicAuctionMan.bid(true, auctionID, user, bidAmount);
    }

    @Override
    public String getBasicAuctionStatus(User user, int auctionID) throws RemoteException {
        return basicAuctionMan.getStatusBuyer(user, auctionID);
    }

    @Override
    public String getBasicAuctionsDisplay(int itemID) throws RemoteException {
        return basicAuctionMan.getDisplay(itemID);
    }

    @Override
    public String getItemsDisplay() throws RemoteException {
        return basicAuctionMan.getItemsDisplay();
    }

    @Override
    public String getReversedBasicAuctionsDisplay(int itemID) throws RemoteException {
        return basicAuctionMan.getReversedDisplay(itemID);
    }

    @Override
    public String getDoubleAuctionsDisplay(User user) throws RemoteException {
        return doubleAuctionMan.getDoubleAuctionsDisplayBuyers(user);
    }

    @Override
    public boolean doubleAuctionBid(int auctionID, User user, double bidAmount) throws RemoteException {
        return doubleAuctionMan.bid(true, auctionID, user, bidAmount);
    }

    @Override
    public String getDoubleAuctionResolution(User user, int auctionID) throws RemoteException {
        return doubleAuctionMan.getResolutionBuyer(user, auctionID);
    }

    @Override
    public User logIn(String username, String password) throws RemoteException {
        return userMan.logIn(username, password, 'B');
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return userMan.signUp(true, username, email, password, 'B');
    }

    @Override
    public boolean doesUsernameExist(String username) throws RemoteException {
        return userMan.doesUsernameExist(username);
    }

    @Override
    public byte[] signDocument(byte[] dataToSign) throws Exception {
        return keyMan.signFile(dataToSign);
    }

    @Override
    public Certificate exportCertificate() throws Exception {
        return keyMan.exportCertificate();
    }

}
