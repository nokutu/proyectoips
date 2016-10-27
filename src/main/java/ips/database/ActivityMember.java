package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by jorge on 27/10/2016.
 */
public class ActivityMember implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activitymember VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE_QUERY =
            "UPDATE SET assistance=?, deleted=? " +
                    "WHERE activity_name=? AND booking_time_start=? AND member_id=?";

    private static PreparedStatement createStatement;
    private static PreparedStatement updateStatement;

    private String activityName;
    private Timestamp bookingTimeStart;
    private int memberId;
    private boolean assistance;
    private boolean deleted;

    public ActivityMember(String activityName, Timestamp bookingTimeStart, int memberId, boolean assistance, boolean deleted) {
        this.activityName = activityName;
        this.bookingTimeStart = bookingTimeStart;
        this.memberId = memberId;
        this.assistance = assistance;
        this.deleted = deleted;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setString(1, activityName);
        createStatement.setTimestamp(2, bookingTimeStart);
        createStatement.setInt(3, memberId);
        createStatement.setBoolean(4, assistance);
        createStatement.setBoolean(5, deleted);

        createStatement.execute();
    }

    @Override
    public void update() throws SQLException {
        if (updateStatement == null) {
            updateStatement = Database.getInstance().getConnection().prepareStatement(UPDATE_QUERY);
        }

        updateStatement.setBoolean(1, assistance);
        updateStatement.setBoolean(2, deleted);
        updateStatement.setString(3, activityName);
        updateStatement.setTimestamp(4, bookingTimeStart);
        updateStatement.setInt(5, memberId);

        updateStatement.execute();
    }

    public int getMemberId() {
        return memberId;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
