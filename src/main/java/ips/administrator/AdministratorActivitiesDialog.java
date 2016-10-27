package ips.administrator;

import ips.MainWindow;
import ips.database.Database;
import ips.database.Member;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by nokutu on 25/10/2016.
 */
public class AdministratorActivitiesDialog extends JDialog {

    private JList<String> memberList;
    private List<Member> membersInSession;

    public AdministratorActivitiesDialog() {
        super(MainWindow.getInstance(), true);

        setLayout(new BorderLayout());

        createLeftPanel();
        createRightPanel();
        // TODO añadir el panel central

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

        JComboBox<String> activities = new JComboBox<>();
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
                    .map(ab -> new SimpleDateFormat().format(ab.getBookingTimeStart()))
                    .forEach(sessionsModel::addElement);
            sessions.setModel(sessionsModel);
        });
        activities.setSelectedIndex(0);

        sessions.addActionListener(l -> {
            // TODO update central list
        });

        leftPanel.add(sessions, c);
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

        JButton addMember = new JButton("Apuntar a socio");
        rightPanel.add(addMember, c);

        c.gridy = 1;

        JButton removeMember = new JButton("Eliminar socio");
        removeMember.addActionListener(l -> {
            Member m = membersInSession.get(memberList.getSelectedIndex());
            Database.getInstance().getActivityMembers().stream()
                    .filter(am -> am.getMemberId() == m.getMemberId())
                    .forEach(am -> {
                        try {
                            am.setDeleted(true);
                            am.update();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
        });
        rightPanel.add(removeMember, c);
    }

    private void createCenterPanel() {
        memberList = new JList<>();

        // TODO

        add(memberList, BorderLayout.CENTER);
    }
}