package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class ActivityBooking implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activitybooking VALUES (?, ?, ?)";

    private String activityName;
    private int facilityId;
    private Timestamp bookingTimeStart;

    private PreparedStatement createStatement;

    public ActivityBooking(String activityName, int facilityId, Timestamp bookingTimeStart) {
        this.activityName = activityName;
        this.facilityId = facilityId;
        this.bookingTimeStart = bookingTimeStart;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setString(1, activityName);
        createStatement.setInt(2, facilityId);
        createStatement.setTimestamp(3, bookingTimeStart);

        createStatement.execute();
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }

    public String getActivityName() {
        return activityName;
    }

    public Timestamp getBookingTimeStart() {
        return bookingTimeStart;
    }
}
