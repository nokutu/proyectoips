package ips.administrator;

import ips.MainWindow;
import ips.database.Activity;
import ips.database.ActivityMember;
import ips.database.Database;
import ips.database.Member;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by nokutu on 25/10/2016.
 */
public class AdministratorActivitiesDialog extends JDialog {

    private CheckBoxList memberList = new CheckBoxList();
    private List<Member> membersInSession;
    private JLabel assistanceLabel = new JLabel("");

    private JComboBox<String> activities;
    private JButton addMember = new JButton("Apuntar a socio");

    public AdministratorActivitiesDialog() {
        super(MainWindow.getInstance(), true);

        setLayout(new BorderLayout());

        createLeftPanel();
        createBottomPanel();
        // TODO añadir el panel central
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

        leftPanel.add(new JLabel("Sesión:"), c);

        c.gridx++;

        JComboBox<String> sessions = new JComboBox<>();
        activities.addActionListener(l -> {
            DefaultComboBoxModel<String> sessionsModel = new DefaultComboBoxModel<>();
            Database.getInstance().getActivityBookings().stream()
                    .filter(ab -> ab.getActivityName().equals(Database.getInstance().getActivities().get(activities.getSelectedIndex()).getActivityName()))
                    .map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
                    .forEach(sessionsModel::addElement);
            sessions.setModel(sessionsModel);
            refreshAssistanceCount();
        });
        activities.setSelectedIndex(0);

        sessions.addActionListener(l -> {
            memberList.removeAll();
            membersInSession = new ArrayList<>();
            for (ActivityMember am : Database.getInstance().getActivityMembers()) {
                if (am.getActivityName().equals(activities.getSelectedItem())) {
                    Member member = Database.getInstance().getMemberById(am.getMemberId());
                    if (am.isAssistance()) {
                        //la lista contiene a los miembros asistentes
                        membersInSession.add(member);
                    }

                    JCheckBox chk = new JCheckBox(member.getMemberName());
                    chk.setSelected(am.isAssistance());
                    chk.addActionListener(lis -> {
                        try {
                            am.setAssistance(chk.isSelected());
                            am.update();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        refreshAssistanceCount();
                    });
                    memberList.addLine(chk);
                }
                refreshAssistanceCount();
            }

            // Force the panel to refresh
            SwingUtilities.invokeLater(() -> {
                memberList.setVisible(false);
                memberList.setVisible(true);
            });
        });
        if (sessions.getModel().getSize() > 0) {
            sessions.setSelectedIndex(0);
        }

        leftPanel.add(sessions, c);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        bottomPanel.add(new JLabel("Socios apuntados:"));
        bottomPanel.add(assistanceLabel);
        
        bottomPanel.add(addMember);
    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JLabel colorlabel = new JLabel();
        colorlabel.setText("Marcado: asiste. Desmarcado: no asiste");

        centerPanel.add(colorlabel, BorderLayout.NORTH);
        centerPanel.add(memberList, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void refreshAssistanceCount() {
        Optional<Integer> assistantsOptional = Database.getInstance().getActivityMembers().parallelStream()
                .filter(am -> am.getActivityName().equals(activities.getSelectedItem()))
                .map(am -> am.isAssistance() ? 1 : 0)
                .reduce(Integer::sum);
        Optional<Activity> activityOptional = Database.getInstance().getActivities().parallelStream()
                .filter(a -> a.getActivityName().equals(activities.getSelectedItem()))
                .findAny();
        if (assistantsOptional.isPresent() && activityOptional.isPresent()) {
            assistanceLabel.setText(assistantsOptional.get() + "/" + activityOptional.get().getAssistantLimit());
            addMember.setEnabled(assistantsOptional.get() < activityOptional.get().getAssistantLimit());
        } else {
            assistanceLabel.setText("");
            addMember.setEnabled(false);
        }
    }

    private class CheckBoxList extends JPanel {

        private GridBagConstraints c;

        private JLabel endLabel = new JLabel();

        public CheckBoxList() {
            setLayout(new GridBagLayout());
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1;
            c.anchor = GridBagConstraints.NORTHWEST;
            setBackground(Color.white);

            c.weighty = 1;
            add(endLabel, c);
            c.weighty = 0;
        }

        public void addLine(JCheckBox ch) {
            remove(endLabel);

            ch.setBackground(Color.white);
            add(ch, c);
            c.gridy++;

            c.weighty = 1;
            add(endLabel, c);
            c.weighty = 0;
        }
    }
}
