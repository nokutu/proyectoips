package ips.administrator;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import ips.MainWindow;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;

import com.toedter.calendar.JDateChooser;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

public class PayCurrentDebt  extends JDialog{
	
	 
		private final Form form;

	    private FacilityBooking book=null;

	    private JButton confirm;
	    private JButton cancel;

	public PayCurrentDebt(JFrame owner) 
	{
		super(owner,true);
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
	 
	  private void createButtons() 
	  {
	        confirm = new JButton("Confirmar");
	        confirm.addActionListener(this::confirm);
	        cancel = new JButton("Cancel");
	        cancel.addActionListener(this::cancel);
	    }
	  
	  private void confirm(ActionEvent actionEvent) 
	  {
		  Date date =new Date();
		  Timestamp fecha = new Timestamp(date.getTime());
		  
		  //extract the correct booking
		  List<String> results = form.getResults();
		  List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
	      for( FacilityBooking f : bookings)
	      {
	    	  int facilityId = Integer.parseInt(results.get(0));
              int memberId = Integer.parseInt(results.get(1));
              
              
              if(f.getFacilityId()==facilityId&&f.getMemberId()==memberId&&!f.isPaid()&&f.getPaymentMethod().equals("Cash"))
              {
            	  book=f;
              }
	      }
	      
	      //if there was a booking find if its currently in use
	      if(book!=null)
	      {
		  
	    	  if(fecha.after(book.getTimeEnd())||fecha.before(book.getTimeStart()))
	    	  {
	    		  String errors = "\n";
	    		  errors += "Esta reserva no coincide con la hora actual, esta pagada o su metodo de pago no es efectivo\n";
	    		  form.setError(errors);
	    	  }
	    	  else
	    	  {
        	  
	    		  
	    			  book.setPayed(true);
	    			  try {
	                        book.update();
	                    } catch (SQLException e1) {
	                        e1.printStackTrace();
	                    }
	    			  Recibo recibo=new Recibo(book);
	    			  recibo.grabarRecibo();
	    		  
          
	    		  dispose();
	    	  }
	      }
	      //if it wasnt found notify the user
	      else
	      {
	    	  String errors = "\n";
    		  errors += "La reserva elegida no existe \n";
    		  form.setError(errors);
	      }
	    
      

          
	    }
	  
	private void cancel(ActionEvent actionEvent) {
	        dispose();
	    }

	private void addForm() 
	{
       
            form.addLine(new JLabel("ID instalacion:"), new JTextField(20));
            form.addLine(new JLabel("ID miembro:"), new JTextField(20));
	}
	
	
}
