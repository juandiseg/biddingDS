import java.rmi.RemoteException;

public interface iBuyer extends iUser {

        // BIDDING
        public Double normalAuctionBid(int auctionID, User user, Double bidAmount) throws RemoteException;

        public boolean doubleAuctionBid(int auctionID, User user, double bidAmount)
                        throws RemoteException;

        // BROWSING
        public String getBasicAuctionsDisplay(int itemID) throws RemoteException;

        public String getReversedBasicAuctionsDisplay(int itemID) throws RemoteException;

        public String getItemsDisplay() throws RemoteException;

}
