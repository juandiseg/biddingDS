import java.util.HashMap;

public class dataWrapper implements java.io.Serializable {

    private final HashMap<Integer, DoubleAuction> doubleAuctionsMap = new HashMap<Integer, DoubleAuction>();
    private final HashMap<Integer, BasicAuction> basicAuctionsMap = new HashMap<Integer, BasicAuction>();
    private final HashMap<String, User> userMap = new HashMap<String, User>();

    public dataWrapper() {
    }

    public dataWrapper(HashMap<String, User> users, HashMap<Integer, BasicAuction> bAuctions,
            HashMap<Integer, DoubleAuction> dAuctions) {
        userMap.putAll(users);
        basicAuctionsMap.putAll(bAuctions);
        doubleAuctionsMap.putAll(dAuctions);
    }

    public void update(dataWrapper newData) {
        userMap.putAll(newData.getUserMap());
        basicAuctionsMap.putAll(newData.getBasicAuctionMap());
        doubleAuctionsMap.putAll(newData.getDoubleAuctionMap());
    }

    public HashMap<String, User> getUserMap() {
        return userMap;
    }

    public HashMap<Integer, BasicAuction> getBasicAuctionMap() {
        return basicAuctionsMap;
    }

    public HashMap<Integer, DoubleAuction> getDoubleAuctionMap() {
        return doubleAuctionsMap;
    }

}
