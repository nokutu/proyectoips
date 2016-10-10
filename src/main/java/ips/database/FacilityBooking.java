package ips.database;


import java.sql.Date;

/**
 * Created by nokutu on 27/09/16.
 */
public class FacilityBooking implements DatabaseItem {

    private final static String PAYMENT_CASH = "cash";
    private final static String PAYMENT_FEE = "fee";

    // Use Date.valueOf(dd/MM/yyyy) to get the value. Use first day of the
    // month
    private Date timeStart;
    private Date timeEnd;
    private int facilityId;
    private int memberId;
    private String paymentMethod;
    private boolean paid;
    private boolean deleted_flag;

    public FacilityBooking(int facilityId, int memberId, Date timeStart, Date timeEnd, String paymentMethod, boolean paid,
                           boolean deleted_flag) {
        this.setTimeStart(timeStart);
        this.setTimeEnd(timeEnd);
        this.setFacilityId(facilityId);
        this.setMemberId(memberId);
        this.paymentMethod = paymentMethod;
        this.paid = paid;
        this.deleted_flag = deleted_flag;
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

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public boolean isDeleted_flag() {
        return deleted_flag;
    }

    public void setDeleted_flag(boolean deleted_flag) {
        this.deleted_flag = deleted_flag;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(int facilityId) {
        this.facilityId = facilityId;
    }
}
