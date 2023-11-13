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

    private static void addBasicAuction(iSeller server, Scanner scanner) throws NoSuchElementException {
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
            System.out.println("The formatting of the data was incorrect.");
            System.out.println("E.g.: 1, Item's title, Item's description, 1, 19.99, 24.99");
            addBasicAuction(server, scanner);
            return;
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
            System.out.println(server.closeAuction(userInfo, auctionID) + "\n");
        } catch (Exception e) {
            System.out.println("The formatting of the data was incorrect.");
            closeBasicAuction(server, scanner);
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