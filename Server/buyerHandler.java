import java.rmi.RemoteException;

public class buyerHandler implements iBuyer {

    @Override
    public float bid(int auctionID, String username, String email, float bidAmount) throws RemoteException {
        return AuctionManager.bid(auctionID, username, email, bidAmount);
    }

    @Override
    public String displayAuctions() throws RemoteException {
        return AuctionManager.getDisplayString();
    }

    @Override
    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException {
        return "Auction going fine";
    }

}
