package ips.administrator;

import ips.MainWindow;

import javax.swing.*;

/**
 * Main panel for administration
 */
public class AdministratorMain extends JPanel {

    public AdministratorMain() {
        JButton book = new JButton("Book");
        book.addActionListener((e) -> new BookingDialog(MainWindow.getInstance(), null, 0, 0).setVisible(true));
        add(book);
    }
}
