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
import java.awt.event.ActionEvent;
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
    private static final int LINE_MONITORS = 5;
    private static final int LINE_WEEK_CHK = 2;
    private static final int LINE_TIME_START = 3;
    public static final int LINE_TIME_END = 4;
    private static final int LINE_DATE_RANGE = 6;
    private static final int LINE_OPTIONS = 7;
    private static final int LINE_ERROR_PANEL = 9;

    private JComboBox<String> activities;
    private JDateChooser startDate;
    private JDateChooser endDate;
    private JComboBox<String> facilities;
    private JSpinner timeStart;
    private JSpinner timeEnd;
    private JTextPane errorPanel;
    private JComboBox<String> monitors;

    private JCheckBox[] weekChk;
    private JSpinner[] weekStart;
    private JSpinner[] weekEnd;
    private JComboBox[] weekMonitor;

    private JCheckBox chooseMonitor;
    private JCheckBox chooseHours;

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
        addDateRangeChooser(center, c);
        addOptionChecks(center, c);
        addErrorPanel(center, c);
        addBottomPanel(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addWeekCheckboxes(JPanel center, GridBagConstraints c) {
        c.gridwidth = 1;
        c.gridy = LINE_WEEK_CHK;
        c.gridx = 1;

        weekChk = new JCheckBox[DAYS.length];
        for (int i = 0; i < DAYS.length; i++) {
        	JCheckBox weekBox = new JCheckBox(DAYS[i]);
        	int aux=i;
        	weekBox.addActionListener(l->{
        		
        		if(weekBox.isSelected())
        		{
        			weekStart[aux].setEnabled(true);
        			weekEnd[aux].setEnabled(true);
        			weekMonitor[aux].setEnabled(true);
        		}
        		else
        		{
        			weekStart[aux].setEnabled(false);
        			weekEnd[aux].setEnabled(false);
        			weekMonitor[aux].setEnabled(false);
        		}
        		
        	});
            weekChk[i] = weekBox;
            center.add(weekChk[i], c);
            c.gridx++;
        }
    }

    private void addOptionChecks(JPanel center, GridBagConstraints c) {
        c.gridy = LINE_OPTIONS;
        c.gridx = 0;

        chooseHours = new JCheckBox("Fijar mismas horas");
        chooseMonitor = new JCheckBox("Fijar mismos monitores");

        chooseHours.setSelected(false);
        
        chooseHours.addActionListener(l->
    	{
    		if(chooseHours.isSelected())
    		{
    			for(JSpinner box: weekStart)
    			{
    				box.setValue(0);
    			}
    			
    			for(JSpinner box: weekEnd)
    			{
    				box.setValue(0);
    			}
    			
    			
    		}
    	});
        chooseMonitor.setSelected(false);
        
        chooseMonitor.addActionListener(l->
        	{
        		if(chooseMonitor.isSelected())
        		{
        			for(JComboBox box: weekMonitor)
        			{
        				box.setSelectedIndex(0);
        			}
        		}
        	});
        

        c.gridwidth = 4;
        center.add(chooseHours, c);
        c.gridx = 4;
        c.gridwidth = 4;
        center.add(chooseMonitor, c);
    }


    private void addActivities(JPanel center, GridBagConstraints c) {
        c.gridwidth = 4;

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
        //c.gridy = LINE_FACILITIES;
        c.gridx = 4;
        c.gridwidth = 4;
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
        c.gridx = 0;
        c.gridwidth = 1;

        JPanel monitorPanel = new JPanel();
        center.add(monitorPanel, c);

        monitorPanel.add(new JLabel("Monitor:"), c);
       // monitors = new JComboBox<>();
        DefaultComboBoxModel<String> monitorsModel = new DefaultComboBoxModel<>();
        Database.getInstance().getMonitors().stream()
                .map(Monitor::getName)
                .forEach(monitorsModel::addElement);
       // monitors.setModel(monitorsModel);
      // monitorPanel.add(monitors);
        c.gridx = 1;
        weekMonitor = new JComboBox[DAYS.length];
        for (int i = 0; i < DAYS.length; i++) {
        	JComboBox monitor = new JComboBox<>();
        	monitor.setModel(monitorsModel);
        	monitor.setEnabled(false);
        	monitor.addActionListener(l->
        	{
        		if(chooseMonitor.isSelected())
        		{
        			for(JComboBox box: weekMonitor)
        			{
        				box.setSelectedIndex(monitor.getSelectedIndex());
        			}
        		}
        	});
            weekMonitor[i] = monitor;
            center.add(weekMonitor[i], c);
            c.gridx++;
        }

    }

    private void addTimeSpinners(JPanel center, GridBagConstraints c) {
        c.gridwidth = 1;
        c.gridy = LINE_TIME_START;
        c.gridx = 0;

        JPanel timeStartPanel = new JPanel();
        center.add(timeStartPanel, c);
        timeStartPanel.add(new JLabel("Hora inicio:"));
        //timeStart = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        c.gridx = 1;
        //timeStartPanel.add(timeStart);
        weekStart = new JSpinner[DAYS.length];
        for (int i = 0; i < DAYS.length; i++) {
        	JSpinner spin = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        	spin.addChangeListener(l->
        	{
        		if(chooseHours.isSelected())
        		{
        			for(JSpinner start: weekStart)
        			{
        				start.setValue(spin.getValue());
        			}
        		}
        	});
        	spin.setEnabled(false);
            weekStart[i] = spin;
            center.add(weekStart[i], c);
            c.gridx++;
        }

        c.gridwidth = 1;
        c.gridy = LINE_TIME_END;
        c.gridx = 0;

        JPanel timeEndPanel = new JPanel();
        center.add(timeEndPanel, c);
        timeEndPanel.add(new JLabel("Hora fin:"));
        // timeEnd = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        // timeEndPanel.add(timeEnd);
        c.gridx = 1;

        weekEnd = new JSpinner[DAYS.length];
        for (int i = 0; i < DAYS.length; i++) {
        	JSpinner spin = new JSpinner(new SpinnerNumberModel(0, 0, 24, 1));
        	spin.addChangeListener(l->
        	{
        		if(chooseHours.isSelected())
        		{
        			for(JSpinner end: weekEnd)
        			{
        				end.setValue(spin.getValue());
        			}
        		}
        	});
        	spin.setEnabled(false);
            weekEnd[i] = spin;
            center.add(weekEnd[i], c);
            c.gridx++;
        }
    }
    
   
    private void addDateRangeChooser(JPanel center, GridBagConstraints c) {
        c.gridx = 0;
        c.gridy = LINE_DATE_RANGE;
        c.gridwidth = 4;

        JPanel startDatePanel = new JPanel();
        center.add(startDatePanel, c);

        startDatePanel.add(new JLabel("Fecha de inicio:"));
        startDate = new JDateChooser("dd/MM/yyyy", "", '_');
        Dimension d = startDate.getPreferredSize();
        d.width += 5;
        startDate.setPreferredSize(d);
        startDate.setDate(Utils.getCurrentDate());
        startDatePanel.add(startDate);

        c.gridx = 4;
        c.gridwidth = 4;

        JPanel endDatePanel = new JPanel();
        center.add(endDatePanel, c);

        endDatePanel.add(new JLabel("Fecha de fin:"));
        endDate = new JDateChooser("dd/MM/yyyy", "", '_');
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

        Date week = startDate.getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(week);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        week = c.getTime();

        int amountCreated = 0;

        while (week.before(endDate.getDate())) {

            for (int i = 0; i < weekChk.length; i++) {
                if (weekChk[i].isSelected()) {
                    c.setTime(week);
                    c.add(Calendar.DAY_OF_WEEK, i);

                    FacilityBooking fb = new FacilityBooking(facility.getFacilityId(), 0,
                            new Timestamp(Utils.addHourToDay(c.getTime(), (Integer) weekStart[i].getValue()).getTime()),
                            new Timestamp(Utils.addHourToDay(c.getTime(), (Integer) weekEnd[i].getValue()).getTime()),
                            "Fee", true);

                    if (fb.getTimeStart().after(startDate.getDate()) && BookingUtils.checkValidCenter(fb, errorPanel::setText)) {
                        Monitor monitor = Database.getInstance().getMonitors().get(weekMonitor[i].getSelectedIndex());
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
                    } else if (amountCreated > 0) {
                        pack();
                        return;
                    }
                }
            }

            c.setTime(week);
            c.add(Calendar.WEEK_OF_YEAR, 1);
            week = c.getTime();
        }

        JOptionPane.showMessageDialog(this, "Se han creado " + amountCreated + " reservas");
    }
}
