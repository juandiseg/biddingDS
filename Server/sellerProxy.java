import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.util.HashMap;

public class sellerProxy implements iSeller {

    private BasicAuctionManager basicAuctionMan;
    private DoubleAuctionManager doubleAuctionMan;
    private UserManager userMan;
    private KeyManager keyMan;

    public sellerProxy(BasicAuctionManager basicAuctionMan, DoubleAuctionManager doubleAuctionMan,
            UserManager userMan,
            KeyManager keyMan) {
        this.basicAuctionMan = basicAuctionMan;
        this.doubleAuctionMan = doubleAuctionMan;
        this.userMan = userMan;
        this.keyMan = keyMan;
    }

    @Override
    public HashMap<Integer, AuctionItem> getSpec(User user, int itemId) throws RemoteException {
        return basicAuctionMan.getSpec(user, itemId);
    }

    @Override
    public int createBasicAuction(String sellerUsername, int itemID, String itemTitle, String itemDescription,
            int itemCondition, Double reservePrice, Double sellingPrice) throws RemoteException {
        return basicAuctionMan.add(true,
                new AuctionItem(userMan.getUser(sellerUsername), itemID, itemTitle, itemDescription,
                        itemCondition, sellingPrice),
                reservePrice);
    }

    @Override
    public String closeBasicAuction(User user, int auctionID) throws RemoteException {
        return basicAuctionMan.close(true, user, auctionID);
    }

    @Override
    public String closeBasicAuctionAndApproveWinner(int auctionID, boolean isApproved)
            throws RemoteException {
        return basicAuctionMan.closeAndApproveWinner(true, auctionID, isApproved);
    }

    @Override
    public String getBasicAuctionStatus(User user, int auctionID) throws RemoteException {
        return basicAuctionMan.getStatusSeller(user, auctionID);
    }

    @Override
    public String getDisplayAvailableItems() throws RemoteException {
        return basicAuctionMan.getDisplayAvailableItems();
    }

    @Override
    public int createDoubleAuction(int itemID, int limitSellers, int limitBids) {
        return doubleAuctionMan.createDoubleAuction(true, itemID, limitSellers, limitBids);
    }

    @Override
    public void joinDoubleAuction(int doubleAuctionID, AuctionItem newItem) {
        doubleAuctionMan.joinDoubleAuction(true, doubleAuctionID, newItem);
    }

    @Override
    public int getDoubleAuctionsItemID(int auctionID) {
        return doubleAuctionMan.getDoubleAuctionsItemID(auctionID);
    }

    @Override
    public boolean isSellersLimitReached(int auctionID) {
        return doubleAuctionMan.isSellersLimitReached(auctionID);
    }

    @Override
    public String getDoubleAuctionsDisplay(User user) throws RemoteException {
        return doubleAuctionMan.getDoubleAuctionsDisplaySellers(user);
    }

    @Override
    public boolean doesDoubleAuctionExists(int auctionID) throws RemoteException {
        return doubleAuctionMan.doesDoubleAuctionExists(auctionID);
    }

    @Override
    public String getDoubleAuctionResolution(User user, int auctionID) throws RemoteException {
        return doubleAuctionMan.getResolutionSeller(user, auctionID);
    }

    @Override
    public User logIn(String username, String password) throws RemoteException {
        User temp = userMan.logIn(username, password, 'S');
        return temp;
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return userMan.signUp(true, username, email, password, 'S');
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
