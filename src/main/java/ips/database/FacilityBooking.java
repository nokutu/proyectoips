package ips.database;

import java.sql.Date;

/**
 * Created by nokutu on 27/09/16.
 */
public class FacilityBooking implements DatabaseItem {

    private final static String PAYMENT_CASH = "cash";
    private final static String PAYMENT_FEE = "fee";

    // Use Date.valueOf(YYYY-MM--DD) to get the value. Use first day of the month
    private Date date;
    private int timeStart;
    private int timeEnd;
    private int facilityId;
    private int memberId;
    private String paymentMethod;
    private boolean payed;

    public FacilityBooking(int timeStart, int timeEnd, int facilityId, int memberId, String paymentMethod) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.facilityId = facilityId;
        this.memberId = memberId;
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
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
