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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

public class BookForCenterDialog extends JDialog {

    private static final long serialVersionUID = 8497586255693077533L;

    private Facility facility;
    private Timestamp hourStart;
    private Timestamp hourEnd;
    private int centerId = 0;

    private JDateChooser dateChooser;
    private JSpinner hourSp1;
    private JSpinner hourSp2;
    private Form form;

    public BookForCenterDialog(JFrame owner, Facility facility, Timestamp hourStart,
                               Timestamp hourEnd) {
        super(owner, true);
        setResizable(false);

        this.facility = facility;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;

        construir(owner);
    }

    public void construir(JFrame owner) {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        //form creada
        form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);
        addForm(hourStart == null);

        //botones

        JButton btnConfirm = new JButton("OK");
        btnConfirm.addActionListener(this::confirm);
        btnConfirm.setMnemonic('A');

        JButton btnCancel = new JButton("Cancelar");
        btnCancel.addActionListener(this::cancel);
        btnCancel.setMnemonic('C');


        GridBagConstraints c;

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        content.add(bottom, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        bottom.add(buttons, BorderLayout.EAST);


        buttons.add(btnConfirm, c);
        c.insets = new Insets(20, 5, 10, 10);
        c.gridx = 1;
        buttons.add(btnCancel, c);


        pack();
        setLocationRelativeTo(owner);

    }

    public BookForCenterDialog(JFrame owner) {
        this(owner, null, null, null);
    }


    private void addForm(boolean addDate) {

        if (addDate) {
            dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setCalendar(Calendar.getInstance());
            form.addLine(new JLabel("Fecha:"), dateChooser);
        }

        selectStartEndTime(form);

    }

    private void selectStartEndTime(Form form) {
        hourSp1 = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        form.addLine(new JLabel("Hora de inicio:"), hourSp1);
        hourSp2 = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        form.addLine(new JLabel("Hora de fin: "), hourSp2);


        form.addLine(new JLabel("Facility ID:"), new JTextField(20));
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }

    public void confirm(ActionEvent arg0) {

        Timestamp hourStart;
        Timestamp hourEnd;
        int facilityId = -1;
        List<String> results = form.getResults();


        if (this.hourStart == null) {
            hourStart = new Timestamp(
                    Utils.addHourToDay(new Timestamp(Long.parseLong(results.get(0))),
                            Integer.parseInt(results.get(1))).getTime());
            hourEnd = new Timestamp(Utils.addHourToDay(
                    new Timestamp(Long.parseLong(results.get(0))),
                    Integer.parseInt(results.get(2))).getTime());
            facilityId = Integer.parseInt(results.get(3));
        } else {
            facilityId = facility.getFacilityId();
            hourStart = this.hourStart;
            hourEnd = this.hourEnd;
        }


        FacilityBooking fb = new FacilityBooking(facilityId, centerId, hourStart, hourEnd, null, false, false);

        if (valid(fb)) {
            Database.getInstance().getFacilityBookings().add(fb);
        }

        try {
            fb.create();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dispose();

    }

    private boolean valid(FacilityBooking fb) {
        boolean valid = true;


        Optional<Facility> facility = Database.getInstance().getFacilities().stream().filter((f) -> f.getFacilityId() == fb.getFacilityId()).findAny();

        //la facility debe existir
        if (!facility.isPresent()) {
            valid = false;
        }

        //invalided de los spinners 
        if (fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() < 0) {
            valid = false;
        }

        return valid;
    }

}
