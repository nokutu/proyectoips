package ips.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Availability {
	private final static String SELECT_QUERY_ADMIN = "SELECT 0, 'Admin', FACILITYBOOKING.TIME_START, FACILITYBOOKING.TIME_END, FACILITYBOOKING.FACILITYBOOKING_ID FROM FACILITYBOOKING, MEMBER WHERE FACILITY_ID = ? AND FACILITYBOOKING.MEMBER_ID =0 AND FACILITYBOOKING.STATE = 'Valid'";
	private final static String SELECT_QUERY_USER = "SELECT MEMBER.MEMBER_ID, MEMBER.MEMBER_NAME, FACILITYBOOKING.TIME_START, FACILITYBOOKING.TIME_END, FACILITYBOOKING.FACILITYBOOKING_ID FROM FACILITYBOOKING, MEMBER WHERE FACILITY_ID = ? AND FACILITYBOOKING.MEMBER_ID = MEMBER.MEMBER_ID AND FACILITYBOOKING.STATE = 'Valid' ";
	private final static String SELECT_ACTIVIDAD_EN_RESERVA = "SELECT ACTIVITY_ID, ACTIVITY_NAME, ASSISTANT_LIMIT FROM ACTIVITY WHERE ACTIVITY_ID in (SELECT ACTIVITY_ID FROM ACTIVITYBOOKING WHERE FACILITYBOOKING_ID = ?) AND DELETED='False'";
	
	private static PreparedStatement createStatement;

	public static ArrayList<Booking> select(int idFacility) throws SQLException {
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_QUERY_ADMIN);
		createStatement.setInt(1, idFacility);
		ResultSet rs = createStatement.executeQuery();
		while (rs.next()) {
			Booking b = new Booking();
			b.setUserID(rs.getInt(1));
			b.setUserName(rs.getString(2));
			b.setTimeStart(rs.getTimestamp(3));
			b.setTimeEnd(rs.getTimestamp(4));
			b.setBookingId(rs.getInt(5));
			bookings.add(b);
		}
		createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_QUERY_USER);
		createStatement.setInt(1, idFacility);
		rs = createStatement.executeQuery();
		while (rs.next()) {
			Booking b = new Booking();
			b.setUserID(rs.getInt(1));
			b.setUserName(rs.getString(2));
			b.setTimeStart(rs.getTimestamp(3));
			b.setTimeEnd(rs.getTimestamp(4));
			b.setBookingId(rs.getInt(5));
			bookings.add(b);
		}
		return bookings;
	}
	
	public static Activity ActividadesEnReserva(int idReserva) {
		Activity actividad = null;
		try {
			createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_ACTIVIDAD_EN_RESERVA);
			createStatement.setInt(1, idReserva);
			ResultSet rs = createStatement.executeQuery();
			while (rs.next()) {
				actividad = new Activity(rs.getInt(1), rs.getString(2), rs.getInt(3));
			}
		} catch (SQLException e) {
			System.out.println("Error en Availability -> ActividadesEnReserva");
			e.printStackTrace();
		}
		return actividad;
	}
}