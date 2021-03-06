package ips.administrator.activities;

import ips.MainWindow;
import ips.administrator.AssistanceDialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Created by nokutu on 14/11/2016.
 */
public class AdministratorActivitiesManagementDialog extends JDialog {

    public AdministratorActivitiesManagementDialog() {
        super(MainWindow.getInstance(), true);

        JPanel content = new JPanel(new GridBagLayout());
        setContentPane(content);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 20, 10, 20);

        JButton createActivity = new JButton("Crear una actividad");
        createActivity.addActionListener(l -> new AdministratorActivityCreatorDialog(this).setVisible(true));
        content.add(createActivity, c);

        c.gridy++;
        
        JButton deleteActivities = new JButton("Cancelar actividades");
        deleteActivities.addActionListener(l -> new AdministratorActivityCancelDialog(this).setVisible(true));
        content.add(deleteActivities, c);

        c.gridy++;

        JButton activitiesButton = new JButton("Ver actividades");
        activitiesButton.addActionListener(l -> new AdministratorActivitiesDialog(this).setVisible(true));
        content.add(activitiesButton, c);

        c.gridy++;

        JButton btnasistencia = new JButton("Mostrar faltas de asistencia");
        btnasistencia.addActionListener(e -> new AssistanceDialog(this).setVisible(true));
        add(btnasistencia, c);

        c.gridy++;

        JButton btnbooking = new JButton("Reservar para actividades");
        btnbooking.addActionListener(l -> new AdministratorActivitiesBookingDialog(this).setVisible(true));
        add(btnbooking, c);

        pack();
        setLocationRelativeTo(MainWindow.getInstance());
    }
}
