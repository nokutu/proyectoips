package ips.database;

/**
 * Created by nokutu on 03/10/2016.
 */
public class FeeItem implements DatabaseItem {

    // Represents cents
    private int amount;
    private int fee_id;

    public FeeItem(int amount,int fee) {
        this.amount=amount;
        this.fee_id=fee;
    }

    @Override
    public void create() {
        // TODO
    }

    @Override
    public void update() {
        // TODO
    }
    
}
