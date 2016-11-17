package ips.administrator.debts;

import ips.utils.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PayCurrentDebt extends JDialog {


    private final Form form;

    private FacilityBooking book = null;

    private JButton confirm;
    private JButton cancel;

    private List<FacilityBooking> bookingsList;
    List<Facility> finalbook;

    private boolean empty;

    public PayCurrentDebt(JFrame owner) {
        super(owner, true);
        setResizable(false);

        createButtons();

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);

        addForm();
        addButtons(content);

        pack();
        setLocationRelativeTo(owner);
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
        buttons.add(confirm, c);
        c.insets = new Insets(20, 5, 10, 10);
        c.gridx = 1;
        buttons.add(cancel, c);
    }

    private void createButtons() {
        confirm = new JButton("Confirmar");
        confirm.addActionListener(this::confirm);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this::cancel);
    }

    private void confirm(ActionEvent actionEvent) {

        //extract the correct booking
        List<String> results = form.getResults();
        //List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
        // for( FacilityBooking f : bookings)
        //{

        book = bookingsList.get((Integer.parseInt(results.get(0))));
        int memberId = Integer.parseInt(results.get(1));//finalbook.get(Integer.parseInt(results.get(0))).getFacilityId();
              
             /* if(f.getFacilityId()==facilityId&&f.getMemberId()==memberId&&(f.getTimeEnd().after(Utils.getCurrentTime())&&f.getTimeStart().before(Utils.getCurrentTime())))
              {
            	  book=f;
              }
	      }*/

        //if there was a booking find if its currently in use
        if (book.getMemberId() == memberId) {

            //  if(book.getPaymentMethod().equals("Cash"))
            // {

            // if(!book.isPaid())
            //{
            book.setPayed(true);
            try {
                book.update();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            Recibo recibo = new Recibo(book);
            recibo.grabarRecibo();

            JOptionPane.showMessageDialog(this, "ID de socio identificada,reserva pagada \n");

            dispose();
                      /*	}
	    		  		else
	    		  		{
	    		  			JOptionPane.showConfirmDialog(this, "Esta reserva ya esta pagada \n"); 
	    		  		}
	    		  }
	    		  else
	    		  {
	    			  
	    		  }*/

        }
        //if it wasnt found notify the user
        else {
            JOptionPane.showMessageDialog(this, "ID de socio no se corresponde con la reserva \n");
        }


    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }

    private void getBookings() {

        bookingsList = Database.getInstance().getFacilityBookings().stream()
                .filter(f -> f.getTimeEnd().after(Utils.getCurrentTime()) &&
                        f.getTimeStart().before(Utils.getCurrentTime()) &&
                        f.getPaymentMethod().equals("Cash") && !f.isPaid() &&
                        f.getState().equals(FacilityBooking.STATE_VALID) && f.getMemberId() != 0)
                .collect(Collectors.toList());
    }

    private void addForm() {
        JComboBox<String> facilities = new JComboBox<>();
        getBookings();
        finalbook = new ArrayList<>();


        for (FacilityBooking fb : bookingsList) {
            for (Facility f : Database.getInstance().getFacilities()) {
                if (f.getFacilityId() == fb.getFacilityId()) {
                    finalbook.add(f);
                }
            }

        }

        List<String> names = finalbook.stream().map(Facility::getFacilityName)
                .collect(Collectors.toList());
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        names.forEach(model::addElement);
        facilities.setModel(model);

        form.addLine(new JLabel("Instalaci\u00F3n:"), facilities, false);

        form.addLine(new JLabel("ID miembro:"), new JTextField(20));

    }


}
