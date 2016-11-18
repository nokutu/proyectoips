package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents each of the bookings made in a facility.
 */
public class FacilityBooking implements DatabaseItem {

	private final static String CREATE_QUERY = "INSERT INTO facilitybooking VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_QUERY = "UPDATE facilitybooking SET payment_method=?, paid=?, facilitybooking_deleted=?, entrance=?, abandon=?, state=? WHERE facilitybooking_id=?";

	public final static String PAYMENT_CASH = "Cash";
	public final static String PAYMENT_FEE = "Fee";

	public static final String STATE_VALID = "Valid";
	public static final String STATE_ANNULLED = "Annulled";
	public static final String STATE_CANCELLED = "Cancelled";

	private static Map<String, String> TRANSLATIONS;

	private static PreparedStatement createStatement;
	private static PreparedStatement updateStatement;

	static {
		TRANSLATIONS = new HashMap<>();
		TRANSLATIONS.put("Cuota", "Fee");
		TRANSLATIONS.put("Efectivo", "Cash");
	}

	private int facilityBookingId;
	private String cancellationCause;
	private Timestamp cancellationDate;
	private int facilityId;
	private int memberId;
	private Timestamp timeStart;
	private Timestamp timeEnd;
	private String paymentMethod;
	private boolean paid;
	private Timestamp entrance;
	private Timestamp abandon;
	private String state;

	private Facility lazyFacility;
	private Member lazyMember;

	/**
	 * Simple constructor. Entrance, abandon and state are set to default
	 * values.
	 */
	public FacilityBooking(int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod,
			boolean paid) {
		this(facilityId, memberId, timeStart, timeEnd, paymentMethod, paid, null, null, STATE_VALID, null, null);
	}

	public FacilityBooking(int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod,
			boolean paid, Timestamp entrance, Timestamp abandon, String state, String cancellationCause, Timestamp cancellationDate) {
		this(getId() + 1, facilityId, memberId, timeStart, timeEnd, paymentMethod, paid, entrance, abandon, state, cancellationCause, cancellationDate);
	}

	public FacilityBooking(int facilityBookingId, int facilityId, int memberId, Timestamp timeStart, Timestamp timeEnd, String paymentMethod,
						   boolean paid, Timestamp entrance, Timestamp abandon, String state, String cancellationCause, Timestamp cancellationDate) {
		this.facilityBookingId = facilityBookingId;
		this.setTimeStart(timeStart);
		this.setTimeEnd(timeEnd);
		this.setFacilityId(facilityId);
		this.setMemberId(memberId);
		this.paymentMethod = paymentMethod;
		this.paid = paid;
		this.entrance = entrance;
		this.abandon = abandon;
		this.state = state;
		this.cancellationCause = cancellationCause;
		this.cancellationDate = cancellationDate;
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
		if (entrance != null) {
			createStatement.setTimestamp(8, new Timestamp(entrance.getTime()));
		} else {
			createStatement.setTimestamp(8, null);
		}

		if (abandon != null) {
			createStatement.setTimestamp(9, new Timestamp(abandon.getTime()));
		} else {
			createStatement.setTimestamp(9, null);
		}
		createStatement.setString(10, state);
		createStatement.setString(11, cancellationCause);
		createStatement.setTimestamp(12, cancellationDate);

		createStatement.execute();
	}

	@Override
	public void update() throws SQLException {
		if (updateStatement == null) {
			updateStatement = Database.getInstance().getConnection().prepareStatement(UPDATE_QUERY);
		}
		updateStatement.setString(1, paymentMethod);
		updateStatement.setBoolean(2, paid);

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

	public String getState() {
		return state;
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
	

	public String getCancellationCause() {
		return cancellationCause;
	}

	public void setCancellationCause(String cancellationCause) {
		this.cancellationCause = cancellationCause;
	}

	public Timestamp getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(Timestamp cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	/**
	 * @return a string representation of the whole class
	 */
	public String toStringFull() {
		String linea = "Instalacion: " + this.getFacility().getFacilityName() + " \n ";
		linea += "ID de la instalacion: " + this.getFacility().getFacilityId() + " \n ";
		if(this.getMemberId()!=0)
		{
		linea += "IDSocio: " + Integer.toString(this.getMemberId()) + " \n ";
		linea += "Socio: " + Database.getInstance().getMemberById(this.getMemberId()).getMemberName() + " \n ";
		}
		else
		{
		linea += "Reservado para la administracion  \n ";	
		}
		
		linea += "Inicio: " + this.getTimeStart().toString() + " \n ";
		linea += "Final: " + this.getTimeEnd().toString() + " \n ";
		linea += "Metodo de pago: " + this.getPaymentMethod() + " \n ";
		if(this.isPaid())
		{
			linea += "Sin pagar \n ";
		}
		else
			linea += "Pagada \n ";
		
		switch (this.state) {
		case STATE_VALID:
			linea += "Estado: Válida \n";
			break;
		case STATE_ANNULLED:
			linea += "Estado: Anulada \n";
			linea += "Cancelada por la administracion \n ";
			linea += "Causa de la cancelacion: "+this.getCancellationCause()+ " \n";
			linea += "Fecha de la cancelacion: "+this.getCancellationDate().toString()+ " \n";
			break;
		case STATE_CANCELLED:
			linea += "Estado: Cancelada \n";
			linea += "Cancelada por el usuario \n ";
			linea += "Fecha de la cancelacion: "+this.getCancellationDate().toString()+ " \n";
			break;
		default:
			break;
		}
		return linea;
	}

	/**
	 * @return a string representation to show to the user
	 */
	public String toString() {
		String s = "";
		String name = "";
		s += "Instalacion: " + Database.getInstance().getFacilityById(getFacilityId()).getFacilityName();
		if(this.getMemberId()==0)
		{
			s += ", por la administracion \n";
		}
		else
		{
		name = Database.getInstance().getMemberById(this.getMemberId()).getMemberName();
		s += ", por el socio: " + name + " \n";
		}
		return s;
	}

	public Facility getFacility() {
		if (lazyFacility == null) {
			Optional<Facility> ofb = Database.getInstance().getFacilities().parallelStream()
					.filter(f -> f.getFacilityId() == facilityId).findAny();
			if (ofb.isPresent()) {
				lazyFacility = ofb.get();
			} else {
				throw new IllegalStateException("No FacilityBooking found for selected FacilityBooking");
			}
		}
		return lazyFacility;
	}

	public Member getMember() {
		if (memberId == 0) {
			return null;
		}
		if (lazyMember == null) {
			Optional<Member> om = Database.getInstance().getMembers().parallelStream()
					.filter(m -> m.getMemberId() == memberId)
					.findAny();
			if (om.isPresent()) {
				lazyMember = om.get();
			} else {
				throw new IllegalStateException("Member id of the FacilityBooking not found");
			}
		}
		return lazyMember;
	}

	public void setState(String state) {
		this.state = state;
	}

	public static String translate(String string) {
		return TRANSLATIONS.get(string);
	}

	private static int getId() {
		Optional<Integer> maxId = Database.getInstance().getFacilityBookings()
				.parallelStream()
				.map(FacilityBooking::getFacilityBookingId)
				.max(Integer::compare);
		if (maxId.isPresent()) {
			return maxId.get();
		} else {
			return 0;
		}
	}
}
