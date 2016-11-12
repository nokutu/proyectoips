package ips.administrator;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;

import com.toedter.calendar.JDateChooser;

import ips.Utils;
import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.database.Monitor;
import ips.gui.Form;

public class CreateMonitorDialog extends JDialog 
{
	 private Form form;
	private JButton createMonitor;
	private JButton back;
	private Monitor monitor;

	public CreateMonitorDialog(JFrame owner) {
	        super(owner, true);

	        JPanel content = new JPanel();
	        content.setLayout(new BorderLayout());
	        setContentPane(content);

	        form = new Form();
	        createButtons();

	        content.add(form.getPanel(), BorderLayout.CENTER);

	        addForm();

	        addButtons(content);
	        pack();
	        setLocationRelativeTo(owner);
	    }

	    private void createButtons() {
	        createMonitor = new JButton("Crear Monitor");
	        createMonitor.addActionListener(this::confirm);
	        back = new JButton("Atras");
	        back.addActionListener(this::cancel);
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
	        buttons.add(createMonitor, c);
	        c.insets = new Insets(20, 5, 10, 10);
	        c.gridx = 1;
	        buttons.add(back, c);
	    }
	    
	    private void cancel(ActionEvent actionEvent) {
	        dispose();
	    }

	    

	    private void confirm(ActionEvent actionEvent) 
	    {
	    		getMonitor();
	            if(monitor!=null)
	            {
	            	if (alreadyExits()) 
	            	{
	            		JOptionPane.showMessageDialog(null,"El Id del monitor ya esta asignado");
	            	} 
	            	else 
	            	{
	            		try {
							monitor.create();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		dispose();
	            	}
	            }
	            else
	            {
	            	JOptionPane.showMessageDialog(null,"Complete los datos");
	            }
	            
	        }

		private boolean alreadyExits() 
		{
			for(Monitor m:Database.getInstance().getMonitors())
			{
				if(m.getMonitorId()==monitor.getMonitorId())
				{
					return true;
				}
		
			}
			return false;
		} 
		
		private void addForm() {
	    	

	        form.addLine(new JLabel("ID Monitor:"), new JTextField(20));

	        form.addLine(new JLabel("Nombre del nuevo monitor:"), new JTextField(20));

	    } 	

		 private void getMonitor() {
		    	
		        List<String> results = form.getResults();
		        if(results.get(0)!=null)
		        {
		        	int newID=  Integer.parseInt(results.get(0));
		        	if(results.get(1)!=null)
		        	{
		        		String newName= results.get(1);
		        		monitor= new Monitor(newID, newName);
		        	}
		        	else
		        	{
		        		monitor=null;
		        	}
		        		
		        }
		        else
	        	{
	        		monitor=null;
	        	}
		        
		        
		        

		    }

	

}
