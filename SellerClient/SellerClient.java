import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
        userInfo = UserLogInHandlerSeller.getUserInfo(server, sc);
        if (userInfo != null)
            handleRequests(server, sc);
    }

    private static void handleRequests(iSeller server, Scanner scanner) throws IOException {
        UtilityHandlerSeller.printCommands();
        boolean exit = false;
        try {
            while (!exit) {
                String command = scanner.nextLine().toUpperCase();
                if (command.contains("DOUBLE")) {
                    if (!DoubleAuctionHandlerSeller.handleDoubleAuctionCommand(command, server, scanner, userInfo)) {
                        System.out.println("Please, enter a valid command. Use HELP to check the available commands.");
                    }
                } else if (command.contains("BASIC") || command.contains("REVERSE")) {
                    if (!BasicAuctionHandlerSeller.handleBasicAuctionCommand(command, userInfo, server, scanner)) {
                        System.out.println("Please, enter a valid command. Use HELP to check the available commands.");
                    }
                } else {
                    if (!UtilityHandlerSeller.handleUtilityCommand(command, server, scanner, userInfo)) {
                        System.out.println("Please, enter a valid command. Use HELP to check the available commands.");
                    }
                }
            }
        } catch (NoSuchElementException e) {

        }
    }

}