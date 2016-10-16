package ips.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Availability {
	private final static String SELECT_QUERY = "SELECT MEMBER.MEMBER_NAME, FACILITYBOOKING.TIME_START, FACILITYBOOKING.TIME_END FROM FACILITYBOOKING, MEMBER WHERE FACILITY_ID = ? AND FACILITYBOOKING.MEMBER_ID = MEMBER.MEMBER_ID ORDER BY FACILITYBOOKING.TIME_START ";

	 private static PreparedStatement createStatement;
	 
	 public static ArrayList<Booking> Select(int idFacility) throws SQLException{
		 if (createStatement == null) {
	            createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_QUERY);
	        }
	        createStatement.setInt(1, idFacility);

	        ResultSet rs = createStatement.executeQuery();
	        ArrayList<Booking> bookings = new ArrayList<Booking>();
	        while (rs.next()) {
	        	bookings.add(new Booking(rs.getString(1), rs.getTimestamp(2), rs.getTimestamp(3)));
	        }
		return bookings;
	 }
}