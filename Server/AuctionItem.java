import java.io.Serializable;
import java.rmi.RemoteException;

class AuctionItem implements Serializable {

    private int itemId;
    private String itemTitle;
    private String itemDescription;
    private int itemCondition;

    public AuctionItem(int itemId, String itemTitle, String itemDescription, int itemCondition)
            throws RemoteException {
        this.itemId = itemId;
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        this.itemCondition = itemCondition;
    }

    public int getItemId() throws RemoteException {
        return itemId;
    }

    public String getItemTitle() throws RemoteException {
        return itemTitle;
    }

    public String getItemDescription() throws RemoteException {
        return itemDescription;
    }

    public int getItemCondition() throws RemoteException {
        return itemCondition;
    }

    public void printSummary() throws RemoteException {
        System.out.println("{ itemId : " + itemId + " },");
        System.out.println("{ itemTitle : '" + itemTitle + "'' },");
        System.out.println("{ itemDescription : '" + itemDescription + "' },");
        String condition = "New";
        if (itemCondition == 1)
            condition = "Like new";
        if (itemCondition == 2)
            condition = "In good state";
        if (itemCondition == 3)
            condition = "Has signs of use";
        if (itemCondition == 4)
            condition = "Broken";
        System.out.println("{ itemCondition : '" + condition + "' }");
    }

}