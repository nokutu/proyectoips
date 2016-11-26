package ips.member;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ips.database.Booking;
import ips.database.PaymentsDetails;

import java.awt.BorderLayout;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PaymentsDetailsPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JList list;

	DefaultListModel<String> model;
	ArrayList<Booking> bookings;

	public PaymentsDetailsPanel(int idUser) {
		setLayout(new BorderLayout(0, 0));
		model = new DefaultListModel();
		list = new JList(model);
		list.setEnabled(false);
		JScrollPane pane = new JScrollPane(list);

		try {
			bookings = PaymentsDetails.select(idUser);
		} catch (SQLException e) {
			System.out.println("Error en el constructor de PaymentDetailsPanel");
			e.printStackTrace();
		}

		int anio = 0;
		String mes = null;
		int duracion = 0;
		int total = 0;
		for (Booking booking : bookings) {
			Date d = new Date(booking.getTimeStart().getTime());
			Date e = new Date(booking.getTimeEnd().getTime() - booking.getTimeStart().getTime());
			duracion = Integer.parseInt(new SimpleDateFormat("HH").format(e.getTime()));
			duracion -= 1;
			if (anio == 0 || anio != Integer.parseInt(new SimpleDateFormat("YYYY").format(d.getTime()))) {
				anio = Integer.parseInt(new SimpleDateFormat("YYYY").format(d.getTime()));
				model.addElement("" + anio);
			}
			if (mes == null || !mes.equals(new SimpleDateFormat("MMMM").format(d.getTime()))) {
				if (total != 0) {
					model.addElement("TOTAL " + mes + " " + total + "€");
					total = 0;
				}
				mes = new SimpleDateFormat("MMMM").format(d.getTime());
				model.addElement(""+mes);
			}
			model.addElement("Instalación: " + booking.getFacilityName() + " Fecha: "
					+ new SimpleDateFormat("dd/MM/YYYY HH:mm").format(d.getTime()) + " Duración: " + duracion + " horas"
					+ " Total: " + duracion * booking.getPricePerHour() + "€");
			total += duracion * booking.getPricePerHour();
		}
		add(pane, BorderLayout.CENTER);
		repaint();
		revalidate();
	}

}
