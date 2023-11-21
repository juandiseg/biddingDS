import java.rmi.RemoteException;
import java.util.Scanner;

public class BasicAuctionHandlerBuyer {

    public static boolean handleBasicAuctionCommand(String command, User userInfo, iBuyer server, Scanner scanner)
            throws RemoteException {
        if (command.equals("VIEW BASIC AUCTIONS")) {
            viewBasicAuction(server, scanner);
            return true;
        } else if (command.equals("CHECK BASIC AUCTION")) {
            checkBasicAuction(server, scanner, userInfo);
            return true;
        } else if (command.equals("BID BASIC AUCTION")) {
            bidBasicAuction(server, scanner, userInfo);
            return true;
        } else if (command.equals("VIEW REVERSE AUCTIONS")) {
            viewReverseAuction(server, scanner);
            return true;
        } else {
            return false;
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
                UtilityHandlerBuyer.printCommands();
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

    private static void checkBasicAuction(iBuyer server, Scanner scanner, User userInfo) throws RemoteException {
        try {
            System.out.print("Enter the auction's ID you want to check for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerBuyer.printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.checkAuctionStatus(userInfo, auctionID));
        } catch (NumberFormatException e) {
            System.out.println("The formatting of the data was incorrect.");
            checkBasicAuction(server, scanner, userInfo);
            return;
        }
    }

    private static void bidBasicAuction(iBuyer server, Scanner scanner, User userInfo) throws RemoteException {
        System.out.print("Enter the auction's ID you want to BID for: ");
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

    private static void viewReverseAuction(iBuyer server, Scanner scanner) throws RemoteException {
        try {
            System.out.print("Enter the item's ID you want to reverse view for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerBuyer.printCommands();
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
}