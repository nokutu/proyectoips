package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Member implements DatabaseItem {

    private int memberId;
    private String memberName;

    public Member(int memberId, String memberName) {
        this.memberId = memberId;
        this.memberName = memberName;
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
        return memberId;
    }

	public String getMemberName() {
		return memberName;
	}

}
