import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

import java.io.*;
import java.util.HashMap;

public class SimpleChat extends ReceiverAdapter {
    JChannel channel;
    final HashMap<String, User> state = new HashMap<String, User>();
    final String nameSelf = "F" + ((int) (Math.random() * 100));

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        User temp = (User) msg.getObject();
        System.out.println(temp);
        synchronized (state) {
            state.put(temp.getUsername(), temp);
        }
    }

    public void getState(OutputStream output) throws Exception {
        synchronized (state) {
            Util.objectToStream(state, new DataOutputStream(output));
        }
    }

    @SuppressWarnings("unchecked")
    public void setState(InputStream input) throws Exception {
        HashMap<String, User> map = (HashMap<String, User>) Util.objectFromStream(new DataInputStream(input));
        synchronized (state) {
            state.clear();
            state.putAll(map);
        }
        System.out.println("received state (" + map.size() + " messages in chat history):");
        map.values().forEach(System.out::println);
    }

    private void start() throws Exception {
        channel = new JChannel();
        channel.setDiscardOwnMessages(true);
        channel.setReceiver(this);
        channel.connect("FrankCluster");
        channel.getState(null, 10000);
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        int count = 1;
        while (true) {
            try {
                Thread.sleep(5000);
                User tempUser = new User("USER" + count + nameSelf, "EMAIL" + count, "PASS" + count, 'B');
                state.put("user" + count + nameSelf, tempUser);
                count++;
                Message mess = new Message(null, null, tempUser);
                channel.send(mess);
                for (User temp : state.values()) {
                    System.out.print(temp.getUsername() + ", ");
                }
                System.out.println(".");
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new SimpleChat().start();
    }
}