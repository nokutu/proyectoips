package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class FacilityBooking implements DatabaseItem {

    private int timeStart;
    private int timeEnd;
    private int facilityId;
    private int memberId;

    public FacilityBooking(int timeStart, int timeEnd, int facilityId, int memberId) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.facilityId = facilityId;
        this.memberId = memberId;
    }

    @Override
    public void create() {
        // TODO
    }

    @Override
    public void update() {
        // TODO
    }
}
