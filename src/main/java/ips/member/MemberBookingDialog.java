package ips.member;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The member makes a booking for himself.
 */
public class MemberBookingDialog extends JDialog {

    private Facility facility;
    private Timestamp timeStart;
    private Timestamp timeEnd;

    private JButton confirm;
    private JButton cancel;

    private Form form;

    public MemberBookingDialog(JFrame owner) {
        this(owner, null, null, null);
    }

    public MemberBookingDialog(JFrame owner, Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        super(owner, true);
        setResizable(false);

        this.facility = facility;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        createButtons();

        form = new Form();
        addForm(timeStart == null);

        content.add(form.getPanel());
        addButtons(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addForm(boolean addExtra) {
        if (addExtra) {
            JDateChooser dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setCalendar(Calendar.getInstance());
            form.addLine(new JLabel("Fecha:"), dateChooser);

            JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
            JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));

            JCheckBox wholeDay = new JCheckBox("Reservar para todo el día");
            wholeDay.addActionListener(l -> {
                hourStartSpinner.setEnabled(!wholeDay.isSelected());
                hourEndSpinner.setEnabled(!wholeDay.isSelected());
                if (wholeDay.isSelected()) {
                    hourStartSpinner.setValue(0);
                    hourEndSpinner.setValue(23);
                }
            });
            form.addLine(new JLabel(), wholeDay);

            form.addLine(new JLabel("Hora de inicio:"), hourStartSpinner);
            form.addLine(new JLabel("Hora de fin:"), hourEndSpinner);

            JComboBox<String> facilities = new JComboBox<>();
            List<String> names = Database.getInstance().getFacilities().stream().map(Facility::getFacilityName).collect(Collectors.toList());
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            names.forEach(model::addElement);
            facilities.setModel(model);
            form.addLine(new JLabel("Instalación:"), facilities, false
            );
        }

        JComboBox<String> paymentCombo = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cash", "Fee"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("Método de pago:"), paymentCombo);
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

    private void confirm(ActionEvent actionEvent) {
        FacilityBooking fb = createBooking();

        if (checkValid(fb)) {
            Database.getInstance().getFacilityBookings().add(fb);
            try {
                fb.create();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            dispose();
        }
    }

    private FacilityBooking createBooking() {
        int facilityId;
        int memberId;
        Timestamp timeStart;
        Timestamp timeEnd;
        String paymentMethod;

        try {
            List<String> results = form.getResults();
            if (this.timeStart == null) {
                timeStart = new Timestamp(Utils.addHourToDay(
                        new Timestamp(Long.parseLong(results.get(0))),
                        Integer.parseInt(results.get(2))).getTime());
                timeEnd = new Timestamp(Utils.addHourToDay(
                        new Timestamp(Long.parseLong(results.get(0))),
                        Integer.parseInt(results.get(3))).getTime());
                facilityId = Database.getInstance().getFacilities().get(Integer.parseInt(results.get(4))).getFacilityId();
                memberId = MemberMain.userID;
                paymentMethod = results.get(5);
            } else {
                facilityId = facility.getFacilityId();
                timeStart = this.timeStart;
                timeEnd = this.timeEnd;
                memberId = MemberMain.userID;
                paymentMethod = results.get(0);
            }

            return new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false, false);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }

    private boolean checkValid(FacilityBooking fb) {
        boolean valid = true;
        String errors = "\n";

        if (fb != null) {
            if (fb.getTimeStart().before(Utils.getCurrentDate())) {
                valid = false;
                errors += "No puedes reservar para el pasado\n";
            } else if (fb.getTimeStart().after(Utils.addHourToDay(Utils.getCurrentDate(), 24 * 15))) {
                valid = false;
                errors += "Solo puedes reservar hasta 15 días en adelante\n";
            }
            if (fb.getTimeStart().before(Utils.addHourToDay(Utils.getCurrentDate(), 1))) {
                valid = false;
                errors += "Tienes que reservar con una hora de antelación\n";
            }
            if (fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() > 2 * 3600 * 1000 ||
                    fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() <= 0) {
                // More than 2 hours
                valid = false;
                errors += "Un socio solo puedo reservar un máximo de 2 horas. El tiempo de finalización debe ser posterior al de inicio\n";
            }
            if (!Database.getInstance().getMembers().stream()
                    .filter((m) -> m.getMemberId() == fb.getMemberId()).findAny().isPresent()) {
                // Member not valid
                errors += "Invalid member id\n";
                valid = false;
            }
            Optional<Facility> of = Database.getInstance().getFacilities().stream()
                    .filter((f) -> f.getFacilityId() == fb.getFacilityId()).findAny();
            assert of.isPresent();

            if (!Utils.isFacilityFree(of.get(), fb.getTimeStart(), fb.getTimeEnd())) {
                // Facility not free
                valid = false;
                errors += "La instalación está ocupada en la horas seleccionadas\n";
            }
        } else {
            errors += "Por favor, rellena todos los campos\n";
            valid = false;
        }

        if (!valid) {
            form.setError(errors);
            setSize(getWidth(), getPreferredSize().height);
        }

        return valid;
    }

    private void createButtons() {
        confirm = new JButton("Confirm");
        confirm.addActionListener(this::confirm);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this::cancel);
    }
}
