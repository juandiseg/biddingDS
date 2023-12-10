
import java.io.File;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.security.cert.Certificate;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Scanner;

public class UtilityHandlerBuyer {

    public static boolean handleUtilityCommand(String command, iBuyer server, Scanner scanner) throws RemoteException {
        if (command.equals("SEE ITEMS")) {
            viewItemsDisplay(server);
            return true;
        } else if (command.equals("CHECK SIGNATURE")) {
            checkSignature(server);
            return true;
        } else if (command.equals("HELP")) {
            printCommands();
            return true;
        } else {
            return false;
        }
    }

    private static void viewItemsDisplay(iBuyer server) throws RemoteException {
        System.out.println("\n" + server.getItemsDisplay());
    }

    private static void checkSignature(iBuyer server) {
        try {
            if (isSignatureReliable(server, signDocument(server))) {
                System.out.println("The signature is RELIABLE.\n");
            } else {
                System.out.println("The signature is NOT RELIABLE.\n");
            }
        } catch (Exception e) {
            System.out.println("The signature is NOT RELIABLE.\n");
        }
    }

    private static byte[] signDocument(iBuyer server) throws Exception {
        File dummyFile = new File("keyCertification/dummyFile.txt");
        byte[] fileContent = Files.readAllBytes(dummyFile.toPath());
        return server.signDocument(fileContent);
    }

    private static Boolean isSignatureReliable(iBuyer server, byte[] signedDoc) throws Exception {
        Certificate theCert = server.exportCertificate();
        PublicKey pubKey = theCert.getPublicKey();
        byte[] sigToVerify = signedDoc;
        // tamperSignature(sigToVerify);
        File dummyFile = new File("keyCertification/dummyFile.txt");
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

    public static void printCommands() {
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
        System.out.println("CHECK BASIC AUCTION\t| Checks the state of a regular auction.");
        System.out.println("SEE ITEMS\t\t| Displays items foundable on open auctions.");
        System.out.println("CHECK SIGNATURE\t\t| Tests whether the server's signature has been tampered.");
        System.out.println("---------------------------------------------------------------------------------------\n");
    }

}