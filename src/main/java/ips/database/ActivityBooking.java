package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * Created by nokutu on 17/10/2016.
 */
public class ActivityBooking implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activitybooking VALUES (?, ?, ?)";

    private int activityId;
    private int facilityBookingId;

    private static PreparedStatement createStatement;
    private int monitorId;

    private FacilityBooking lazyFacilityBooking;
    private Activity lazyActivity;
    private Monitor lazyMonitor;

    public ActivityBooking(int activityId, int facilityBookingId, int monitorId) {
        this.activityId = activityId;
        this.facilityBookingId = facilityBookingId;
        this.monitorId = monitorId;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setInt(1, activityId);
        createStatement.setInt(2, facilityBookingId);
        createStatement.setInt(3, monitorId);

        createStatement.execute();
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }

    public int getMonitorId() {
        return monitorId;
    }

    public int getActivityId() {
        return activityId;
    }

    public FacilityBooking getFacilityBooking() {
        if (lazyFacilityBooking == null) {
            Optional<FacilityBooking> ofb = Database.getInstance().getFacilityBookings().parallelStream().filter(fb -> fb.getFacilityBookingId() == facilityBookingId).findAny();
            if (ofb.isPresent()) {
                lazyFacilityBooking = ofb.get();
            } else {
                throw new IllegalStateException("No FacilityBooking found for selected ActivityBooking");
            }
        }
        return lazyFacilityBooking;
    }

    public Activity getActivity() {
        if (lazyActivity == null) {
            Optional<Activity> ofb = Database.getInstance().getActivities().parallelStream().filter(a -> a.getActivityId() == facilityBookingId).findAny();
            if (ofb.isPresent()) {
                lazyActivity = ofb.get();
            } else {
                throw new IllegalStateException("No Activity found for selected ActivityBooking");
            }
        }
        return lazyActivity;
    }

    public Monitor getMonitor() {
        if (lazyMonitor == null) {
            Optional<Monitor> ofb = Database.getInstance().getMonitors().parallelStream().filter(m -> m.getMonitorId() == monitorId).findAny();
            if (ofb.isPresent()) {
                lazyMonitor = ofb.get();
            } else {
                throw new IllegalStateException("No Activity found for selected ActivityBooking");
            }
        }
        return lazyMonitor;
    }

    public int getFacilityBookingId() {
        return facilityBookingId;
    }
}
