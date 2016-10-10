package ips.administrator;

import ips.gui.Form;

import javax.swing.*;

import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BookForCenterDialog extends JDialog {

	private static final long serialVersionUID = 8497586255693077533L;
	private Date date;
	private int hourStart;
	private int hourEnd;
	private int id = 0;

	private JDateChooser dateChooser;
	private JComboBox hourBox1;
	private JComboBox hourBox2;

	public BookForCenterDialog(JFrame owner, Date date, int hourStart,
			int hourEnd) {
		super(owner, true);
		setResizable(false);

		this.date = date;

		this.hourStart = hourStart;
		this.hourEnd = hourEnd;

		JPanel content = new JPanel();
		content.setLayout(new BorderLayout());
		setContentPane(content);

		Form form = new Form();
		content.add(form.getPanel(), BorderLayout.CENTER);

		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnConfirm.setMnemonic('C');
		content.add(btnConfirm, BorderLayout.SOUTH);

		JComboBox comboBox = new JComboBox();

		content.add(comboBox, BorderLayout.NORTH);

		addForm(form, date == null);

		pack();
		setLocationRelativeTo(owner);

	}

	private void addForm(Form form, boolean addDate) {

		if (addDate) {
			dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
			dateChooser.setCalendar(Calendar.getInstance());
			form.addLine(new JLabel("Date:"), dateChooser);
		}

		selectStartTime(form);

	}

	private void selectStartTime(Form form) {
		hourBox2.setEnabled(true);
		hourBox1 = new JComboBox();
		hourBox2 = new JComboBox();

		for (int i = 0; i < 24; i++) {
			hourBox1.addItem(i + ":00");
		}

		hourBox1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				hourStart = hourBox1.getSelectedIndex();
				selectEndTime(form);

			}
		});

		form.addLine(new JLabel("Start hour: "), hourBox1);
	}

	protected void selectEndTime(Form form) {
		hourBox2.setEnabled(true);
		for (int i = hourEnd + 1; i < 24; i++) {
			hourBox1.addItem(i + ":00");
		}
		hourBox1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				hourStart = hourBox1.getSelectedIndex();
				selectEndTime(form);
			}
		});

	}

}
