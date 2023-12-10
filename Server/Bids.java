
public class Bids implements java.io.Serializable {
    private User user;
    private Double bidAmount;

    public Bids(User user, Double bidAmount) {
        this.user = user;
        this.bidAmount = bidAmount;
    }

    public User getUser() {
        return user;
    }

    public Double getBidAmount() {
        return bidAmount;
    }
}