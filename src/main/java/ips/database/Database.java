package ips.database;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by nokutu on 27/09/16.
 */
public class Database {

    private static Database instance;
    private Connection conn;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "SA");
        try {
            conn = DriverManager.getConnection("jdbc:h2:~/test", connectionProps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
