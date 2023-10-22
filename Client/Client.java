import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter args.");
            return;
        }
        int n = Integer.parseInt(args[0]);
        try {
            String name = "auction_server";
            Registry registry = LocateRegistry.getRegistry("auction_server");
            ICalc server = (ICalc) registry.lookup(name);

            AuctionItem result = server.getSpec(n, 0);
            System.out.println("result is " + result);
            result.printSummary();
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }
}