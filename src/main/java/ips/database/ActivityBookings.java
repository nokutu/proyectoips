package ips.database;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class ActivityBookings implements DatabaseItem {

    private int activityId;
    private int facilityId;
    private Timestamp  bookingTimeStart;

    public ActivityBookings(int activityId, int facilityId, Timestamp bookingTimeStart) {
        this.activityId = activityId;
        this.facilityId = facilityId;
        this.bookingTimeStart = bookingTimeStart;
    }

    @Override
    public void create() throws SQLException {
        // TODO
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }
}
