package ips.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by nokutu on 27/09/16.
 */
public class Database {

    private final static String QUERY_FACILITIES = "SELECT * FROM facility";
    private final static String QUERY_MEMBERS = "SELECT * FROM member";
    private final static String QUERY_FACILITYBOOKINGS = "SELECT * FROM facilitybooking";

    private static Database instance;
    private Connection conn;

    private List<Facility> facilities;
    private List<FacilityBooking> facilityBookings;
    private List<Member> members;
    private List<Fee> fees;
    private List<FeeItem> feeItems;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        facilities = new ArrayList<>();
        facilityBookings = new ArrayList<>();
        members = new ArrayList<>();
        fees = new ArrayList<>();
        feeItems = new ArrayList<>();

        Properties connectionProps = new Properties();
        connectionProps.put("user", "SA");
        try {
            conn = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/test", connectionProps);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        try {
            fillDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillDatabase() throws SQLException {
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(QUERY_FACILITIES);
        while (rs.next()) {
            facilities.add(new Facility(rs.getInt(1), rs.getString(2)));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_MEMBERS);
        while (rs.next()) {
            members.add(new Member(rs.getInt(1), rs.getString(2)));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_FACILITYBOOKINGS);
        while (rs.next()) {
            facilityBookings.add(new FacilityBooking(rs.getInt(1), rs.getInt(2), rs.getTimestamp(3), rs.getTimestamp(4),
                    rs.getString(5), rs.getBoolean(6), rs.getBoolean(7)));
        }

        // TODO missing fee and feeitem
    }

    public Connection getConnection() {
        return conn;
    }

    public List<Facility> getFacilities() {
        return facilities;
    }

    public List<FacilityBooking> getFacilityBookings() {
        return facilityBookings;
    }

    public List<Member> getMembers() {
        return members;
    }
}
