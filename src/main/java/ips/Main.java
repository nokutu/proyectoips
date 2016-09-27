package ips;

import ips.database.Database;

/**
 * Created by nokutu on 26/09/16.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world");

        Database.getInstance().close();
    }
}
