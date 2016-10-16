package ips.database;

import java.sql.Timestamp;

public class Booking {
	private String userName;
	private Timestamp timeStart;
	private Timestamp timeEnd;
	
	public Booking(String userName, Timestamp timeStart, Timestamp timeEnd) {
		this.userName = userName;
		this.timeStart = timeStart;
		this.timeEnd = timeEnd;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
	
	@Override
	public String toString() {
		return "Username: " + userName + " timeStart: " + timeStart + "timeEnd: " + timeEnd;
	}
}
