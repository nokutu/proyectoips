package ips.administrator.activities;

import ips.database.Activity;
import ips.database.ActivityBooking;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.utils.Utils;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.demo.DateChooserPanel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 14/11/2016.
 */
public class AdministratorActivityCancelDialog extends JDialog {

    private final JComboBox<String> activities;
    private JCheckBox option1;
    private JCheckBox desdeFecha;
    private JDateChooser dateSelecter;
    private List<Activity> activityList = null;

    public AdministratorActivityCancelDialog(JDialog owner) {
        super(owner, true);

        JPanel content = new JPanel(new GridBagLayout());
        setContentPane(content);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 20, 10, 5);

        activities = new JComboBox<>();
        option1=new JCheckBox();
        option1.setSelected(false);
        refreshActivities();

        content.add(new JLabel("Actividad:"), c);

        c.gridx++;
        c.insets = new Insets(10, 5, 10, 20);

        content.add(activities, c);
        
        option1.setText("Eliminar reservas futuras");
        content.add(option1,c);

        c.gridx = 0;
        c.gridy = 1;

        c.insets = new Insets(10, 20, 10, 20);
        content.add(getDesdeFecha(),c);
        

        c.gridx = 1;
        c.gridy = 1;

//        GridBagConstraints d = new GridBagConstraints();
//		d.insets = new Insets(0, 0, 0, 5);
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = 4;
		c.insets = new Insets(10, 20, 10, 20);
        content.add(getDateSelecter(),c);
        
        
        c.gridx = 0;
        c.gridy = 2;

        c.gridwidth = 2;
        c.insets = new Insets(10, 20, 10, 20);

        JButton cancel = new JButton("Cancelar");
        cancel.addActionListener(l -> {
            int res = JOptionPane.showConfirmDialog(this, "¿Estás seguro de querer cancelar la actividad?", "", JOptionPane.YES_NO_OPTION);

            if (res == JOptionPane.YES_OPTION) {
            	cancelActivity();
                try {
                    activityList.get(activities.getSelectedIndex()).update();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                refreshActivities();
            }
        });
        content.add(cancel, c);

        pack();
        setLocationRelativeTo(owner);
    }

    private void cancelActivity() {
    	Activity activity = activityList.get(activities.getSelectedIndex());
    	activity.setDeleted(true);
    	
        //cancelar las reservas a partir de una fecha seleccionada
    	if(desdeFecha.isSelected()){
    		// obtener las reservas asociadas a la actividad pertienente y recorrerlas
    		for(ActivityBooking ab:activity.getActivityBookings())
    		{
        		FacilityBooking f = ab.getFacilityBooking();
        		if(f.getTimeStart().after(dateSelecter.getDate())){
        			f.setState(FacilityBooking.STATE_CANCELLED); // TODO confirmar si estado cancelado o anulado(?)
					f.setCancellationCause("la actividad " + activity.getActivityName()
							+ " ha sido cancelada a partir de la fecha: " + dateSelecter.getDate().toString());
        			f.setCancellationDate(new Timestamp(Utils.getCurrentTime().getTime()));
        			try{
        				f.update();
        			}catch(SQLException ex){
        				ex.printStackTrace();
        			}
        		}
        	}
    	}
        //cancelar las reservas futuras
    	else if(option1.isSelected())
        {
        	for(ActivityBooking ac:activity.getActivityBookings())
        	{
        		FacilityBooking f = ac.getFacilityBooking();
        		if(f.getTimeStart().after(Utils.getCurrentTime()))
        		{
        		
        			f.setCancellationCause("Cancelacion debido a la cancelacion de la actividad a impartir en esta reserva");
        			f.setCancellationDate(new Timestamp(Utils.getCurrentTime().getTime()));
        		}
        	}
        }
        
	}

	private void refreshActivities() {
        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        activityList = Database.getInstance().getActivities().stream()
                .filter(a -> !a.isDeleted())
                .collect(Collectors.toList());
        activityList.stream().map(Activity::getActivityName).forEach(activitiesModel::addElement);
        activities.setModel(activitiesModel);
    }
    
	private JCheckBox getDesdeFecha() {
		if (desdeFecha == null) {
			desdeFecha = new JCheckBox("Cancelar reservas a partir de:");
	        desdeFecha.setSelected(false);
	        desdeFecha.addActionListener(l->{
	        	if(desdeFecha.isSelected())
	        		getDateSelecter().setEnabled(true);
	        	else
	        		getDateSelecter().setEnabled(false);
	        });
		}
		return desdeFecha;
	}
	/*
	 * private JButton getBtnFecha() { if (btnFecha == null) { btnFecha = new
	 * JButton("FECHA"); } return btnFecha; }
	 */

	private JDateChooser getDateSelecter() {
		if (dateSelecter == null) {
			dateSelecter = new JDateChooser("dd/MM/yyyy", "", '_');
			dateSelecter.setDate(Utils.getCurrentDate());
			dateSelecter.setEnabled(false);
		}
		return dateSelecter;
	}
	
	
}