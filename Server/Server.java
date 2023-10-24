import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;

public class Server implements iUser {

    public Server() {
        super();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            iUser userStub = (iUser) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("my_server", userStub);
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
    public int createAuction(int sellerID, String itemTitle, String itemDescription, int itemCondition,
            Float startingPrice, Float acceptablePrice) throws RemoteException {
        int auctionID = AuctionManager.generateAuctionID();
        AuctionManager.addAuction(new Auction(sellerID, auctionID,
                new AuctionItem(AuctionManager.generateItemID(), itemTitle, itemDescription, itemCondition),
                startingPrice, acceptablePrice));
        return auctionID;
    }

    @Override
    public float bid(int auctionID, String username, String email, float bidAmount) throws RemoteException {
        return AuctionManager.bid(auctionID, username, email, bidAmount);
    }

    @Override
    public String checkAuctionStatus(int userID, int auctionID) throws RemoteException {
        return "Auction going fine";
    }

    @Override
    public String closeAuction(int userID, int auctionID) throws RemoteException {
        return AuctionManager.closeAuction(userID, auctionID);
    }

    @Override
    public String displayAuctions() throws RemoteException {
        return AuctionManager.getDisplayString();
    }
}