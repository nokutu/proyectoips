package ips.member;

import com.toedter.calendar.JDateChooser;
import ips.utils.BookingUtils;
import ips.utils.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.database.Member;
import ips.gui.Form;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The member makes a booking for himself.
 */
public class MemberBookPanel extends JPanel {

    private static final int DATE = 0;
    private static final int TIME_START = 1;
    private static final int TIME_END = 2;
    private static final int FACILITy = 3;
    private static final int PAYMENT_METHOD = 4;

    private Facility facility;
    private Timestamp timeStart;
    private Timestamp timeEnd;

    private JButton confirm;

    private Form form;

    public MemberBookPanel() {
        this(null, null, null);
    }

    public MemberBookPanel(Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        this.facility = facility;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;

        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        c.gridx = 0;
        c.gridy = 0;

        form = new Form();
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(form.getPanel(), c);

        addForm();

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 0, 0, 5);
        c.gridy = 1;
        confirm = new JButton("Reservar");
        confirm.addActionListener(this::confirm);
        add(confirm, c);
    }

    private void addForm() {
        JDateChooser dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        Dimension size = dateChooser.getPreferredSize();
        size.width += 20;
        dateChooser.setPreferredSize(size);
        form.addLine(new JLabel("Fecha:"), dateChooser);
        if (timeStart != null) {
            dateChooser.setDate(Utils.getDateFromTime(timeStart));
        } else {
            dateChooser.setDate(Utils.getCurrentDate());

        }

        JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        if (timeStart != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(timeStart);
            hourStartSpinner.setValue(c.get(Calendar.HOUR_OF_DAY));
            if (timeEnd != null) {
                c.setTime(timeEnd);
                hourEndSpinner.setValue(c.get(Calendar.HOUR_OF_DAY));
            }
        }

        form.addLine(new JLabel("Hora de inicio:"), hourStartSpinner);
        form.addLine(new JLabel("Hora de fin:"), hourEndSpinner);

        JComboBox<String> facilities = new JComboBox<>();
        List<String> names = Database.getInstance().getFacilities().stream()
                .map(Facility::getFacilityName)
                .collect(Collectors.toList());
        DefaultComboBoxModel<String> facilityModel = new DefaultComboBoxModel<>();
        names.forEach(facilityModel::addElement);
        facilities.setModel(facilityModel);
        form.addLine(new JLabel("Instalaci\u00F3n:"), facilities, false);

        JComboBox<String> paymentCombo = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Efectivo", "Cuota"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("Método de pago:"), paymentCombo);
    }

    private void confirm(ActionEvent actionEvent) {
        FacilityBooking fb = createBooking();

        if (BookingUtils.checkValidMember(fb, false, form::setError) & fb != null) {
            Database.getInstance().getFacilityBookings().add(fb);
            try {
                fb.create();
                form.setMessage("Reserva realizada");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private FacilityBooking createBooking() {
        try {
            List<String> results = form.getResults();
            timeStart = new Timestamp(Utils.addHourToDay(
                    new Timestamp(Long.parseLong(results.get(DATE))),
                    Integer.parseInt(results.get(TIME_START))).getTime());
            timeEnd = new Timestamp(Utils.addHourToDay(
                    new Timestamp(Long.parseLong(results.get(DATE))),
                    Integer.parseInt(results.get(TIME_END))).getTime());
            int facilityId = Database.getInstance().getFacilities().get(Integer.parseInt(results.get(FACILITy))).getFacilityId();
            int memberId = MemberMainScreen.userID;
            String paymentMethod = FacilityBooking.translate(results.get(PAYMENT_METHOD));

            return new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
