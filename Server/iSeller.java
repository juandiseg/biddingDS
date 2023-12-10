import java.rmi.RemoteException;
import java.util.HashMap;

public interface iSeller extends iUser {
    public HashMap<Integer, AuctionItem> getSpec(User user, int itemId) throws RemoteException;

    // CREATING & JOINING

    public int createBasicAuction(String sellerUsername, int itemID, String itemTitle,
            String itemDescription,
            int itemCondition, Double reservePrice, Double sellingPrice) throws RemoteException;

    public int createDoubleAuction(int itemID, int limitSellers, int limitBids)
            throws RemoteException;

    public void joinDoubleAuction(int doubleAuctionID, AuctionItem newItem) throws RemoteException;

    // CLOSE
    public String closeBasicAuction(User user, int auctionID) throws RemoteException;

    public String closeBasicAuctionAndApproveWinner(int auctionID, boolean isApproved)
            throws RemoteException;

    // GET DISPLAY
    public String getDisplayAvailableItems() throws RemoteException;

    // GET ITEM ID
    public int getDoubleAuctionsItemID(int auctionID) throws RemoteException;

    // VERIFICATIONS
    public boolean isSellersLimitReached(int auctionID) throws RemoteException;

    public boolean doesDoubleAuctionExists(int auctionID) throws RemoteException;

}
