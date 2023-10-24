import java.rmi.RemoteException;
import java.rmi.Remote;

public interface iBuyer extends Remote {
    public float bid(int auctionID, String username, String email, float bidAmount) throws RemoteException;

    public String displayAuctions() throws RemoteException;

    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException;
}
