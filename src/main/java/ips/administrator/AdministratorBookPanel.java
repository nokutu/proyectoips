package ips.administrator;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.*;
import ips.gui.Form;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 24/10/2016.
 */
public class AdministratorBookPanel extends JPanel {

    private final Form form;

    private Facility facility;
    private Timestamp timeStart;
    private Timestamp timeEnd;

    private JButton confirm;
    private JDateChooser dateChooser;

    public AdministratorBookPanel() {
        this(null, null, null);
    }

    public AdministratorBookPanel(Facility facility, Timestamp timeStart, Timestamp timeEnd) {
        super();

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
            dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setDate(Utils.getCurrentDate());
            dateChooser.getDateEditor().addPropertyChangeListener("date", new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    System.out.println();
                }
            });
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
            form.addLine(new JLabel("Instalación:"), facilities, false);
        }

        JDateChooser endDateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        endDateChooser.setDate(Utils.getCurrentDate());
        endDateChooser.setEnabled(false);

        form.addSpace();

        JComboBox<String> recursive = new JComboBox<>();
        DefaultComboBoxModel<String> recursiveModel = new DefaultComboBoxModel<>(new String[]{"No repetir", "Semanalmente", "Mensualmente"});
        recursive.addActionListener((l) -> endDateChooser.setEnabled(recursive.getSelectedIndex() > 0));
        recursive.setModel(recursiveModel);
        form.addLine(new JLabel("Repetir:"), recursive, false);

        form.addLine(new JLabel("Fecha fin repetición:"), endDateChooser);

        form.addSpace();

        JCheckBox bookForCenter = new JCheckBox("Reservar para el centro");
        JTextField idTextField = new JTextField(10);
        JComboBox<String> paymentCombo = new JComboBox<>();

        JCheckBox assignToActivity = new JCheckBox("Asignar a actividad");
        JComboBox<String> activities = new JComboBox<>();

        bookForCenter.addActionListener(l -> {
            idTextField.setEnabled(!bookForCenter.isSelected());
            paymentCombo.setEnabled(!bookForCenter.isSelected());
            assignToActivity.setEnabled(bookForCenter.isSelected());
            activities.setEnabled(assignToActivity.isSelected() && bookForCenter.isSelected());
            idTextField.setText(String.valueOf(0));
        });
        form.addLine(bookForCenter);
        form.addLine(new JLabel("ID de socio:"), idTextField);

        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cash", "Fee"});
        paymentCombo.setModel(model);
        form.addLine(new JLabel("Método de pago:"), paymentCombo);

        assignToActivity.setEnabled(false);
        activities.setEnabled(false);
        assignToActivity.addActionListener(l -> activities.setEnabled(assignToActivity.isSelected()));

        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
        activities.setModel(activitiesModel);

        form.addSpace();

        form.addLine(assignToActivity, true);
        form.addLine(new JLabel("Actividad:"), activities);
    }


    private void confirm(ActionEvent actionEvent) {
        FacilityBooking fb = createBooking();

        if (Integer.parseInt(form.getResults().get(4)) == 0) {
            // No repetir
        } else {
            // Repetir
        }

        if (fb != null) {
            if ((fb.getMemberId() != 0 && checkValidMember(fb)) || (
                    fb.getMemberId() == 0 && checkValidCenter(fb))) {
                try {

                    if (Boolean.parseBoolean(form.getResults().get(8))) {
                        ActivityBooking ab = new ActivityBooking(form.getResults().get(9), fb.getFacilityId(), fb.getTimeStart());
                        Database.getInstance().getActivityBookings().add(ab);
                        ab.create();
                    }

                    Database.getInstance().getFacilityBookings().add(fb);
                    fb.create();

                    form.setMessage("Reserva creada correctamente");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private FacilityBooking createBooking() {
        int facilityId = -1;
        int memberId = -1;
        Timestamp timeStart;
        Timestamp timeEnd;
        String paymentMethod;

        try {
            List<String> results = form.getResults();
            if (this.timeStart == null) {
                timeStart = new Timestamp(
                        Utils.addHourToDay(
                                new Timestamp(Long.parseLong(results.get(0))),
                                Integer.parseInt(results.get(1))).getTime()
                );
                timeEnd = new Timestamp(Utils.addHourToDay(
                        new Timestamp(Long.parseLong(results.get(0))),
                        Integer.parseInt(results.get(2))).getTime()
                );
                facilityId = Database.getInstance().getFacilities().get(Integer.parseInt(results.get(3))).getFacilityId();
                memberId = Integer.parseInt(results.get(6));
                paymentMethod = results.get(7);
            } else {
                facilityId = facility.getFacilityId();
                timeStart = this.timeStart;
                timeEnd = this.timeEnd;
                memberId = Integer.parseInt(results.get(0));
                paymentMethod = results.get(1);
            }

            return new FacilityBooking(facilityId, memberId, timeStart, timeEnd, paymentMethod, false, false);
        } catch (NumberFormatException e) {
            form.setError("Por favor, rellena todos los campos.\n");
            setSize(getWidth(), getPreferredSize().height);
            return null;
        }
    }

    private boolean checkValidMember(FacilityBooking fb) {
        boolean valid = true;
        String errors = "";

        if (fb != null) {
            if (fb.getTimeStart().before(Utils.getCurrentTime())) {
                valid = false;
                errors += "No puedes reservar para el pasado.\n";
            } else if (fb.getTimeStart().after(Utils.addHourToDay(Utils.getCurrentTime(), 24 * 15))) {
                valid = false;
                errors += "Solo puedes reservar hasta 15 días en adelante.\n";
            }
            if (fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() > 2 * 3600 * 1000 ||
                    fb.getTimeEnd().getTime() - fb.getTimeStart().getTime() <= 0) {
                // More than 2 hours
                valid = false;
                errors += "Un socio solo puedo reservar un máximo de 2 horas. El tiempo de finalización debe ser posterior al de inicio.\n";
            }
            Optional<Member> member = Database.getInstance().getMembers().stream()
                    .filter((m) -> m.getMemberId() == fb.getMemberId()).findAny();
            if (!member.isPresent()) {
                // Member not valid
                errors += "Id de miembro no válida.\n";
                valid = false;
            } else if (!Utils.isMemberFree(member.get(), fb.getTimeStart(), fb.getTimeEnd())) {
                errors += "El socio ya tiene una reserva en esta franja de tiempo.\n";
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

    private boolean checkValidCenter(FacilityBooking fb) {
        boolean valid = true;
        String errors = "";

        Optional<Facility> facility = Database.getInstance().getFacilities()
                .stream()
                .filter((f) -> f.getFacilityId() == fb.getFacilityId())
                .findAny();

        // la facility debe existir
        if (!facility.isPresent()) {
            throw new IllegalStateException("La instalación tiene que existir");
        }

        // invalided de los spinners
        if (fb.getTimeEnd().before(fb.getTimeStart())
                || fb.getTimeStart().before(Utils.getCurrentDate())) {
            errors += "El tiempo de finalización debe ser posterior al de inicio.\n";
            valid = false;
        }

        if (!Utils.isFacilityFree(facility.get(), fb.getTimeStart(), fb.getTimeEnd())) {
            errors += "La instalación está ocupada en la horas seleccionadas.\n";
            valid = false;
        }
        form.setError(errors);

        return valid;
    }
}
