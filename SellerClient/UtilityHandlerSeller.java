import java.io.File;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.HashMap;

public class UtilityHandlerSeller {

    public static boolean handleUtilityCommand(String command, iSeller server, Scanner scanner, User userInfo)
            throws RemoteException {
        if (command.equals("GET SPECIFICATIONS ITEMS")) {
            getSpecsAuction(server, scanner, userInfo);
            return true;
        } else if (command.equals("HELP")) {
            printCommands();
            return true;
        } else if (command.equals("SEE SELLABLE ITEMS")) {
            displaySellableItems(server);
            return true;
        } else if (command.equals("CHECK SIGNATURE")) {
            checkSignature(server);
            return true;
        } else {
            return false;
        }
    }

    private static void checkSignature(iSeller server) {
        try {
            if (checkSignature(server, signDocument(server))) {
                System.out.println("The signature is RELIABLE.\n");
            } else {
                System.out.println("The signature is NOT RELIABLE.\n");
            }
        } catch (Exception e) {
            System.out.println("The signature is NOT RELIABLE.\n");
        }
    }

    private static void displaySellableItems(iSeller server) {
        try {
            System.out.println("\n" + server.getItemsReferenceID());
        } catch (RemoteException e) {
            System.out.println("Something went wrong while communicating with the server.");
        }
    }

    private static void getSpecsAuction(iSeller server, Scanner scanner, User userInfo) throws NoSuchElementException {
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
            BasicAuctionHandlerSeller.checkBasicAuction(server, scanner, userInfo);
        }
    }

    public static void printCommands() {
        System.out.println("\n---------------------------------------------------------------------------------------");
        System.out.println("Here are the available commands:");
        System.out.println("HELP\t\t\t| Prints all available commands.");
        System.out.println("BACK\t\t\t| You can use it at any time to go back to the previous menu.");
        System.out.println("SEE SELLABLE ITEMS\t| Displays the items which can be listed on the auction.");
        System.out.println("GET SPECIFICATIONS ITEMS| Displays the specs for all the items listed on auctions.");
        System.out.println("ADD BASIC AUCTION\t| Creates a new regular auction.");
        System.out.println("ADD DOUBLE AUCTION\t| Creates a new double auction.");
        System.out.println("VIEW DOUBLE AUCTIONS\t| Displays the open double auctions.");
        System.out.println("CHECK BASIC AUCTION\t| Checks the state of a regular auction.");
        System.out.println(
                "CHECK DOUBLE AUCTION\t| Displays the resolution of a double auction you've an item listed in.");
        System.out.println("CLOSE BASIC AUCTION\t| Closes an existing regular auction and prints the results.");
        System.out.println("CHECK SIGNATURE\t\t| Tests whether the server's signature has been tampered.");
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

    private static byte[] signDocument(iSeller server) throws Exception {
        File dummyFile = new File("keyCertification\\dummyFile.txt");
        byte[] fileContent = Files.readAllBytes(dummyFile.toPath());
        return server.signDocument(fileContent);
    }

    private static Boolean checkSignature(iSeller server, byte[] signedDoc) throws Exception {
        Certificate theCert = server.exportCertificate();
        PublicKey pubKey = theCert.getPublicKey();
        byte[] sigToVerify = signedDoc;
        // tamperSignature(sigToVerify);
        File dummyFile = new File("keyCertification\\dummyFile.txt");
        byte[] fileContent = Files.readAllBytes(dummyFile.toPath());
        Signature sig = Signature.getInstance("SHA256WithRSA");
        sig.initVerify(pubKey);
        sig.update(fileContent, 0, fileContent.length);
        return sig.verify(sigToVerify);
    }

    /*
     * private static void tamperSignature(byte[] sigToVerify) {
     * sigToVerify[1] = sigToVerify[5];
     * sigToVerify[2] = sigToVerify[5];
     * sigToVerify[3] = sigToVerify[5];
     * sigToVerify[4] = sigToVerify[5];
     * sigToVerify[6] = sigToVerify[5];
     * sigToVerify[5] = sigToVerify[9];
     * }
     */

}