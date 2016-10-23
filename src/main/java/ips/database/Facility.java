package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Facility implements DatabaseItem {

    private int facilityId;
    private String facilityName;
    private double price;

    public Facility(int facilityId, String facilityName, int price) {
        this.facilityId = facilityId;
        this.facilityName = facilityName;
        this.price=price;
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

	public double getPrice() {
		return price;
	}

	public String getFacilityName() {
		return facilityName;
	}

}
