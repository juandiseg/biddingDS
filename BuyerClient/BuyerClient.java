import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BuyerClient {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java Client n");
            return;
        }
        String name = "buyer_server";
        Registry registry = LocateRegistry.getRegistry();
        iBuyer server = (iBuyer) registry.lookup(name);

        String command = args[0].toUpperCase();

        if (command.equals("BID")) {
            int auctionID = Integer.parseInt(args[1]);
            String username = args[2];
            String email = args[3];
            Double bidAmount = Double.parseDouble(args[4]);
            Double result = server.bid(auctionID, username, email, bidAmount);
            if (result == 0.0)
                System.out.println("BID DENIED. The given auctionID doesn't exist.");
            else if (result == -1.0)
                System.out.println("BID ACCEPTED. Congrats you're winning this bid.");
            else if (result == -2.0)
                System.out.println("BID DENIED. You're alredy the current winning bid.");
            else if (result == -3.0)
                System.out.println("BID DENIED. Your bid doesn't reach the minimum acceptable price.");
            else if (result == -4.0)
                System.out.println("BID DENIED. The auction is already closed!.");
            else
                System.out.println("BID DENIED. Somebody else has a higher bid {" + result + "}.");
            return;
        } else if (command.equals("CHECK")) {
            String username = args[1];
            int auctionID = Integer.parseInt(args[2]);
            System.out.println(server.checkAuctionStatus(username, auctionID));
            return;
        }
        command = command.concat(" " + args[1].toUpperCase());
        if (command.equals("GET AUCTIONS")) {
            System.out.println(server.getAuctionsDisplay(-1));
            return;
        } else if (command.equals("GET ITEMS")) {
            System.out.println(server.getItemsDisplay());
            return;
        }
        command = command.concat(" " + args[2].toUpperCase());
        if (command.equals("GET ITEM AUCTIONS")) {
            System.out.println(server.getAuctionsDisplay(Integer.parseInt(args[3])));
            return;
        }
    }
}