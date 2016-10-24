package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class Activity implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activity VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_QUERY = "";

    private static PreparedStatement createStatement;
    private static PreparedStatement updateStatement;

    private static int MAX_ID = 0;

    private int activityId;
    private String activityName;
    private int facilityID;
    private int assistantLimit;
    private Timestamp activityTimeStart;
    private Timestamp activityTimeEnd;
    private int activityDuration;
    private boolean activityRecursive;

    public Activity(int activityId, String activityName, int facilityID, int assistantLimit, Timestamp activityTimeStart, Timestamp activityTimeEnd, int activityDuration, boolean activityRecursive) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.facilityID = facilityID;
        this.assistantLimit = assistantLimit;
        this.activityTimeStart = activityTimeStart;
        this.activityTimeEnd = activityTimeEnd;
        this.activityDuration = activityDuration;
        this.activityRecursive = activityRecursive;

        MAX_ID = Math.max(activityId, MAX_ID);
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setInt(1, activityId);
        createStatement.setString(2, activityName);
        createStatement.setInt(3, facilityID);
        createStatement.setInt(4, assistantLimit);
        createStatement.setTimestamp(5, activityTimeStart);
        createStatement.setTimestamp(6, activityTimeEnd);
        createStatement.setInt(7, activityDuration);
        createStatement.setBoolean(8, activityRecursive);

        createStatement.execute();
    }

    public String getActivityName() {
		return activityName;
	}

	@Override
    public void update() throws SQLException {
        // TODO
    }

    public static int getValidID() {
        return ++MAX_ID;
    }
}
