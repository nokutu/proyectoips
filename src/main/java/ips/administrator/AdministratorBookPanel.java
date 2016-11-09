package ips.administrator;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.*;
import ips.gui.Form;
import javax.swing.*;

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
 * Created by nokutu on 24/10/2016.
 */
public class AdministratorBookPanel extends JPanel {

    private static final int DATE = 0;
    private static final int TIME_START = 1;
    private static final int TIME_END = 2;
    private static final int FACILITY_ID = 3;
    private static final int MEMBER_ID = 4;
    private static final int PAYMENT_METHOD = 5;
    private static final int REPEAT = 6;
    private static final int REPETITION_END_DATE = 7;
    private static final int ASSIGN_TO_ACTIVITY = 8;
    private static final int ACTIVITY_POS = 9;
    private static final int MONITOR_POS = 10;

    private static final int NO_REPEAT = 0;
    private static final int REPEAT_WEEKLY = 1;
    private static final int REPEAT_MONTHLY = 2;

    private final Form form;


    private JButton confirm;
    private JDateChooser dateChooser;

    public AdministratorBookPanel() {
        this(null, null, null);
    }

    public AdministratorBookPanel(Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        super();

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        c.gridx = 0;
        c.gridy = 0;

        form = new Form();
        add(form.getPanel(), c);

        addForm(facility, timeStart, timeEnd);

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 0, 0, 5);
        c.gridy = 1;
        confirm = new JButton("Reservar");
        confirm.addActionListener(this::confirm);
        add(confirm, c);
    }

    private void addForm(Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        if (timeStart != null) {
            dateChooser.setDate(Utils.getDateFromTime(timeStart));
        } else {
            dateChooser.setDate(Utils.getCurrentDate());
        }

        form.addLine(new JLabel("Fecha:"), dateChooser);

        JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));

        if (timeStart != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(timeStart);
            hourStartSpinner.setValue(c.get(Calendar.HOUR_OF_DAY));
            if (timeEnd != null) {
                c.setTime(timeEnd);
                hourEndSpinner.setValue(c.get(Calendar.HOUR_OF_DAY));
            }
        }


        form.addLine(new JLabel("Hora de inicio:"), hourStartSpinner);
        form.addLine(new JLabel("Hora de fin:"), hourEndSpinner);

        JComboBox<String> facilities = new JComboBox<>();
        List<String> names = Database.getInstance().getFacilities().stream().map(Facility::getFacilityName)
                .collect(Collectors.toList());
        DefaultComboBoxModel<String> facilitiesModel = new DefaultComboBoxModel<>();
        names.forEach(facilitiesModel::addElement);
        facilities.setModel(facilitiesModel);
        if (facility != null) {
            for (int i = 0; i < Database.getInstance().getFacilities().size(); i++) {
                if (facility.equals(Database.getInstance().getFacilities().get(i))) {
                    facilities.setSelectedIndex(i);
                }
            }
        }

        form.addLine(new JLabel("Instalaci\u00F3n:"), facilities, false);

        JDateChooser endDateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        endDateChooser.setDate(Utils.getCurrentDate());
        endDateChooser.setEnabled(false);

        form.addSpace();

        JCheckBox bookForCenter = new JCheckBox("Reservar para el centro");
        JTextField idTextField = new JTextField(10);
        JComboBox<String> paymentCombo = new JComboBox<>();

        JCheckBox assignToActivity = new JCheckBox("Asignar a actividad");
        JComboBox<String> activities = new JComboBox<>();
        JComboBox<String> monitors = new JComboBox<>();

        JComboBox<String> recursive = new JComboBox<>();

        bookForCenter.addActionListener(l -> {
            idTextField.setEnabled(!bookForCenter.isSelected());
            paymentCombo.setEnabled(!bookForCenter.isSelected());
            assignToActivity.setEnabled(bookForCenter.isSelected());
            activities.setEnabled(assignToActivity.isSelected() && bookForCenter.isSelected());
            monitors.setEnabled(assignToActivity.isSelected() && bookForCenter.isSelected());
            recursive.setEnabled(bookForCenter.isSelected());
            endDateChooser.setEnabled(bookForCenter.isSelected() && recursive.getSelectedIndex() != 0);
            idTextField.setText(String.valueOf(0));
        });
        form.addLine(bookForCenter);
        form.addLine(new JLabel("ID de socio:"), idTextField);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cash", "Fee"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("M\u00E9todo de pago:"), paymentCombo);

        form.addSpace();

        recursive.setEnabled(false);
        DefaultComboBoxModel<String> recursiveModel = new DefaultComboBoxModel<>(
                new String[]{"No repetir", "Semanalmente", "Mensualmente"});
        recursive.addActionListener((l) -> endDateChooser.setEnabled(recursive.getSelectedIndex() > 0));
        recursive.setModel(recursiveModel);
        form.addLine(new JLabel("Repetir:"), recursive, false);

        form.addLine(new JLabel("Fecha fin repetici\u00F3n:"), endDateChooser);

        assignToActivity.setEnabled(false);
        activities.setEnabled(false);
        monitors.setEnabled(false);
        assignToActivity.addActionListener(l -> activities.setEnabled(assignToActivity.isSelected()));

        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
        activities.setModel(activitiesModel);

        DefaultComboBoxModel<String> monitorsModel = new DefaultComboBoxModel<>();
        Database.getInstance().getMonitors().stream().map(Monitor::getName).forEach(monitorsModel::addElement);
        monitors.setModel(monitorsModel);

        form.addSpace();

        form.addLine(assignToActivity, true);
        form.addLine(new JLabel("Actividad:"), activities, false);
        form.addLine(new JLabel("Monitor"), monitors, false);
    }

    private void confirm(ActionEvent actionEvent) {
        List<FacilityBooking> bookings = new ArrayList<>();

        Timestamp time = new Timestamp(Utils.addHourToDay(new Timestamp(Long.parseLong(form.getResults().get(DATE))),
                Integer.parseInt(form.getResults().get(TIME_START))).getTime());
        Timestamp repetitionEnd = new Timestamp(Long.parseLong(form.getResults().get(REPETITION_END_DATE)));

        long increase = 0;
        while (time.before(repetitionEnd) || increase == 0) {
            bookings.add(createBooking(increase));

            if (Integer.parseInt(form.getResults().get(REPEAT)) == REPEAT_WEEKLY) {
                // Semanalmente
                Calendar c = Calendar.getInstance();
                c.setTime(time);
                c.add(Calendar.WEEK_OF_YEAR, 1);
                increase += c.getTime().getTime() - time.getTime();
                time = new Timestamp(c.getTime().getTime());
            } else if (Integer.parseInt(form.getResults().get(REPEAT)) == REPEAT_MONTHLY) {
                // Mensualmente
                Calendar c = Calendar.getInstance();
                c.setTime(time);
                c.add(Calendar.MONTH, 1);
                increase += c.getTime().getTime() - time.getTime();
                time = new Timestamp(c.getTime().getTime());
            } else {
                // No repetir
                break;
            }
        }

        boolean valid = true;
        for (FacilityBooking fb : bookings) {
            if (fb == null || (fb.getMemberId() != 0 && !checkValidMember(fb)) || (fb.getMemberId() == 0 && !checkValidCenter(fb))) {
                valid = false;
                break;
            }
        }

        if (valid) {
            try {
                int monitorId = Database.getInstance().getMonitors().get(Integer.parseInt(form.getResults().get(MONITOR_POS))).getMonitorId();
                for (FacilityBooking fb : bookings) {
                    if (Boolean.parseBoolean(form.getResults().get(ASSIGN_TO_ACTIVITY))) {
                        ActivityBooking ab = new ActivityBooking(
                                Database.getInstance().getActivities().get(Integer.parseInt(form.getResults().get(ACTIVITY_POS))).getActivityId(),
                                fb.getFacilityBookingId(), monitorId);
                        Database.getInstance().getActivityBookings().add(ab);
                        ab.create();
                    }

                    Database.getInstance().getFacilityBookings().add(fb);
                    fb.create();
                }
                form.setMessage("Reserva creada correctamente");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private FacilityBooking createBooking() {
        return createBooking(0);
    }

    private FacilityBooking createBooking(long addTime) {
        try {
            List<String> results = form.getResults();
            Timestamp timeStart = new Timestamp(Utils
                    .addHourToDay(new Timestamp(Long.parseLong(results.get(DATE))), Integer.parseInt(results.get(TIME_START)))
                    .getTime());
            Timestamp timeEnd = new Timestamp(Utils
                    .addHourToDay(new Timestamp(Long.parseLong(results.get(DATE))), Integer.parseInt(results.get(TIME_END)))
                    .getTime());
            timeStart = new Timestamp(timeStart.getTime() + addTime);
            timeEnd = new Timestamp(timeEnd.getTime() + addTime);

            int facilityId = Database.getInstance().getFacilities().get(Integer.parseInt(results.get(FACILITY_ID)))
                    .getFacilityId();
            int memberId = Integer.parseInt(results.get(MEMBER_ID));
            String paymentMethod = results.get(PAYMENT_METHOD);

            return new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false);
        } catch (NumberFormatException e) {
            form.setError("Por favor, rellena todos los campos.\n");
            setSize(getWidth(), getPreferredSize().height);
            return null;
        }
    }

    private boolean checkValidMember(FacilityBooking fb) {
        boolean valid = true;
        String errors = "";

        if (fb != null) {
            if (fb.getTimeStart().before(Utils.getCurrentTime())) {
                valid = false;
                errors += "No puedes reservar para el pasado.\n";
            } else if (fb.getTimeStart().after(Utils.addHourToDay(Utils.getCurrentTime(), 24 * 15))) {
                valid = false;
                errors += "Solo puedes reservar hasta 15 d\u00EDs en adelante.\n";
            }
            if (fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() > 2 * 3600 * 1000
                    || fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() <= 0) {
                // More than 2 hours
                valid = false;
                errors += "Un socio solo puedo reservar un m\u00E1ximo de 2 horas.\nEl tiempo de finalizaci\u00F3n debe ser posterior al de inicio.\n";
            }
            Optional<Member> member = Database.getInstance().getMembers().stream()
                    .filter((m) -> m.getMemberId() == fb.getMemberId()).findAny();
            if (!member.isPresent() || fb.getMemberId() == 0) {
                // Member not valid
                errors += "n\u00FAmero de socio no v\u00E1lida.\n";
                valid = false;
            } else if (!Utils.isMemberFree(member.get(), fb.getTimeStart(), fb.getTimeEnd())) {
                errors += "El socio ya tiene una reserva en esta franja de tiempo.\n";
                valid = false;
            }
            Optional<Facility> of = Database.getInstance().getFacilities().stream()
                    .filter((f) -> f.getFacilityId() == fb.getFacilityId()).findAny();
            assert of.isPresent();

            if (of.isPresent() && !Utils.isFacilityFree(of.get(), fb.getTimeStart(), fb.getTimeEnd())) {
                // Facility not free
                List<FacilityBooking> reserva = Utils.getBookingsAt(of.get(), fb.getTimeStart(), fb.getTimeEnd());
                OverrideBookingDialog ob;
                (ob = new OverrideBookingDialog(reserva)).setVisible(true);
                valid = ob.getValid();
                if (!valid)
                    errors += "La instalaci\u00F3n est\u00E1 ocupada en la horas seleccionadas.\n";
                //valid = false;
                //errors += "La instalaci\u00F3n est\u00E1 ocupada en las horas seleccionadas.\n";
            }
        } else {
            errors += "Por favor, rellena todos los campos.\n";
            valid = false;
        }

        if (!valid) {
            form.setError(errors);
            setSize(getWidth(), getPreferredSize().height);
        }

        return valid;
    }

    private boolean checkValidCenter(FacilityBooking fb) {
        boolean valid = true;
        String errors = "";

        Optional<Facility> facility = Database.getInstance().getFacilities().stream()
                .filter((f) -> f.getFacilityId() == fb.getFacilityId()).findAny();

        // la facility debe existir
        if (!facility.isPresent()) {
            throw new IllegalStateException("La instalaci\u00F3n tiene que existir");
        }

        // invalided de los spinners
        if (fb.getTimeEnd().before(fb.getTimeStart()) || fb.getTimeStart().before(Utils.getCurrentDate())) {
            errors += "El tiempo de finalizaci\u00F3n debe ser posterior al de inicio.\n";
            valid = false;
        }

        if (facility.isPresent() && !Utils.isFacilityFree(facility.get(), fb.getTimeStart(), fb.getTimeEnd())) {
            List<FacilityBooking> reserva = Utils.getBookingsAt(facility.get(), fb.getTimeStart(), fb.getTimeEnd());
            OverrideBookingDialog ob;
            (ob = new OverrideBookingDialog(reserva)).setVisible(true);
            valid = ob.getValid();
            if (!valid)
                errors += "La instalaci\u00F3n est\u00E1 ocupada en la horas seleccionadas.\n";
        }
        form.setError(errors);

        return valid;
    }
}
