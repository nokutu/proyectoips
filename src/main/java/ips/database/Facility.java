package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Facility implements DatabaseItem {

    private int facilityId;
    private String facilityName;

    public Facility(int facilityId, String facilityName) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
    }

    @Override
    public void create() {
        // TODO
    }

    @Override
    public void update() {
        // TODO
    }

    public int getFacilityId() {
        return facilityId;
    }
}
