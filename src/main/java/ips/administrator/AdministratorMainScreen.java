package ips.administrator;

import javax.swing.JButton;
import javax.swing.JPanel;

import ips.FeeUpdater;
import ips.MainWindow;

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
        
        JButton btnCurrentBooks = new JButton("Listar reservas en uso");
    	btnCurrentBooks.addActionListener(e ->  new CurrentViewDialog(MainWindow.getInstance()).setVisible(true));
    	upperPanel.add(btnCurrentBooks);
    	
    	JButton btnPayDebts = new JButton("Pagar deuda en efectivo (no actual)");
    	btnPayDebts.addActionListener(e -> new PayDebtsDialog(MainWindow.getInstance()).setVisible(true));
    	upperPanel.add(btnPayDebts);
    	
    	JButton btnCurrentDebts = new JButton("Pagar deuda en efectivo (actual)");
    	btnCurrentDebts.addActionListener(e ->  new PayCurrentDebt(MainWindow.getInstance()).setVisible(true));
    	upperPanel.add(btnCurrentDebts);
    	
    	JButton btnasistencia = new JButton("Mostrar faltas de asistencia");
    	btnasistencia.addActionListener(e ->  new AssistanceDialog().setVisible(true));
    	upperPanel.add(btnasistencia);
    	
    	JButton btnFeeUpdater = new JButton("Actualizar Tarifas");
		btnFeeUpdater.addActionListener(e -> FeeUpdater.update());
		upperPanel.add(btnFeeUpdater);
    	
    	
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
