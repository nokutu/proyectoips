package ips.administrator;

import ips.database.Database;
import ips.database.FacilityBooking;
import ips.database.Fee;
import ips.database.FeeItem;

import javax.print.attribute.standard.DateTimeAtCompleted;
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
	FacilityBooking booking;

	public AdministratorBookingCancelDialog(FacilityBooking booking) {
		this.booking = booking;
		int r = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this booking?", "Delete confirmation", JOptionPane.OK_CANCEL_OPTION);
		if (r == JOptionPane.OK_OPTION) {
			if (booking.getMemberId()==0) {// ADMIN BOOKING (we identify the admin bookings by the 0 member id)
				booking.setDeletedFlag(true);
				try{
					booking.update();
				} catch (SQLException e) {
					e.printStackTrace();
				}			} else { // MEMBER BOOKING
					if (isRequieredPayment()) {
						r = JOptionPane.showOptionDialog(null, "A payment shall be done. Charge into the member's fee?\n(in case of cash payment click \"No\")", "Warning",
								JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
						if(r==JOptionPane.YES_OPTION){ // cobrar el pago
							Database.getInstance().getFeeItems().add(
									new FeeItem( 
											Database.getInstance().getFacilityById(
													booking.getFacilityId()
													).getPrice(),
											Database.getInstance().getFeeByMember(booking.getMemberId(),new Date().getMonth()).getFeeId()
											)
									);

						}
						Database.getInstance().getFees(); // TODO añadir el pago adicional
						booking.setDeletedFlag(true);
						try{
							booking.update();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					} else {
						booking.setDeletedFlag(true);
						try{
							booking.update();
						} catch (SQLException e) {
							e.printStackTrace();
						}
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
	private boolean isRequieredPayment() {
		long current = new Date().getTime(); // the current time

		long duration = current - booking.getTimeStart().getTime();
		if (duration > 0) { // the time hasnt overtaken
			return false; // a payment is not needed
		} else // the booking time has passed (or is just now) and a payment is
			// needed
			return true;
	}

}
