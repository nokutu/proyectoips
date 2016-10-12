package ips;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.GridLayout;

import javax.swing.JTable;
import java.awt.Color;

public class AvailabilityPane extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel lblFacility;
	private JTable tableAvailable;
	Calendar calendar = Calendar.getInstance();
	private int weeksFromNow = 0;
	JLabel lblWeek;

	public AvailabilityPane() {
		setLayout(new BorderLayout(0, 0));

		JPanel weekPane = new JPanel();
		add(weekPane, BorderLayout.NORTH);

		JLabel lblAvailable = new JLabel("Facilities Availability. Select week.");
		weekPane.add(lblAvailable);
		lblAvailable.setHorizontalAlignment(SwingConstants.CENTER);

		JButton btnPrevious = new JButton("<");
		btnPrevious.setEnabled(false);
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				weeksFromNow--;
				setWeek();
				if(weeksFromNow == 0){
					btnPrevious.setEnabled(false);
				}
			}
		});
		weekPane.add(btnPrevious);
		
		lblWeek = new JLabel("empty");
		weekPane.add(lblWeek);

		setWeek();
		

		JButton btnNext = new JButton(">");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				weeksFromNow++;
				setWeek();
				if(weeksFromNow > 0){
					btnPrevious.setEnabled(true);
				}
			}
		});
		weekPane.add(btnNext);

		JScrollPane buttonScrollPane = new JScrollPane();
		add(buttonScrollPane, BorderLayout.WEST);

		JPanel centralPane = new JPanel();
		add(centralPane, BorderLayout.CENTER);
		centralPane.setLayout(new BorderLayout(0, 0));

		lblFacility = new JLabel();
		lblFacility.setOpaque(true);
		lblFacility.setBackground(Color.WHITE);
		centralPane.add(lblFacility, BorderLayout.NORTH);
		lblFacility.setText("Select facility");

		tableAvailable = new JTable();
		tableAvailable.setRowSelectionAllowed(false);
		tableAvailable.setEnabled(false);
		tableAvailable.setVisible(false);

		JScrollPane tableScrollPane = new JScrollPane(tableAvailable);
		centralPane.add(tableScrollPane, BorderLayout.CENTER);

		JPanel buttonPane = new JPanel();
		GridLayout gl_buttonPane = new GridLayout(14, 0);
		buttonPane.setLayout(gl_buttonPane);

		for (int i = 0; i < 14; i++) {
			final int a = i;
			JButton botonAux = new JButton("Instalacion: " + i);
			botonAux.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					lblFacility.setText("Horario de la instalacion: " + a);
					tableAvailable.setModel(getModel(a));
					tableAvailable.setVisible(true);
				}
			});
			buttonPane.add(botonAux);
		}

		buttonScrollPane.setViewportView(buttonPane);
	}

	private void setWeek() {
		calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, weeksFromNow*7);
		String week = "From: " + calendar.get(Calendar.DAY_OF_MONTH) + " of " + calendar.get(Calendar.MONTH) + " to: ";
		calendar.add(Calendar.DATE, +6);
		week += calendar.get(Calendar.DAY_OF_MONTH) + " of " + calendar.get(Calendar.MONTH);
		lblWeek.setText(week);
	}

	private TableModel getModel(int instalacion) {
		DefaultTableModel tableModel = new DefaultTableModel(getHeaders(), 0);

		ArrayList<String> fila = new ArrayList<String>();

		for (int hour = 0; hour < 24; hour++) {
			fila.clear();
			fila.add(hour + ":00");
			calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -1);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DATE, 1);
				fila.add(CheckAvailable(instalacion, calendar.get(Calendar.DAY_OF_MONTH),
						calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR), hour));
			}
			tableModel.addRow(fila.toArray());
		}

		return tableModel;
	}

	private String CheckAvailable(int instalacion, int day, int month, int year, int hour) {
		return hour + " " + day + " " + month + " " + year;
	}

	private Object[] getHeaders() {
		ArrayList<String> headers = new ArrayList<String>();
		headers.add("Hour\\Day");
		Date date = calendar.getTime();
		calendar.add(Calendar.DATE, -1);
		for (int i = 0; i < 7; i++) {
			calendar.add(Calendar.DATE, 1);
			date = calendar.getTime();
			headers.add("" + new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime()));
		}

		return headers.toArray();
	}
}
