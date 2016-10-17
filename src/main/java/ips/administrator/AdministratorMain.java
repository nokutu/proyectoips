package ips.administrator;

import ips.AvailabilityPane;
import ips.FeeUpdater;
import ips.MainWindow;
import ips.database.Database;
import ips.member.MemberBookingCancelDialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

    public AdministratorMain() {
        JButton book = new JButton("Reservar para socio");
        book.addActionListener((e) -> new BookForMemberDialog(MainWindow.getInstance()).setVisible(true));
        add(book);

        add(getBtnBookActivity());
        add(getBtnPayDebts());
        add(getBtnCurrentBookings());
        add(getBtnFeeUpdater());
        add(getBtnAvailabilityOfFacilities());

        JButton createActivity = new JButton("Crear actividad");
        createActivity.addActionListener((e) -> new CreateActivityDialog(MainWindow.getInstance()).setVisible(true));
        add(createActivity);
    }

    private JButton getBtnBookActivity() {
        if (btnBookActivity == null) {
            btnBookActivity = new JButton("Reservar para el centro");
            btnBookActivity.addActionListener(e -> {
                BookForCenterDialog adminbook = new BookForCenterDialog(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnBookActivity;
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
        	btnPayDebts= new JButton("Pay in effective(not current)");
        	btnPayDebts.addActionListener(e -> {
                PayDebtsDialog adminbook = new PayDebtsDialog(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnPayDebts;
    }
    
    private JButton getBtnCurrentDebt() {
        if (btnCurrentDebts == null) {
        	btnCurrentDebts= new JButton("Pay in effective(current)");
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
        		adminbook.setContentPane(new AvailabilityPane(true, null));
        		adminbook.setSize(MainWindow.getInstance().getPreferredSize());
        		adminbook.setVisible(true);
            });
        }
        return btnAvailabilityOfFacilities;
	}
}
