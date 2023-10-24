import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private static iBuyer buyersHandler = new buyerHandler();
    private static iSeller sellersHandler = new sellerHandler();

    public Server() {
        super();
    }

    public static void main(String[] args) {
        try {
            iBuyer buyerStub = (iBuyer) UnicastRemoteObject.exportObject(buyersHandler, 0);
            Registry buyerRegistry = LocateRegistry.getRegistry();
            buyerRegistry.rebind("buyer_server", buyerStub);

            iSeller sellerStub = (iSeller) UnicastRemoteObject.exportObject(sellersHandler, 0);
            Registry sellerRegistry = LocateRegistry.getRegistry();
            sellerRegistry.rebind("seller_server", sellerStub);

            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }
}