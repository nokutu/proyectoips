package ips.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PaymentsDetails {
	private final static String SELECT_QUERY = "SELECT FACILITY.FACILITY_NAME, FACILITY.PRICE, FACILITYBOOKING.MEMBER_ID, FACILITYBOOKING.TIME_START, FACILITYBOOKING.TIME_END FROM FACILITY, FACILITYBOOKING WHERE FACILITY.FACILITY_ID = FACILITYBOOKING.FACILITY_ID AND FACILITYBOOKING.MEMBER_ID = ? AND FACILITYBOOKING.STATE = 'Valid' AND FACILITYBOOKING.PAID = 'True' ORDER BY FACILITYBOOKING.TIME_START";

	 private static PreparedStatement createStatement;
	 
	 public static ArrayList<Booking> Select(int idUser) throws SQLException{
		 if (createStatement == null) {
	            createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_QUERY);
	        }
	        createStatement.setInt(1, idUser);

	        ResultSet rs = createStatement.executeQuery();
	        ArrayList<Booking> bookings = new ArrayList<Booking>();
	        while (rs.next()) {
	        	Booking b = new Booking();
	        	b.setFacilityName(rs.getString(1));
	        	b.setPricePerHour(rs.getInt(2));
	        	b.setTimeStart(rs.getTimestamp(4));
	        	b.setTimeEnd(rs.getTimestamp(5));
	        	bookings.add(b);
	        }
		return bookings;
	 }
}