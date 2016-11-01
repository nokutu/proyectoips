package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class Activity implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activity VALUES (?, ?, ?, ?)";
    private final static String UPDATE_QUERY = "";

    private static PreparedStatement createStatement;
    private static PreparedStatement updateStatement;

    private int activityId;
    private String activityName;
    private int assistantLimit;
    private int monitorId;

    public Activity(int activityId, String activityName, int assistantLimit, int monitorId) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.assistantLimit = assistantLimit;
        this.monitorId = monitorId;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setInt(1, activityId);
        createStatement.setString(2, activityName);
        createStatement.setInt(3, assistantLimit);
        createStatement.setInt(4, monitorId);

        createStatement.execute();
    }

    public String getActivityName() {
		return activityName;
	}

    public int getActivityId() {
        return activityId;
    }

    public int getAssistantLimit() {
        return assistantLimit;
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }
}
