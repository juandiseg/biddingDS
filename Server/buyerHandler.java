
import java.rmi.RemoteException;

public class buyerHandler implements iBuyer {

    @Override
    public Double bid(int auctionID, String username, String email, Double bidAmount) throws RemoteException {
        return NormalAuctionManager.bid(auctionID, username, email, bidAmount);
    }

    @Override
    public String checkAuctionStatus(String username, int auctionID) throws RemoteException {
        return "Auction going fine";
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

}
