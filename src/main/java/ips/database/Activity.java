package ips.database;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class Activity implements DatabaseItem {

    private int activityId;
    private String acitivityName;
    private int assistanceLimit;
    private Timestamp activityTimeStart;
    private Timestamp activityTimeEnd;
    private int activityDuration;
    private boolean activityRecursive;

    public Activity(int activityId, String acitivityName, int assistanceLimit, Timestamp activityTimeStart, Timestamp activityTimeEnd, int activityDuration, boolean activityRecursive) {
        this.activityId = activityId;
        this.acitivityName = acitivityName;
        this.assistanceLimit = assistanceLimit;
        this.activityTimeStart = activityTimeStart;
        this.activityTimeEnd = activityTimeEnd;
        this.activityDuration = activityDuration;
        this.activityRecursive = activityRecursive;
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
