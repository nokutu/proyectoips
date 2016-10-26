package ips.administrator;

import ips.database.Activity;
import ips.database.ActivityBooking;
import ips.database.Database;

import javax.swing.JDialog;
import javax.swing.JComboBox;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class ListActivityMembersDialog extends JDialog{
	private JComboBox combActivity;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JList listClases;
	private Activity activity;
	private JScrollPane scrollPane_1;
	private JList listAsistentes;
	private JPanel panel_1;
	public ListActivityMembersDialog() {
		getContentPane().add(getPanel(), BorderLayout.NORTH);
		getContentPane().add(getScrollPane(), BorderLayout.EAST);
		getContentPane().add(getScrollPane_1(), BorderLayout.WEST);
		getContentPane().add(getPanel_1(), BorderLayout.CENTER);
	}
	

	private JComboBox getCombActivity() {
		if (combActivity == null) {
			combActivity = new JComboBox();
			combActivity.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					activity=Database.getInstance().getActivities().get(combActivity.getSelectedIndex());
					//actualiza lista de clases
					getClassList();
				}
				
				
			});
			List<String> names= new ArrayList<>();
			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
			for(Activity ac: Database.getInstance().getActivities())
			{
				names.add(ac.getActivityName()); 
			}
			names.forEach(model::addElement);
			combActivity.setModel(model);
		}
		return combActivity;
	}
	private JPanel getPanel() {
		if (panel == null) {
			panel = new JPanel();
			panel.setLayout(new BorderLayout(0, 0));
			panel.add(getCombActivity());
		
		}
		return panel;
	}
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Seleccione un socio", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
			scrollPane.setViewportView(getListClases());
		}
		return scrollPane;
	}
	private JList getListClases() {
		if (listClases == null) 
		{
			listClases = new JList();
			getClassList();
		}
		return listClases;
	}
	
	private void getClassList()
	{
		DefaultListModel model=new DefaultListModel();
		if(activity!=null)
		{
			for(ActivityBooking ab: Database.getInstance().getActivityBookings())
			{
				if(activity.getActivityName().equals(ab.getActivityName()))
				{
					String line="Fecha: "+ab.getBookingTimeStart().getDay()+"/"+ ab.getBookingTimeStart().getMonth()+"/"+ab.getBookingTimeStart().getYear() +"Hora:"+ab.getBookingTimeStart().getHours();
					model.addElement(line);
				}
			}
		}
		listClases.setModel(model);
	}
	private JScrollPane getScrollPane_1() {
		if (scrollPane_1 == null) {
			scrollPane_1 = new JScrollPane();
			scrollPane_1.setViewportBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Seleccione una clase", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
			scrollPane_1.setViewportView(getListAsistentes());
		}
		return scrollPane_1;
	}
	
	private void getAsisList()
	{
		DefaultListModel model=new DefaultListModel();
		if(activity!=null)
		{
			for(ActivityBooking ab: Database.getInstance().getActivityBookings())
			{
				if(activity.getActivityName().equals(ab.getActivityName()))
				{
					String line="Fecha: "+ab.getBookingTimeStart().getDay()+"/"+ ab.getBookingTimeStart().getMonth()+"/"+ab.getBookingTimeStart().getYear() +"Hora:"+ab.getBookingTimeStart().getHours();
					model.addElement(line);
				}
			}
		}
		listAsistentes.setModel(model);
	}
	private JList getListAsistentes() {
		if (listAsistentes == null) {
			listAsistentes = new JList();
			DefaultListModel model=new DefaultListModel();
			listAsistentes.setModel(model);
		}
		return listAsistentes;
	}
	private JPanel getPanel_1() {
		if (panel_1 == null) {
			panel_1 = new JPanel();
		}
		return panel_1;
	}
}
