package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class Activity implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activity VALUES (?, ?)";
    private final static String UPDATE_QUERY = "";

    private static PreparedStatement createStatement;
    private static PreparedStatement updateStatement;

    private String activityName;
    private int assistantLimit;

    public Activity(String activityName, int assistantLimit) {
        this.activityName = activityName;
        this.assistantLimit = assistantLimit;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setString(1, activityName);
        createStatement.setInt(2, assistantLimit);

        createStatement.execute();
    }

    public String getActivityName() {
		return activityName;
	}

    public int getAssistantLimit() {
        return assistantLimit;
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }
}
