import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server extends UnicastRemoteObject implements ICalc {
    public Server() throws RemoteException {
        super();
    }

    public static void main(String[] args) {
        try {
            Server s = new Server();
            String name = "auction_server";
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, s);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }

    @Override
    public AuctionItem getSpec(int itemId, int clientId) throws RemoteException {
        System.out.println("Server sending response");
        return new AuctionItem(1, "ola", "olaaa", 2);
    }

}