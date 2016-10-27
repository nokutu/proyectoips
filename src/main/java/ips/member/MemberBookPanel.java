package ips.member;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.database.Member;
import ips.gui.Form;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The member makes a booking for himself.
 */
public class MemberBookPanel extends JPanel {

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

        addForm(timeStart == null);

        c.anchor = GridBagConstraints.LINE_END;
        c.insets = new Insets(0, 0, 0, 5);
        c.gridy = 1;
        confirm = new JButton("Reservar");
        confirm.addActionListener(this::confirm);
        add(confirm, c);
    }

    private void addForm(boolean addExtra) {
        if (addExtra) {
            JDateChooser dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setDate(Utils.getCurrentDate());
            form.addLine(new JLabel("Fecha:"), dateChooser);

            JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
            JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));

            form.addLine(new JLabel("Hora de inicio:"), hourStartSpinner);
            form.addLine(new JLabel("Hora de fin:"), hourEndSpinner);

            JComboBox<String> facilities = new JComboBox<>();
            List<String> names = Database.getInstance().getFacilities().stream().map(Facility::getFacilityName).collect(Collectors.toList());
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
            names.forEach(model::addElement);
            facilities.setModel(model);
            form.addLine(new JLabel("Instalación:"), facilities, false
            );
        }

        JComboBox<String> paymentCombo = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cash", "Fee"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("Método de pago:"), paymentCombo);
    }

    private void confirm(ActionEvent actionEvent) {
        FacilityBooking fb = createBooking();

        if (checkValid(fb) & fb != null) {
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
        int facilityId;
        int memberId;
        Timestamp timeStart;
        Timestamp timeEnd;
        String paymentMethod;

        try {
            List<String> results = form.getResults();
            if (this.timeStart == null) {
                timeStart = new Timestamp(Utils.addHourToDay(
                        new Timestamp(Long.parseLong(results.get(0))),
                        Integer.parseInt(results.get(1))).getTime());
                timeEnd = new Timestamp(Utils.addHourToDay(
                        new Timestamp(Long.parseLong(results.get(0))),
                        Integer.parseInt(results.get(2))).getTime());
                facilityId = Database.getInstance().getFacilities().get(Integer.parseInt(results.get(3))).getFacilityId();
                memberId = MemberMainScreen.userID;
                paymentMethod = results.get(4);
            } else {
                facilityId = facility.getFacilityId();
                timeStart = this.timeStart;
                timeEnd = this.timeEnd;
                memberId = MemberMainScreen.userID;
                paymentMethod = results.get(0);
            }

            return new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false, false);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean checkValid(FacilityBooking fb) {
        boolean valid = true;
        String errors = "\n";

        if (fb != null) {
            if (fb.getTimeStart().before(Utils.getCurrentTime())) {
                valid = false;
                errors += "No puedes reservar para el pasado.\n";
            } else if (fb.getTimeStart().after(Utils.addHourToDay(Utils.getCurrentTime(), 24 * 15))) {
                valid = false;
                errors += "Solo puedes reservar hasta 15 días en adelante.\n";
            }
            if (fb.getTimeStart().before(Utils.addHourToDay(Utils.getCurrentTime(), 1))) {
                valid = false;
                errors += "Tienes que reservar con una hora de antelación.\n";
            }
            if (fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() > 2 * 3600 * 1000 ||
                    fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() <= 0) {
                // More than 2 hours
                valid = false;
                errors += "Un socio solo puedo reservar un máximo de 2 horas.\nEl tiempo de finalización debe ser posterior al de inicio.\n";
            }

            Optional<Member> member = Database.getInstance().getMembers().stream()
                    .filter((m) -> m.getMemberId() == fb.getMemberId()).findAny();
            if (!member.isPresent()) {
                // Member not valid
                errors += "Id de miembro no válida.\n";
                valid = false;
            } else if (!Utils.isMemberFree(member.get(), fb.getTimeStart(), fb.getTimeEnd())) {
                errors += "Ya tienes una reserva en esta franja de tiempo.\n";
                valid = false;
            }

            Optional<Facility> of = Database.getInstance().getFacilities().stream()
                    .filter((f) -> f.getFacilityId() == fb.getFacilityId()).findAny();
            assert of.isPresent();

            if (!Utils.isFacilityFree(of.get(), fb.getTimeStart(), fb.getTimeEnd())) {
                // Facility not free
                valid = false;
                errors += "La instalación está ocupada en la horas seleccionadas.\n";
            }
        } else {
            errors += "Por favor, rellena todos los campos.\n";
            valid = false;
        }

        if (!valid) {
            form.setError(errors);
            setSize(getWidth(), getPreferredSize().height);
        }

        return valid;
    }
}