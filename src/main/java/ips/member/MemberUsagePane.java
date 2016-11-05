package ips.member;

import ips.database.Booking;
import ips.database.MemberUsage;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MemberUsagePane extends JPanel {
	private JLabel lblWeek;
	ArrayList<Booking> bookings;
	private int weeksFromNow = 0;
	JButton btnNext = new JButton(">");
	JButton btnPrevious = new JButton("<");
	JPanel centralWeekPanel = new JPanel();
	private static final long serialVersionUID = 1L;
	private Calendar calendar = Calendar.getInstance();

	public MemberUsagePane(int idUser) {
		setLayout(new BorderLayout(0, 0));

		JPanel weekPane = new JPanel();
		add(weekPane, BorderLayout.NORTH);

		btnPrevious.setEnabled(false);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				weeksFromNow--;
				setWeek();
				addRows(idUser);
			}
		});
		weekPane.add(btnPrevious);

		lblWeek = new JLabel("Libre");
		weekPane.add(lblWeek);
		setWeek();

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weeksFromNow++;
				setWeek();
				addRows(idUser);
			}
		});
		weekPane.add(btnNext);

		JPanel centralPane = new JPanel();
		add(centralPane, BorderLayout.CENTER);
		centralPane.setLayout(new BorderLayout(0, 0));

		centralPane.add(centralWeekPanel, BorderLayout.CENTER);
		centralWeekPanel.setLayout(new GridLayout(0, 8, 0, 0));
		addRows(idUser);
	}

	private void addRows(int idUser) {
		try {
			bookings = MemberUsage.Select(idUser);
		} catch (SQLException e) {
			System.out.println("Error en el método addrows de MemberUsagePane");
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
			JButton botonAux = new JButton("No hay reservas");
			botonAux.setBackground(Color.GREEN);
			botonAux.setEnabled(false);
			for (Booking booking : bookings) {
				if (now >= booking.getTimeStart().getTime() && now < booking.getTimeEnd().getTime()) {
					botonAux = setboton(booking);
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
		String week = "Desde: " + calendar.get(Calendar.DAY_OF_MONTH) + " del " + calendar.get(Calendar.MONTH) + " hasta: ";
		calendar.add(Calendar.DATE, +6);
		week += calendar.get(Calendar.DAY_OF_MONTH) + " del " + calendar.get(Calendar.MONTH);
		lblWeek.setText(week);
	}

	private JButton setboton(Booking booking) {
		JButton btnNot = new JButton(booking.getFacilityName());
		btnNot.setEnabled(true);
		switch (booking.getState()) {
		case "Valid":
			btnNot.setBackground(Color.GREEN);
			break;
		case "Annulled":
			btnNot.setBackground(Color.GRAY);
			break;
		case "Canceled":
			btnNot.setBackground(Color.RED);
			break;
		default:
			break;
		}
		return btnNot;
	}
}