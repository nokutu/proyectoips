package ips.administrator;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JSplitPane;
import javax.swing.JList;
import javax.swing.JTable;

public class availabilityOfFacilities extends JPanel{
	public availabilityOfFacilities() {
		setLayout(new BorderLayout(0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		
		JPanel pnFacilities = new JPanel();
		splitPane.setLeftComponent(pnFacilities);
		
		JList listFacilities = new JList();
		pnFacilities.add(listFacilities);
		
		JPanel pnSchedule = new JPanel();
		splitPane.setRightComponent(pnSchedule);
		
		tableSchedule = new JTable();
		pnSchedule.add(tableSchedule);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable tableSchedule;

}
