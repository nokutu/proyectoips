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
	int userID = 0;
	MainScreen MS;
	List<Facility> facilities;

	public AvailabilityPane(boolean admin, int userID, MainScreen mainScreen) {
		MS = mainScreen;
		this.admin = admin;
		if(userID != 0)
			this.userID = userID;
		setLayout(new BorderLayout(0, 0));

		JPanel weekPane = new JPanel();
		add(weekPane, BorderLayout.NORTH);

		JLabel lblAvailable = new JLabel("Disponibilidad de las instalaciones. Seleccione Semana.");
		weekPane.add(lblAvailable);
		lblAvailable.setHorizontalAlignment(SwingConstants.CENTER);

		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				weeksFromNow--;
				setWeek();
				nextPreviousButtons();
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
		lblFacility.setText("Seleccione Instalación");

		centralPane.add(centralWeekPanel, BorderLayout.CENTER);
		centralWeekPanel.setLayout(new GridLayout(0, 8, 0, 0));

		JPanel buttonPane = new JPanel();
		GridLayout gl_buttonPane = new GridLayout(14, 0);
		buttonPane.setLayout(gl_buttonPane);

		facilities = Database.getInstance().getFacilities();
		for (Facility facility : facilities) {
			int a = facility.getFacilityId();
			String b = facility.getFacilityName();
			JButton botonAux = new JButton("Instalacion: " + b);
			botonAux.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					instalacion = a;
					lblFacility.setText("Horario de la instalacion: " + b);
					addRows(a);
					repaint();
					revalidate();
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
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		rowPanel.add(new JLabel("" + new SimpleDateFormat("EEEE", new Locale("es", "ES")).format(date.getTime())));

		for (int i = 0; i < 24; i++) {
			date = calendar.getTime();
			long now = date.getTime();
			JButton botonAux = new JButton("Libre");
			for (Booking booking : bookings) {
				if (now >= booking.getTimeStart().getTime() && now < booking.getTimeEnd().getTime()) {
					botonAux = setColor(booking);
					break;
				}
			}/*
			botonAux.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					long oneHour = now + 3600000;
					Facility esta = null;
					for (Facility facility : facilities) {
						if (facility.getFacilityId() == instalacion) {
							esta = facility;
							break;
						}
					}
					MS.setRightPanel(new AdministratorBookPanel(esta, new Timestamp(now), new Timestamp(oneHour)));
				}
			});*/
			rowPanel.add(botonAux);
			calendar.add(Calendar.HOUR, +1);
		}
		centralWeekPanel.add(rowPanel);
	}

	private void setWeek() {
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, weeksFromNow * 7);
		String week = "Desde: " + calendar.get(Calendar.DAY_OF_MONTH) + " de " + calendar.get(Calendar.MONTH) + " hasta: ";
		calendar.add(Calendar.DATE, +6);
		week += calendar.get(Calendar.DAY_OF_MONTH) + " de " + calendar.get(Calendar.MONTH);
		lblWeek.setText(week);
	}

	private void nextPreviousButtons() {
		if (instalacion != -1)
			addRows(instalacion);
	}

	private JButton setColor(Booking booking) {
		JButton btnNot = new JButton("No Disponible");
		btnNot.setBackground(Color.RED);
		if (admin) {
			String user = "Administrador";
			if(booking.getUserName() != null)
				user = booking.getUserName();
			btnNot.setText(user);
			if (booking.getUserID()==0) 
				btnNot.setBackground(Color.ORANGE);
			else
				btnNot.setBackground(Color.BLUE);
		} 
		else{
			if (booking.getUserID() == userID) {
				btnNot.setText(booking.getUserName());
				btnNot.setBackground(Color.GREEN);
			}
			else
				btnNot.setBackground(Color.BLUE);
		}
		btnNot.setEnabled(false);
		return btnNot;
	}
}