package ips;

import ips.database.Database;

/**
 * Main class of the application. Starts database, runs application and closes the database at the end.
 */
public class Main {
  public static void main(String[] args) {
    // Load database
    Database.getInstance();

    MainWindow.getInstance().setVisible(true);

    Database.getInstance().close();
  }
}
