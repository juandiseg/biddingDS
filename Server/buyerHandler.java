import java.rmi.RemoteException;

public class buyerHandler implements iBuyer {

    @Override
    public Double bid(int auctionID, String username, String email, Double bidAmount) throws RemoteException {
        return AuctionManager.bid(auctionID, username, email, bidAmount);
    }

    @Override
    public String checkAuctionStatus(String username, int auctionID) throws RemoteException {
        return "Auction going fine";
    }

    @Override
    public String getAuctionsDisplay(int itemID) throws RemoteException {
        return AuctionManager.getAuctionsDisplay(itemID);
    }

    @Override
    public String getItemsDisplay() throws RemoteException {
        return AuctionManager.getItemsDisplay();
    }

}
