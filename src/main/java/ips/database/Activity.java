package ips.database;

import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 17/10/2016.
 */
public class Activity implements DatabaseItem {

    private static int MAX_ID = 0;

    private int activityId;
    private String activityName;
    private int facilityID;
    private int assistanceLimit;
    private Timestamp activityTimeStart;
    private Timestamp activityTimeEnd;
    private int activityDuration;
    private boolean activityRecursive;

    public Activity(int activityId, String activityName, int facilityID, int assistanceLimit, Timestamp activityTimeStart, Timestamp activityTimeEnd, int activityDuration, boolean activityRecursive) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.facilityID = facilityID;
        this.assistanceLimit = assistanceLimit;
        this.activityTimeStart = activityTimeStart;
        this.activityTimeEnd = activityTimeEnd;
        this.activityDuration = activityDuration;
        this.activityRecursive = activityRecursive;

        MAX_ID = Math.max(activityId, MAX_ID);
    }

    @Override
    public void create() throws SQLException {
        // TODO
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }

    public static int getValidID() {
        return ++MAX_ID;
    }
}
