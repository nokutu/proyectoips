package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 17/10/2016.
 */
public class Activity implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activity VALUES (?, ?, ?, ?)";
    private final static String UPDATE_QUERY = "UPDATE activity SET deleted = ? WHERE activity_id = ?";

    private static PreparedStatement createStatement;
    private static PreparedStatement updateStatement;

    private int activityId;
    private String activityName;
    private int assistantLimit;

    private List<ActivityBooking> lazyActivityBookings;
    private boolean deleted;

    public Activity(int activityId, String activityName, int assistantLimit) {
        this(activityId, activityName, assistantLimit, false);
    }

    public Activity(int activityId, String activityName, int assistantLimit, boolean deleted) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.assistantLimit = assistantLimit;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setInt(1, activityId);
        createStatement.setString(2, activityName);
        createStatement.setInt(3, assistantLimit);
        createStatement.setBoolean(4, deleted);

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
        if (updateStatement == null) {
            updateStatement = Database.getInstance().getConnection().prepareStatement(UPDATE_QUERY);
        }

        updateStatement.setBoolean(1, deleted);
        updateStatement.setInt(2, activityId);

        updateStatement.execute();
    }

    public List<ActivityBooking> getActivityBookings() {
        if (lazyActivityBookings == null) {
            lazyActivityBookings = Database.getInstance().getActivityBookings().stream().filter(ab -> ab.getActivityId() == activityId).collect(Collectors.toList());
        }
        return lazyActivityBookings;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
