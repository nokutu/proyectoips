package ips.administrator;

import ips.database.Activity;
import ips.database.Database;

import javax.swing.JDialog;
import javax.swing.JComboBox;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ListActivityMembersDialog extends JDialog{
	private JComboBox combActivity;
	private JPanel panel;
	private JScrollPane scrollPane;
	private JList listClases;
	private Activity activity;
	public ListActivityMembersDialog() {
		getContentPane().add(getPanel(), BorderLayout.NORTH);
		getContentPane().add(getScrollPane(), BorderLayout.CENTER);
	}
	

	private JComboBox getCombActivity() {
		if (combActivity == null) {
			combActivity = new JComboBox();
			combActivity.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					activity=Database.getInstance().getActivities().get(combActivity.getSelectedIndex());
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
			scrollPane.setViewportView(getListClases());
		}
		return scrollPane;
	}
	private JList getListClases() {
		if (listClases == null) 
		{
			listClases = new JList();
		}
		return listClases;
	}
}
