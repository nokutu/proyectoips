package ips.administrator;

import java.time.LocalDateTime;
import javax.swing.JOptionPane;

import ips.database.FacilityBooking;

/**
 * Class to be used when the admin clicks in the 
 * CANCEL button in the panel where the bookings are shown.
 * It cashes to the member if the time is out
 * @author Sergio Fl�rez
 *
 */
public class AdministratorBookingCancelDialog {

	/**the <code>FacilityBooking</code> of the booking to cancel*/
	FacilityBooking booking; 


	@SuppressWarnings("unused")
	public AdministratorBookingCancelDialog(FacilityBooking booking) {
		this.booking=booking;
		int r = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this booking?","Delete confirmation",JOptionPane.OK_CANCEL_OPTION);
		if(r==JOptionPane.OK_OPTION){
			if(/*ADMIN BOOKING*/false){
				booking.setDeleted_flag(true);
				booking.update();
			}
			else{ // MEMBER BOOKING
				if(isRequieredPayment()){
					booking.setDeleted_flag(true);
					booking.update();
				}
				else{
					
				}
			}
		}
		else{
			// nothing, is just an emphatic else
		}
	}

	/**
	 * This method determines if the admin. have to make a cash.
	 * If the time of the booking is not before 'now', then we have to do it
	 * @return
	 */
	private boolean isRequieredPayment() {
		LocalDateTime current = LocalDateTime.now(); // the current time
		if(!booking.isPaid() && !booking.isDeleted_flag() && 
				current.getYear()<=booking.getTimeStart().getYear() && current.getMonthValue()<=booking.getTimeStart().getMonth()
				&& current.getDayOfMonth()<=booking.getTimeStart().getDay() && current.getHour()<booking.getTimeStart().getHours())
			return false; 
		return true;
	}

}
