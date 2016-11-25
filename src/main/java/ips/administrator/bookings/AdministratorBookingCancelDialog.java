package ips.administrator.bookings;

import ips.MainWindow;
import ips.database.Fee;
import ips.utils.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.database.FeeItem;

import javax.swing.JOptionPane;

import java.awt.Window;
import java.sql.SQLException;
import java.sql.Timestamp;
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
		int r = JOptionPane.showConfirmDialog(MainWindow.getInstance(), "\u00BFEst\u00E1s seguro que quieres cancelar esta reserva?",
				"Confirmacion de cancelacion", JOptionPane.OK_CANCEL_OPTION);
		if (r == JOptionPane.OK_OPTION) {
			if (booking.getMemberId() == 0) {// ADMIN BOOKING (we identify the
												// admin bookings by the 0
												// member id)
				booking.setState(FacilityBooking.STATE_ANNULLED);
				booking.setCancellationDate(new Timestamp(new Date().getTime()));

				try {
					booking.update();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else { // MEMBER BOOKING
				String cancellationCause = JOptionPane.showInputDialog(MainWindow.getInstance(),
						"¿Cual es el motivo de la cancelacion?");
				booking.setCancellationCause(cancellationCause);
				booking.setCancellationDate(new Timestamp(new Date().getTime()));
				booking.setState(FacilityBooking.STATE_CANCELLED);

				if (isRequieredPayment()) { // cobrar el pago
					r = JOptionPane.showOptionDialog(MainWindow.getInstance(),
							"Se cargar\u00E1 el pago a la cuota del socio", "Aviso", JOptionPane.DEFAULT_OPTION,
							JOptionPane.WARNING_MESSAGE, null, null, null);

					booking.setPayed(true); // payed
					try {
						booking.update();
					} catch (SQLException ex) {
						ex.printStackTrace();
					}

					Fee theFee = Fee.getOrCreate(booking.getMember(), new Timestamp(Utils.getCurrentDate().getTime()));
					double cost = booking.getFacility().getPrice();

					assert theFee.getMemberId() == booking.getMemberId();

					theFee.addFeeItem(cost, "Pago por cancelacion");
				} else {
					try {
						booking.update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
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
