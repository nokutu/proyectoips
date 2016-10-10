package ips.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by nokutu on 27/09/16.
 */
public class FacilityBooking implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO facilitybooking VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final static String UPDATE_QUERY = "";

    private final static String PAYMENT_CASH = "cash";
    private final static String PAYMENT_FEE = "fee";

    private static PreparedStatement createStatement;

    private int facilityId;
    private int memberId;
    private Date timeStart;
    private Date timeEnd;
    private String paymentMethod;
    private boolean paid;
    private boolean deletedFlag;

    public FacilityBooking(int facilityId, int memberId, Date timeStart, Date timeEnd, String paymentMethod, boolean paid,
                           boolean deletedFlag) {
        this.setTimeStart(timeStart);
        this.setTimeEnd(timeEnd);
        this.setFacilityId(facilityId);
        this.setMemberId(memberId);
        this.paymentMethod = paymentMethod;
        this.paid = paid;
        this.deletedFlag = deletedFlag;
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
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }
        createStatement.setInt(1, facilityId);
        createStatement.setInt(2, memberId);
        createStatement.setDate(3, timeStart);
        createStatement.setDate(4, timeEnd);
        createStatement.setString(5, paymentMethod);
        createStatement.setBoolean(6, paid);
        createStatement.setBoolean(7, deletedFlag);

        createStatement.execute();
    }

    @Override
    public void update() {
        // TODO
        throw new UnsupportedOperationException();
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

    public boolean isDeletedFlag() {
        return deletedFlag;
    }

    public void setDeletedFlag(boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
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
