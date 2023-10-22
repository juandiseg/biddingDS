import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements ICalc {

    public Server() {
        super();
    }

    public static void main(String[] args) {
        try {
            Server s = new Server();
            ICalc stub = (ICalc) UnicastRemoteObject.exportObject(s, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("myserver", stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }

    @Override
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException {
        AuctionItem item = AuctionManager.getAuctionItem(itemId);
        if (item == null)
            return null; // Create default object.
        return item;
    }

    @Override
    public int createListing(String itemTitle, String itemDescription, int itemCondition, Float startingPrice,
            Float acceptablePrice) throws RemoteException {
        int auctionID = AuctionManager.generateAuctionID();
        AuctionManager.addAuction(new Auction(auctionID,
                new AuctionItem(AuctionManager.generateItemID(), itemTitle, itemDescription, itemCondition),
                startingPrice, acceptablePrice));
        return auctionID;
    }

    @Override
    public float bid(int userID, int auctionID, float bidAmount) throws RemoteException {
        return AuctionManager.bid(userID, auctionID, bidAmount);
    }
}