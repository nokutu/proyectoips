package ips.administrator;

import ips.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main panel for administration
 */
public class AdministratorMain extends JPanel {
    private JButton btnBookActivity;

    public AdministratorMain() {
        JButton book = new JButton("Book");
        book.addActionListener((e) -> new BookForMemberDialog(MainWindow.getInstance(), null, 0, 0).setVisible(true));
        add(book);
        add(getBtnBookActivity());
    }

    private JButton getBtnBookActivity() {
        if (btnBookActivity == null) {
            btnBookActivity = new JButton("Book for the center(Priviledged)");
            btnBookActivity.addActionListener(e -> {
                BookForCenterDialog adminbook = new BookForCenterDialog(MainWindow.getInstance(), null, 0, 0);
                adminbook.setVisible(true);
            });
        }
        return btnBookActivity;
    }
}
