import org.jgroups.*;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.Util;

import java.io.*;
import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
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
    private List<String> servers = new LinkedList<>();
    private String self;
    private static iBuyer buyersHandler = new buyerProxy();
    private static iSeller sellersHandler = new sellerProxy();

    public ServerReplication() throws Exception {
        jChannel = new JChannel();
        int number = (int) (Math.random() * 100);
        self = "Franklin" + number;
        jChannel.name(self);
        jChannel.setReceiver(this);
        jChannel.connect("FranklinCluster");
        this.state = new LinkedList<String>();
        this.state.add("Franklin" + number);
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
        if (!msg.getSrc().toString().equals(self)) {
            System.out.println("Received a message from: " + msg.getSrc() + "\nmessage: " + message);
        }
    }

    @Override
    public void viewAccepted(View new_view) {
        // This tells when a server disconnects
        System.out.println("view: " + new_view);
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

    private void bindRegistry() throws AccessException, RemoteException, AlreadyBoundException {
        if (jChannel.getView().getMembers().size() <= 1) {
            forceBindRegistry();
        }
        System.out.println("Server ready");
    }

    private void forceBindRegistry() {
        iBuyer buyerStub = (iBuyer) UnicastRemoteObject.exportObject(buyersHandler, 0);
        Registry buyerRegistry = LocateRegistry.getRegistry();
        buyerRegistry.bind("buyer_server", buyerStub);

        iSeller sellerStub = (iSeller) UnicastRemoteObject.exportObject(sellersHandler, 0);
        Registry sellerRegistry = LocateRegistry.getRegistry();
        sellerRegistry.bind("seller_server", sellerStub);
    }
}
