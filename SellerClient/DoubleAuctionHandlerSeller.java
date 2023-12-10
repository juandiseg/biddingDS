import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DoubleAuctionHandlerSeller {

    public static boolean handleDoubleAuctionCommand(String command, iSeller server, Scanner scanner, User userInfo)
            throws RemoteException {
        if (command.equals("ADD DOUBLE AUCTION")) {
            doubleAuctionHandler(server, scanner, userInfo);
            return true;
        } else if (command.equals("VIEW DOUBLE AUCTIONS")) {
            viewDoubleAuctions(server, userInfo);
            return true;
        } else if (command.equals("CHECK DOUBLE AUCTION")) {
            checkDoubleAuction(server, scanner, userInfo);
            return true;
        } else {
            return false;
        }
    }

    private static void viewDoubleAuctions(iSeller server, User userInfo) throws RemoteException {
        System.out.println(server.getDoubleAuctionsDisplay(userInfo));
    }

    private static void checkDoubleAuction(iSeller server, Scanner scanner, User userInfo) throws RemoteException {
        try {
            System.out.print("Enter the double auction's ID you want to check for: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerSeller.printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.getDoubleAuctionResolution(userInfo, auctionID));
        } catch (NumberFormatException e) {
            System.out.println("The formatting of the auction's ID is incorrect.");
            checkDoubleAuction(server, scanner, userInfo);
            return;
        }
    }

    private static void doubleAuctionHandler(iSeller server, Scanner scanner, User userInfo) {
        System.out.println("Do you want to join an existing double auction or create one?");
        System.out.print("Type CREATE or JOIN: ");
        String decision = scanner.nextLine();
        if (decision.toUpperCase().equals("CREATE")) {
            System.out.println(createDoubleAuction(server, scanner, userInfo));
        } else if (decision.toUpperCase().equals("JOIN")) {
            System.out.println(handleJoin(server, scanner, userInfo));
        } else {
            doubleAuctionHandler(server, scanner, userInfo);
        }
    }

    private static String handleJoin(iSeller server, Scanner scanner, User userInfo) {
        try {
            System.out.print("\nEnter the double auction's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return "";
            }
            int auctionID = Integer.parseInt(possibleExit);
            if (server.doesDoubleAuctionExists(auctionID)) {
                return joinDoubleAuction(server, scanner, userInfo, auctionID);
            } else {
                System.out.println("The given double auction ID does not exist.");
                return handleJoin(server, scanner, userInfo);
            }
        } catch (Exception e) {
            return handleJoin(server, scanner, userInfo);
        }
    }

    private static String joinDoubleAuction(iSeller server, Scanner scanner, User userInfo, int auctionID)
            throws NoSuchElementException {
        try {
            if (server.isSellersLimitReached(auctionID)) {
                return "The limit of sellers has already been reached.";
            } else {
                int itemID = server.getDoubleAuctionsItemID(auctionID);
                System.out.print("Enter the item's title: ");
                String title = scanner.nextLine();
                System.out.print("Enter the item's description: ");
                String description = scanner.nextLine();
                System.out.print("Enter the item's condition (1 New - 5 Broken): ");
                int condition = Integer.parseInt(scanner.nextLine());
                System.out.print("Enter the selling price: ");
                Double sellingPrice = Double.parseDouble(scanner.nextLine().replace(",", "."));
                if (sellingPrice <= 0) {
                    System.out.println("Please, intput a selling price greater than 0.");
                    return joinDoubleAuction(server, scanner, userInfo, auctionID);
                }

                server.joinDoubleAuction(auctionID,
                        new AuctionItem(userInfo, itemID, title, description, condition, sellingPrice));
                return "You have successfully joined the double auction.";
            }
        } catch (Exception e) {
            System.out.println("Please, use the appropiate format for your inputs.");
            return joinDoubleAuction(server, scanner, userInfo, auctionID);
        }
    }

    private static String createDoubleAuction(iSeller server, Scanner scanner, User userInfo)
            throws NoSuchElementException {
        try {
            System.out.println(
                    "A double auction needs to be specified a number of sellers and number of bids(one per buyer).");
            System.out.println(
                    "The double auction will be visible to buyers once the specified number of sellers have joined it.");
            System.out.println(
                    "When the numebr of bids is reached the double auction will be closed. Then the results can be checked using ADD COMMAND HERE.\n");
            System.out.print("Enter the double auction's items ID: ");
            int itemID = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the limit number of sellers: ");
            int limitSellers = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the limit number of bids: ");
            int limitBids = Integer.parseInt(scanner.nextLine());
            int auctionID = server.createDoubleAuction(itemID, limitSellers, limitBids);
            System.out.println("Double auction #" + auctionID + " successfully created.");
            return joinDoubleAuction(server, scanner, userInfo, auctionID);
        } catch (Exception e) {
            return createDoubleAuction(server, scanner, userInfo);
        }
    }
}