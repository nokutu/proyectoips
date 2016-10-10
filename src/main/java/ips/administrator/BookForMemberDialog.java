package ips.administrator;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by nokutu on 3/10/16.
 */
public class BookForMemberDialog extends JDialog {

    private final Form form;

    private Facility facility;
    private Date timeStart;
    private Date timeEnd;

    private JButton confirm;
    private JButton cancel;

    public BookForMemberDialog(JFrame owner) {
        this(owner, null, null, null);
    }

    public BookForMemberDialog(JFrame owner, Facility facility, Date timeStart, Date timeEnd) {
        super(owner, true);
        setResizable(false);

        this.facility = facility;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

        createButtons();

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);

        addForm(timeStart == null);
        addButtons(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addButtons(JPanel content) {
        GridBagConstraints c;

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        content.add(bottom, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        bottom.add(buttons, BorderLayout.EAST);
        buttons.add(confirm, c);
        c.insets = new Insets(20, 5, 10, 10);
        c.gridx = 1;
        buttons.add(cancel, c);
    }

    private void addForm(boolean addExtra) {

        if (addExtra) {
            JDateChooser dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setCalendar(Calendar.getInstance());
            form.addLine(new JLabel("Date:"), dateChooser);

            JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
            form.addLine(new JLabel("Start time:"), hourStartSpinner);

            JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
            form.addLine(new JLabel("End time:"), hourEndSpinner);

            form.addLine(new JLabel("Facility ID:"), new JTextField(20));
        }

        form.addLine(new JLabel("Member ID:"), new JTextField(20));

        JComboBox<String> paymentCombo = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cash", "Fee"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("Payment type:"), paymentCombo);
    }

    private void createButtons() {
        confirm = new JButton("Confirm");
        confirm.addActionListener(this::confirm);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this::cancel);
    }


    private void confirm(ActionEvent actionEvent) {
        int facilityId = -1;
        int memberId = -1;
        Date timeStart;
        Date timeEnd;
        String paymentMethod;

        List<String> results = form.getResults();
        if (this.timeStart == null) {
            timeStart = Utils.addHourToDay(new Date(Long.parseLong(results.get(0))), Integer.parseInt(results.get(1)));
            timeEnd = Utils.addHourToDay(new Date(Long.parseLong(results.get(0))), Integer.parseInt(results.get(2)));
            facilityId = Integer.parseInt(results.get(3));
            memberId = Integer.parseInt(results.get(4));
            paymentMethod = results.get(5);
        } else {
            facilityId = facility.getFacilityId();
            timeStart = this.timeStart;
            timeEnd = this.timeEnd;
            memberId = Integer.parseInt(results.get(0));
            paymentMethod = results.get(1);
        }

        FacilityBooking fb = new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false, false);
        Database.getInstance().getFacilityBookings().add(fb);
        try {
            fb.create();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dispose();
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }
}
