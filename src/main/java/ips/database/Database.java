package ips.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

/**
 * Created by nokutu on 27/09/16.
 */
public class Database {

    private final static String QUERY_FACILITIES = "SELECT * FROM facility";
    private final static String QUERY_MEMBERS = "SELECT * FROM member";
    private final static String QUERY_FACILITYBOOKINGS = "SELECT * FROM facilitybooking";
    private final static String QUERY_FEE = "SELECT * FROM fee";
    private final static String QUERY_FEEITEM = "SELECT * FROM feeItem";
    private final static String QUERY_ACTIVITY = "SELECT * FROM activity";
    private final static String QUERY_ACTIVITY_BOOKING = "SELECT * FROM activitybooking";
    private final static String QUERY_ACTIVITY_MEMBER = "SELECT * FROM activitymember";
    private final static String QUERY_MONITOR = "SELECT * FROM monitor";

    private static Database instance;
    private Connection conn;

    private List<Facility> facilities;
    private List<FacilityBooking> facilityBookings;
    private List<Member> members;
    private List<Fee> fees;
    private List<FeeItem> feeItems;
    private List<Activity> activities;
    private List<ActivityBooking> activityBookings;
    private List<ActivityMember> activityMembers;
    private List<Monitor> monitors;

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
        activities = new ArrayList<>();
        activityBookings = new ArrayList<>();
        activityMembers = new ArrayList<>();
        monitors = new ArrayList<>();

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
            facilities.add(new Facility(rs.getInt("facility_id"), rs.getString("facility_name"), rs.getInt("price")));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_MEMBERS);
        while (rs.next()) {
            if (rs.getInt(1) != 0) {
                members.add(new Member(rs.getInt(1), rs.getString(2), rs.getBoolean(3)));
            }
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_FACILITYBOOKINGS);
        while (rs.next()) {
            facilityBookings.add(new FacilityBooking(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getTimestamp(4), rs.getTimestamp(5),
                    rs.getString(6), rs.getBoolean(7), rs.getTimestamp(8), rs.getTimestamp(9), rs.getString(10), rs.getString(11), rs.getTimestamp(12)));
        }


        s = conn.createStatement();
        rs = s.executeQuery(QUERY_FEE);
        while (rs.next()) {
            fees.add(new Fee(rs.getInt("fee_member_id"), rs.getTimestamp("fee_month"), rs.getDouble("fee_base")));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_FEEITEM);
        while (rs.next()) {
            feeItems.add(new FeeItem(rs.getInt(2), rs.getTimestamp(5), rs.getDouble(3), rs.getString(4)));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_ACTIVITY);
        while (rs.next()) {
            activities.add(new Activity(rs.getInt("activity_id"), rs.getString("activity_name"), rs.getInt("assistant_limit"), rs.getBoolean("deleted")));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_ACTIVITY_BOOKING);
        while (rs.next()) {
            activityBookings.add(new ActivityBooking(rs.getInt(1), rs.getInt(2), rs.getInt(3)));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_ACTIVITY_MEMBER);
        while (rs.next()) {
            activityMembers.add(new ActivityMember(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getBoolean(4), rs.getBoolean(5)));
        }

        s = conn.createStatement();
        rs = s.executeQuery(QUERY_MONITOR);
        while (rs.next()) {
            monitors.add(new Monitor(rs.getInt(1), rs.getString(2)));
        }

        s.close();
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

    public List<Fee> getFees() {
        return fees;
    }

    public List<FeeItem> getFeeItems() {
        return feeItems;
    }

    public Facility getFacilityById(int id) {
        for (Facility facility : facilities) {
            if (facility.getFacilityId() == id)
                return facility;
        }
        throw new RuntimeException("Do not exists");
    }


    public Fee getFeeByMember(int memberId, int month) {
        for (Fee fee : fees) {
            if (fee.getMemberId() == memberId && fee.getMonth().getMonth() == month)
                return fee;
        }
        throw new RuntimeException("Do not exists");
    }

    public Fee getFeeByMember(Member member, int m) {
        return getFeeByMember(member.getMemberId(), m);
    }

    public Member getMemberById(int id) {
        for (Member member : members) {
            if (member.getMemberId() == id)
                return member;
        }
        throw new RuntimeException("Do not exists");
    }

    public Fee getFeeByMonth(int memberId, int month) {
        for (Fee fee : fees) {
            Calendar c = Calendar.getInstance();
            c.setTime(fee.getMonth());
            if (fee.getMemberId() == memberId && c.get(Calendar.MONTH) == month)
                return fee;
        }
        throw new RuntimeException("Do not exists");
    }

    public FacilityBooking getFacilityBookingById(int fbId){
    	return facilityBookings.stream().filter(fb->fb.getFacilityBookingId()==fbId).findFirst().get();
    }
    
    public Activity getActivityById(int activity_id){
    	return activities.stream().filter(a->a.getActivityId()==activity_id).findFirst().get();
    }

    /**
     * @param id   facility_id
     * @param hora
     */
    public FacilityBooking getBookingById(int id, int hora) {
        for (FacilityBooking b : facilityBookings) {
            if (b.getFacilityId() == id && b.getTimeStart().getHours() == hora)
                return b;
        }
        throw new RuntimeException("Do not exists");
    }


    public List<Activity> getActivities() {
        return activities;
    }

    public List<ActivityBooking> getActivityBookings() {
        return activityBookings;
    }

    public List<ActivityMember> getActivityMembers() {
        return activityMembers;
    }

    public List<Monitor> getMonitors() {
        return monitors;
    }
}
