import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class BuyerClient {
    private static User userInfo;

    public static void main(String[] args) throws Exception {
        String name = "buyer_server";
        Registry registry = LocateRegistry.getRegistry();

        boolean onlineStubFound = false;
        iBuyer server = null;
        int i = 1;
        while (!onlineStubFound && i <= 99) {
            try {
                server = (iBuyer) registry.lookup(name + i);
                try {
                    server.exportCertificate();
                    onlineStubFound = true;
                } catch (Exception e) {
                    onlineStubFound = false;
                }
            } catch (NotBoundException e) {
                onlineStubFound = false;
            }
            i++;
        }
        System.out.println("\nWELCOME TO MY BIDDING SYSTEM.");
        Scanner sc = new Scanner(System.in);
        userInfo = UserLogInHandlerBuyer.getUserInfo(server, sc);
        if (userInfo != null)
            handleRequests(server, sc);
    }

    private static void handleRequests(iBuyer server, Scanner scanner) throws IOException {
        UtilityHandlerBuyer.printCommands();
        boolean exit = false;
        try {
            while (!exit) {
                String command = scanner.nextLine().toUpperCase();
                if (command.contains("DOUBLE")) {
                    if (!DoubleAuctionHandlerBuyer.handleDoubleAuctionCommand(command, userInfo, server, scanner)) {
                        System.out.println("Please, enter a valid command. Use HELP to check the available commands.");
                    }
                } else if (command.contains("BASIC") || command.contains("REVERSE")) {
                    if (!BasicAuctionHandlerBuyer.handleBasicAuctionCommand(command, userInfo, server, scanner)) {
                        System.out.println("Please, enter a valid command. Use HELP to check the available commands.");
                    }
                } else {
                    if (!UtilityHandlerBuyer.handleUtilityCommand(command, server, scanner)) {
                        System.out.println("Please, enter a valid command. Use HELP to check the available commands.");
                    }
                }
            }
        } catch (NoSuchElementException e) {

        }
    }
}