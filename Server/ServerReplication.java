import org.jgroups.util.Util;
import org.jgroups.*;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerReplication extends ReceiverAdapter {

    private JChannel channel;

    private final DoubleAuctionManager doubleManager = new DoubleAuctionManager();
    private final BasicAuctionManager basicManager = new BasicAuctionManager();
    private final UserManager userManager = new UserManager();
    private final KeyManager keyManager = new KeyManager();

    private final static dataWrapper data = new dataWrapper();

    private iSeller sellersHandler;
    private iBuyer buyersHandler;

    private int numberServers = 0;

    public ServerReplication() throws Exception {
        String serverName = "Franklin" + ((int) (Math.random() * 98 + 1));

        channel = new JChannel();
        channel.setDiscardOwnMessages(true);
        channel.setReceiver(this);
        channel.name(serverName);

        buyersHandler = new buyerProxy(basicManager, doubleManager, userManager, keyManager);
        sellersHandler = new sellerProxy(basicManager, doubleManager, userManager, keyManager);

        channel.connect("FranklinCluster");
        channel.getState(null, 10000);

        eventLoop();

        channel.close();
    }

    private void eventLoop() {
        // If a method that must be called by other server instances is called, it is
        // registered in the Manager's RPCallsToMake List object.
        ArrayList<MethodCaller> unsyncUserManager = userManager.getRPCallsToMake();
        ArrayList<MethodCaller> unsyncDoubleManager = basicManager.getRPCallsToMake();
        ArrayList<MethodCaller> unsyncBasicManager = doubleManager.getRPCallsToMake();
        while (true) {
            try {
                Thread.sleep(500);
                // If there are pending RPCalls, send them to all servers, and clear list.
                if (!unsyncUserManager.isEmpty() || !unsyncDoubleManager.isEmpty() || !unsyncBasicManager.isEmpty()) {
                    unsyncDoubleManager.addAll(unsyncBasicManager);
                    unsyncDoubleManager.addAll(unsyncUserManager);
                    for (MethodCaller temp : unsyncDoubleManager) {
                        Message mess = new Message(null, null, temp);
                        channel.send(mess);
                    }
                    userManager.emptyRPCallsToMake();
                    basicManager.emptyRPCallsToMake();
                    doubleManager.emptyRPCallsToMake();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // OVERRIDES

    @Override
    public void receive(Message msg) {
        // This method is invoked when a message is received.
        MethodCaller temp = (MethodCaller) msg.getObject();
        handleRPCall(temp);
    }

    @Override
    public void viewAccepted(View new_view) {
        // This method is invoked when a new view (server) is visible.
        System.out.println("** view: " + new_view);
        if (numberServers < new_view.getMembers().size() || numberServers == 0) {
            // If server disconnects, attempt rebinding the stubs.
            unbindStubs();
            bindStubs();
            numberServers = new_view.getMembers().size();
        }
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        // A new server instance receives the state (data wrapper) form the coordinator.
        synchronized (data) {
            Util.objectToStream(data, new DataOutputStream(output));
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        // The coordinator sends the state (data wrapper) to a new server instance.
        dataWrapper stateData = (dataWrapper) Util.objectFromStream(new DataInputStream(input));
        synchronized (data) {
            data.update(stateData);
        }
        System.out.println("Data Synch'ed");
    }

    // REMOTE PROCEDURE CALLS handling

    private void handleRPCall(MethodCaller call) {
        String managerName = call.getManagerName();
        String methodName = call.getMethodName();
        Object[] args = call.getArgs();
        if (managerName.equals("UserManager")) {
            handleUserCall(args);
        } else if (managerName.equals("BasicAuctionManager")) {
            handleBasicAuctionCall(methodName, args);
        } else {
            handleDoubleAuctionCall(methodName, args);
        }
    }

    private void handleUserCall(Object[] args) {
        System.out.println("RPC: User sign up");
        userManager.signUp((boolean) args[0], (String) args[1], (String) args[2], (String) args[3], (char) args[4]);
    }

    private void handleBasicAuctionCall(String methodName, Object[] args) {
        if (methodName.equals("bid")) {
            System.out.println("RPC: Basic auction bid");
            basicManager.bid((boolean) args[0], (int) args[1], (User) args[2], (double) args[3]);
        } else if (methodName.equals("add")) {
            System.out.println("RPC: Basic auction creation");
            basicManager.add((boolean) args[0], (AuctionItem) args[1], (Double) args[2]);
        } else if (methodName.equals("close")) {
            System.out.println("RPC: Basic auction closing");
            basicManager.close((boolean) args[0], (User) args[1], (int) args[2]);
        } else {
            System.out.println("RPC: Basic auction closing with winner approval");
            basicManager.closeAndApproveWinner((boolean) args[0], (int) args[1], (boolean) args[2]);
        }
    }

    private void handleDoubleAuctionCall(String methodName, Object[] args) {
        if (methodName.equals("bid")) {
            System.out.println("RPC: Double auction bid");
            doubleManager.bid((boolean) args[0], (int) args[1], (User) args[2], (double) args[3]);
        } else if (methodName.equals("createDoubleAuction")) {
            System.out.println("RPC: Double auction creation");
            doubleManager.createDoubleAuction((boolean) args[0], (int) args[1], (int) args[2], (int) args[3]);
        } else {
            System.out.println("RPC: Double auction joining");
            doubleManager.joinDoubleAuction((boolean) args[0], (int) args[1], (AuctionItem) args[2]);
        }
    }

    // STUB REGISTRY BIDING

    private boolean unbindStubs() {
        // Catched exceptions are not printed because these are expected to occur at
        // some point or another for the method to execute successfully.
        try {
            Registry registry = LocateRegistry.getRegistry();
            try {
                registry.unbind("buyer_server");
                registry.unbind("seller_server");
                return true;
            } catch (NotBoundException e) {
                return false;
            }
        } catch (RemoteException b) {
            return false;
        }
    }

    private void bindStubs() {
        try {
            Registry registry = LocateRegistry.getRegistry();
            iBuyer buyerStub = (iBuyer) UnicastRemoteObject.exportObject(buyersHandler, 0);
            iSeller sellerStub = (iSeller) UnicastRemoteObject.exportObject(sellersHandler, 0);
            registry.bind("buyer_server", buyerStub);
            registry.bind("seller_server", sellerStub);
        } catch (Exception e) {
        }
    }

    /*
     * STATE GETTERS
     * These are called for the managers to access a reference of their respective
     * HashMap's in the data wrapper.
     */

    public static HashMap<String, User> getUserState() {
        return data.getUserMap();
    }

    public static HashMap<Integer, DoubleAuction> getDoubleAuctionState() {
        return data.getDoubleAuctionMap();
    }

    public static HashMap<Integer, BasicAuction> getBasicAuctionState() {
        return data.getBasicAuctionMap();
    }

}
