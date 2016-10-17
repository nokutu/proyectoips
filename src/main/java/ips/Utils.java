package ips;

import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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
    public static boolean isFacilityFree(Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
        for (FacilityBooking fb : bookings) {
            if (fb.getFacilityId() == facility.getFacilityId() && areSameDay(fb.getTimeStart(), timeStart) && !fb.isDeletedFlag()) {
                if (timeStart.before(fb.getTimeStart()) && timeEnd.after(fb.getTimeStart()) ||
                        timeStart.before(fb.getTimeEnd()) && timeEnd.after(fb.getTimeEnd()) ||
                        timeStart.equals(fb.getTimeStart()) || timeEnd.equals(fb.getTimeEnd())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Date addHourToDay(Date day, int hour) {
        return new Date(day.getTime() + hour * 1000 * 3600);
    }

    public static boolean areSameDay(Date a, Date b) {
        Calendar calA = Calendar.getInstance();
        Calendar calB = Calendar.getInstance();
        calA.setTime(a);
        calB.setTime(b);

        return calA.get(Calendar.DAY_OF_YEAR) == calB.get(Calendar.DAY_OF_YEAR) &&
                calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR);
    }

    public static boolean areSameWeek(Date a, Date b) {
        Calendar calA = Calendar.getInstance();
        Calendar calB = Calendar.getInstance();
        calA.setTime(a);
        calB.setTime(b);

        return calA.get(Calendar.WEEK_OF_YEAR) == calB.get(Calendar.WEEK_OF_YEAR) &&
                calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR);
    }

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
