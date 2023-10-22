import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java Client n");
            return;
        }
        String name = "myserver";
        Registry registry = LocateRegistry.getRegistry();
        ICalc server = (ICalc) registry.lookup(name);

        String command = args[0];

        if (command.equals("GET")) {
            int itemID = Integer.parseInt(args[1]);
            AuctionItem result = server.getSpec(itemID, 1);
            if (result != null)
                result.printSummary();
        } else if (command.equals("ADD")) {
            String title = args[1];
            String description = args[2];
            int condition = Integer.parseInt(args[3]);
            float startingPrice = Float.parseFloat(args[4]);
            float acceptablePrice = Float.parseFloat(args[5]);
            int auctionID = server.createListing(title, description, condition, startingPrice, acceptablePrice);
            System.out.println("Auction successfully generated. AUCTION ID = {" + auctionID + "}.");
        } else if (command.equals("BID")) {
            int userID = Integer.parseInt(args[1]);
            int auctionID = Integer.parseInt(args[2]);
            float bidAmount = Float.parseFloat(args[3]);
            float result = server.bid(userID, auctionID, bidAmount);
            if (result == 0)
                System.out.println("The given auctionID doesn't exist.");
            else if (result == -1)
                System.out.println("Congrats you're winning this bid.");
            else if (result == -2)
                System.out.println("You're alredy winning this bid.");
            else
                System.out.println("Somebody else is winning the bid {" + result + "}.");
        } else if (command.equals("CHECK")) {
            int auctionID = Integer.parseInt(args[1]);
        }

    }
}