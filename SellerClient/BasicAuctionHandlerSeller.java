import java.rmi.RemoteException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BasicAuctionHandlerSeller {

    public static boolean handleBasicAuctionCommand(String command, User userInfo, iSeller server, Scanner scanner)
            throws RemoteException {
        if (command.equals("ADD BASIC AUCTION")) {
            addBasicAuction(server, scanner, userInfo);
            return true;
        } else if (command.equals("CLOSE BASIC AUCTION")) {
            closeBasicAuction(server, scanner, userInfo);
            return true;
        } else if (command.equals("CHECK BASIC AUCTION")) {
            checkBasicAuction(server, scanner, userInfo);
            return true;
        } else {
            return false;
        }
    }

    private static void addBasicAuction(iSeller server, Scanner scanner, User userInfo) {
        try {
            String sellerUsername = userInfo.getUsername();
            System.out.print("\nEnter the item's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerSeller.printCommands();
                return;
            }
            int itemID = Integer.parseInt(possibleExit);
            // check if it exists
            System.out.print("Enter the item's title: ");
            String title = scanner.nextLine();
            System.out.print("Enter the item's description: ");
            String description = scanner.nextLine();
            System.out.print("Enter the item's condition (1 New - 5 Broken): ");
            int condition = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter the reserve price: ");
            Double reservePrice = Double.parseDouble(scanner.nextLine().replace(",", "."));
            System.out.print("Enter the selling price: ");
            Double sellingPrice = Double.parseDouble(scanner.nextLine().replace(",", "."));
            if (reservePrice > sellingPrice) {
                System.out.println("The given start price is higher than the acceptable price.");
                addBasicAuction(server, scanner, userInfo);
                return;
            }
            int auctionID = server.createBasicAuction(sellerUsername, itemID, title, description, condition,
                    reservePrice,
                    sellingPrice);
            if (auctionID == -1) {
                System.out.println("There aren't any registered items for the reference item ID. Use command ITEMS");
                addBasicAuction(server, scanner, userInfo);
                return;
            } else {
                System.out.println("Auction SUCCESSFULLY CREATED. Auction's ID = " + auctionID);
                System.out.println("---------------------------------------------------------");
            }
        } catch (Exception e) {
            addBasicAuction(server, scanner, userInfo);
            return;
        }
    }

    protected static void checkBasicAuction(iSeller server, Scanner scanner, User userInfo)
            throws NoSuchElementException {
        try {
            System.out.print("\nEnter the auction's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerSeller.printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.getBasicAuctionStatus(userInfo, auctionID) + "\n");
        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            checkBasicAuction(server, scanner, userInfo);
        }
    }

    private static void closeBasicAuction(iSeller server, Scanner scanner, User userInfo)
            throws NoSuchElementException {
        try {
            System.out.print("\nEnter the item's auction's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                UtilityHandlerSeller.printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            String answer = server.closeBasicAuction(userInfo, auctionID);
            System.out.println(answer + "\n");
            if (answer.contains("Do you want to grant the item to this bidder?")) {
                closeBasicAuctionConfirmation(server, scanner, auctionID);
                return;
            }
        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            closeBasicAuction(server, scanner, userInfo);
        }
    }

    private static void closeBasicAuctionConfirmation(iSeller server, Scanner scanner, int auctionID)
            throws NoSuchElementException {
        try {
            System.out.print("Types YES or NOT: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("YES") || possibleExit.toUpperCase().equals("Y")) {
                System.out.println(server.closeBasicAuctionAndApproveWinner(auctionID, true));
            } else {
                System.out.println(server.closeBasicAuctionAndApproveWinner(auctionID, false));
            }

        } catch (Exception e) {
            System.out.println("Please try again. Type YES, NOT or BACK.");
            closeBasicAuctionConfirmation(server, scanner, auctionID);
        }
    }

}