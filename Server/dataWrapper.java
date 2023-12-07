import java.util.HashMap;

public class dataWrapper implements java.io.Serializable {

    private static HashMap<String, User> userMap;
    private static HashMap<Integer, BasicAuction> basicAuctionsMap;
    private static HashMap<Integer, DoubleAuction> doubleAuctionsMap;

    public dataWrapper() {
        userMap = new HashMap<String, User>();
        basicAuctionsMap = new HashMap<Integer, BasicAuction>();
        doubleAuctionsMap = new HashMap<Integer, DoubleAuction>();
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
