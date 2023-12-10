import org.jgroups.*;
import org.jgroups.blocks.ReplicatedHashMap;
import org.jgroups.util.Util;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServerReplication extends ReceiverAdapter {

    private JChannel channel;

    public static ReplicatedHashMap<Integer, BasicAuction> availableBasicAuctions;
    private final static dataWrapper data = new dataWrapper();
    private static iBuyer buyersHandler = new buyerProxy();
    private static iSeller sellersHandler = new sellerProxy();
    private int numberServers = 0;
    private final String serverName = "Franklin" + ((int) (Math.random() * 98 + 1));

    public static HashMap<String, User> getUserState() {
        return data.getUserMap();
    }

    public static HashMap<Integer, DoubleAuction> getDoubleAuctionState() {
        return data.getDoubleAuctionMap();
    }

    public static HashMap<Integer, BasicAuction> getBasicAuctionState() {
        return data.getBasicAuctionMap();
    }

    public ServerReplication() throws Exception {
        channel = new JChannel();
        channel.setDiscardOwnMessages(true);
        channel.setReceiver(this);
        channel.name(serverName);
        channel.connect("FranklinCluster");
        channel.getState(null, 10000);

        eventLoop();
        channel.close();
    }

    @Override
    public void receive(Message msg) {
        // Receives new User entries and adds them to theuser's HashMap.
        dataWrapper temp = (dataWrapper) msg.getObject();
        synchronized (data) {
            data.update(temp);
        }
    }

    @Override
    public void viewAccepted(View new_view) {
        // This tells when a server connects / disconnects
        System.out.println("** view: " + new_view);
        if (numberServers < new_view.getMembers().size() || numberServers == 0) { // If server disconnects, rebind stub.
            bindRegistry();
            numberServers = new_view.getMembers().size();
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        dataWrapper stateData = (dataWrapper) Util.objectFromStream(new DataInputStream(input));
        synchronized (data) {
            data.update(stateData);
        }
        System.out.println("Data Synch'ed");
        // System.out.println("received userState (" + map.size() + " messages in chat
        // history):");
        // map.values().forEach(System.out::println);
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (data) {
            Util.objectToStream(data, new DataOutputStream(output));
        }
    }

    private void eventLoop() {
        while (true) {
            try {
                Thread.sleep(1000);
                HashMap<String, User> unsyncUsers = UserManager.getUnsyncronizedUsers();
                HashMap<Integer, BasicAuction> unsyncBasicAuctions = BasicAuctionManager.getUnsyncronizedAuctions();
                HashMap<Integer, DoubleAuction> unsyncDoubleAuctions = DoubleAuctionManager.getUnsyncronizedAuctions();
                if (!unsyncUsers.isEmpty() && !unsyncBasicAuctions.isEmpty() && !unsyncDoubleAuctions.isEmpty()) {
                    dataWrapper temp = new dataWrapper(unsyncUsers, unsyncBasicAuctions, unsyncDoubleAuctions);
                    Message mess = new Message(null, null, temp);
                    channel.send(mess);
                    UserManager.cleanUnsynchronizedUsers();
                    BasicAuctionManager.cleanUnsynchronizedAuctions();
                    DoubleAuctionManager.cleanUnsynchronizedAuctions();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void bindRegistry() {
        // Catched exceptions are not printed because these are expected to occur at
        // some point or another for the method to execute successfully.
        try {
            iBuyer buyerStub = (iBuyer) UnicastRemoteObject.exportObject(buyersHandler, 0);
            Registry buyerRegistry = LocateRegistry.getRegistry();
            iSeller sellerStub = (iSeller) UnicastRemoteObject.exportObject(sellersHandler, 0);
            Registry sellerRegistry = LocateRegistry.getRegistry();
            try {
                buyerRegistry.unbind("buyer_server");
                sellerRegistry.unbind("seller_server");
            } catch (NotBoundException e) {
            } finally {
                try {
                    buyerRegistry.bind("buyer_server", buyerStub);
                    sellerRegistry.bind("seller_server", sellerStub);
                } catch (Exception e) {
                }
            }

        } catch (RemoteException b) {
        }
    }
}
