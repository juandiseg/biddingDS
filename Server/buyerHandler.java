
import java.rmi.RemoteException;

public class buyerHandler implements iBuyer {

    @Override
    public Double normalAuctionBid(int auctionID, User user, Double bidAmount) throws RemoteException {
        return NormalAuctionManager.bid(auctionID, user, bidAmount);
    }

    @Override
    public String checkAuctionStatus(User user, int auctionID) throws RemoteException {
        return NormalAuctionManager.checkAuctionStatusBuyer(user, auctionID);
    }

    @Override
    public String getAuctionsDisplay(int itemID) throws RemoteException {
        return NormalAuctionManager.getAuctionsDisplay(itemID);
    }

    @Override
    public String getItemsDisplay() throws RemoteException {
        return NormalAuctionManager.getItemsDisplay();
    }

    @Override
    public User logInUsername(String username, String password) throws RemoteException {
        if (UserManager.validateUserByUsername(username, password) == 'B')
            return UserManager.getUser(username);
        return null;
    }

    @Override
    public User signUp(String username, String email, String password) throws RemoteException {
        return UserManager.addUser(username, email, password, 'B');
    }

    @Override
    public boolean doesUsernameExist(String username) throws RemoteException {
        return UserManager.usernamePresent(username);
    }

    @Override
    public String viewReverseAuction(int itemID) throws RemoteException {
        return NormalAuctionManager.viewReverseAuction(itemID);
    }

    @Override
    public String viewDoubleAuctions(User user) throws RemoteException {
        return DoubleAuctionManager.viewDoubleAuctionsBuyers(user);
    }

    @Override
    public boolean doubleAuctionBid(int auctionID, User user, double bidAmount) throws RemoteException {
        return DoubleAuctionManager.bid(auctionID, user, bidAmount);
    }

    @Override
    public String checkDoubleAuctionResolution(int auctionID, User user) throws RemoteException {
        return DoubleAuctionManager.getResolutionBuyer(auctionID, user);
    }

}
