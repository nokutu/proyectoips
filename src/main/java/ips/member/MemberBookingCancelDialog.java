package ips.member;

import ips.MainWindow;
import ips.database.FacilityBooking;

import javax.swing.JOptionPane;
import java.util.Date;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.sql.SQLException;

import static java.util.concurrent.TimeUnit.HOURS;

/**
 * Class to be used when the user clicks in the CANCEL button in the panel where
 * the bookings are shown. It determines if the cancellation process obey the
 * requirement of 1 hour at least before.
 *
 * @author Sergio Florez
 */
public class MemberBookingCancelDialog {

	/**
	 * the <code>FacilityBooking</code> of the booking to cancel
	 */
	private static FacilityBooking booking;

	/**
	 * Using the date of a booking, determines if you can delete or not that
	 * booking. </br>
	 * In case of can, a warning will be shown.
	 *
	 * @param booking
	 */
	public MemberBookingCancelDialog() { }
	
	public static void show(FacilityBooking booking){
		MemberBookingCancelDialog.booking = booking;
		if (isPossible()) {
			int r = JOptionPane.showConfirmDialog(MainWindow.getInstance(), "¿Estas seguro que quieres cancelar esta reserva?",
					"Delete confirmation", JOptionPane.OK_CANCEL_OPTION);
			if (r == JOptionPane.OK_OPTION) {
				booking.setDeletedFlag(true);// to have a bookings log, we dont
												// delete, we mark a deleted
												// flag
				try {
					booking.update();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				// nothing, is just an emphatic else
			}
		} else {
			JOptionPane.showMessageDialog(MainWindow.getInstance(),
					"Esta reserva no puede ser cancelada porque fue realizada hace menos de una hora");
		}
	}

	/**
	 * @param current
	 *            the current date of now(?)
	 * @return if it's possible or not to cancel (actualy just mark as deleted)
	 *         the pertintent booking
	 */
	private static boolean isPossible() {
		long current = new Date().getTime(); // the current time
		long MAX_DURATION = MILLISECONDS.convert(1, HOURS);

		if (!booking.isPaid() && !booking.isDeletedFlag()) {

			long duration = booking.getTimeStart().getTime() - current;

			if (duration > MAX_DURATION) {
				return true; // the booking can be done
			}
		}
		return false;
	}

}
