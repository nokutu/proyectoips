package ips.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Availability {
	private final static String SELECT_QUERY = "SELECT MEMBER.MEMBER_ID, MEMBER.MEMBER_NAME, FACILITYBOOKING.TIME_START, FACILITYBOOKING.TIME_END, FACILITYBOOKING.FACILITYBOOKING_ID FROM FACILITYBOOKING, MEMBER WHERE FACILITY_ID = ? AND FACILITYBOOKING.MEMBER_ID = MEMBER.MEMBER_ID AND FACILITYBOOKING.STATE = 'Valid' ORDER BY FACILITYBOOKING.TIME_START ";

	 private static PreparedStatement createStatement;
	 
	 public static ArrayList<Booking> Select(int idFacility) throws SQLException{
		 if (createStatement == null) {
	            createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_QUERY);
	        }
	        createStatement.setInt(1, idFacility);

	        ResultSet rs = createStatement.executeQuery();
	        ArrayList<Booking> bookings = new ArrayList<Booking>();
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
}