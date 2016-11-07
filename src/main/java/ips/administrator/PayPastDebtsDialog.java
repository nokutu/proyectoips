package ips.administrator;

import ips.MainWindow;
import ips.Utils;
import ips.database.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class PayPastDebtsDialog extends JDialog {

    private JList<String> bookList = new JList();

    private List<Facility> facilitiesList=new ArrayList();
    private List<FacilityBooking> selectedByFacilitysList;

    private JLabel assistanceLabel = new JLabel("");
    private JComboBox<String> facilities;

	private List<FacilityBooking> bookingsList;

    public PayPastDebtsDialog() {
        super(MainWindow.getInstance(), true);

        setLayout(new BorderLayout());

        createLeftPanel();
        createCenterPanel();
        createBottomPanel();

        
        setMinimumSize(new Dimension(620, 320));
        setVisible(true);
        pack();
        setLocationRelativeTo(MainWindow.getInstance());
    }


    private void createBottomPanel() {
    	JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        bottomPanel.setLayout(new FlowLayout());
        
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        JButton pagar=new JButton("Pagar");
        pagar.addActionListener(l->{
        	FacilityBooking fb = selectedByFacilitysList.get(bookList.getSelectedIndex());
        	fb.setPayed(true);
 			  try {
                     fb.update();
                 } catch (SQLException e1) {
                     e1.printStackTrace();
                 }
 			  Recibo recibo=new Recibo(fb);
 			  recibo.grabarRecibo();
 			 refreshCentralPanelList();
        });
     
        bottomPanel.add(pagar);
        
        
        JButton atras=new JButton("Atras");
        atras.addActionListener(l->{
        	dispose();
        });
        
        bottomPanel.add(atras);
        
		
	}


	private void createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(leftPanel, BorderLayout.WEST);

        leftPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 10, 2, 5);

        leftPanel.add(new JLabel("Actividad:"), c);

        c.gridx++;

        facilities = new JComboBox<>();
        DefaultComboBoxModel<String> facilitiesModel = new DefaultComboBoxModel<>();
        bookingsList = Database.getInstance().getFacilityBookings().stream()
                .filter(f ->f.getTimeEnd().before(Utils.getCurrentTime())&& f.getPaymentMethod().equals("Cash")&&!f.isPaid()&&!f.isDeletedFlag()&&f.getMemberId()!=0)
                .collect(Collectors.toList());
        for(FacilityBooking fb: bookingsList)
        {
        	for(Facility f: Database.getInstance().getFacilities())
        	{
        		if(f.getFacilityId()==fb.getFacilityId()&&!facilitiesList.contains(f))
        		{
        			facilitiesList.add(f);
        		}
        	}
        }
        facilitiesList.forEach(a -> facilitiesModel.addElement(a.getFacilityName()));
        facilities.setModel(facilitiesModel);
        leftPanel.add(facilities, c);

      
        facilities.addActionListener(l -> {
        	
        	 refreshCentralPanelList();
           /* bookList.removeAll();
            DefaultComboBoxModel<String> sessionsModel = new DefaultComboBoxModel<>();
            sessionsList = Database.getInstance().getActivityBookings().stream()
                    .filter(ab -> ab.getActivityId() == getSelectedActivity().getActivityId())
                    .collect(Collectors.toList());
            sessionsList.stream()
                    .map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
                    .forEach(sessionsModel::addElement);
            sessions.setModel(sessionsModel);*/
           
        });
        facilities.setSelectedIndex(0);

    }

    private void refreshCentralPanelList() {
    	refreshArrays();
        bookList.removeAll();
        selectedByFacilitysList = new ArrayList<>();
        DefaultListModel<String> d= new DefaultListModel<>();
        for (FacilityBooking fb : bookingsList) 
        {
            if (fb.getFacilityId() == getSelectedFacility().getFacilityId()) 
            {
                selectedByFacilitysList.add(fb);

                
                d.addElement("ID de socio: "+fb.getMemberId()+" fecha: "+fb.getTimeStart().toLocalDateTime());
                bookList.setModel(d);
            }
        }

        // Force the panel to refresh
        SwingUtilities.invokeLater(() -> {
            bookList.setVisible(false);
            bookList.setVisible(true);
        });
    }
    
    
    private void refreshArrays()
    {
    	bookingsList= new ArrayList();
    	facilitiesList= new ArrayList();
    	DefaultComboBoxModel<String> facilitiesModel = new DefaultComboBoxModel<>();
        bookingsList = Database.getInstance().getFacilityBookings().stream()
                .filter(f ->f.getTimeEnd().before(Utils.getCurrentTime())&& f.getPaymentMethod().equals("Cash")&&!f.isPaid()&&!f.isDeletedFlag()&&f.getMemberId()!=0)
                .collect(Collectors.toList());
        for(FacilityBooking fb: bookingsList)
        {
        	for(Facility f: Database.getInstance().getFacilities())
        	{
        		if(f.getFacilityId()==fb.getFacilityId()&&!facilitiesList.contains(f))
        		{
        			facilitiesList.add(f);
        		}
        	}
        }
        if(facilitiesList.isEmpty())
        {
        	dispose();
        }
        facilitiesList.forEach(a -> facilitiesModel.addElement(a.getFacilityName()));
        facilities.setModel(facilitiesModel);
    }


    private void createCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        JLabel colorlabel = new JLabel();
        colorlabel.setText("Lista de reservas con deudas efectivas:");

        centerPanel.add(colorlabel, BorderLayout.NORTH);
        centerPanel.add(bookList, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private Facility getSelectedFacility() {
        return facilitiesList.get(facilities.getSelectedIndex());
    }

 
}
