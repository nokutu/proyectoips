package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Member implements DatabaseItem {

	public static double fee_base = 15; // cuota base de todos los socios
	
	private int memberId;
	private String memberName;
	private boolean subscribed; // si se da de baja pasa a ser false

	public Member(int memberId, String memberName) {
		this(memberId, memberName, true);
	}

	public Member(int memberId, String memberName, boolean alta) {
		this.memberId = memberId;
		this.memberName = memberName;
		subscribed = alta;
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

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}
}
