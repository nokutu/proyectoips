package ips.administrator.main;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ips.AvailabilityPane;
import ips.MainScreen;
import ips.MainWindow;
import ips.utils.Utils;
import ips.administrator.CurrentViewDialog;
import ips.administrator.activities.AdministratorActivitiesManagementDialog;
import ips.administrator.bookings.AdministratorBookPanel;
import ips.administrator.debts.FeeUpdater;
import ips.administrator.debts.PayCurrentDebt;
import ips.administrator.debts.PayPastDebtsDialog;
import ips.database.Database;
import ips.database.FacilityBooking;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 24/10/2016.
 */
public class AdministratorMainScreen extends JPanel implements MainScreen {

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

        JButton btnCurrentBooks = new JButton("Listar reservas en uso");
        btnCurrentBooks.addActionListener(e -> new CurrentViewDialog(MainWindow.getInstance()).setVisible(true));
        upperPanel.add(btnCurrentBooks, c);

        c.gridx++;

        JButton btnPayDebts = new JButton("Pagar deuda en efectivo (no actual)");
        btnPayDebts.addActionListener(e -> {
            List<FacilityBooking> bookingsList = Database.getInstance().getFacilityBookings().stream()
                    .filter(f -> f.getTimeEnd().before(Utils.getCurrentTime()) &&
                            f.getPaymentMethod().equals("Cash") && !f.isPaid() &&
                            f.getState().equals(FacilityBooking.STATE_VALID) && f.getMemberId() != 0)
                    .collect(Collectors.toList());
            if (bookingsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, " No existe ninguna reserva por pagar en efectivo");
            } else {
                new PayPastDebtsDialog();
            }
        });
        upperPanel.add(btnPayDebts, c);

        c.gridx++;

        JButton btnCurrentDebts = new JButton("Pagar deuda en efectivo (actual)");
        btnCurrentDebts.addActionListener(e ->
        {
            List<FacilityBooking> bookingsList = Database.getInstance().getFacilityBookings().stream()
                    .filter(f -> f.getTimeEnd().after(Utils.getCurrentTime()) &&
                            f.getTimeStart().before(Utils.getCurrentTime()) &&
                            f.getPaymentMethod().equals("Cash") && !f.isPaid() &&
                            f.getState().equals(FacilityBooking.STATE_VALID) && f.getMemberId() != 0)
                    .collect(Collectors.toList());
            if (bookingsList.isEmpty()) {
                JOptionPane.showMessageDialog(this, " No existe ninguna reserva a pagar en efectivo a esta hora");
            } else {
                new PayCurrentDebt(MainWindow.getInstance()).setVisible(true);
            }
        });
        upperPanel.add(btnCurrentDebts, c);

        c.gridx = 0;
        c.gridy++;

        JButton btnFeeUpdater = new JButton("Actualizar Tarifas");
        btnFeeUpdater.addActionListener(e -> new FeeUpdater() );
        upperPanel.add(btnFeeUpdater, c);

        c.gridx++;

		/*JButton btnDetails = new JButton("Detalles de Reserva");
		btnDetails.addActionListener(e -> new DetailsDialog(MainWindow.getInstance(),null));
		upperPanel.add(btnDetails, c);

		c.gridx++;*/

        JButton manageActivities = new JButton("Gestión de actividades");
        manageActivities.addActionListener(l -> new AdministratorActivitiesManagementDialog().setVisible(true));
        upperPanel.add(manageActivities, c);

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
