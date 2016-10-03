package ips.database;

import java.sql.Date;
import java.util.List;

/**
 * Created by nokutu on 03/10/2016.
 */
public class Fee implements DatabaseItem {

    private Member member;
    // Use Date.valueOf(YYYY-MM--DD) to get the value. Use first day of the month
    private Date month;
    private List<FeeItem> feeItems;

    public Fee() {
        // TODO
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
