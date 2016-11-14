package ips.administrator;

import ips.MainWindow;
import ips.database.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class AssistanceDialog extends JDialog {

    private JList<String> memberList = new JList();

    private List<ActivityBooking> sessionsList;
    private List<Member> membersInSession;
    private List<ActivityMember> activityMembersInSession;
    private List<JLabel> activityMembersInSessionlbl;

    private JLabel assistanceLabel = new JLabel("");
    private JComboBox<String> activities;
    private JComboBox<String> sessions;

    public AssistanceDialog(JDialog owner) {
        super(owner, true);

        setLayout(new BorderLayout());

        createLeftPanel();
        createBottomPanel();
        createCenterPanel();

        setMinimumSize(new Dimension(320, 180));
        pack();
        setLocationRelativeTo(owner);
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
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 10, 2, 5);

        leftPanel.add(new JLabel("Actividad:"), c);

        c.gridx++;

        activities = new JComboBox<>();
        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
        activities.setModel(activitiesModel);
        leftPanel.add(activities, c);

        c.gridx = 0;
        c.gridy++;

        leftPanel.add(new JLabel("Sesi\u00F3n:"), c);

        c.gridx++;

        sessions = new JComboBox<>();
        activities.addActionListener(l -> {
            memberList.removeAll();
            DefaultComboBoxModel<String> sessionsModel = new DefaultComboBoxModel<>();
            sessionsList = Database.getInstance().getActivityBookings().stream()
                    .filter(ab -> ab.getActivityId() == getSelectedActivity().getActivityId())
                    .collect(Collectors.toList());
            sessionsList.stream()
                    .map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
                    .forEach(sessionsModel::addElement);
            sessions.setModel(sessionsModel);

            if (sessions.getModel().getSize() > 0) {
                sessions.setSelectedIndex(0);
            }
            refreshAssistanceCount();
        });
        activities.setSelectedIndex(0);

        sessions.addActionListener(l -> {
            refreshCentralPanelList();
        });
        if (sessions.getModel().getSize() > 0) {
            sessions.setSelectedIndex(0);
        }

        leftPanel.add(sessions, c);
    }

    private void refreshCentralPanelList() {
        memberList.removeAll();
        membersInSession = new ArrayList<>();
        activityMembersInSession = new ArrayList<>();
        activityMembersInSessionlbl = new ArrayList<>();
        DefaultListModel<String> d= new DefaultListModel<>();
        for (ActivityMember am : Database.getInstance().getActivityMembers()) {
            if (am.getActivityId() == getSelectedActivity().getActivityId() &&
                    am.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId() &&
                    !am.isDeleted()&&!am.isAssistance()) {
                activityMembersInSession.add(am);

                Member member = Database.getInstance().getMemberById(am.getMemberId());
                membersInSession.add(member);

                
                d.addElement(member.getMemberName());
                memberList.setModel(d);
            }
            refreshAssistanceCount();
        }

        // Force the panel to refresh
        SwingUtilities.invokeLater(() -> {
            memberList.setVisible(false);
            memberList.setVisible(true);
        });
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        bottomPanel.add(new JLabel("Faltas totales:"));
        bottomPanel.add(assistanceLabel);

    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JLabel colorlabel = new JLabel();
        colorlabel.setText("Lista de faltas de asistencia:");

        centerPanel.add(colorlabel, BorderLayout.NORTH);
        centerPanel.add(memberList, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void refreshAssistanceCount() {
        Optional<Integer> assistantsOptional = Database.getInstance().getActivityMembers().parallelStream()
                .filter(am -> am.getActivityId() == getSelectedActivity().getActivityId() &&
                        !am.isDeleted() &&
                        am.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId())
                .map(am -> am.isAssistance() ? 0 : 1)
                .reduce(Integer::sum);
        Optional<Activity> activityOptional = Database.getInstance().getActivities().parallelStream()
                .filter(a -> a.getActivityName().equals(activities.getSelectedItem()))
                .findAny();
        if (activityOptional.isPresent()) {
            if (assistantsOptional.isPresent()) {
                // n assistants
                assistanceLabel.setText(assistantsOptional.get() + "/" + activityOptional.get().getAssistantLimit());
            } else {
                // 0 assistants
                assistanceLabel.setText("0/" + activityOptional.get().getAssistantLimit());
           
            }
        } else {
            // no activity selected
            assistanceLabel.setText("");
        }
    }

    private Activity getSelectedActivity() {
        return Database.getInstance().getActivities().get(activities.getSelectedIndex());
    }

 
}
