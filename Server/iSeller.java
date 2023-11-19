
import java.rmi.RemoteException;
import java.util.HashMap;

public interface iSeller extends iUser {
    public HashMap<Integer, AuctionItem> getSpec(User user, int itemId) throws RemoteException;

    public int createAuction(String sellerUsername, int itemID, String itemTitle, String itemDescription,
            int itemCondition, Double reservePrice, Double sellingPrice) throws RemoteException;

    public String closeAuction(User user, int auctionID) throws RemoteException;

    public String closeAuctionConfirmation(int auctionID, boolean isApproved) throws RemoteException;

    public String checkAuctionStatus(User user, int auctionID) throws RemoteException;

    public String getItemsReferenceID() throws RemoteException;

    public int addDoubleAuction(int itemID, int limitSellers, int limitBids);

    public void addAuctionItem(int doubleAuctionID, AuctionItem newItem);

    public int getItemIDofAuction(int auctionID);

    public boolean isSellersLimitReached(int auctionID);
}
