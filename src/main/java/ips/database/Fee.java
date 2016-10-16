package ips.database;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nokutu on 03/10/2016.
 */
public class Fee implements DatabaseItem {

	private int FeeId;
    private int member_id;
    // Use Date.valueOf(YYYY-MM--DD) to get the value. Use first day of the month
    private Date month;
    private List<FeeItem> feeItems;

    private static int count = Database.getInstance().getFees().size()+1;
    
    public Fee(int member,Date date) {
    	FeeId=count;
        feeItems=new LinkedList<>();
        this.month=date;
        this.member_id=member;
    }

    public Fee(int id, Date month, int member) {
		FeeId=id;
		this.month=month;
		member_id=member;
        feeItems=new LinkedList<>();
	}

	@Override
    public void create() {
        // TODO
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

	public int getFeeId() {
		return FeeId;
	}

	public List<FeeItem> getFeeItems() {
		return feeItems;
	}
}
