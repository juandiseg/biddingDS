
import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.util.HashMap;

public class sellerHandler implements iSeller {

    @Override
    public HashMap<Integer, AuctionItem> getSpec(User user, int itemId) throws RemoteException {
        return NormalAuctionManager.getSpec(user, itemId);
    }

    @Override
    public int createAuction(String sellerUsername, int itemID, String itemTitle, String itemDescription,
            int itemCondition, Double reservePrice, Double sellingPrice) throws RemoteException {
        if (NormalAuctionManager.getAvailableItems().get(itemID) == null) {
            return -1;
        }
        return NormalAuctionManager.addAuction(
                new AuctionItem(sellerUsername, itemID, itemTitle, itemDescription, itemCondition, sellingPrice),
                reservePrice);
    }

    @Override
    public String closeAuction(User user, int auctionID) throws RemoteException {
        return NormalAuctionManager.closeAuction(user, auctionID);
    }

    @Override
    public String closeAuctionConfirmation(int auctionID, boolean isApproved) throws RemoteException {
        return NormalAuctionManager.closeAuctionConfirmation(auctionID, isApproved);
    }

    @Override
    public String checkAuctionStatus(User user, int auctionID) throws RemoteException {
        return NormalAuctionManager.checkAuctionStatusSeller(user, auctionID);
    }

    @Override
    public String getItemsReferenceID() throws RemoteException {
        HashMap<Integer, String> items = NormalAuctionManager.getAvailableItems();
        String text = "";
        for (int i = 1; i < items.size() + 1; i++) {
            text = text.concat("The item #" + i + " references the product: \"" + items.get(i) + "\".\n");
        }
        return text;
    }

    @Override
    public User logInUsername(String username, String password) throws RemoteException {
        if (UserManager.validateUserByUsername(username, password) == 'S')
            return UserManager.getUser(username);
        return null;
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return UserManager.addUser(username, email, password, 'S');
    }

    @Override
    public boolean doesUsernameExist(String username) throws RemoteException {
        return UserManager.usernamePresent(username);
    }

    @Override
    public int addDoubleAuction(int itemID, int limitSellers, int limitBids) {
        return DoubleAuctionManager.addDoubleAuction(itemID, limitSellers, limitBids);
    }

    @Override
    public void addAuctionItem(int doubleAuctionID, AuctionItem newItem) {
        DoubleAuctionManager.addAuctionItem(doubleAuctionID, newItem);
    }

    @Override
    public int getItemIDofAuction(int auctionID) {
        return DoubleAuctionManager.getItemIDofAuction(auctionID);
    }

    @Override
    public boolean isSellersLimitReached(int auctionID) {
        return DoubleAuctionManager.isSellersLimitReached(auctionID);
    }

    @Override
    public String viewDoubleAuctions(User user) throws RemoteException {
        return DoubleAuctionManager.viewDoubleAuctionsSellers(user);
    }

    @Override
    public boolean doubleAuctionExists(int auctionID) throws RemoteException {
        return DoubleAuctionManager.doubleAuctionExists(auctionID);
    }

    @Override
    public String checkDoubleAuctionResolution(int auctionID, User user) throws RemoteException {
        return DoubleAuctionManager.getResolutionSeller(auctionID, user);
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
