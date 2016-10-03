package ips.database;

/**
 * Created by nokutu on 27/09/16.
 */
public class Member implements DatabaseItem {

    private int member_id;

    public Member(int member_id) {
        this.member_id = member_id;
    }

    @Override
    public void create() {
        // TODO
    }

    @Override
    public void update() {
        // TODO
    }
}
