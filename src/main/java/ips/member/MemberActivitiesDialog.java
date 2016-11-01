package ips.member;

import ips.MainWindow;
import ips.database.ActivityBooking;
import ips.database.ActivityMember;
import ips.database.Database;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class MemberActivitiesDialog extends JDialog {

    private List<ActivityBooking> activityBookings;
    private JList<String> activitiesList;
    private List<ActivityMember> activityMembers;

    public MemberActivitiesDialog() {
        super(MainWindow.getInstance(), true);

        setLayout(new BorderLayout());

        createLeftPanel();
        createRightPanel();
        createCenterPanel();

        setMinimumSize(new Dimension(320, 180));
        pack();
        setLocationRelativeTo(MainWindow.getInstance());
    }

    private void createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(leftPanel, BorderLayout.WEST);

        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 10, 2, 0);

        leftPanel.add(new JLabel("Actividad:"), c);

        c.gridx++;

        JComboBox<String> activities = new JComboBox<>();
        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
        activities.setModel(activitiesModel);
        leftPanel.add(activities, c);

        c.gridx = 0;
        c.gridy++;

        leftPanel.add(new JLabel("Sesi\u00F3n:"), c);

        c.gridx++;

        JComboBox<String> sessions = new JComboBox<>();
        activities.addActionListener(l -> {
            DefaultComboBoxModel<String> sessionsModel = new DefaultComboBoxModel<>();
            activityBookings = Database.getInstance().getActivityBookings().stream()
                    .filter(ab -> ab.getActivityId() == Database.getInstance().getActivities().get(activities.getSelectedIndex()).getActivityId())
                    .collect(Collectors.toList());
            activityBookings.stream().map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart())).forEach(sessionsModel::addElement);
            sessions.setModel(sessionsModel);
        });
        activities.setSelectedIndex(0);

        leftPanel.add(sessions, c);

        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 2;
        c.gridy++;
        c.gridx = 0;

        JButton join = new JButton("Apuntarse");
        leftPanel.add(join, c);
    }

    private void createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(rightPanel, BorderLayout.EAST);

        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(2, 5, 2, 10);

        JButton remove = new JButton("Borrarse");
        rightPanel.add(remove, c);
    }

    private void createCenterPanel() {
        activitiesList = new JList<>();
        add(activitiesList, BorderLayout.CENTER);
        refreshActivitiesList();
    }

    private void refreshActivitiesList() {
        DefaultListModel<String> m = new DefaultListModel<>();
        activityMembers = Database.getInstance().getActivityMembers().stream().filter(am -> !am.isDeleted() && am.getMemberId() == MemberMainScreen.userID).collect(Collectors.toList());
        activityMembers.forEach(am -> m.addElement(am.getActivity().getActivityName()));
        activitiesList.setModel(m);
    }
}
