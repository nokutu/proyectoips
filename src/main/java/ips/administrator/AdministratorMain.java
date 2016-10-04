package ips.administrator;

import ips.MainWindow;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Main panel for administration
 */
public class AdministratorMain extends JPanel {
	private JButton btnBookActivity;

    public AdministratorMain() {
        JButton book = new JButton("Book");
        book.addActionListener((e) -> new BookingDialog(MainWindow.getInstance(), null, 0, 0).setVisible(true));
        add(book);
        add(getBtnBookActivity());
    }
	private JButton getBtnBookActivity() {
		if (btnBookActivity == null) {
			btnBookActivity = new JButton("Book for the center(Priviledged)");
			btnBookActivity.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) 
				{
					AdministratorBookingDialog adminbook = new AdministratorBookingDialog(MainWindow.getInstance(), null, 0, 0);
					adminbook.setVisible(true);
				}
			});
		}
		return btnBookActivity;
	}
}
