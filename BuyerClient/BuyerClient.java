import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class BuyerClient {
    public static void main(String[] args) throws Exception {
        String name = "buyer_server";
        Registry registry = LocateRegistry.getRegistry();
        iBuyer server = (iBuyer) registry.lookup(name);

        System.out.println("\nWELCOME TO MY BIDDING SYSTEM.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        User userInfo = getUserInfo(server, br);

        /*
         * String command = args[0].toUpperCase();
         * 
         * if (command.equals("BID")) {
         * int auctionID = Integer.parseInt(args[1]);
         * String username = args[2];
         * String email = args[3];
         * Double bidAmount = Double.parseDouble(args[4]);
         * Double result = server.bid(auctionID, username, email, bidAmount);
         * if (result == 0.0)
         * System.out.println("BID DENIED. The given auctionID doesn't exist.");
         * else if (result == -1.0)
         * System.out.println("BID ACCEPTED. Congrats you're winning this bid.");
         * else if (result == -2.0)
         * System.out.println("BID DENIED. You're alredy the current winning bid.");
         * else if (result == -3.0)
         * System.out.
         * println("BID DENIED. Your bid doesn't reach the minimum acceptable price.");
         * else if (result == -4.0)
         * System.out.println("BID DENIED. The auction is already closed!.");
         * else
         * System.out.println("BID DENIED. Somebody else has a higher bid {" + result +
         * "}.");
         * return;
         * } else if (command.equals("CHECK")) {
         * String username = args[1];
         * int auctionID = Integer.parseInt(args[2]);
         * System.out.println(server.checkAuctionStatus(username, auctionID));
         * return;
         * }
         * command = command.concat(" " + args[1].toUpperCase());
         * if (command.equals("GET AUCTIONS")) {
         * System.out.println(server.getAuctionsDisplay(-1));
         * return;
         * } else if (command.equals("GET ITEMS")) {
         * System.out.println(server.getItemsDisplay());
         * return;
         * }
         * command = command.concat(" " + args[2].toUpperCase());
         * if (command.equals("GET ITEM AUCTIONS")) {
         * System.out.println(server.getAuctionsDisplay(Integer.parseInt(args[3])));
         * return;
         * }
         */
    }

    private static User getUserInfo(iBuyer server, BufferedReader br) throws IOException {
        boolean correctInput = false, first = true;
        while (!correctInput) {
            if (first) {
                System.out.println("\nEnter \"L\" if you're ALREADY A USER, or enter \"S\" to SIGN UP.");
            } else {
                System.out.println("\nPlease, enter \"L\" to LOG IN or \"S\" to SIGN UP.");
                first = false;
            }
            String line = br.readLine();
            if (line.equals("L")) {
                return logIn(server, br);
            } else if (line.equals("S")) {
                return signUp(server, br);
            }
        }
        return null;
    }

    private static User signUp(iBuyer server, BufferedReader br) throws IOException {
        boolean correctInput = false, first = true;
        while (!correctInput) {
            if (first) {
                System.out.println("\nPlease username, email and password.");
                System.out.println("E.g.: [username] [email] [password].");
            } else {
                System.out.println("\nPlease follow the format: [username] [email] [password].");
            }
            String[] line = br.readLine().split(" ");
            if (line.length == 1 && line[0].toUpperCase().equals("BACK")) {
                return getUserInfo(server, br);
            }
            if (line.length > 3) {
                System.out.println("You have entered more than 3 fields.");
            } else if (line.length < 3) {
                System.out.println("You have entered less than 3 fields.");
            } else {
                User user = server.signUp(line[0], line[1], line[2]);
                if (user == null) {
                    System.out.println("The given username is already taken.");
                } else {
                    return user;
                }
            }
        }
        return null;
    }

    private static User logIn(iBuyer server, BufferedReader br) throws IOException {
        boolean correctInput = false, first = true;
        while (!correctInput) {
            if (first) {
                System.out.println("\nPlease enter your username and password.");
                System.out.println("E.g.: [username] [password].");
            } else {
                System.out.println("\nPlease follow the format: [username] [password].");
            }
            String[] line = br.readLine().split(" ");
            if (line.length == 1 && line[0].toUpperCase().equals("BACK")) {
                return getUserInfo(server, br);
            }
            if (line.length > 2) {
                System.out.println("You have entered more than 2 fields.");
            } else if (line.length < 2) {
                System.out.println("You have entered less than 2 fields.");
            } else {
                User user = server.logInUsername(line[0], line[1]);
                if (user == null) {
                    System.out.println("The given details aren't valid.");
                } else {
                    return user;
                }
            }
        }
        return null;
    }
}