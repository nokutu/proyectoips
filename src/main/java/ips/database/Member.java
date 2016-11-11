package ips.database;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 27/09/16.
 */
public class Member implements DatabaseItem {

    public final static double fee_base = 15; // cuota base de todos los socios

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

    /**
     * Returns all the member's {@link FacilityBooking}s, including those that have been cancelled
     *
     * @return the {@link FacilityBooking}s assigned to this member
     */
    public List<FacilityBooking> getFacilityBookings() {
        return Database.getInstance().getFacilityBookings().stream()
                .filter(fb -> fb.getMemberId() == memberId)
                .collect(Collectors.toList());
    }

    public static Member get(int id) {
        Optional<Member> member = Database.getInstance().getMembers().stream()
                .filter((m) -> m.getMemberId() == id).findAny();
        if (member.isPresent()) {
            return member.get();
        } else {
            return null;
        }
    }
}
