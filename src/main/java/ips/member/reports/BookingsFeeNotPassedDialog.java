package ips.member.reports;

import ips.utils.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.member.MemberMainScreen;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jorge on 02/11/2016.
 */
public class BookingsFeeNotPassedDialog extends JDialog {

    private final JTextPane center;
    private Date currentWeek;
    private JLabel weekPlaceholder;

    public BookingsFeeNotPassedDialog(JDialog owner) {
        super(owner, true);
        setMinimumSize(new Dimension(320, 180));

        setLayout(new BorderLayout());
        add(new JLabel("Lista de reservas para pagar con la cuota en el futuro"), BorderLayout.NORTH);

        center = new JTextPane();
        center.setEditable(false);
        add(center, BorderLayout.CENTER);

        addNorthPanel();

        setDefaultWeek();

        pack();
        setLocationRelativeTo(owner);
    }

    public void addNorthPanel() {
        JPanel northPanel = new JPanel();

        JButton previousWeek = new JButton("<");
        previousWeek.addActionListener(l -> addWeek(false));
        northPanel.add(previousWeek);

        weekPlaceholder = new JLabel();
        northPanel.add(weekPlaceholder);

        JButton nextWeek = new JButton(">");
        nextWeek.addActionListener(l -> addWeek(true));
        northPanel.add(nextWeek);

        add(northPanel, BorderLayout.NORTH);
    }

    private void addWeek(boolean up) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentWeek);
        c.add(Calendar.WEEK_OF_YEAR, up ? 1 : -1);

        if (c.getTime().after(new Date())) {
            currentWeek = c.getTime();
            refreshList();
        }
    }

    private void setDefaultWeek() {
        currentWeek = Utils.getCurrentWeek();
        refreshList();
    }

    private void refreshList() {
        String text = Database.getInstance().getFacilityBookings().stream()
                .filter(fb -> fb.getTimeStart().after(currentWeek) && fb.getTimeStart().before(getNextWeek()) &&
                        fb.getTimeStart().after(new Date()))
                .filter(fb -> fb.getMemberId() == MemberMainScreen.userID &&
                        fb.getTimeStart().after(Utils.getCurrentTime()) &&
                        fb.getPaymentMethod().equals(FacilityBooking.PAYMENT_FEE))
                .sorted((a, b) -> a.getFacility().getFacilityId() - b.getFacility().getFacilityId())
                .map(fb -> fb.getFacility().getFacilityName() + " - " + new SimpleDateFormat().format(fb.getTimeStart()) + "\n")
                .reduce("", String::concat);
        center.setText(text);

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        weekPlaceholder.setText(df.format(currentWeek) + " - " + df.format(getNextWeek()));
    }

    private Date getNextWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(currentWeek);
        c.add(Calendar.WEEK_OF_YEAR, 1);
        return c.getTime();
    }
}
