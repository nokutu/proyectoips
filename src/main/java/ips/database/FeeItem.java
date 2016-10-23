package ips.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by nokutu on 03/10/2016.
 */
public class FeeItem implements DatabaseItem {

    private final static String CREATE_QUERY = "INSERT INTO feeitem(feeitem_concept,fee_month,fee_member_id,feeitem_amount) VALUES (?, ?, ?)";
    //private static int count = 1;

    private static PreparedStatement createStatement;

    // Represents cents
    private double amount;
    private String concept;
    private Fee fee_asociada;
   // private Timestamp date;// esta seria la fecha de la reserva realizada

    /**
     * 
     * @param amount
     * @param fee
     */
    public FeeItem(double amount, Fee fee) {
    	//id=count++;
    	//this.fee_id=fee_id;
        this.fee_asociada=fee;
        this.amount=amount;
        

    }

    @Override
    public void create() throws SQLException {
    	if (createStatement == null) {
            createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
        }
        //createStatement.setInt(1, id);
        createStatement.setString(1, concept);
        createStatement.setDate(2, fee_asociada.getMonth());
        createStatement.setInt(3, fee_asociada.getMemberId());
        createStatement.setDouble(4,amount);

        createStatement.execute();
    }

    @Override
    public void update() {
        // TODO
    }
    
	public Fee getFee_asociada() {
		return fee_asociada;
	}

	public void setFee_asociada(Fee fee_asociada) {
		this.fee_asociada = fee_asociada;
	}
    
}
