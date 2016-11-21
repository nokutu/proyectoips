package ips.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 03/10/2016.
 */
public class Fee implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO Fee(fee_month,fee_member_id,fee_base) VALUES (?, ?, ?)";
    // private static int count = 1;

    private static PreparedStatement createStatement;

    // private int FeeId;
    private int member_id;
    // Use Date.valueOf(YYYY-MM--DD) to get the value. Use first day of the
    // month
    private Timestamp month;
    private double fee_base;

    // private static int count = Database.getInstance().getFees().size()+1;

    public Fee(int member, Timestamp mes, double fee_base) {
        this.month = mes;
        this.member_id = member;
        this.fee_base = fee_base;
    }

    public Fee(int member, Timestamp mes) {
        // FeeId=id;
        this.month = mes;
        this.member_id = member;
        this.fee_base = Member.fee_base;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }
        createStatement.setTimestamp(1, this.month);
        createStatement.setInt(2, member_id);
        createStatement.setDouble(3, fee_base);

        createStatement.execute();
    }

    @Override
    public void update() {
        // TODO
    }

    public int getMemberId() {
        return member_id;
    }

    public Timestamp getMonth() {
        return month;
    }

    public static Fee getOrCreate(Member member, Date month) {
        // TODO
        return null;
    }

    public void addFeeItem(double amount, String concept) {
        FeeItem fi = new FeeItem(member_id, month, amount, concept);
        try {
            fi.create();
            Database.getInstance().getFeeItems().add(fi);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
