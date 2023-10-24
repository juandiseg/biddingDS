import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BuyerClient {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java Client n");
            return;
        }
        String name = "my_server";
        Registry registry = LocateRegistry.getRegistry();
        iUser server = (iUser) registry.lookup(name);

        String command = args[0];

        if (command.equals("BID")) {
            int auctionID = Integer.parseInt(args[1]);
            String username = args[2];
            String email = args[3];
            float bidAmount = Float.parseFloat(args[4]);
            float result = server.bid(auctionID, username, email, bidAmount);
            if (result == 0)
                System.out.println("The given auctionID doesn't exist.");
            else if (result == -1)
                System.out.println("Congrats you're winning this bid.");
            else if (result == -2)
                System.out.println("You're alredy winning this bid.");
            else if (result == -3)
                System.out.println("Your bid doesn't reach the minimum acceptable price.");
            else
                System.out.println("Somebody else is winning the bid {" + result + "}.");
        } else if (command.equals("DISPLAY")) {
            System.out.println(server.displayAuctions());
        }

    }
}