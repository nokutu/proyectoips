package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

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
    private Activity lazyActivity;
    private FacilityBooking lazyFacilityBooking;

    public ActivityMember(int activityId, int facilityBookingId, int memberId, boolean assistance, boolean deleted) {
        this.activityId = activityId;
        this.facilityBookingId = facilityBookingId;
        this.memberId = memberId;
        this.assistance = assistance;
        this.deleted = deleted;
    }
    public ActivityMember(int activityId, int facilityBookingId, int memberId){
    	this(activityId, facilityBookingId, memberId, true, false);
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

    public int getFacilityBookingId() {
        return facilityBookingId;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Activity getActivity() {
        if (lazyActivity == null) {
            Optional<Activity> ofb = Database.getInstance().getActivities().parallelStream().filter(a -> a.getActivityId() == activityId).findAny();
            if (ofb.isPresent()) {
                lazyActivity = ofb.get();
            } else {
                throw new IllegalStateException("No Activity found for selected ActivityBooking: " + activityId);
            }
        }
        return lazyActivity;
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
    
    @Override
    public boolean equals(Object obj) {
    	if(obj instanceof ActivityMember){
    		ActivityMember obj2 = (ActivityMember)obj;
    		return obj2.getActivityId()==this.activityId
			&& obj2.getFacilityBookingId()==this.facilityBookingId
			&& obj2.getMemberId()==this.memberId;
    	}
    	else return false;
    	
    }
}
