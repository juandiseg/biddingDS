import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SellerClient {
    private static User userInfo;

    public static void main(String[] args) throws Exception {
        String name = "seller_server";
        Registry registry = LocateRegistry.getRegistry();
        iSeller server = (iSeller) registry.lookup(name);

        System.out.println("\nWELCOME TO MY BIDDING SYSTEM.");
        Scanner sc = new Scanner(System.in);
        userInfo = getUserInfo(server, sc);
        if (userInfo != null)
            handleRequests(server, sc);
    }

    private static void handleRequests(iSeller server, Scanner scanner) throws IOException {
        printCommands();
        boolean exit = false;
        try {
            while (!exit) {
                String command = scanner.nextLine().toUpperCase();
                if (command.equals("ADD BASIC AUCTION")) {
                    addBasicAuction(server, scanner);
                } else if (command.equals("ADD DOUBLE AUCTION")) {
                    doubleActionHandler(server, scanner);
                } else if (command.equals("GET SPECIFICATIONS ITEMS")) {
                    getSpecsAuction(server, scanner);
                } else if (command.equals("CLOSE BASIC AUCTION")) {
                    closeBasicAuction(server, scanner);
                } else if (command.equals("CHECK BASIC AUCTION")) {
                    checkBasicAuction(server, scanner);
                } else if (command.equals("HELP")) {
                    printCommands();
                } else if (command.equals("SEE SELLABLE ITEMS")) {
                    displaySellableItems(server);
                }
            }
        } catch (NoSuchElementException e) {

        }
    }

    private static void displaySellableItems(iSeller server) {
        try {
            System.out.println("\n" + server.getItemsReferenceID());
        } catch (RemoteException e) {
            System.out.println("Something went wrong while communicating with the server.");
        }
    }

    private static void getSpecsAuction(iSeller server, Scanner scanner) throws NoSuchElementException {
        try {
            System.out.print("\nEnter the item's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            }
            int itemID = Integer.parseInt(possibleExit);
            HashMap<Integer, AuctionItem> result = server.getSpec(userInfo, itemID);
            if (!result.isEmpty()) {
                System.out.print("\n");
                for (Integer temp : result.keySet()) {
                    result.get(temp).printSummary(temp);
                }
            } else
                System.out.println("The ID for the given item doesn't exist.");

        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            checkBasicAuction(server, scanner);
        }
    }

    private static void addBasicAuction(iSeller server, Scanner scanner) {
        try {
            String sellerUsername = userInfo.getUsername();
            System.out.print("\nEnter the item's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
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
                addBasicAuction(server, scanner);
                return;
            }
            int auctionID = server.createAuction(sellerUsername, itemID, title, description, condition, reservePrice,
                    sellingPrice);
            if (auctionID == -1) {
                System.out.println("There aren't any registered items for the reference item ID. Use command ITEMS");
                addBasicAuction(server, scanner);
                return;
            } else {
                System.out.println("Auction SUCCESSFULLY CREATED. Auction's ID = " + auctionID);
                System.out.println("---------------------------------------------------------");
            }
        } catch (Exception e) {
            addBasicAuction(server, scanner);
            return;
        }
    }

    private static void doubleActionHandler(iSeller server, Scanner scanner) {
        System.out.println("Do you want to join an existing double auction or create one?");
        System.out.print("Type CREATE or JOIN:");
        String decision = scanner.nextLine();
        if (decision.toUpperCase().equals("CREATE")) {
            System.out.println(addDoubleAuction(server, scanner));
        } else if (decision.toUpperCase().equals("JOIN")) {
            handleJoin(server, scanner);
        }
        return;
    }

    private static void handleJoin(iSeller server, Scanner scanner) {
        try {
            System.out.print("Enter the double auction's ID: ");
            int auctionID = Integer.parseInt(scanner.nextLine());
            joinDoubleAuction(server, scanner, auctionID);
        } catch (Exception e) {
            handleJoin(server, scanner);
        }
    }

    private static String joinDoubleAuction(iSeller server, Scanner scanner, int auctionID)
            throws NoSuchElementException {
        try {
            if (server.isSellersLimitReached(auctionID)) {
                return "The limit of sellers has already been reached.";
            } else {
                int itemID = server.getItemIDofAuction(auctionID);
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
                    return joinDoubleAuction(server, scanner, auctionID);
                }
                server.addAuctionItem(auctionID,
                        new AuctionItem(userInfo.getUsername(), itemID, title, description, condition, condition));
                return "You have successfully joined the double auction.";
            }
        } catch (Exception e) {
            return joinDoubleAuction(server, scanner, auctionID);
        }
    }

    private static String addDoubleAuction(iSeller server, Scanner scanner) throws NoSuchElementException {
        try {
            System.out.println(
                    "A double auction needs to be specified a number of sellers and number of bids(one per buyer).\n");
            System.out.println(
                    "The double auction will be visible to buyers once the specified number of sellers have joined it.\n");
            System.out.println(
                    "When the numebr of bids is reached the double auction will be closed. Then the results can be checked using ADD COMMAND HERE.\n");
            System.out.print("\nEnter the double auction's items ID: ");
            int itemID = Integer.parseInt(scanner.nextLine());
            System.out.print("\nEnter the limit number of sellers: ");
            int limitSellers = Integer.parseInt(scanner.nextLine());
            System.out.print("\nEnter the limit number of bids: ");
            int limitBids = Integer.parseInt(scanner.nextLine());
            int auctionID = server.addDoubleAuction(itemID, limitSellers, limitBids);
            System.out.println("Double auction successfully created.");
            return joinDoubleAuction(server, scanner, auctionID);
        } catch (Exception e) {
            return addDoubleAuction(server, scanner);
        }
    }

    private static void closeBasicAuction(iSeller server, Scanner scanner) throws NoSuchElementException {
        try {
            System.out.print("\nEnter the item's auction's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            String answer = server.closeAuction(userInfo, auctionID);
            System.out.println(answer + "\n");
            if (answer.contains("Do you want to grant the item to this bidder?")) {
                closeBasicAuctionConfirmation(server, scanner, auctionID);
                return;
            }
        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            closeBasicAuction(server, scanner);
        }
    }

    private static void closeBasicAuctionConfirmation(iSeller server, Scanner scanner, int auctionID)
            throws NoSuchElementException {
        try {
            System.out.print("Types YES or NOT: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("YES")) {
                System.out.println(server.closeAuctionConfirmation(auctionID, true));
            } else {
                System.out.println(server.closeAuctionConfirmation(auctionID, false));
            }

        } catch (Exception e) {
            System.out.println("Please try again. Type YES, NOT or BACK.");
            closeBasicAuctionConfirmation(server, scanner, auctionID);
        }
    }

    private static void checkBasicAuction(iSeller server, Scanner scanner) throws NoSuchElementException {
        try {
            System.out.print("\nEnter the auction's ID: ");
            String possibleExit = scanner.nextLine();
            if (possibleExit.toUpperCase().equals("BACK")) {
                return;
            } else if (possibleExit.toUpperCase().equals("HELP")) {
                printCommands();
                return;
            }
            int auctionID = Integer.parseInt(possibleExit);
            System.out.println("\n" + server.checkAuctionStatus(userInfo, auctionID) + "\n");
        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            checkBasicAuction(server, scanner);
        }
    }

    private static void printCommands() {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("Here are the available commands:");
        System.out.println("HELP\t\t\t| Prints all available commands.");
        System.out.println("BACK\t\t\t| You can use it at any time to go back to the previous menu.");
        System.out.println("SEE SELLABLE ITEMS\t| Displays the items which can be listed on the auction.");
        System.out.println("GET SPECIFICATIONS ITEMS| Displays the specs for all the items listed on auctions.");
        System.out.println("ADD BASIC AUCTION\t| Creates a new regular auction.");
        System.out.println("ADD DOUBLE AUCTION\t| Creates a new double auction.");
        System.out.println("CHECK BASIC AUCTION\t| Checks the state of a regular auction.");
        System.out.println("CLOSE BASIC AUCTION\t| Closes an existing regular auction and prints the results.");
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

    private static User getUserInfo(iSeller server, Scanner scanner) throws IOException {
        try {
            boolean first = true;
            while (true) {
                if (first) {
                    System.out.print("\nEnter \"L\" if you're ALREADY A USER, or enter \"S\" to SIGN UP: ");
                } else {
                    System.out.print("\nPlease, enter \"L\" to LOG IN or \"S\" to SIGN UP: ");
                    first = false;
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

    private static User signUp(iSeller server, Scanner scanner) throws IOException, NoSuchElementException {
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
                System.out.print("The given username is already taken.");
            } else {
                return user;
            }
        }
    }

    private static User logIn(iSeller server, Scanner scanner) throws IOException, NoSuchElementException {
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