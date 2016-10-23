package ips.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nokutu on 03/10/2016.
 */
public class Fee implements DatabaseItem {

	private final static String CREATE_QUERY = "INSERT INTO Fee(fee_month,fee_member_id,cuota_base) VALUES (?, ?, ?)";
	// private static int count = 1;

	private static PreparedStatement createStatement;

	// private int FeeId;
	private int member_id;
	// Use Date.valueOf(YYYY-MM--DD) to get the value. Use first day of the
	// month
	private Date month;
	private double cuota_base;
	private List<FeeItem> feeItems;

	// private static int count = Database.getInstance().getFees().size()+1;

	public Fee(int member, Date mes, double cuota_base) {
		// FeeId=id;
		feeItems = new LinkedList<>();
		this.month = mes;
		this.member_id = member;
		this.cuota_base=cuota_base;
	}
	public Fee(int member, Date mes) {
		// FeeId=id;
		feeItems = new LinkedList<>();
		this.month = mes;
		this.member_id = member;
		this.cuota_base=Member.cuota_base;
	}

	@Override
	public void create() throws SQLException {
		if (createStatement == null) {
			createStatement = Database.getInstance().getConnection().prepareStatement(CREATE_QUERY);
		}
		createStatement.setDate(1, this.month);
		createStatement.setInt(2, member_id);
		createStatement.setDouble(3, cuota_base);

		createStatement.execute();
	}

	@Override
	public void update() {
		// TODO
	}

	public int getMemberId() {
		return member_id;
	}

	public Date getMonth() {
		return month;
	}

	/*
	 * public int getFeeId() { return FeeId; }
	 */

	public List<FeeItem> getFeeItems() {
		return feeItems;
	}
}
