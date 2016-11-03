package ips.administrator;

import ips.AvailabilityPane;
import ips.FeeUpdater;
import ips.MainWindow;
import ips.database.Database;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Main panel for administration
 */
public class AdministratorMain extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -4556204503732370163L;
    private JButton btnBookActivity;
    private JButton btnPayDebts;
    private JButton btnCurrentDebts;
    private JButton btnCurrentBooks;
    private JButton btnFeeUpdater;
    private JButton btnAvailabilityOfFacilities;
    private JButton btnBorrarCuandoEl;

    public AdministratorMain() {
        add(getBtnPayDebts());
        add(getBtnCurrentDebt());
        add(getBtnCurrentBookings());
        add(getBtnFeeUpdater());
        add(getBtnAvailabilityOfFacilities());
        add(getBtnBorrarCuandoEl());
    }
    
    private JButton getBtnCurrentBookings() 
    {
        if (btnCurrentBooks == null) {
        	btnCurrentBooks= new JButton("Listar reservas en uso");
        	btnCurrentBooks.addActionListener(e -> {
                CurrentViewDialog adminbook = new CurrentViewDialog(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnCurrentBooks;
    }
    
    private JButton getBtnPayDebts() {
        if (btnPayDebts == null) {
        	btnPayDebts= new JButton("Pagar deuda en efectivo (no actual)");
        	btnPayDebts.addActionListener(e -> {
                PayDebtsDialog adminbook = new PayDebtsDialog(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnPayDebts;
    }
    
    private JButton getBtnCurrentDebt() {
        if (btnCurrentDebts == null) {
        	btnCurrentDebts= new JButton("Pagar deuda en efectivo (actual)");
        	btnCurrentDebts.addActionListener(e -> {
                PayCurrentDebt adminbook = new PayCurrentDebt(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnCurrentDebts;
    }
    

	private JButton getBtnFeeUpdater() {
		if (btnFeeUpdater == null) {
			btnFeeUpdater = new JButton("Update Fees");
			btnFeeUpdater.addActionListener(e -> {
				FeeUpdater.update();
			});
		}
		return btnFeeUpdater;
	}
	
	private JButton getBtnAvailabilityOfFacilities() {
		if (btnAvailabilityOfFacilities == null) {
        	btnAvailabilityOfFacilities = new JButton("Availability of facilities");
        	btnAvailabilityOfFacilities.addActionListener(e -> {
        		JDialog adminbook = new JDialog(MainWindow.getInstance());
        		adminbook.setContentPane(new AvailabilityPane(true, null, null));
        		adminbook.setSize(MainWindow.getInstance().getPreferredSize());
        		adminbook.setVisible(true);
            });
        }
        return btnAvailabilityOfFacilities;
	}
	private JButton getBtnBorrarCuandoEl() {
		if (btnBorrarCuandoEl == null) {
			btnBorrarCuandoEl = new JButton("Borrar esto cuando el panel de detalles este integrado con el panel de de ver las reservas");
			btnBorrarCuandoEl.addActionListener(e -> {
				String id = JOptionPane.showInputDialog("Dame el id de la reserva");
				String hIn = JOptionPane.showInputDialog("Dame la hora de inicio de la reserva");
				try{
				DetailsDialog d = new DetailsDialog(MainWindow.getInstance(), Database.getInstance().getBookingById(Integer.parseInt(id),Integer.parseInt(hIn)));
				d.setVisible(true);
				}catch(Exception ex){System.err.println("Ups, no existe");}
			});
		}
		return btnBorrarCuandoEl;
	}
}
