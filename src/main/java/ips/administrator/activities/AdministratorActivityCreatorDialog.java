package ips.administrator.activities;

import ips.database.Activity;
import ips.database.Database;
import javax.swing.*;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.LayoutManager;

/**
 * Created by nokutu on 14/11/2016.
 */
public class AdministratorActivityCreatorDialog extends JDialog {

	private JTextField textFieldNombreActividad;
	private JCheckBox chckbxAsignarAMonitor;
	private JLabel lblNumeroDelMonitor;
	private JTextField textFieldMonitorId;
	private JCheckBox chckbxAsignarLimitePlazas;
	private JSpinner spinnerLimiteDePlazas;
	private JButton btnCrearActividad;
	private JLabel lblNombreActividad;

	public AdministratorActivityCreatorDialog(JDialog owner) {
		super(owner, true);
		setResizable(false);

		GridBagLayout gbl_content = new GridBagLayout();
		gbl_content.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0 };
		gbl_content.columnWeights = new double[] { 0.0, 1.0, 0.0 };
		JPanel content = new JPanel(gbl_content);
		setContentPane(content);
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(10, 20, 10, 5);

		lblNombreActividad = new JLabel("Nombre de la Actividad:");
		content.add(lblNombreActividad, c);

		/*
		 * c.gridx++; c.insets = new Insets(10, 5, 10, 20);
		 * 
		 * content.add(txtActiviyName, c);
		 * 
		 * c.gridx=0; c.gridy++;
		 * 
		 * content.add(txtActiviyName, c);
		 * 
		 * c.gridx = 0; c.gridy = 1;
		 * 
		 * c.gridwidth = 2; c.insets = new Insets(10, 20, 10, 20);
		 * 
		 * JButton crear = new JButton("Crear Actividad");
		 * crear.addActionListener(l -> { int res =
		 * JOptionPane.showConfirmDialog(this,
		 * "¿Estás seguro de que todos los datos son correctos?", "Alerta",
		 * JOptionPane.YES_NO_OPTION);
		 * 
		 * if (res == JOptionPane.YES_OPTION) { //
		 * activityList.get(activities.getSelectedIndex()).setDeleted(true); //
		 * try { // activityList.get(activities.getSelectedIndex()).update(); //
		 * } catch (SQLException e) { // e.printStackTrace(); // }
		 * refreshActivities(); } }); content.add(crear, c);
		 */
		GridBagConstraints gbc_textFieldNombreActividad = new GridBagConstraints();
		gbc_textFieldNombreActividad.gridwidth = 2;
		gbc_textFieldNombreActividad.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldNombreActividad.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldNombreActividad.gridx = 1;
		gbc_textFieldNombreActividad.gridy = 0;
		content.add(getTextFieldNombreActividad(), gbc_textFieldNombreActividad);

		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 0, 5, 5);
		c.gridx = 0;
		c.gridy = 1;
		content.add(getChckbxAsignarAMonitor(), c);

		c.insets = new Insets(0, 0, 5, 5);
		c.gridx = 1;
		c.gridy = 1;
		content.add(getLblNumeroDelMonitor(), c);

		c.insets = new Insets(0, 0, 5, 0);
		c.gridx = 2;
		c.gridy = 1;
		content.add(getTextFieldMonitorId(), c);

		c.anchor = GridBagConstraints.WEST;
		c.insets = new Insets(0, 0, 5, 5);
		c.gridx = 0;
		c.gridy = 2;
		content.add(getChckbxAsignarLimitePlazas(), c);

		c.insets = new Insets(0, 0, 5, 0);
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		content.add(getSpinnerLimiteDePlazas(), c);

		c.gridwidth = 3;
		c.insets = new Insets(0, 0, 5, 5);
		c.gridx = 0;
		c.gridy = 3;
		content.add(getBtnCrearActividad(), c);

		pack();
		setLocationRelativeTo(owner);
	}

	private JTextField getTextFieldNombreActividad() {
		if (textFieldNombreActividad == null) {
			textFieldNombreActividad = new JTextField();
			textFieldNombreActividad.setColumns(10);
		}
		return textFieldNombreActividad;
	}

	private JCheckBox getChckbxAsignarAMonitor() {
		if (chckbxAsignarAMonitor == null) {
			chckbxAsignarAMonitor = new JCheckBox("Asignar a Monitor");
			chckbxAsignarAMonitor.setSelected(true);
			chckbxAsignarAMonitor.addActionListener(l -> {
				if (!chckbxAsignarAMonitor.isSelected()) {
					textFieldMonitorId.setEnabled(false);
					textFieldMonitorId.setEnabled(false);
				} else {
					lblNumeroDelMonitor.setEnabled(true);
					textFieldMonitorId.setEnabled(true);
				}
			});
		}
		return chckbxAsignarAMonitor;
	}

	private JLabel getLblNumeroDelMonitor() {
		if (lblNumeroDelMonitor == null) {
			lblNumeroDelMonitor = new JLabel("numero del Monitor:");
		}
		return lblNumeroDelMonitor;
	}

	private JTextField getTextFieldMonitorId() {
		if (textFieldMonitorId == null) {
			textFieldMonitorId = new JTextField();
			textFieldMonitorId.setColumns(10);
		}
		return textFieldMonitorId;
	}

	private JCheckBox getChckbxAsignarLimitePlazas() {
		if (chckbxAsignarLimitePlazas == null) {
			chckbxAsignarLimitePlazas = new JCheckBox("Asignar Limite de Plazas");
			chckbxAsignarLimitePlazas.addActionListener(l -> {
				if (!chckbxAsignarLimitePlazas.isSelected())
					spinnerLimiteDePlazas.setEnabled(false);
				else
					spinnerLimiteDePlazas.setEnabled(true);
			});
		}
		return chckbxAsignarLimitePlazas;

	}

	private JSpinner getSpinnerLimiteDePlazas() {
		if (spinnerLimiteDePlazas == null) {
			spinnerLimiteDePlazas = new JSpinner();
			spinnerLimiteDePlazas.setEnabled(false);

		}
		return spinnerLimiteDePlazas;
	}

	private JButton getBtnCrearActividad() {
		if (btnCrearActividad == null) {
			btnCrearActividad = new JButton("Crear la Actividad");
		}
		return btnCrearActividad;
	}

}
