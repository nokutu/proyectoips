package ips.administrator;


import ips.database.Database;
import ips.database.FacilityBooking;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;
import java.awt.FlowLayout;

public class DetailsDialog extends JDialog {
    FacilityBooking book;
    private JScrollPane scrollPane;
    private JButton btnBack;
    private JTextArea textDescription;
    private JPanel panelSur;
    private JButton btnCancelarEstaReserva;
    private JComboBox bookingsBox;

    public DetailsDialog(JFrame owner, FacilityBooking book) {
        super(owner, true);
        this.book = book;
        if(book==null)
        {
        getContentPane().add(getBookingsBox(), BorderLayout.WEST);
        }
        getContentPane().add(getScrollPane(), BorderLayout.CENTER);
        getContentPane().add(getPanelSur(), BorderLayout.SOUTH);
        
        

        pack();
        setLocationRelativeTo(owner);
        this.setVisible(true);
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
            if(book!=null)
            textDescription.setText(book.toStringFull());
            else
            textDescription.setText("Seleccione una reserva");
        }
        return textDescription;
    }
	private JPanel getPanelSur() {
		if (panelSur == null) {
			panelSur = new JPanel();
			panelSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelSur.add(getBtnCancelarEstaReserva());
			panelSur.setBackground(UIManager.getColor("Panel.background"));
			panelSur.add(getBtnBack());
		}
		return panelSur;
	}
	private JButton getBtnCancelarEstaReserva() {
		if (btnCancelarEstaReserva == null) {
			btnCancelarEstaReserva = new JButton("Cancelar esta reserva");
			btnCancelarEstaReserva.addActionListener(e -> {
				AdministratorBookingCancelDialog.show(book);
				this.dispose();
			});
		}
		return btnCancelarEstaReserva;
	}
	private JComboBox getBookingsBox() {
		if (bookingsBox == null) {
			bookingsBox = new JComboBox();
			DefaultComboBoxModel<String> booksModel = new DefaultComboBoxModel<>();
			Database.getInstance().getFacilityBookings().forEach(a -> booksModel.addElement(a.toString()));
			bookingsBox.setModel(booksModel);
			bookingsBox.addActionListener(l -> {
				FacilityBooking fb = Database.getInstance().getFacilityBookings().get(bookingsBox.getSelectedIndex());
				textDescription.setText(fb.toStringFull());
			
			});
			book= Database.getInstance().getFacilityBookings().get(bookingsBox.getSelectedIndex());
		}
		return bookingsBox;
	}
}
