package ips;

import ips.database.Availability;
import ips.database.Booking;
import ips.database.Database;
import ips.database.Facility;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class AvailabilityPane extends JPanel {
	private boolean admin;
	private JLabel lblWeek;
	private JLabel lblFacility;
	ArrayList<Booking> bookings;
	private int instalacion = -1;
	private int weeksFromNow = 0;
	JButton btnNext = new JButton(">");
	JButton btnPrevious = new JButton("<");
	JPanel centralWeekPanel = new JPanel();
	private static final long serialVersionUID = 1L;
	private Calendar calendar = Calendar.getInstance();
	private String userName = "Administrator";

	public AvailabilityPane(boolean admin, String userName) {
		this.admin = admin;
		if(userName != null)
			this.userName = userName;
		setLayout(new BorderLayout(0, 0));

		JPanel weekPane = new JPanel();
		add(weekPane, BorderLayout.NORTH);

		JLabel lblAvailable = new JLabel("Facilities Availability. Select week.");
		weekPane.add(lblAvailable);
		lblAvailable.setHorizontalAlignment(SwingConstants.CENTER);

		btnPrevious.setEnabled(false);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				weeksFromNow--;
				setWeek();
				nextPreviousButtons();
			}
		});
		weekPane.add(btnPrevious);

		lblWeek = new JLabel("empty");
		weekPane.add(lblWeek);
		setWeek();

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weeksFromNow++;
				setWeek();
				nextPreviousButtons();
			}
		});
		weekPane.add(btnNext);

		JScrollPane buttonScrollPane = new JScrollPane();
		add(buttonScrollPane, BorderLayout.WEST);

		JPanel centralPane = new JPanel();
		add(centralPane, BorderLayout.CENTER);
		centralPane.setLayout(new BorderLayout(0, 0));

		lblFacility = new JLabel();
		centralPane.add(lblFacility, BorderLayout.NORTH);
		lblFacility.setText("Select facility");

		centralPane.add(centralWeekPanel, BorderLayout.CENTER);
		centralWeekPanel.setLayout(new GridLayout(0, 8, 0, 0));

		JPanel buttonPane = new JPanel();
		GridLayout gl_buttonPane = new GridLayout(14, 0);
		buttonPane.setLayout(gl_buttonPane);

		List<Facility> facilities = Database.getInstance().getFacilities();
		for (Facility facility : facilities) {
			int a = facility.getFacilityId();
			JButton botonAux = new JButton("Instalacion: " + a);
			botonAux.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					instalacion = a;
					lblFacility.setText("Horario de la instalacion: " + a);
					addRows(a);
				}
			});
			buttonPane.add(botonAux);
		}

		buttonScrollPane.setViewportView(buttonPane);
	}

	private void addRows(int instalacion) {
		try {
			bookings = Availability.Select(instalacion);
		} catch (SQLException e) {
			System.out.println("Error en el método addrows de AvailabilityPane");
			e.printStackTrace();
		}
		centralWeekPanel.removeAll();
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, (weeksFromNow * 7));
		JPanel rowPanel = new JPanel(new GridLayout(25, 1));
		rowPanel.add(new JLabel());
		for (int i = 1; i < 25; i++) {
			rowPanel.add(new JLabel((i - 1) + ":00 - " + i + ":00"));
		}
		centralWeekPanel.add(rowPanel);
		for (int i = 0; i < 7; i++) {
			addRow();
		}
	}

	private void addRow() {
		JPanel rowPanel = new JPanel(new GridLayout(25, 1));
		Date date = calendar.getTime();
		rowPanel.add(new JLabel("" + new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime())));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		for (int i = 0; i < 24; i++) {
			date = calendar.getTime();
			long now = date.getTime();
			JButton botonAux = new JButton("Empty");
			for (Booking booking : bookings) {
				if (now >= booking.getTimeStart().getTime() && now < booking.getTimeEnd().getTime()) {
					botonAux = notAvailable(booking.getUserName());
					break;
				}
			}
			rowPanel.add(botonAux);
			calendar.add(Calendar.HOUR, +1);
		}
		centralWeekPanel.add(rowPanel);
	}

	private void setWeek() {
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, weeksFromNow * 7);
		String week = "From: " + calendar.get(Calendar.DAY_OF_MONTH) + " of " + calendar.get(Calendar.MONTH) + " to: ";
		calendar.add(Calendar.DATE, +6);
		week += calendar.get(Calendar.DAY_OF_MONTH) + " of " + calendar.get(Calendar.MONTH);
		lblWeek.setText(week);
	}

	private void nextPreviousButtons() {
		if (weeksFromNow == 0)
			btnPrevious.setEnabled(false);
		else
			btnPrevious.setEnabled(true);
		if (instalacion != -1)
			addRows(instalacion);
	}

	private JButton notAvailable(String user) {
		JButton btnNot = new JButton("Not Available");
		btnNot.setBackground(Color.RED);
		if (admin) {
			btnNot.setText(user);
			if (user.equals("Administrator")) 
				btnNot.setBackground(Color.ORANGE);
			else
				btnNot.setBackground(Color.BLUE);
		} 
		else{
			if (user.equals(userName)) 
				btnNot.setBackground(Color.GREEN);
			else
				btnNot.setBackground(Color.BLUE);
		}
		btnNot.setEnabled(false);
		return btnNot;
	}
}