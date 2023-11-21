import java.rmi.RemoteException;
import java.util.Scanner;

public class DoubleAuctionHandlerBuyer {

    public static boolean handleDoubleAuctionCommand(String command, User userInfo, iBuyer server, Scanner scanner)
            throws RemoteException {
        if (command.equals("VIEW DOUBLE AUCTIONS")) {
            viewDoubleAuctions(server, userInfo);
            return true;
        } else if (command.equals("BID DOUBLE AUCTION")) {
            bidDoubleAuction(server, scanner, userInfo);
            return true;
        } else if (command.equals("CHECK DOUBLE AUCTION")) {
            checkDoubleAuction(server, scanner, userInfo);
            return true;
        } else {
            return false;
        }
    }

    private static void viewDoubleAuctions(iBuyer server, User userInfo) throws RemoteException {
        System.out.println("\n" + server.viewDoubleAuctions(userInfo));
    }

    private static void checkDoubleAuction(iBuyer server, Scanner scanner, User userInfo) throws RemoteException {
        try {
            System.out.print("Enter the double auction's ID you want to check for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerBuyer.printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.checkDoubleAuctionResolution(auctionID, userInfo));
        } catch (NumberFormatException e) {
            System.out.println("The formatting of the auction's ID is incorrect.");
            checkDoubleAuction(server, scanner, userInfo);
            return;
        }
    }

    private static void bidDoubleAuction(iBuyer server, Scanner scanner, User userInfo) throws RemoteException {
        System.out.print("Enter the double auction's ID you want to BID for: ");
        String possibleExit = scanner.nextLine();
        if (possibleExit.toUpperCase().equals("BACK")) {
            return;
        } else if (possibleExit.toUpperCase().equals("HELP")) {
            UtilityHandlerBuyer.printCommands();
            return;
        }
        int auctionID = Integer.parseInt(possibleExit);
        System.out.print("Enter your bid: ");
        Double bidAmount = Double.parseDouble(scanner.nextLine());
        if (bidAmount <= 0) {
            System.out.println("Please enter a bid greater than 0 EUR");
            bidDoubleAuction(server, scanner, userInfo);
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
}