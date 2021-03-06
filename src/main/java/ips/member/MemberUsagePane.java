package ips.member;

import ips.MainScreen;
import ips.administrator.DetailsDialog;
import ips.database.AdminActitivies;
import ips.database.Booking;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.database.MemberUsage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MemberUsagePane extends JPanel {
	private JLabel lblWeek;
	ArrayList<Booking> bookings;
	ArrayList<AdminActitivies> adminActitivies;
	private int weeksFromNow = 0;
	JButton btnNext = new JButton(">");
	JButton btnPrevious = new JButton("<");
	JPanel centralWeekPanel = new JPanel();
	private static final long serialVersionUID = 1L;
	private Calendar calendar = Calendar.getInstance();
	MainScreen MS;

	public MemberUsagePane(int idUser, MainScreen mainScreen) {
		this.MS = mainScreen;
		setLayout(new BorderLayout(0, 0));

		JPanel weekPane = new JPanel();
		add(weekPane, BorderLayout.NORTH);

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
		
		JButton hoy = new JButton("Hoy");
		hoy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weeksFromNow = 0;
				setWeek();
				addRows(idUser);
			}
		});
		
		weekPane.add(hoy);

		JPanel centralPane = new JPanel();
		add(centralPane, BorderLayout.CENTER);
		centralPane.setLayout(new BorderLayout(0, 0));

		centralPane.add(centralWeekPanel, BorderLayout.CENTER);
		centralWeekPanel.setLayout(new GridLayout(0, 8, 0, 0));
		addRows(idUser);
	}

	private void addRows(int idUser) {
		bookings = MemberUsage.select(idUser);
		adminActitivies = MemberUsage.getAdminActitivies(idUser);
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
		rowPanel.add(
				new JLabel("" + new SimpleDateFormat("EEEE dd/MM", new Locale("es", "ES")).format(date.getTime())));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		for (int i = 0; i < 24; i++) {
			date = calendar.getTime();
			long now = date.getTime();
			JButton botonAux = new JButton("No hay reservas");
			botonAux.setEnabled(false);
			boolean ocupado = false;
			for (Booking booking : bookings) {
				if (now >= booking.getTimeStart().getTime() && now < booking.getTimeEnd().getTime()) {
					botonAux = setboton(booking);
					ocupado = true;
					botonAux.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							for (FacilityBooking fb : Database.getInstance().getFacilityBookings()) {
								if (fb.getFacilityBookingId() == booking.getBookingId()) {
									MS.setRightPanel(new DetailsDialog(fb));
									repaint();
									revalidate();
								}
							}
						}
					});
					break;
				}
			}
			if (!ocupado) {
				for (AdminActitivies ac : adminActitivies) {
					if (now >= ac.fb.getTimeStart().getTime() && now < ac.fb.getTimeEnd().getTime()) {
						botonAux = setboton(ac);
						botonAux.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								for (FacilityBooking fb : Database.getInstance().getFacilityBookings()) {
									if (fb.getFacilityBookingId() == ac.fb.getFacilityBookingId()) {
										MS.setRightPanel(
												new DetailsDialog(fb, "Actividad: " + ac.ac.getActivityName()));
										repaint();
										revalidate();
									}
								}
							}
						});
						break;
					}
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
		Date date = calendar.getTime();
		String week = "Desde: " + new SimpleDateFormat("dd MMMM YYYY", new Locale("es", "ES")).format(date.getTime())
				+ ". Hasta: ";
		calendar.add(Calendar.DATE, +6);
		date = calendar.getTime();
		week += new SimpleDateFormat("dd MMMM YYYY", new Locale("es", "ES")).format(date.getTime());
		lblWeek.setText(week);
	}

	private JButton setboton(Booking booking) {
		JButton btnNot = new JButton(booking.getFacilityName());
		btnNot.setEnabled(true);
		switch (booking.getState()) {
		case FacilityBooking.STATE_VALID:
			if (booking.getTimeStart().after(new Timestamp(System.currentTimeMillis())))
				btnNot.setBackground(new Color(0, 255, 60)); //verde
			else
				btnNot.setBackground(new Color(0, 180, 255)); //cian
			break;
		case FacilityBooking.STATE_ANNULLED:
			btnNot.setBackground(Color.GRAY);
			break;
		case FacilityBooking.STATE_CANCELLED:
			btnNot.setBackground(Color.RED);
			break;
		default:
			break;
		}
		return btnNot;
	}

	private JButton setboton(AdminActitivies ac) {
		JButton btnNot = new JButton(ac.ac.getActivityName());
		btnNot.setEnabled(true);
		btnNot.setBackground(new Color(234, 255, 0)); //amarillo
		return btnNot;
	}
}