package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by nokutu on 03/10/2016.
 */
public class FeeItem implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO feeitem(fee_member_id, fee_month, feeitem_amount, feeitem_concept) VALUES (?, ?, ?, ?)";
    //private static int count = 1;

    private static PreparedStatement createStatement;

    //private int feeItemId;
    private int memberId;
    private Timestamp month;
    // Represents cents
    private double amount;
    private String concept;

    // private Timestamp date;// esta seria la fecha de la reserva realizada

    public FeeItem(int memberId, Timestamp month, double amount, String concept) {
        this.memberId = memberId;
        this.month = month;
        this.concept = concept;
        this.amount = amount;
    }

    @Override
    public void create() throws SQLException {
        if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }
        //createStatement.setInt(1, id);
        createStatement.setInt(1, memberId);
        createStatement.setTimestamp(2, month);
        createStatement.setDouble(3, amount);
        createStatement.setString(4, concept);

        createStatement.execute();
    }

    @Override
    public void update() {
        // TODO
    	throw new UnsupportedOperationException();
    }

}
