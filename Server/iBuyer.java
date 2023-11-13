
import java.rmi.RemoteException;

public interface iBuyer extends iUser {

    // public interface iBuyer extends Remote {
    public Double bid(int auctionID, String username, String email, Double bidAmount) throws RemoteException;

    public String getAuctionsDisplay(int itemID) throws RemoteException;

    public String getItemsDisplay() throws RemoteException;

    public String checkAuctionStatus(String username, int auctionID) throws RemoteException;
}
