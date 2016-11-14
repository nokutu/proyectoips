package ips.administrator;

import ips.database.Activity;
import ips.database.Database;
import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 14/11/2016.
 */
public class AdministratorActivityCancelDialog extends JDialog {

    private final JComboBox activities;
    private List<Activity> activityList = null;

    public AdministratorActivityCancelDialog(JDialog owner) {
        super(owner, true);

        JPanel content = new JPanel(new GridBagLayout());
        setContentPane(content);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 20, 10, 5);

        activities = new JComboBox<>();
        refreshActivities();

        content.add(new JLabel("Actividad:"), c);

        c.gridx++;
        c.insets = new Insets(10, 5, 10, 20);

        content.add(activities, c);

        c.gridx = 0;
        c.gridy = 1;

        c.gridwidth = 2;
        c.insets = new Insets(10, 20, 10, 20);

        JButton cancel = new JButton("Cancelar");
        cancel.addActionListener(l -> {
            int res = JOptionPane.showConfirmDialog(this, "¿Estás seguro de querer cancelar la actividad?", "", JOptionPane.YES_NO_OPTION);

            if (res == JOptionPane.YES_OPTION) {
                activityList.get(activities.getSelectedIndex()).setDeleted(true);
                try {
                    activityList.get(activities.getSelectedIndex()).update();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                refreshActivities();
            }
        });
        content.add(cancel, c);

        pack();
        setLocationRelativeTo(owner);
    }

    private void refreshActivities() {
        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        activityList = Database.getInstance().getActivities().stream()
                .filter(a -> !a.isDeleted())
                .collect(Collectors.toList());
        activityList.stream().map(Activity::getActivityName).forEach(activitiesModel::addElement);
        activities.setModel(activitiesModel);
    }
}
