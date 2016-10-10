package ips.member;

import ips.database.FacilityBooking;

import javax.swing.JOptionPane;
import java.time.LocalDateTime;

/**
 * Class to be used when the user clicks in the
 * CANCEL button in the panel where the bookings are shown.
 * It determines if the cancellation process obey the requirement
 * of 1 hour at least before.
 *
 * @author Sergio Florez
 */
public class MemberBookingCancelDialog {

    /**
     * the <code>FacilityBooking</code> of the booking to cancel
     */
    FacilityBooking booking;

    /**
     * Using the date of a booking, determines if you can delete or not that booking. </br>
     * In case of can, a warning will be shown.
     *
     * @param booking
     */
    public MemberBookingCancelDialog(FacilityBooking booking) {
        this.booking = booking;
        LocalDateTime current = LocalDateTime.now(); // the current time
        if (isPossible(current)) {
            int r = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this booking?", "Delete confirmation", JOptionPane.OK_CANCEL_OPTION);
            if (r == JOptionPane.OK_OPTION) {
                booking.setDeletedFlag(true);// to have a bookings log, we dont delete, we mark a deleted flag
                booking.update(); // update de database maybe?
            } else {
                // nothing, is just an emphatic else
            }
        } else {
            JOptionPane.showConfirmDialog(null, "This booking cannot be canceled because it is booked to less than one hour since now");
        }
    }


    /**
     * @param current the current date of now(?)
     * @return if it's possible or not to cancel (actualy just mark as deleted)
     * the pertintent booking
     */
    @SuppressWarnings("deprecation")
    private boolean isPossible(LocalDateTime current) {
        if (!booking.isPaid() && !booking.isDeletedFlag())// por revisar este metodo
            if ((current.getYear() <= booking.getTimeStart().getYear() && current.getMonthValue() <= booking.getTimeStart().getMonth()))
                if (current.getDayOfMonth() <= booking.getTimeStart().getDay() && current.getHour() < booking.getTimeStart().getHours())
                    return true; // the booking can be done
        return false;
    }


}
