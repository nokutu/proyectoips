package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Facility implements DatabaseItem {

    private int facility_id;
    private String facility_name;

    public Facility(int facility_id, String facility_name) {
        this.facility_id = facility_id;
        this.facility_name = facility_name;
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
