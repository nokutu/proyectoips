package ips.database;

import java.sql.SQLException;

/**
 * Created by nokutu on 03/10/2016.
 */
public interface DatabaseItem {

    /**
     * Called when a new instance is created and needs to be insterted in the database
     */
    void create() throws SQLException;

    /**
     * Called when the object has been modified and the changes must be written in the database
     */
    void update();
}
