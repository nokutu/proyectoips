package ips;

import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;

import java.sql.Date;
import java.util.List;

/**
 * Utilities class
 */
public class Utils {

    /**
     * Checks if a given facility is free during a given range of time.
     *
     * @param facility  the Facility.
     * @param timeStart the start time.
     * @param timeEnd   the end time.
     * @return true if the facility is free; false otherwise.
     */
    public static boolean isFacilityFree(Facility facility, Date timeStart, Date timeEnd) {
        List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
        for (FacilityBooking fb : bookings) {
            if (fb.getFacilityId() == facility.getFacilityId()) {
                if (timeStart.before(fb.getTimeStart()) && timeEnd.after(fb.getTimeStart())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Date addHourToDay(Date day, int hour) {
        return new Date(day.getTime() + hour * 1000 * 3600);
    }
}
