import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.util.HashMap;

public class sellerProxy implements iSeller {

    @Override
    public HashMap<Integer, AuctionItem> getSpec(User user, int itemId) throws RemoteException {
        return BasicAuctionManager.getSpec(user, itemId);
    }

    @Override
    public int createBasicAuction(String sellerUsername, int itemID, String itemTitle, String itemDescription,
            int itemCondition, Double reservePrice, Double sellingPrice) throws RemoteException {
        return BasicAuctionManager.addAuction(
                new AuctionItem(sellerUsername, itemID, itemTitle, itemDescription, itemCondition, sellingPrice),
                reservePrice);
    }

    @Override
    public String closeBasicAuction(User user, int auctionID) throws RemoteException {
        return BasicAuctionManager.closeAuction(user, auctionID);
    }

    @Override
    public String closeBasicAuctionAndApproveWinner(int auctionID, boolean isApproved) throws RemoteException {
        return BasicAuctionManager.closeBasicAuctionAndApproveWinner(auctionID, isApproved);
    }

    @Override
    public String getBasicAuctionStatus(User user, int auctionID) throws RemoteException {
        return BasicAuctionManager.getStatusSeller(user, auctionID);
    }

    @Override
    public String getDisplayAvailableItems() throws RemoteException {
        return BasicAuctionManager.getDisplayAvailableItems();
    }

    @Override
    public User logIn(String username, String password) throws RemoteException {
        return UserManager.logIn(username, password, 'S');
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return UserManager.signUp(username, email, password, 'S');
    }

    @Override
    public boolean doesUsernameExist(String username) throws RemoteException {
        return UserManager.doesUsernameExist(username);
    }

    @Override
    public int createDoubleAuction(int itemID, int limitSellers, int limitBids) {
        return DoubleAuctionManager.createDoubleAuction(itemID, limitSellers, limitBids);
    }

    @Override
    public void joinDoubleAuction(int doubleAuctionID, AuctionItem newItem) {
        DoubleAuctionManager.joinDoubleAuction(doubleAuctionID, newItem);
    }

    @Override
    public int getDoubleAuctionsItemID(int auctionID) {
        return DoubleAuctionManager.getDoubleAuctionsItemID(auctionID);
    }

    @Override
    public boolean isSellersLimitReached(int auctionID) {
        return DoubleAuctionManager.isSellersLimitReached(auctionID);
    }

    @Override
    public String getDoubleAuctionsDisplay(User user) throws RemoteException {
        return DoubleAuctionManager.getDoubleAuctionsDisplaySellers(user);
    }

    @Override
    public boolean doesDoubleAuctionExists(int auctionID) throws RemoteException {
        return DoubleAuctionManager.doesDoubleAuctionExists(auctionID);
    }

    @Override
    public String getDoubleAuctionResolution(User user, int auctionID) throws RemoteException {
        return DoubleAuctionManager.getResolutionSeller(user, auctionID);
    }

    @Override
    public byte[] signDocument(byte[] dataToSign) throws Exception {
        return KeyManager.signFile(dataToSign);
    }

    @Override
    public Certificate exportCertificate() throws Exception {
        return KeyManager.exportCertificate();
    }

}
