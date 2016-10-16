package ips.administrator;

import javax.swing.*;

import ips.MainWindow;
import ips.database.Database;
import ips.database.FacilityBooking;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import java.awt.Color;

public class CurrentViewDialog extends JDialog
{
	private JButton btnBack;
	private JTextPane textBookings;
	private JPanel panel;

	public CurrentViewDialog(JFrame owner) 
	{
		super(owner,true);
		getContentPane().setLayout(null);
		getContentPane().add(getBtnBack());
		getContentPane().add(getPanel());
		
		pack();
        setLocationRelativeTo(owner);
		
	}
	private JButton getBtnBack() {
		if (btnBack == null) {
			btnBack = new JButton("Back");
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			btnBack.setBounds(164, 227, 89, 23);
		}
		return btnBack;
	}
	private JTextPane getTextBookings() {
		if (textBookings == null) {
			textBookings = new JTextPane();
			textBookings.setText(getCurrentFacilities());
			textBookings.setEditable(false);
		}
		return textBookings;
	}
	
	private String getCurrentFacilities()
	{
		String line="";
		boolean found=false;
		Date date =new Date();
		Timestamp fecha = new Timestamp(date.getTime());
		List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
	      for( FacilityBooking f : bookings)
	      {
            if(fecha.after(f.getTimeEnd())||fecha.before(f.getTimeStart()))
            {
            	found=true;
          	  line+=f.toString();
            }
	      }
	      
	      if(!found)
	      {
	    	  line="There are no facilities being used at the moment \n";
	      }
		return line;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setBorder(new TitledBorder(null, "Facilities in use", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
			panel.setBounds(92, 30, 229, 177);
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getTextBookings());
		}
		return panel;
	}
}
