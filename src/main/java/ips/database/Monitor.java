package ips.database;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 02/11/2016.
 */
public class Monitor implements DatabaseItem {

	private int monitorId;
	private String name;

	public Monitor(int monitorId, String name) {
		this.monitorId = monitorId;
		this.name = name;
	}

	@Override
	public void create() throws SQLException {
		throw new UnsupportedOperationException();
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
