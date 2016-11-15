package ips.administrator.debts;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PayDebtsDialog extends JDialog {


    private List<FacilityBooking> debts;
    private FacilityBooking deudor = null;
    private final Form form;
    private JComboBox comboBox;

    private JButton back;
    private JButton cancel_debt;
    private JComboBox comboBox_1;


    public PayDebtsDialog(JFrame owner) {
        super(owner, true);

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        form = new Form();
        createButtons();

        content.add(form.getPanel(), BorderLayout.CENTER);
        getDeudores();

        if (debts.isEmpty()) {
            String errors = "\n";
            errors += "No hay deudas que pagar\n";
            form.setError(errors);
        } else {
            addForm();
        }

        addButtons(content);
        pack();
        setLocationRelativeTo(owner);
    }

    private void createButtons() {
        cancel_debt = new JButton("Cancelar deuda");
        cancel_debt.addActionListener(this::confirm);
        back = new JButton("Atras");
        back.addActionListener(this::cancel);
    }


    private void getDeudores() {
        debts = new ArrayList<>();


        List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
        for (FacilityBooking f : bookings) {
            //si no esta pagado entra en cuenta
            if (f.getPaymentMethod() != null && !f.isPaid() && f.getPaymentMethod().equals("Cash")&&f.getTimeEnd().before(Utils.getCurrentTime())) {
                debts.add(f);
            }
        }
    }

    private void addButtons(JPanel content) {
        GridBagConstraints c;

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        content.add(bottom, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        bottom.add(buttons, BorderLayout.EAST);
        buttons.add(cancel_debt, c);
        c.insets = new Insets(20, 5, 10, 10);
        c.gridx = 1;
        buttons.add(back, c);
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }


    private void confirm(ActionEvent actionEvent) {
    	
        getDeudor();
        if (deudor != null) 
        {
            boolean i = Database.getInstance().getFacilityBookings().contains(deudor);
            if (!i) 
            {
                String errors = "\n";
                errors += "Miembro no encontrado\n";
                form.setError(errors);
            } 
            else 
            {
            	deudor.setPayed(true);
   			  try {
                       deudor.update();
                   } catch (SQLException e1) {
                       e1.printStackTrace();
                   }
   			  Recibo recibo=new Recibo(deudor);
   			  recibo.grabarRecibo();
            }


            dispose();
        } else {
        	JOptionPane.showMessageDialog(null,"Escoja una reserva antes");
        }

    }

    private void getDeudor() {
    	
        List<String> results = form.getResults();
        Timestamp timeStart = new Timestamp(Utils.addHourToDay(
                new Timestamp(Long.parseLong(results.get(0))),
                Integer.parseInt(results.get(1))).getTime());
        Timestamp timeEnd = new Timestamp(Utils.addHourToDay(
                new Timestamp(Long.parseLong(results.get(0))),
                Integer.parseInt(results.get(2))).getTime());
        int facilityId = Integer.parseInt(results.get(3));
        int memberId = Integer.parseInt(results.get(4));

        for (FacilityBooking f : debts) {
            if (f.getFacilityId() == facilityId && f.getTimeEnd().equals(timeEnd) && f.getTimeStart().equals(timeStart) && f.getMemberId() == memberId) {
                deudor = f;
            }
        }


    }

    private void addForm() {
    	
        JDateChooser dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
        dateChooser.setDate(Utils.getCurrentDate());
        form.addLine(new JLabel("Fecha:"), dateChooser);

        JSpinner hourStartSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        form.addLine(new JLabel("Hora de inicio:"), hourStartSpinner);

        //JSpinner hourEndSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        //form.addLine(new JLabel("Hora de fin:"), hourEndSpinner);

        form.addLine(new JLabel("ID instalacion:"), new JTextField(20));

        form.addLine(new JLabel("ID miembro:"), new JTextField(20));

    }

}
