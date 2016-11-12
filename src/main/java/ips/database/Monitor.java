package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 02/11/2016.
 */
public class Monitor implements DatabaseItem {

	private final static String CREATE_QUERY = "INSERT INTO monitor VALUES (?, ?)";
	
	private int monitorId;
	private String name;
	
	private static PreparedStatement createStatement;

	public Monitor(int monitorId, String name) {
		this.monitorId = monitorId;
		this.name = name;
	}

	@Override
	public void create() throws SQLException {
		 if (createStatement == null) {
	            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
	        }

	        createStatement.setInt(1, monitorId);
	        createStatement.setString(2, name);

	        createStatement.execute();
	}

	@Override
	public void update() throws SQLException {
		throw new UnsupportedOperationException();
	}

	public int getMonitorId() {
		return monitorId;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return name;
	}

	public void setMonitorId(int monitorId) {
		this.monitorId = monitorId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ActivityBooking> getActivityBookings() {
		return Database.getInstance().getActivityBookings().stream()
				.filter(ab -> ab.getMonitorId() == monitorId)
				.collect(Collectors.toList());
	}

	public List<Activity> getActivities() {
		return Database.getInstance().getActivities().stream()
				.filter(a -> a.getActivityBookings().stream().filter(ab -> ab.getMonitorId() == monitorId).findAny().isPresent())
				.collect(Collectors.toList());
	}

	public static Monitor get(int id) {
		Optional<Monitor> om = Database.getInstance().getMonitors().parallelStream()
				.filter(m -> m.getMonitorId() == id)
				.findAny();
		if (om.isPresent()) {
			return om.get();
		} else {
			return null;
		}
	}

}
