package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by jorge on 27/10/2016.
 */
public class ActivityMember implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activitymember VALUES (?, ?, ?, ?, ?)";
    private final static String UPDATE_QUERY =
            "UPDATE activitymember SET assistance=?, deleted=? " +
                    "WHERE activity_ID=? AND member_id=?";

    private static PreparedStatement createStatement;
    private static PreparedStatement updateStatement;

    private int activityId;
    private int facilityBookingId;
    private int memberId;
    private boolean assistance;
    private boolean deleted;

    public ActivityMember(int activityId, int facilityBookingId, int memberId, boolean assistance, boolean deleted) {
        this.activityId = activityId;
        this.facilityBookingId = facilityBookingId;
        this.memberId = memberId;
        this.assistance = assistance;
        this.deleted = deleted;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setInt(1, activityId);
        createStatement.setInt(2, facilityBookingId);
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
        updateStatement.setInt(3, activityId);
        updateStatement.setInt(4, memberId);

        updateStatement.execute();
    }

    public int getMemberId() {
        return memberId;
    }

    public int getActivityId() {
		return activityId;
	}
    
	public boolean isAssistance() {
		return assistance;
	}

	public void setAssistance(boolean assistance) {
		this.assistance = assistance;
	}

	public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
