package ips.database;

import java.sql.SQLException;

/**
 * Created by nokutu on 02/11/2016.
 */
public class Monitor implements DatabaseItem {

	private int id;
	private String name;

	public Monitor(int id, String name) {
		this.id = id;
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
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String toString(){
		return name;
	}

}
