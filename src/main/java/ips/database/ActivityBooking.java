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

    private String activityName;
    private int facilityBookingId;

    private static PreparedStatement createStatement;
    private Date bookingTimeStart;

    private FacilityBooking lazyFacilityBooking;

    public ActivityBooking(String activityName, int facilityBookingId) {
        this.activityName = activityName;
        this.facilityBookingId = facilityBookingId;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }

        createStatement.setString(1, activityName);
        createStatement.setInt(2, facilityBookingId);

        createStatement.execute();
    }

    @Override
    public void update() throws SQLException {
        // TODO
    }

    public String getActivityName() {
        return activityName;
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
}
