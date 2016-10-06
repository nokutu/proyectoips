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
    private boolean paid;

    public FacilityBooking(int facilityId, int memberId, Date date, int timeStart,
                           int timeEnd, String paymentMethod, boolean paid) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.facilityId = facilityId;
        this.memberId = memberId;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPayed(boolean paid) {
        this.paid = paid;
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
