package ips.utils;

import ips.administrator.bookings.OverrideBookingDialog;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.database.Member;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by jorge on 17/11/2016.
 */
public class BookingUtils {

    public static boolean checkValidCenter(FacilityBooking fb, Consumer<String> errorOutput) {
        boolean valid = true;
        String errors = "";

        Facility facility = fb.getFacility();

        // la facility debe existir
        if (facility == null) {
            throw new IllegalStateException("La instalaci\u00F3n tiene que existir");
        }

        // invalided de los spinners
        if (fb.getTimeEnd().before(fb.getTimeStart()) || fb.getTimeStart().before(Utils.getCurrentDate())) {
            errors += "El tiempo de finalizaci\u00F3n debe ser posterior al de inicio.\n";
            valid = false;
        }

        if (!Utils.isFacilityFree(facility, fb.getTimeStart(), fb.getTimeEnd())) {
            List<FacilityBooking> reserva = Utils.getBookingsAt(facility, fb.getTimeStart(), fb.getTimeEnd());
            OverrideBookingDialog ob;
            (ob = new OverrideBookingDialog(reserva)).setVisible(true);
            valid = ob.getValid();
            if (!valid)
                errors += "La instalaci\u00F3n est\u00E1 ocupada en la horas seleccionadas.\n";
        }
        if (!valid) {
            errorOutput.accept(errors);
        }

        return valid;
    }

    public static boolean checkValidMember(FacilityBooking fb, boolean isAdmin, Consumer<String> errorOutput) {
        boolean valid = true;
        String errors = "";

        if (fb != null) {
            if (fb.getTimeStart().before(Utils.getCurrentTime())) {
                valid = false;
                errors += "No puedes reservar para el pasado.\n";
            } else if (fb.getTimeStart().after(Utils.addHourToDay(Utils.getCurrentTime(), 24 * 15))) {
                valid = false;
                errors += "Solo puedes reservar hasta 15 d\u00EDs en adelante.\n";
            }
            if (fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() > 2 * 3600 * 1000
                    || fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() <= 0) {
                // More than 2 hours
                valid = false;
                errors += "Un socio solo puedo reservar un m\u00E1ximo de 2 horas.\nEl tiempo de finalizaci\u00F3n debe ser posterior al de inicio.\n";
            }
            Member member = Member.get(fb.getMemberId());
            if (member == null || fb.getMemberId() == 0) {
                // Member not valid
                errors += "n\u00FAmero de socio no v\u00E1lida.\n";
                valid = false;
            } else if (!Utils.isMemberFree(member, fb.getTimeStart(), fb.getTimeEnd())) {
                errors += "El socio ya tiene una reserva en esta franja de tiempo.\n";
                valid = false;
            }

            if (!Utils.isMemberActivityFree(member, fb.getTimeStart(), fb.getTimeEnd())) {
                errors += "El socio está apuntado a una actividad en esta franja de tiempo.\n";
                valid = false;
            }


            Facility facility = fb.getFacility();
            if (facility != null && !Utils.isFacilityFree(facility, fb.getTimeStart(), fb.getTimeEnd())) {
                if (isAdmin) {
                    // Facility not free
                    List<FacilityBooking> reserva = Utils.getBookingsAt(facility, fb.getTimeStart(), fb.getTimeEnd());
                    OverrideBookingDialog ob;
                    (ob = new OverrideBookingDialog(reserva)).setVisible(true);
                    valid = ob.getValid();
                    if (!valid)
                        errors += "La instalaci\u00F3n est\u00E1 ocupada en la horas seleccionadas.\n";
                } else {
                    valid = false;
                    errors += "La instalaci\u00F3n est\u00E1 ocupada en la horas seleccionadas.\n";
                }
                //valid = false;
                //errors += "La instalaci\u00F3n est\u00E1 ocupada en las horas seleccionadas.\n";
            }
        } else {
            errors += "Por favor, rellena todos los campos.\n";
            valid = false;
        }

        if (!valid) {
            errorOutput.accept(errors);
            //setSize(getWidth(), getPreferredSize().height);
        }

        return valid;
    }
}
