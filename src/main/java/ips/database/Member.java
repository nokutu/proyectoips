package ips.database;

import java.util.List;

/**
 * Created by nokutu on 27/09/16.
 */
public class Member implements DatabaseItem {

    private int member_id;
    private String member_name;

    public Member(int member_id, String member_name) {
        this.member_id = member_id;
        this.member_name = member_name;
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
