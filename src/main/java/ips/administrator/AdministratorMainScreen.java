package ips.administrator;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ips.AvailabilityPane;
import ips.FeeUpdater;
import ips.MainScreen;
import ips.MainWindow;
import ips.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 24/10/2016.
 */
public class AdministratorMainScreen extends JPanel implements MainScreen{

	private JPanel rightPanel;
	private JPanel upperPanel;
	private JPanel centerPanel;

	public AdministratorMainScreen() {
		setLayout(new BorderLayout());

		upperPanel = new JPanel(new GridBagLayout());
		add(upperPanel, BorderLayout.NORTH);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 10);
		c.gridx = 0;
		c.gridy = 0;

		centerPanel = new AvailabilityPane(true, 0, this);
		add(centerPanel, BorderLayout.CENTER);

		// TODO añadir el panel de Tony a center y los listeners que llamen a
		// setRightPanel

		JButton btnCurrentBooks = new JButton("Listar reservas en uso");
		btnCurrentBooks.addActionListener(e -> new CurrentViewDialog(MainWindow.getInstance()).setVisible(true));
		upperPanel.add(btnCurrentBooks, c);

		c.gridx++;

		JButton btnPayDebts = new JButton("Pagar deuda en efectivo (no actual)");
		btnPayDebts.addActionListener(e -> new PayDebtsDialog(MainWindow.getInstance()).setVisible(true));
		upperPanel.add(btnPayDebts, c);

		c.gridx++;

		JButton btnCurrentDebts = new JButton("Pagar deuda en efectivo (actual)");
		btnCurrentDebts.addActionListener(e -> 
		{
          List<FacilityBooking> bookingsList = Database.getInstance().getFacilityBookings().stream()
                .filter(f ->f.getTimeEnd().after(Utils.getCurrentTime())&&f.getTimeStart().before(Utils.getCurrentTime())&& f.getPaymentMethod().equals("Cash")&&f.isPaid())
                .collect(Collectors.toList());
          	if(bookingsList.isEmpty())
          	{
          		JOptionPane.showMessageDialog(this, " No existe ninguna reserva a pagar en efectivo a esta hora");
          	}
          	else
			{
          		new PayCurrentDebt(MainWindow.getInstance()).setVisible(true);
			}  
        });
		upperPanel.add(btnCurrentDebts, c);

		c.gridx = 0;
		c.gridy++;

		JButton btnasistencia = new JButton("Mostrar faltas de asistencia");
		btnasistencia.addActionListener(e -> new AssistanceDialog().setVisible(true));
		upperPanel.add(btnasistencia, c);

		c.gridx++;

		JButton btnFeeUpdater = new JButton("Actualizar Tarifas");
		btnFeeUpdater.addActionListener(e -> FeeUpdater.update());
		upperPanel.add(btnFeeUpdater, c);

		c.gridx++;
		
		JButton btnDetails = new JButton("Detalles de Reserva");
		btnDetails.addActionListener(e -> new DetailsDialog(MainWindow.getInstance(),null));
		upperPanel.add(btnDetails, c);

		c.gridx++;

		JButton activitiesButton = new JButton("Ver actividades");
		activitiesButton.addActionListener(l -> new AdministratorActivitiesDialog().setVisible(true));
		upperPanel.add(activitiesButton, c);

		setRightPanel(new AdministratorBookPanel());
	}

	public void setRightPanel(JPanel panel) {
		if (rightPanel != null) {
			remove(rightPanel);
		}
		add(panel, BorderLayout.EAST);
		rightPanel = panel;
		this.repaint();
		this.revalidate();
	}
}
