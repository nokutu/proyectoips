package ips.administrator;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BorderLayout;

/**
 * Created by nokutu on 24/10/2016.
 */
public class AdministratorMainScreen extends JPanel {


    private JPanel rightPanel;
    private JPanel upperPanel;

    public AdministratorMainScreen() {
        setLayout(new BorderLayout());

        upperPanel = new JPanel();
        add(upperPanel, BorderLayout.NORTH);

        // TODO añadir el panel de Tony a center y los listeners que llamen a setRightPanel

        JButton activitiesButton = new JButton("Ver actividades");
        activitiesButton.addActionListener(l -> new AdministratorActivitiesDialog().setVisible(true));
        upperPanel.add(activitiesButton);

        setRightPanel(new AdministratorBookPanel());
    }

    public void setRightPanel(JPanel panel) {
        if (rightPanel != null) {
            remove(rightPanel);
        }
        add(panel, BorderLayout.EAST);
        rightPanel = panel;
    }
}
