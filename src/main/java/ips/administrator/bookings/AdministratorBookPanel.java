package ips.administrator.bookings;

import com.toedter.calendar.JDateChooser;
import ips.utils.BookingUtils;
import ips.utils.Utils;
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
        List<String> names = Database.getInstance().getFacilities().stream()
                .map(Facility::getFacilityName)
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

        JCheckBox recursive = new JCheckBox("Repetir");

        bookForCenter.addActionListener(l -> {
            idTextField.setEnabled(!bookForCenter.isSelected());
            paymentCombo.setEnabled(!bookForCenter.isSelected());
            assignToActivity.setEnabled(bookForCenter.isSelected());
            activities.setEnabled(assignToActivity.isSelected() && bookForCenter.isSelected());
            monitors.setEnabled(assignToActivity.isSelected() && bookForCenter.isSelected());
            recursive.setEnabled(bookForCenter.isSelected());
            endDateChooser.setEnabled(bookForCenter.isSelected() && recursive.isSelected());
            idTextField.setText(String.valueOf(0));
        });
        form.addLine(bookForCenter);
        form.addLine(new JLabel("ID de socio:"), idTextField);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cuota", "Efectivo"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("M\u00E9todo de pago:"), paymentCombo);

        form.addSpace();

        recursive.setEnabled(false);
        recursive.addActionListener((l) -> endDateChooser.setEnabled(recursive.isSelected()));
        form.addLine(recursive, true);

        form.addLine(new JLabel("Fecha fin repetici\u00F3n:"), endDateChooser);

        assignToActivity.setEnabled(false);
        activities.setEnabled(false);
        monitors.setEnabled(false);
        assignToActivity.addActionListener(l -> {
            activities.setEnabled(assignToActivity.isSelected());
            monitors.setEnabled(assignToActivity.isSelected());
        });

        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getActivities().stream().filter(a->!a.isDeleted()).forEach(a -> activitiesModel.addElement(a.getActivityName()));
        Activity.getValidActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
        activities.setModel(activitiesModel);

        DefaultComboBoxModel<String> monitorsModel = new DefaultComboBoxModel<>();
        Database.getInstance().getMonitors().stream()
                .map(Monitor::getName)
                .forEach(monitorsModel::addElement);
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

            if (Boolean.parseBoolean(form.getResults().get(REPEAT))) {
                // Semanalmente
                Calendar c = Calendar.getInstance();
                c.setTime(time);
                c.add(Calendar.WEEK_OF_YEAR, 1);
                increase += c.getTime().getTime() - time.getTime();
                time = new Timestamp(c.getTime().getTime());
            } else {
                // No repetir
                break;
            }
        }

        boolean valid = true;
        for (FacilityBooking fb : bookings) {
            if (fb == null || (fb.getMemberId() != 0 && !BookingUtils.checkValidMember(fb, true, form::setError)) ||
                    (fb.getMemberId() == 0 && !BookingUtils.checkValidCenter(fb, form::setError))) {
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
                                Activity.getValidActivities().get(Integer.parseInt(form.getResults().get(ACTIVITY_POS))).getActivityId(),
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
            String paymentMethod = FacilityBooking.translate(results.get(PAYMENT_METHOD));

            return new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false);
        } catch (NumberFormatException e) {
            form.setError("Por favor, rellena todos los campos.\n");
            setSize(getWidth(), getPreferredSize().height);
            return null;
        }
    }
}
