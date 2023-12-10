import org.jgroups.*;
import org.jgroups.blocks.ReplicatedHashMap;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Util;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;

public class ServerReplication extends ReceiverAdapter {

    private JChannel channel;

    private final String nameSelf = "F" + ((int) (Math.random() * 100));
    public static ReplicatedHashMap<Integer, BasicAuction> availableBasicAuctions;
    private final static HashMap<String, User> userState = new HashMap<String, User>();

    private static iBuyer buyersHandler = new buyerProxy();
    private static iSeller sellersHandler = new sellerProxy();
    private int numberServers = 0;

    public static HashMap<String, User> getUserState() {
        return ServerReplication.userState;
    }

    public ServerReplication() throws Exception {
        channel = new JChannel();
        channel.setDiscardOwnMessages(true);
        channel.setReceiver(this);
        channel.connect("FranklinCluster");
        channel.getState(null, 10000);

        eventLoop();
        channel.close();
    }

    @Override
    public void receive(Message msg) {
        // Receives new User entries and adds them to theuser's HashMap.
        User temp = (User) msg.getObject();
        System.out.println(temp);
        synchronized (userState) {
            userState.put(temp.getUsername(), temp);
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
        HashMap<String, User> map = (HashMap<String, User>) Util.objectFromStream(new DataInputStream(input));
        synchronized (userState) {
            userState.clear();
            userState.putAll(map);
        }
        System.out.println("received userState (" + map.size() + " messages in chat history):");
        map.values().forEach(System.out::println);
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (userState) {
            Util.objectToStream(userState, new DataOutputStream(output));
        }
    }

    private void eventLoop() {
        while (true) {
            try {
                Thread.sleep(1000);
                LinkedList<User> unsync = UserManager.getUnsyncronizedUsers();
                if (!unsync.isEmpty()) {
                    for (User temp : unsync) {
                        Message mess = new Message(null, null, temp);
                        channel.send(mess);
                    }
                    UserManager.cleanUnsynchronizedUsers();
                    for (User temp : userState.values()) {
                        System.out.print(temp.getUsername() + ", ");
                    }
                    System.out.println(".");
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
