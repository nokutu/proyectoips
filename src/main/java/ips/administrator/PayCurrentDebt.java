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
	        confirm = new JButton("Confirm");
	        confirm.addActionListener(this::confirm);
	        cancel = new JButton("Cancel");
	        cancel.addActionListener(this::cancel);
	    }
	  
	  private void confirm(ActionEvent actionEvent) 
	  {
		  Date date =new Date();
		  Timestamp fecha = new Timestamp(date.getTime());
		 
		  
          if(fecha.after(book.getTimeEnd())||fecha.before(book.getTimeStart()))
          {
        	String errors = "\n";
  			errors += "This faculty is not in use\n";
  			form.setError(errors);
          }
          else
          {
        	  
          int i=Database.getInstance().getFacilityBookings().indexOf(book);
          if(i==-1)
          {
        	  Database.getInstance().getFacilityBookings().get(i).setPayed(true);
        	  Recibo recibo=new Recibo(Database.getInstance().getFacilityBookings().get(i));
        	  recibo.grabarRecibo();
          }
          
          dispose();
          }
      

          
	    }
	  
	private void cancel(ActionEvent actionEvent) {
	        dispose();
	    }

	private void addForm() 
	{
       

            form.addLine(new JLabel("Facility ID:"), new JTextField(20));
	}
	
	private boolean valid()
	{
		return true;
		
	}
}
