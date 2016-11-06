package ips.database;

import java.sql.Timestamp;

public class Booking {
	private int bookingId;
	private int userID;
	private String userName;
	private String facilityName;
	private Timestamp timeStart;
	private Timestamp timeEnd;
	private String state;
	private int pricePerHour;
	
	public Booking() {

	}
	
	public Booking(int bookingId, int userID, String userName, String facilityName, Timestamp timeStart, Timestamp timeEnd, String state) {
		this.bookingId = bookingId;
		this.userID = userID;
		this.userName = userName;
		this.facilityName = facilityName;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
		this.state = state;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
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
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(int pricePerHour) {
		this.pricePerHour = pricePerHour;
	}

	@Override
	public String toString() {
		return "Username: " + userName + " timeStart: " + timeStart + "timeEnd: " + timeEnd;
	}
}
