package ips.administrator;

import ips.MainWindow;
import ips.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.database.FeeItem;

import javax.swing.JOptionPane;

import java.sql.SQLException;
import java.util.Date;

/**
 * Class to be used when the admin clicks in the CANCEL button in the panel
 * where the bookings are shown. It cashes to the member if the time is out
 *
 * @author Sergio Florez
 */
public class AdministratorBookingCancelDialog {

	/**
	 * the <code>FacilityBooking</code> of the booking to cancel
	 */
	private static FacilityBooking booking;
	
	public AdministratorBookingCancelDialog() {}
	
	public static void show(FacilityBooking booking){
		AdministratorBookingCancelDialog.booking = booking;
		int r = JOptionPane.showConfirmDialog(MainWindow.getInstance(), "¿Estas seguro que quieres cancelar esta reserva?",
				"Confirmacion de cancelacion", JOptionPane.OK_CANCEL_OPTION);
		if (r == JOptionPane.OK_OPTION) {
			if (booking.getMemberId() == 0) {// ADMIN BOOKING (we identify the
												// admin bookings by the 0
												// member id)
				booking.setDeletedFlag(true);
				try {
					booking.update();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else { // MEMBER BOOKING
				if (isRequieredPayment()) { // cobrar el pago
					r = JOptionPane.showOptionDialog(MainWindow.getInstance(), "Se cargará el pago a la cuota del socio",
							"Aviso", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, null, null);
//TODO ver esto
					
					
					FeeItem newFeeItem = new FeeItem(
							Database.getInstance().getFacilityById(booking.getFacilityId()).getPrice(),
							Database.getInstance().getFeeByMember(booking.getMemberId(), new Date().getMonth()));
					
					Utils.addFeeItem(newFeeItem,new java.sql.Date(new Date().getTime()),booking.getMemberId());
					
					booking.setPayed(true);
					
				}
				booking.setDeletedFlag(true);
				try {
					booking.update();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			// nothing, is just an emphatic else
		}
	}

	/**
	 * This method determines if the admin. have to make a cash. If the time of
	 * the booking is not before 'now', then we have to do it
	 *
	 * @return true if the time of the booking has passed
	 */
	private static boolean isRequieredPayment() {
		long current = new Date().getTime(); // the current time

		long duration = current - booking.getTimeStart().getTime();
		if (duration < 0) { // the time hasnt overtaken
			return false; // a payment is not needed
		} else // the booking time has passed (or is just now) and a payment is
				// needed
			return true;
	}

}
