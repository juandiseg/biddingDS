
import java.rmi.RemoteException;

public interface iBuyer extends iUser {

    // public interface iBuyer extends Remote {
    public Double bid(int auctionID, User user, Double bidAmount) throws RemoteException;

    public String getAuctionsDisplay(int itemID) throws RemoteException;

    public String getItemsDisplay() throws RemoteException;

    public String checkAuctionStatus(User user, int auctionID) throws RemoteException;

    public String viewReverseAuction(int itemID) throws RemoteException;
}
