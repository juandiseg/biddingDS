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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerReplication extends ReceiverAdapter {
    JChannel jChannel;
    RpcDispatcher dispatcher;
    final List<String> state;
    public static ReplicatedHashMap<String, User> userMap;
    public static ReplicatedHashMap<Integer, BasicAuction> availableBasicAuctions;
    public static ReplicatedHashMap<Integer, DoubleAuction> availableDoubleAuctions;

    private List<String> servers = new LinkedList<>();
    private String nameSelf;
    private static iBuyer buyersHandler = new buyerProxy();
    private static iSeller sellersHandler = new sellerProxy();
    private int numberServers = 0;

    public ServerReplication() throws Exception {
        jChannel = new JChannel();
        nameSelf = "Franklin" + ((int) (Math.random() * 100));
        jChannel.name(nameSelf);
        jChannel.setReceiver(this);
        jChannel.connect("FranklinCluster");
        userMap = new ReplicatedHashMap<String, User>(jChannel);
        userMap.start(5000);
        availableDoubleAuctions = new ReplicatedHashMap<Integer, DoubleAuction>(jChannel);
        availableDoubleAuctions.start(5000);
        availableBasicAuctions = new ReplicatedHashMap<Integer, BasicAuction>(jChannel);
        availableBasicAuctions.start(5000);

        this.state = new LinkedList<String>();
        this.state.add("Franklin" + nameSelf);
        this.state.add("This is kinda cool");
        try {
            jChannel.getState(null, 1000);
        } catch (StateTransferException e) {
            System.err.println(e.getCause());
            System.err.println(e);
        }

        View view = jChannel.getView();

        for (Address address : view.getMembers()) {
            System.out.println("Device is connected: " + address);
        }

    }

    @Override
    public void receive(Message msg) {
        String message = (String) msg.getObject();
        if (!msg.getSrc().toString().equals(nameSelf)) {
            System.out.println("Received a message from: " + msg.getSrc() + "\nmessage: " + message);
        }
    }

    @Override
    public void viewAccepted(View new_view) {
        // This tells when a server disconnects
        System.out.println("view: " + new_view);
        if (numberServers < new_view.getMembers().size() || numberServers == 0) {
            bindRegistry();
            numberServers = new_view.getMembers().size();
        }
    }

    @Override
    public void setState(InputStream input) throws Exception {
        List<String> list;
        list = (List<String>) Util.objectFromStream(new DataInputStream(input));

        synchronized (state) {
            state.clear();
            state.addAll(list);
        }
        System.out.println("Updated State");
        for (String str : list) {
            System.out.println(str);
        }
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        synchronized (state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    public void start() {
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(3000);
                Message message = new Message(null, null, "Hello World");
                jChannel.send(message);
                System.out.println(state);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void bindRegistry() {
        try {
            iBuyer buyerStub = (iBuyer) UnicastRemoteObject.exportObject(buyersHandler, 0);
            Registry buyerRegistry = LocateRegistry.getRegistry();
            iSeller sellerStub = (iSeller) UnicastRemoteObject.exportObject(sellersHandler, 0);
            Registry sellerRegistry = LocateRegistry.getRegistry();
            try {
                buyerRegistry.unbind("buyer_server");
                sellerRegistry.unbind("seller_server");
            } catch (NotBoundException e) {
                System.out.println("NOT BOUND EXCEPTION");
            } finally {
                try {
                    buyerRegistry.bind("buyer_server", buyerStub);
                    sellerRegistry.bind("seller_server", sellerStub);
                } catch (Exception e) {
                    System.out.println("Not needed");
                }
            }

        } catch (RemoteException b) {
            System.out.println(b);
        }
    }
}
