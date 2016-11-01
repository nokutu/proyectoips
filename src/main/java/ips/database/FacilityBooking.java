package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Represents each of the bookings made in a facility.
 */
public class FacilityBooking implements DatabaseItem {

	private final static String CREATE_QUERY = "INSERT INTO facilitybooking VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_QUERY = "UPDATE facilitybooking SET payment_method=?, paid=?, facilitybooking_deleted=?, entrance=?, abandon=?, state=? WHERE facilitybooking_id=?";

	private final static String PAYMENT_CASH = "cash";
	private final static String PAYMENT_FEE = "fee";

	private static int MAX_ID = 0;

	private static PreparedStatement createStatement;
	private static PreparedStatement updateStatement;

	private int facilityBookingId;
	private int facilityId;
	private int memberId;
	private Timestamp timeStart;
	private Timestamp timeEnd;
	private String paymentMethod;
	private boolean paid;
	private boolean deletedFlag;
	private Timestamp entrance;
	private Timestamp abandon;
	private String state;

	/**
	 * Simple constructor. Entrance, abandon and state are set to default
	 * values.
	 */
	public FacilityBooking(int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod,
			boolean paid, boolean deletedFlag) {
		this(facilityId, memberId, timeStart, timeEnd, paymentMethod, paid, deletedFlag, null, null, "Valid");
	}

	public FacilityBooking(int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod,
			boolean paid, boolean deletedFlag, Timestamp entrance, Timestamp abandon, String state) {
		this(MAX_ID + 1, facilityId, memberId, timeStart, timeEnd, paymentMethod, paid, deletedFlag, entrance, abandon, state);
	}

	public FacilityBooking(int facilityBookingId, int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod,
						   boolean paid, boolean deletedFlag, Timestamp entrance, Timestamp abandon, String state) {
		this.facilityBookingId = facilityBookingId;
		this.setTimeStart(timeStart);
		this.setTimeEnd(timeEnd);
		this.setFacilityId(facilityId);
		this.setMemberId(memberId);
		this.paymentMethod = paymentMethod;
		this.paid = paid;
		this.deletedFlag = deletedFlag;
		this.entrance = entrance;
		this.abandon = abandon;
		this.state = state;

		MAX_ID = Math.max(MAX_ID, facilityBookingId + 1);
	}

	public int getFacilityBookingId() {
		return facilityBookingId;
	}

	public Timestamp getEntrance() {
		return entrance;
	}

	public void setEntrance(Timestamp entrance) {
		this.entrance = entrance;
	}

	public Timestamp getAbandon() {
		return abandon;
	}

	public void setAbandon(Timestamp abandon) {
		this.abandon = abandon;
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

		createStatement.setInt(1, facilityBookingId);
		createStatement.setInt(2, facilityId);
		createStatement.setInt(3, memberId);
		createStatement.setTimestamp(4, new Timestamp(timeStart.getTime()));
		createStatement.setTimestamp(5, new Timestamp(timeEnd.getTime()));
		createStatement.setString(6, paymentMethod);
		createStatement.setBoolean(7, paid);
		createStatement.setBoolean(8, deletedFlag);
		if (entrance != null) {
			createStatement.setTimestamp(9, new Timestamp(entrance.getTime()));
		} else {
			createStatement.setTimestamp(9, null);
		}

		if (abandon != null) {
			createStatement.setTimestamp(10, new Timestamp(abandon.getTime()));
		} else {
			createStatement.setTimestamp(10, null);
		}

		createStatement.setString(11, state);

		createStatement.execute();
	}

	@Override
	public void update() throws SQLException {
		if (updateStatement == null) {
			updateStatement = Database.getInstance().getConnection().prepareStatement(UPDATE_QUERY);
		}
		updateStatement.setString(1, paymentMethod);
		updateStatement.setBoolean(2, paid);
		updateStatement.setBoolean(3, deletedFlag);
		
		if (entrance != null) {
			updateStatement.setTimestamp(4, new Timestamp(entrance.getTime()));
		} else {
			updateStatement.setTimestamp(4, null);
		}
		if (abandon != null) {
			updateStatement.setTimestamp(5, new Timestamp(abandon.getTime()));
		} else {
			updateStatement.setTimestamp(5, null);
		}

		updateStatement.setString(6, state);

		updateStatement.setInt(7, facilityBookingId);


		updateStatement.execute();
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

	/**
	 * @return a string representation of the whole class
	 */
	public String toStringFull() {
		String linea = "Facility: " + Integer.toString(this.getFacilityId()) + " ";
		linea += "Member: " + Integer.toString(this.getMemberId()) + " ";
		linea += "Start: " + this.getTimeStart().toString() + " ";
		linea += "End: " + this.getTimeEnd().toString() + " ";
		linea += " ";
		return linea;
	}

	/**
	 * @return a string representation to show to the user
	 */
	public String toString() {
		String s = "";
		s += "Facility " + Database.getInstance().getFacilityById(getFacilityId()).getFacilityName();
		String name = Database.getInstance().getMemberById(this.getMemberId()).getMemberName();
		s += "by member " + name + " \n";
		return s;
	}

	public void cancel() {
		setDeletedFlag(true);
		try {
			update();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
