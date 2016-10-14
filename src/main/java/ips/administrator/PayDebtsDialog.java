package ips.administrator;

import com.toedter.calendar.JDateChooser;
import ips.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.awt.event.ActionListener;

public class PayDebtsDialog extends JDialog {
	
	
	private List<String> deudores;
	private List<FacilityBooking> debts ;
	private FacilityBooking deudor=null;
	private final Form form;
	private JComboBox comboBox;
	
	private JButton back;
	private JButton cancel_debt;
	private JComboBox comboBox_1;


	public PayDebtsDialog(JFrame owner) 
	{
		super(owner,true);
		
		JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);
        
        form = new Form();
		createButtons();
		
		content.add(form.getPanel(), BorderLayout.CENTER);
		getDeudores();
		
		if(deudores.isEmpty())
		{
			String errors = "\n";
			errors += "There are no debts to pay\n";
			form.setError(errors);
		}
		else
		{
		form.addLine(new JLabel("Member:"), getComboBox());
		}
		
		addButtons(content);
		pack();
	    setLocationRelativeTo(owner);
	}
	
	private void createButtons() {
        cancel_debt = new JButton("Cancel Debt");
        cancel_debt.addActionListener(this::confirm);
        back = new JButton("Back");
        back.addActionListener(this::cancel);
    }
	

	private JComboBox getComboBox() {
		if (comboBox == null) {
			comboBox = new JComboBox();
	        
	      String[] deudores1 = new String[deudores.size()];
	      deudor=(FacilityBooking) comboBox.getItemAt(0);
	      comboBox.addActionListener(this::select);
	      
	      DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(deudores.toArray(deudores1));
	      comboBox.setModel(model);
		}
		
		return comboBox;
	}
	
	private void getDeudores()
	{
		debts = new ArrayList<>();
	      deudores= new ArrayList<>();
	      
	       
	      List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
	      for( FacilityBooking f : bookings)
	      {
	    	  //si no esta pagado entra en cuenta
          if(!f.isPaid()&&f.getPaymentMethod().equals("Cash"))
          {
          	debts.add(f);
          	deudores.add(Integer.toString(f.getMemberId()));
          }
	      }
	}
	
	private void addButtons(JPanel content) 
	{
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
		private void cancel(ActionEvent actionEvent) 
		{
        dispose();
		}
		
		private void select(ActionEvent actionEvent) 
		{
			int i=comboBox.getSelectedIndex();
			deudor=debts.get(i);
		}


	  private void confirm(ActionEvent actionEvent) 
	  {
	       if(deudor!=null)
	       {
	           int i= Database.getInstance().getFacilityBookings().indexOf(deudor);
	           if(i==-1)
	           {
	        	   System.err.println("member not found");
	           }
	           else
	           {
	           Database.getInstance().getFacilityBookings().get(i).setPayed(true);
	           }
	       

	           dispose();
	       }
	       else
	       {
	    	   System.err.println("Select a member before");
	       }
	        
	    }

}
