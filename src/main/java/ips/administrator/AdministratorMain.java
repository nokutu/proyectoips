package ips.administrator;

import ips.MainWindow;
import ips.database.Database;

import javax.swing.JButton;
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
    private JButton btnCancelar;

    public AdministratorMain() {
        JButton book = new JButton("Book for a member");
        book.addActionListener((e) -> new BookForMemberDialog(MainWindow.getInstance()).setVisible(true));
        add(book);
        add(getBtnBookActivity());
        add(getBtnPayDebts());
        add(getBtnCancelar());
    }

    private JButton getBtnBookActivity() {
        if (btnBookActivity == null) {
            btnBookActivity = new JButton("Book for the center");
            btnBookActivity.addActionListener(e -> {
                BookForCenterDialog adminbook = new BookForCenterDialog(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnBookActivity;
    }
    
    private JButton getBtnPayDebts() {
        if (btnPayDebts == null) {
        	btnPayDebts= new JButton("Pay in effective");
        	btnPayDebts.addActionListener(e -> {
                PayDebtsDialog adminbook = new PayDebtsDialog(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnPayDebts;
    }
    
    private JButton getBtnSeeDetails() {
        if (btnPayDebts == null) {
        	btnPayDebts= new JButton("Booking details");
        	btnPayDebts.addActionListener(e -> {
                PayCurrentDebt adminbook = new PayCurrentDebt(MainWindow.getInstance());
                adminbook.setVisible(true);
            });
        }
        return btnPayDebts;
    }
	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("cancelar");
			btnCancelar.addActionListener(e ->{
				AdministratorBookingCancelDialog cancelDialog = new AdministratorBookingCancelDialog(Database.getInstance().getFacilityBookings().get(0));
			});
		}
		return btnCancelar;
	}
}
