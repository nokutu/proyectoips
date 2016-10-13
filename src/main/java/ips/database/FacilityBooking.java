package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
    private Timestamp timeStart;
    private Timestamp timeEnd;
    private String paymentMethod;
    private boolean paid;
    private boolean deletedFlag;

    public FacilityBooking(int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod, boolean paid,
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
        createStatement.setTimestamp(3, new Timestamp(timeStart.getTime()));
        createStatement.setTimestamp(4, new Timestamp(timeEnd.getTime()));
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

    public Timestamp getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public Timestamp getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Timestamp timeEnd) {
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
