package ips.member;

import ips.MainWindow;
import ips.member.reports.BookingsCashNotPassedDialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * Created by jorge on 02/11/2016.
 */
public class ReportDialog extends JDialog {

    public ReportDialog() {
        super(MainWindow.getInstance(), true);
        setMinimumSize(new Dimension(320, 180));

        setLayout(new BorderLayout());

        JPanel center = new JPanel(new GridBagLayout());
        add(center, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 0;

        JButton bookingsCashNotPassedButton = new JButton("Ver reservas en efectivo que aun no han pasado");
        bookingsCashNotPassedButton.addActionListener( l-> {
            new BookingsCashNotPassedDialog(this).setVisible(true);
        });
        center.add(bookingsCashNotPassedButton, c);

        pack();
        setLocationRelativeTo(MainWindow.getInstance());
    }
}
