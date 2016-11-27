package ips.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberUsage {
	private final static String SELECT_QUERY = "SELECT FACILITY.FACILITY_ID, FACILITY.FACILITY_NAME, MEMBER.MEMBER_ID, MEMBER.MEMBER_NAME, FACILITYBOOKING.FACILITYBOOKING_ID, FACILITYBOOKING.FACILITY_ID, FACILITYBOOKING.MEMBER_ID, FACILITYBOOKING.TIME_START, FACILITYBOOKING.TIME_END, FACILITYBOOKING.STATE FROM FACILITY, FACILITYBOOKING, MEMBER WHERE FACILITY.FACILITY_ID = FACILITYBOOKING.FACILITY_ID AND MEMBER.MEMBER_ID = FACILITYBOOKING.MEMBER_ID AND MEMBER.MEMBER_ID = ? ORDER BY FACILITYBOOKING.TIME_START";
	private final static String SELECT_USER_ACTIVITIES = "SELECT * FROM ACTIVITY,  ACTIVITYBOOKING , FACILITYBOOKING  WHERE ACTIVITY.ACTIVITY_ID = ACTIVITYBOOKING.ACTIVITY_ID AND FACILITYBOOKING.FACILITYBOOKING_ID = ACTIVITYBOOKING.FACILITYBOOKING_ID AND ACTIVITY. DELETED ='FALSE' AND FACILITYBOOKING.STATE = 'Valid' AND FACILITYBOOKING.FACILITYBOOKING_ID IN (SELECT FACILITYBOOKING_ID FROM ACTIVITYMEMBER WHERE MEMBER_ID = ?)";

	private static PreparedStatement createStatement;

	public static ArrayList<Booking> select(int idUser) throws SQLException {
		if (createStatement == null) {
			createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_QUERY);
		}
		createStatement.setInt(1, idUser);

		ResultSet rs = createStatement.executeQuery();
		ArrayList<Booking> bookings = new ArrayList<Booking>();
		while (rs.next()) {
			bookings.add(new Booking(rs.getInt(5), idUser, rs.getString(4), rs.getString(2), rs.getTimestamp(8),
					rs.getTimestamp(9), rs.getString(10)));
		}
		return bookings;
	}

	public static ArrayList<AdminActitivies> getAdminActitivies(int idUsuario) {
		ArrayList<AdminActitivies> adminActivities = null;
		try {
			createStatement = Database.getInstance().getConnection().prepareStatement(SELECT_USER_ACTIVITIES);
			createStatement.setInt(1, idUsuario);
			ResultSet rs = createStatement.executeQuery();
			adminActivities = new ArrayList<AdminActitivies>();
			while (rs.next()) {
				Activity ac = new Activity(rs.getInt(1), rs.getString(2), rs.getInt(3), false);
				FacilityBooking fb = new FacilityBooking(rs.getInt(8), rs.getInt(9), rs.getInt(10), rs.getTimestamp(11),
						rs.getTimestamp(12), null, false, null, null, null, null, null);
				adminActivities.add(new AdminActitivies(ac, fb));
			}
		} catch (SQLException e) {
			System.out.println("Error en Availability -> getAdminActitivies");
			e.printStackTrace();
		}
		return adminActivities;
	}
}