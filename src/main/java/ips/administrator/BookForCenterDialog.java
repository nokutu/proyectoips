package ips.administrator;

import com.toedter.calendar.JDateChooser;

import ips.database.Database;
import ips.database.Facility;
import ips.database.FacilityBooking;
import ips.gui.Form;

import javax.swing.*;

import ips.Utils;
import java.sql.Date;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

public class BookForCenterDialog extends JDialog {

    private static final long serialVersionUID = 8497586255693077533L;
    
    private Facility facility;
    private Date hourStart;
    private Date hourEnd;
    private int centerId = 0;

    private JDateChooser dateChooser;
    private JSpinner hourSp1;
    private JSpinner hourSp2;
    private Form form;

    public BookForCenterDialog(JFrame owner, Facility facility, Date hourStart,
                               Date hourEnd) 
    {
        super(owner, true);
        setResizable(false);

        this.facility = facility;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;
        
        construir(owner);
    }
    
       public void construir(JFrame owner)
       {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);
        
        //form creada
        form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);
        addForm( hourStart == null);

        //botones
        
        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(this::confirm);
        btnConfirm.setMnemonic('A');    
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this::confirm);
        btnCancel.setMnemonic('C');
        

        GridBagConstraints c;

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        content.add(bottom, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        bottom.add(buttons, BorderLayout.EAST);
        
        
        buttons.add(btnConfirm, c);
        c.insets = new Insets(20, 5, 10, 10);
        c.gridx = 1;
		buttons.add(btnCancel, c);

        

        pack();
        setLocationRelativeTo(owner);

    }

    public BookForCenterDialog(JFrame owner) 
    {
    	 this(owner, null, null, null);
	}


	private void addForm(boolean addDate) {

        if (addDate) 
        {
            dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setCalendar(Calendar.getInstance());
            form.addLine(new JLabel("Date:"), dateChooser);
        }

        selectStartEndTime(form);

    }

    private void selectStartEndTime(Form form)
    {
        hourSp1 = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        form.addLine(new JLabel("Start time:"), hourSp1);
        hourSp2 =new JSpinner(new SpinnerNumberModel(0, 0, 23, 1));
        form.addLine(new JLabel("End time: "), hourSp2);
        
  
        form.addLine(new JLabel("Facility ID:"), new JTextField(20));
      }
    
    
    	public void confirm(ActionEvent arg0) 
        {
            	
                Date hourStart;
                Date hourEnd;
            	int facilityId = -1;
            	String paymentMethod="non";
                List<String> results = form.getResults();
                
                
                if (this.hourStart == null) 
                {
                    hourStart = Utils.addHourToDay(new Date(Long.parseLong(results.get(0))), Integer.parseInt(results.get(1)));
                    hourEnd = Utils.addHourToDay(new Date(Long.parseLong(results.get(0))), Integer.parseInt(results.get(2)));
                    facilityId = Integer.parseInt(results.get(3));
                } 
                else 
                {
                    facilityId = facility.getFacilityId();
                    hourStart = this.hourStart;
                    hourEnd = this.hourEnd;
                }

               
				FacilityBooking fb = new FacilityBooking(facilityId, centerId, hourStart, hourEnd, paymentMethod, false, false);
                
                Database.getInstance().getFacilityBookings().add(fb);
                
                try {
                    fb.create();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                dispose();

            }
        /*for (int i = 0; i < 24; i++) {
            hourSp1.addItem(i + ":00");
        }

        hourSp1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                //hourStart = hourSp1.getSelectedIndex();
                selectEndTime(form);

            }
        });*/

       
    

   /* protected void selectEndTime(Form form) {
        hourSp2.setEnabled(true);
        for (int i = hourEnd + 1; i < 24; i++) {
            hourSp1.addItem(i + ":00");
        }
        hourSp1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                hourStart = hourSp1.getSelectedIndex();
                selectEndTime(form);
            }
        });

    }*/

}
