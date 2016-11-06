package ips.administrator;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import ips.MainWindow;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import ips.database.FacilityBooking;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.awt.Cursor;
import java.awt.Dimension;

public class OverrideBookingDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton okButton;
	private JButton cancelButton;

	private JPanel buttonPane;
	private JTextArea txtYaExiste;
	private JPanel detailsPanel;

	private boolean valid = false;
	

	public OverrideBookingDialog(List<FacilityBooking> bookings) {
		super(MainWindow.getInstance(), true);
		// this.bookings = bookings;
		setTitle("Aviso");
		setMinimumSize(new Dimension(350, 120));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		//setBounds(100, 100, 450, 300);
		setModalityType(JDialog.DEFAULT_MODALITY_TYPE);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			txtYaExiste = new JTextArea();
			txtYaExiste.setRequestFocusEnabled(false);
			txtYaExiste.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			txtYaExiste.setHighlighter(null);
			txtYaExiste.setEditable(false);
			txtYaExiste.setFocusable(false);
			txtYaExiste.setFont(new Font("Tahoma", Font.PLAIN, 13));
			txtYaExiste.setBackground(UIManager.getColor("Panel.background"));
			txtYaExiste.setLineWrap(true);
			txtYaExiste.setWrapStyleWord(true);
			txtYaExiste.setText(
					"La instalaci\u00F3n est\u00E1 ocupada en las horas seleccionadas.\nPuedes cancelar la reserva existente y continuar.");
			contentPanel.add(txtYaExiste, BorderLayout.NORTH);
			txtYaExiste.setColumns(10);
		}
		{
			detailsPanel = new JPanel();
			detailsPanel.setLayout(new GridLayout(0, 1));
			for (FacilityBooking fb : bookings) {
				detailsPanel.add(new JTextArea(fb.toStringFull()));
			}
			contentPanel.add(detailsPanel, BorderLayout.CENTER);

		}
		{
			buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("Sobreescribir Reserva");
				okButton.setActionCommand("OK");
				okButton.addActionListener(e -> {
					for (FacilityBooking fb : bookings) {
						fb.cancel(); // cancelamos todas las reservas...
					}
					// ...e indicamos que podemos reservar
					valid = true;
					dispose();
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancelar");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(e -> {
					valid = false;
					dispose();
				});
				buttonPane.add(cancelButton);
			}
		}
		pack();
		setLocationRelativeTo(MainWindow.getInstance());
	}

	public boolean getValid() {
		return valid;
	}

}
