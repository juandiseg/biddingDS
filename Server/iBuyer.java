import java.rmi.RemoteException;
import java.rmi.Remote;

public interface iBuyer extends Remote {
    public Double bid(int auctionID, String username, String email, Double bidAmount) throws RemoteException;

    public String getAuctionsDisplay(int itemID) throws RemoteException;

    public String getItemsDisplay() throws RemoteException;

    public String checkAuctionStatus(String username, int auctionID) throws RemoteException;
}
