package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

/**
 * Created by nokutu on 17/10/2016.
 */
public class ActivityBooking implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO activitybooking VALUES (?, ?)";

    private int activityId;
    private int facilityBookingId;

    private static PreparedStatement createStatement;
    private Date bookingTimeStart;

    private FacilityBooking lazyFacilityBooking;
    private Activity lazyActivity;

    public ActivityBooking(int activityId, int facilityBookingId) {
        this.activityId = activityId;
        this.facilityBookingId = facilityBookingId;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setInt(1, activityId);
        createStatement.setInt(2, facilityBookingId);

        createStatement.execute();
    }

    @Override
    public void update() throws SQLException {
        // TODO
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

    public int getFacilityBookingId() {
        return facilityBookingId;
    }
}
