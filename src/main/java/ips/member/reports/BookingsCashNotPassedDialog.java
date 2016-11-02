package ips.member.reports;

import ips.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.member.MemberMainScreen;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.SimpleDateFormat;

/**
 * Created by jorge on 02/11/2016.
 */
public class BookingsCashNotPassedDialog extends JDialog {

    public BookingsCashNotPassedDialog(JDialog owner) {
        super(owner, true);
        setMinimumSize(new Dimension(320, 180));

        setLayout(new BorderLayout());
        add(new JLabel("Lista de reservas para pagar en efectivo en el futuro"), BorderLayout.NORTH);

        JTextPane center = new JTextPane();
        center.setEditable(false);
        add(center);

        String text = Database.getInstance().getFacilityBookings().stream()
                .filter(fb -> fb.getMemberId() == MemberMainScreen.userID &&
                        fb.getTimeStart().after(Utils.getCurrentTime()) &&
                        fb.getPaymentMethod().equals(FacilityBooking.PAYMENT_CASH))
                .map(fb -> fb.getFacility().getFacilityName() + " - " + new SimpleDateFormat().format(fb.getTimeStart()) + "\n")
                .reduce("", String::concat);
        center.setText(text);

        pack();
        setLocationRelativeTo(owner);
    }
}
