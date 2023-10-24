import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SellerClient {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java Client n");
            return;
        }
        String name = "seller_server";
        Registry registry = LocateRegistry.getRegistry();
        iSeller server = (iSeller) registry.lookup(name);

        String command = args[0];

        if (command.equals("GET")) {
            int itemID = Integer.parseInt(args[1]);
            AuctionItem result = server.getSpec(itemID, 1);
            if (result != null)
                result.printSummary();
            else
                System.out.println("The ID for the given item doesn't exist.");
        } else if (command.equals("ADD")) {
            int sellerID = Integer.parseInt(args[1]);
            String title = args[2];
            String description = args[3];
            int condition = Integer.parseInt(args[4]);
            float startingPrice = Float.parseFloat(args[5]);
            float acceptablePrice = Float.parseFloat(args[6]);
            int auctionID = server.createAuction(sellerID, title, description, condition, startingPrice,
                    acceptablePrice);
            System.out.println("Auction successfully generated. AUCTION ID = {" + auctionID + "}.");
        } else if (command.equals("CLOSE")) {
            int userID = Integer.parseInt(args[1]);
            int auctionID = Integer.parseInt(args[2]);
            System.out.println(server.closeAuction(userID, auctionID));
        } else if (command.equals("CHECK")) {
            int userID = Integer.parseInt(args[1]);
            int auctionID = Integer.parseInt(args[2]);
            System.out.println(server.checkAuctionStatus(userID, auctionID));
        }
    }
}