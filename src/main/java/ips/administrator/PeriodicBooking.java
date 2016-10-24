package ips.administrator;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.Activity;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 17/10/2016.
 */
public class PeriodicBooking extends JDialog {

    private Form form;

    private JButton confirm;
    private JButton cancel;
    private JDateChooser dateChooser;

    public PeriodicBooking(JFrame owner) {
        super(owner, true);
        setResizable(false);

        createButtons();

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);

        fillForm();
        addButtons(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void fillForm() {
        form.addLine(new JLabel("ID de socio:"), new JTextField(10));

        form.addLine(new JLabel("Limite asistentes:"), new JSpinner(new SpinnerNumberModel(1, 1, 100, 1)));

        JComboBox<String> facilities = new JComboBox<>();
        List<String> names = Database.getInstance().getFacilities().stream().map(Facility::getFacilityName).collect(Collectors.toList());
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        names.forEach(model::addElement);
        facilities.setModel(model);
        form.addLine(new JLabel("Instalación:"), facilities, false);

        dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        dateChooser.setDate(Utils.getCurrentDate());
        form.addLine(new JLabel("Fecha:"), dateChooser);

        JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));

        form.addLine(new JLabel("Hora de inicio:"), hourStartSpinner);
        form.addLine(new JLabel("Hora de fin:"), hourEndSpinner);

        JDateChooser endDateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        endDateChooser.setDate(Utils.getCurrentDate());
        endDateChooser.setEnabled(false);

        JComboBox<String> recursive = new JComboBox<>();
        model = new DefaultComboBoxModel<>(new String[]{"No repetir", "Semanalmente", "Mensualmente"});
        recursive.addActionListener((l) -> endDateChooser.setEnabled(recursive.getSelectedIndex() > 0));
        recursive.setModel(model);
        form.addLine(new JLabel("Repetir:"), recursive, false);

        form.addLine(new JLabel("Fecha fin repetición:"), endDateChooser);
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

    private void createButtons() {
        confirm = new JButton("OK");
        confirm.addActionListener(this::confirm);
        cancel = new JButton("Cancelar");
        cancel.addActionListener(this::cancel);
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }

    private void confirm(ActionEvent actionEvent) {
        List<String> results = form.getResults();

        Timestamp startTime = new Timestamp(Utils.addHourToDay(new Timestamp(Long.parseLong(results.get(3))), Integer.parseInt(results.get(4))).getTime());

        Timestamp endTime;

        if (Integer.parseInt(results.get(6)) == 0) {
            // Don't repeat
            endTime = new Timestamp(Utils.addHourToDay(new Timestamp(Long.parseLong(results.get(3))), Integer.parseInt(results.get(5))).getTime());
        } else {
            // Repeat
            endTime = new Timestamp(Long.parseLong(results.get(7)));
        }

        int duration = Integer.parseInt(results.get(5)) - Integer.parseInt(results.get(4));

        Facility facility = Database.getInstance().getFacilities().get(Integer.parseInt(results.get(2)));

        Activity a = new Activity(results.get(0), Integer.parseInt(results.get(1)));

        Timestamp time = startTime;
        List<FacilityBooking> bookings = new ArrayList<>();
        while (time.before(endTime)) {
            FacilityBooking fb = new FacilityBooking(facility.getFacilityId(), 0, time,
                    new Timestamp(Utils.addHourToDay(time, duration).getTime()), null, false, false);
            if (checkValid(fb)) {
                bookings.add(fb);
            } else {
                return;
            }
            if (Integer.parseInt(results.get(6)) == 1) {
                Calendar c = Calendar.getInstance();
                c.setTime(time);
                c.add(Calendar.WEEK_OF_YEAR, 1);
                time = new Timestamp(c.getTime().getTime());
            } else if (Integer.parseInt(results.get(6)) == 2) {
                Calendar c = Calendar.getInstance();
                c.setTime(time);
                c.add(Calendar.MONTH, 1);
                time = new Timestamp(c.getTime().getTime());
            } else {
                // Do not repeat
                break;
            }
        }

        try {
            a.create();
            for (FacilityBooking fb : bookings) {
                fb.create();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Database.getInstance().getFacilityBookings().addAll(bookings);
        dispose();
    }

    private boolean checkValid(FacilityBooking fb) {
        boolean valid = true;
        String errors = "";
        if (fb != null) {

            Optional<Facility> optional = Database.getInstance().getFacilities().stream()
                    .filter( f -> f.getFacilityId() == fb.getFacilityId()).findAny();
            assert optional.isPresent();
            Facility facility = optional.get();

            if (!Utils.isFacilityFree(facility, fb.getTimeStart(), fb.getTimeEnd())) {
                valid = false;
                Calendar c = Calendar.getInstance();
                c.setTime(fb.getTimeStart());
                errors += "La instalación está ocupada el " + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + ".\n";
            }
        } else {
            valid = false;
        }

        form.setError(errors);

        return valid;
    }
}
