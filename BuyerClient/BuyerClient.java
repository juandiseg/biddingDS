import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BuyerClient {
    private static User userInfo;

    public static void main(String[] args) throws Exception {
        String name = "buyer_server";
        Registry registry = LocateRegistry.getRegistry();
        iBuyer server = (iBuyer) registry.lookup(name);

        System.out.println("\nWELCOME TO MY BIDDING SYSTEM.");
        Scanner sc = new Scanner(System.in);
        userInfo = getUserInfo(server, sc);
        if (userInfo != null)
            handleRequests(server, sc);
    }

    private static void bidBasicAuction(iBuyer server, Scanner scanner) throws RemoteException {
        System.out.print("Enter the auction's ID you want to BID for: ");
        String possibleExit = scanner.nextLine();
        if (possibleExit.toUpperCase().equals("BACK")) {
            return;
        } else if (possibleExit.toUpperCase().equals("HELP")) {
            printCommands();
            return;
        }
        int auctionID = Integer.parseInt(possibleExit);
        System.out.print("Enter your bid: ");
        Double bidAmount = Double.parseDouble(scanner.nextLine());
        Double result = server.normalAuctionBid(auctionID, userInfo, bidAmount);
        if (result == 0.0)
            System.out.println("BID DENIED. The given auctionID doesn't exist.");
        else if (result == -1.0)
            System.out.println("BID ACCEPTED. Congrats you're the current highest bid.");
        else if (result == -2.0)
            System.out.println("BID DENIED. The current winning bid is yours.");
        else if (result == -3.0)
            System.out.println("BID DENIED. Your bid doesn't reach the minimum acceptable price.");
        else if (result == -4.0)
            System.out.println("BID DENIED. The auction is already closed!.");
        else
            System.out.println("BID DENIED. Somebody else has a higher bid {" + result + "}.");
        return;
    }

    private static void bidDoubleAuction(iBuyer server, Scanner scanner) throws RemoteException {
        System.out.print("Enter the double auction's ID you want to BID for: ");
        String possibleExit = scanner.nextLine();
        if (possibleExit.toUpperCase().equals("BACK")) {
            return;
        } else if (possibleExit.toUpperCase().equals("HELP")) {
            printCommands();
            return;
        }
        int auctionID = Integer.parseInt(possibleExit);
        System.out.print("Enter your bid: ");
        Double bidAmount = Double.parseDouble(scanner.nextLine());
        if (bidAmount <= 0) {
            System.out.println("Please enter a bid greater than 0 EUR");
            bidDoubleAuction(server, scanner);
            return;
        }
        boolean result = server.doubleAuctionBid(auctionID, userInfo, bidAmount);
        if (result) {
            System.out.println("BID ACCEPTED!\n");
        } else {
            System.out.println("BID DENIED. There are two possible reasons: ");
            System.out.println("\t-The double auction's ID does not exist.");
            System.out.println("\t-The double auction is already closed.");
            System.out.println("\t-You have already bidded in this double auction.");
            System.out.println("Use the command \"VIEW DOUBLE AUCTIONS\" to check which of these conditions apply.");
        }
    }

    private static void handleRequests(iBuyer server, Scanner scanner) throws IOException {
        printCommands();
        boolean exit = false;
        try {
            while (!exit) {
                String command = scanner.nextLine().toUpperCase();
                if (command.equals("VIEW BASIC AUCTIONS")) {
                    viewBasicAuction(server, scanner);
                } else if (command.equals("VIEW REVERSE AUCTIONS")) {
                    viewReverseAuction(server, scanner);
                } else if (command.equals("VIEW DOUBLE AUCTIONS")) {
                    viewDoubleAuctions(server);
                } else if (command.equals("CHECK BASIC AUCTION")) {
                    checkBasicAuction(server, scanner);
                } else if (command.equals("BID BASIC AUCTION")) {
                    bidBasicAuction(server, scanner);
                } else if (command.equals("BID DOUBLE AUCTION")) {
                    bidDoubleAuction(server, scanner);
                } else if (command.equals("SEE ITEMS")) {
                    viewItemsDisplay(server);
                } else if (command.equals("CHECK DOUBLE AUCTION")) {
                    checkDoubleAuction(server, scanner);
                } else if (command.equals("HELP")) {
                    printCommands();
                }
            }
        } catch (NoSuchElementException e) {

        }
    }

    private static void viewReverseAuction(iBuyer server, Scanner scanner) throws RemoteException {
        try {
            System.out.print("Enter the item's ID you want to reverse view for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            }
            int itemID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.viewReverseAuction(itemID));
        } catch (NumberFormatException e) {
            System.out.println("The formatting of the data was incorrect.");
            viewReverseAuction(server, scanner);
            return;
        }
    }

    private static void checkBasicAuction(iBuyer server, Scanner scanner) throws RemoteException {
        try {
            System.out.print("Enter the auction's ID you want to check for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.checkAuctionStatus(userInfo, auctionID));
        } catch (NumberFormatException e) {
            System.out.println("The formatting of the data was incorrect.");
            checkBasicAuction(server, scanner);
            return;
        }
    }

    private static void checkDoubleAuction(iBuyer server, Scanner scanner) throws RemoteException {
        try {
            System.out.print("Enter the double auction's ID you want to check for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.checkDoubleAuctionResolution(auctionID, userInfo));
        } catch (NumberFormatException e) {
            System.out.println("The formatting of the auction's ID is incorrect.");
            checkDoubleAuction(server, scanner);
            return;
        }
    }

    private static void viewBasicAuction(iBuyer server, Scanner scanner) throws RemoteException {
        try {
            System.out.println("Do you want to search auctions for an specific item?");
            System.out.print("Enter the item's ID or Not if you want to view all basic auctions: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            } else if (possibleExit.toUpperCase().equals("NOT")) {
                System.out.println("\n" + server.getAuctionsDisplay(-1));
                return;
            }
            int itemID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.getAuctionsDisplay(itemID));
            return;
        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            viewBasicAuction(server, scanner);
        }
    }

    private static void viewDoubleAuctions(iBuyer server) throws RemoteException {
        System.out.println("\n" + server.viewDoubleAuctions(userInfo));
    }

    private static void viewItemsDisplay(iBuyer server) throws RemoteException {
        System.out.println("\n" + server.getItemsDisplay());
    }

    private static void printCommands() {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("Here are the available commands:");
        System.out.println("HELP\t\t\t| Prints all available commands.");
        System.out.println("BACK\t\t\t| You can use it at any time to go back to the previous menu.");
        System.out.println("VIEW BASIC AUCTIONS\t| Displays the currently open basic auctions.");
        System.out.println("VIEW REVERSE AUCTIONS\t| Displays the currently open basic auctions sorted by lowest bid.");
        System.out.println("VIEW DOUBLE AUCTIONS\t| Displays the currently open double auctions.");
        System.out.println("BID BASIC AUCTION\t| Allows bidding on a basic auction.");
        System.out.println("BID DOUBLE AUCTION\t| Allows bidding on a double auctoin.");
        System.out.println("CHECK DOUBLE AUCTION\t| Displays the resolution of a double auction you've bid in.");
        System.out.println("SEE ITEMS\t\t| Displays items foundable on open auctions.");
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

    private static User getUserInfo(iBuyer server, Scanner scanner) throws IOException {
        try {
            boolean first = true;
            while (true) {
                if (first) {
                    System.out.print("\nEnter \"L\" if you're ALREADY A USER, or enter \"S\" to SIGN UP: ");
                    first = false;
                } else {
                    System.out.print("\nPlease, enter \"L\" to LOG IN or \"S\" to SIGN UP: ");
                }
                String line = scanner.nextLine().toUpperCase();
                if (line.equals("L")) {
                    return logIn(server, scanner);
                } else if (line.equals("S")) {
                    return signUp(server, scanner);
                }
            }
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private static User signUp(iBuyer server, Scanner scanner) throws IOException {
        while (true) {
            System.out.print("\nEnter Username: ");
            String username = scanner.nextLine();
            if (username.toUpperCase().equals("BACK")) {
                return getUserInfo(server, scanner);
            }
            if (server.doesUsernameExist(username)) {
                System.out.println("The entered username is already taken. Please try again.");
                return signUp(server, scanner);
            }
            System.out.print("Enter Email: ");
            String email = scanner.nextLine();
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();
            User user = server.signUp(username, email, password);
            if (user == null) {
                System.out.print("The given username/email is already taken.");
            } else {
                return user;
            }
        }
    }

    private static User logIn(iBuyer server, Scanner scanner) throws IOException {
        while (true) {
            System.out.print("\nEnter Username: ");
            String username = scanner.nextLine();
            if (username.equals("BACK")) {
                return getUserInfo(server, scanner);
            }
            if (!server.doesUsernameExist(username)) {
                System.out.println("The entered username doesn't exists. Please try again.");
                logIn(server, scanner);
            }
            System.out.print("\nEnter Password: ");
            String password = scanner.nextLine();

            User user = server.logInUsername(username, password);
            if (user == null) {
                System.out.println("The given details aren't valid.");
            } else {
                return user;
            }
        }
    }
}