import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements ICalc {
    public Server() {
        super();
    }

    public int factorial(int n) throws RemoteException {
        if (n <= 1)
            return 1;
        int retNum = 1;
        for (int i = 1; i <= n; i++)
            retNum = retNum * i;
        System.out.println("Client request handled. Input = " + n + " | Output = " + retNum + ".");
        return retNum;
    }

    public static void main(String[] args) {
        try {
            Server s = new Server();
            String name = "myserver";
            ICalc stub = (ICalc) UnicastRemoteObject.exportObject(s, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }
}