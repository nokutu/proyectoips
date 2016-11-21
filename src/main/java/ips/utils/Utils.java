package ips.utils;

import ips.database.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings().stream()
                .filter(fb -> fb.getState().equals(FacilityBooking.STATE_VALID))
                .collect(Collectors.toList());
        for (FacilityBooking fb : bookings) {
            if (fb.getFacilityId() == facility.getFacilityId() && areSameDay(fb.getTimeStart(), timeStart)
                    && fb.getState().equals(FacilityBooking.STATE_VALID)) {
                if (timeStart.before(fb.getTimeStart()) && timeEnd.after(fb.getTimeStart())
                        || timeStart.before(fb.getTimeEnd()) && timeEnd.after(fb.getTimeEnd())
                        || timeStart.equals(fb.getTimeStart()) || timeEnd.equals(fb.getTimeEnd())) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return la reserva(s) ya existente(n) en el lapso de tiempo
     * @see Utils#isFacilityFree
     */
    public synchronized static List<FacilityBooking> getBookingsAt(Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        List<FacilityBooking> result = new ArrayList<>();

        for (FacilityBooking fb : Database.getInstance().getFacilityBookings()) {
            if (fb.getFacilityId() == facility.getFacilityId() && areSameDay(fb.getTimeStart(), timeStart)
                    && fb.getState().equals(FacilityBooking.STATE_VALID)) {
                if (timeStart.before(fb.getTimeStart()) && timeEnd.after(fb.getTimeStart())
                        || timeStart.before(fb.getTimeEnd()) && timeEnd.after(fb.getTimeEnd())
                        || timeStart.equals(fb.getTimeStart()) || timeEnd.equals(fb.getTimeEnd())) {
                    result.add(fb);
                }
            }
        }
        return result;
    }

    /**
     * Checks if a given member has any other reservation during the given time
     * range.
     *
     * @param member    the Member.
     * @param timeStart the start time.
     * @param timeEnd   the end time.
     * @return true if the facility is free; false otherwise.
     */
    public static boolean isMemberFree(Member member, Timestamp timeStart, Timestamp timeEnd) {
        for (FacilityBooking fb : Database.getInstance().getFacilityBookings()) {
            if (fb.getMemberId() == member.getMemberId() && areSameDay(fb.getTimeStart(), timeStart)
                    && fb.getState().equals(FacilityBooking.STATE_VALID)) {
                if (timeStart.before(fb.getTimeStart()) && timeEnd.after(fb.getTimeStart())
                        || timeStart.before(fb.getTimeEnd()) && timeEnd.after(fb.getTimeEnd())
                        || timeStart.equals(fb.getTimeStart()) || timeEnd.equals(fb.getTimeEnd())) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean isMemberActivityFree(Member member, Timestamp timeStart, Timestamp timeEnd) {
        for (ActivityMember am : Database.getInstance().getActivityMembers()) {
        	FacilityBooking fb = am.getFacilityBooking();
            if (am.getMemberId() == member.getMemberId() && areSameDay(fb.getTimeStart(), timeStart)) {
                if (timeStart.before(fb.getTimeStart()) && timeEnd.after(fb.getTimeStart())
                        || timeStart.before(fb.getTimeEnd()) && timeEnd.after(fb.getTimeEnd())
                        || timeStart.equals(fb.getTimeStart()) || timeEnd.equals(fb.getTimeEnd())) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Date addHourToDay(Date day, int hour) {
        return new Date(day.getTime() + hour * 1000 * 3600);
    }

    public static Date addMinutesToHour(Date day, int minutes) {
        return new Date(day.getTime() + minutes * 1000 * 60);
    }

    public static boolean areSameDay(Date a, Date b) {
        Calendar calA = Calendar.getInstance();
        Calendar calB = Calendar.getInstance();
        calA.setTime(a);
        calB.setTime(b);

        return calA.get(Calendar.DAY_OF_YEAR) == calB.get(Calendar.DAY_OF_YEAR)
                && calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR);
    }

    public static boolean areSameWeek(Date a, Date b) {
        Calendar calA = Calendar.getInstance();
        Calendar calB = Calendar.getInstance();
        calA.setTime(a);
        calB.setTime(b);

        return calA.get(Calendar.WEEK_OF_YEAR) == calB.get(Calendar.WEEK_OF_YEAR)
                && calA.get(Calendar.YEAR) == calB.get(Calendar.YEAR);
    }

    public static Date getCurrentTime() {
        return new Date(System.currentTimeMillis());
    }

    public static Date getCurrentDate() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getCurrentWeek() {
        Calendar c = Calendar.getInstance();
        c.setTime(getCurrentDate());
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return c.getTime();
    }

    public static Date getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(getCurrentDate());
        c.set(Calendar.DAY_OF_MONTH, 1);
        //c.add(Calendar.MONTH, 1);
        return c.getTime();
    }

    /**
     * Extracts the date from a given time. It means that returns exactly the beginning of that day.
     */
    public static Date getDateFromTime(Date d) {
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /**
     * Añade un pago al fee del mes dado si aun no hay fee para este mes lo
     * crea
     *
     * @param pago
     * @param month
     * @param member_id
     */
    /*public static void addFeeItem(FeeItem pago, java.sql.Date month, int member_id) {
        Fee thisFee = null;
        try {
            thisFee = Database.getInstance().getFeeByMember(member_id, month.getMonth());
            // existe, entonces se añade sin mas
            thisFee.getFeeItems().add(pago);

        } catch (RuntimeException ex) {
            // no existe, se crea
            thisFee = new Fee(member_id, month, Member.fee_base);
            Database.getInstance().getFees().add(thisFee);
            try {
                thisFee.create();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                pago.create();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }*/
}
