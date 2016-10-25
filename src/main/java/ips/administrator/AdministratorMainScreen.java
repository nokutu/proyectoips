package ips.administrator;

import ips.MainWindow;
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

        upperPanel.add(new JButton("Ver actividades"));

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
