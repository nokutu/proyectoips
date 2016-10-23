package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Member implements DatabaseItem {

	public static double cuota_base = 15; // cuota base de todos los socios
	
	private int memberId;
	private String memberName;
	private boolean de_alta; // si se da de baja pasa a ser false

	public Member(int memberId, String memberName) {
		this.memberId = memberId;
		this.memberName = memberName;
		de_alta = true;
	}

	public Member(int memberId, String memberName, boolean alta) {
		this.memberId = memberId;
		this.memberName = memberName;
		de_alta = alta;
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

	public boolean isDe_alta() {
		return de_alta;
	}

	public void setDe_alta(boolean de_alta) {
		this.de_alta = de_alta;
	}

}
