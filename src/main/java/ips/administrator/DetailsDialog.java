package ips.administrator;


import javax.swing.*;

import ips.database.FacilityBooking;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DetailsDialog extends JDialog
{
	FacilityBooking book;
	private JScrollPane scrollPane;
	private JButton btnBack;
	private JTextArea textDescription;

	public DetailsDialog(JFrame owner,FacilityBooking book)
	{
		super(owner,true);
		this.book = book;
		getContentPane().add(getScrollPane(), BorderLayout.CENTER);
		getContentPane().add(getBtnBack(), BorderLayout.SOUTH);
	}
	
	
	
	
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setViewportView(getTextDescription());
		}
		return scrollPane;
	}
	private JButton getBtnBack() {
		if (btnBack == null) {
			btnBack = new JButton("Back");
			btnBack.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
		}
		return btnBack;
	}
	private JTextArea getTextDescription() {
		if (textDescription == null) {
			textDescription = new JTextArea();
			textDescription.setText(book.toString());
		}
		return textDescription;
	}
}
