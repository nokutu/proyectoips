package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by nokutu on 03/10/2016.
 */
public class FeeItem implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO feeitem VALUES (?, ?, ?, ?)";
    private static int count = 1;

    private static PreparedStatement createStatement;

    // Represents cents
    private int id;
    private int amount;
    private String concept;
    private int member_id;
   // private Timestamp date;// esta seria la fecha de la reserva realizada

    public FeeItem(int amount,int member) {
    	id=count++;
    	//
        this.member_id=member;
        this.amount=amount;

    }

    @Override
    public void create() throws SQLException {
    	if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }
        createStatement.setInt(1, id);
        createStatement.setString(2, concept);
        createStatement.setInt(3, member_id);
        createStatement.setInt(4,amount);

        createStatement.execute();
    }

    @Override
    public void update() {
        // TODO
    }

	public int getMember_id() {
		return member_id;
	}
    
}
