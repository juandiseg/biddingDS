
import java.rmi.RemoteException;

/*  Buyers specific functionality. I.e.: functionality that is not somehow just
        adapted between buyers and sellers not intended for sellers. */
public interface iBuyer extends iUser {

    // BIDDING
    public Double normalAuctionBid(int auctionID, User user, Double bidAmount) throws RemoteException;

    public boolean doubleAuctionBid(int auctionID, User user, double bidAmount) throws RemoteException;

    // BROWSING
    public String getBasicAuctionsDisplay(int itemID) throws RemoteException;

    public String getReversedBasicAuctionsDisplay(int itemID) throws RemoteException;

    public String getItemsDisplay() throws RemoteException;

}
