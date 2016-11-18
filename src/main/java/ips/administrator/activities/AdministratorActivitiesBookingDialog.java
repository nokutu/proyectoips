package ips.administrator.activities;

import com.toedter.calendar.JDateChooser;
import ips.database.*;
import ips.utils.BookingUtils;
import ips.utils.Utils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jorge on 17/11/2016.
 */
public class AdministratorActivitiesBookingDialog extends JDialog {

    private final static String[] DAYS = new String[]{
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    };
    private static final int LINE_FACILITIES = 1;
    private static final int LINE_MONITORS = 2;
    private static final int LINE_WEEK_CHK = 3;
    private static final int LINE_TIME_START = 4;
    public static final int LINE_TIME_END = 5;
    private static final int LINE_END_DATE = 6;
    private static final int LINE_ERROR_PANEL = 7;

    private JComboBox<String> activities;
    private JDateChooser endDate;
    private JComboBox<String> facilities;
    private JSpinner timeStart;
    private JSpinner timeEnd;
    private JTextPane errorPanel;
    private JComboBox<String> monitors;

    private JCheckBox[] weekChk;

    public AdministratorActivitiesBookingDialog(JDialog owner) {
        super(owner, true);

        JPanel content = new JPanel(new BorderLayout());
        setContentPane(content);

        JPanel center = new JPanel(new GridBagLayout());
        content.add(center, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;

        addActivities(center, c);
        addFacilities(center, c);
        addMonitors(center, c);

        addWeekCheckboxes(center, c);

        addTimeSpinners(center, c);
        addEndDateChooser(center, c);
        addErrorPanel(center, c);
        addBottomPanel(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addWeekCheckboxes(JPanel center, GridBagConstraints c) {
        c.gridwidth = 1;
        c.gridy = LINE_WEEK_CHK;

        weekChk = new JCheckBox[DAYS.length];
        for (int i = 0; i < DAYS.length; i++) {
            weekChk[i] = new JCheckBox(DAYS[i]);
            center.add(weekChk[i], c);
            c.gridx++;
        }
    }

    private void addActivities(JPanel center, GridBagConstraints c) {
        c.gridwidth = 7;

        JPanel activitiesPanel = new JPanel();
        center.add(activitiesPanel, c);

        activitiesPanel.add(new JLabel("Actividad:"), c);
        activities = new JComboBox<>();
        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Activity.getValidActivities().stream()
                .map(Activity::getActivityName)
                .forEach(activitiesModel::addElement);
        activities.setModel(activitiesModel);
        activitiesPanel.add(activities);
    }

    private DefaultComboBoxModel<String> addFacilities(JPanel center, GridBagConstraints c) {
        c.gridy = LINE_FACILITIES;

        JPanel facilitiesPanel = new JPanel();
        center.add(facilitiesPanel, c);

        facilitiesPanel.add(new JLabel("Instalación:"), c);
        facilities = new JComboBox<>();
        DefaultComboBoxModel<String> facilitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getFacilities().stream()
                .map(Facility::getFacilityName)
                .forEach(facilitiesModel::addElement);
        facilities.setModel(facilitiesModel);
        facilitiesPanel.add(facilities);
        return facilitiesModel;
    }

    private void addMonitors(JPanel center, GridBagConstraints c) {
        c.gridy = LINE_MONITORS;

        JPanel monitorPanel = new JPanel();
        center.add(monitorPanel, c);

        monitorPanel.add(new JLabel("Instalación:"), c);
        monitors = new JComboBox<>();
        DefaultComboBoxModel<String> monitorsModel = new DefaultComboBoxModel<>();
        Database.getInstance().getMonitors().stream()
                .map(Monitor::getName)
                .forEach(monitorsModel::addElement);
        monitors.setModel(monitorsModel);
        monitorPanel.add(monitors);
    }

    private void addTimeSpinners(JPanel center, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = LINE_TIME_START;
        c.gridwidth = 7;

        JPanel timeStartPanel = new JPanel();
        center.add(timeStartPanel, c);
        timeStartPanel.add(new JLabel("Hora inicio:"));
        timeStart = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        timeStartPanel.add(timeStart);

        c.gridx = 0;
        c.gridy = LINE_TIME_END;
        c.gridwidth = 7;

        JPanel timeEndPanel = new JPanel();
        center.add(timeEndPanel, c);
        timeEndPanel.add(new JLabel("Hora fin:"));
        timeEnd = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        timeEndPanel.add(timeEnd);
    }

    private void addEndDateChooser(JPanel center, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = LINE_END_DATE;
        c.gridwidth = 7;

        JPanel endDatePanel = new JPanel();
        center.add(endDatePanel, c);

        endDatePanel.add(new JLabel("Fecha de fin:"));
        endDate = new JDateChooser("dd/MM/yyyy", "", '_');
        Dimension d = endDate.getPreferredSize();
        d.width += 5;
        endDate.setPreferredSize(d);
        endDate.setDate(Utils.getCurrentDate());
        endDatePanel.add(endDate);
    }

    private void addBottomPanel(JPanel content) {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        content.add(bottom, BorderLayout.SOUTH);

        JButton ok = new JButton("OK");
        ok.addActionListener(l -> book());
        bottom.add(ok);

        JButton cancel = new JButton("Cancelar");
        cancel.addActionListener(l -> dispose());
        bottom.add(cancel);
    }

    private void addErrorPanel(JPanel center, GridBagConstraints c) {
        c.gridy = LINE_ERROR_PANEL;

        errorPanel = new JTextPane();
        errorPanel.setForeground(Color.red);
        errorPanel.setBackground(UIManager.getColor("Panel.background"));
        StyledDocument doc = errorPanel.getStyledDocument();
        SimpleAttributeSet centerAtt = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerAtt, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), centerAtt, false);
        errorPanel.setFocusable(false);
        center.add(errorPanel, c);
    }

    private void book() {
        Activity activity = Activity.getValidActivities().get(activities.getSelectedIndex());
        Facility facility = Database.getInstance().getFacilities().get(facilities.getSelectedIndex());

        Date week = Utils.getCurrentWeek();

        int amountCreated = 0;

        while (week.before(endDate.getDate())) {

            for (int i = 0; i < weekChk.length; i++) {
                if (weekChk[i].isSelected()) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(week);
                    c.add(Calendar.DAY_OF_WEEK, i);

                    FacilityBooking fb = new FacilityBooking(facility.getFacilityId(), 0,
                            new Timestamp(Utils.addHourToDay(c.getTime(), (Integer) timeStart.getValue()).getTime()),
                            new Timestamp(Utils.addHourToDay(c.getTime(), (Integer) timeEnd.getValue()).getTime()),
                            "Fee", true);

                    if (fb.getTimeStart().after(Utils.getCurrentTime()) && BookingUtils.checkValidCenter(fb, errorPanel::setText)) {
                        Monitor monitor = Database.getInstance().getMonitors().get(monitors.getSelectedIndex());
                        ActivityBooking ab = new ActivityBooking(activity.getActivityId(), fb.getFacilityBookingId(), monitor.getMonitorId());
                        try {
                            fb.create();
                            ab.create();
                            Database.getInstance().getFacilityBookings().add(fb);
                            Database.getInstance().getActivityBookings().add(ab);
                            amountCreated++;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else if (amountCreated > 0){
                        pack();
                        return;
                    }
                }
            }

            Calendar c = Calendar.getInstance();
            c.setTime(week);
            c.add(Calendar.WEEK_OF_YEAR, 1);
            week = c.getTime();
        }

        JOptionPane.showMessageDialog(this, "Se han creado " + amountCreated + " reservas");
    }
}
