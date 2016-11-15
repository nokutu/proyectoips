package ips.administrator;


import ips.administrator.bookings.AdministratorBookingCancelDialog;
import ips.database.Database;
import ips.database.FacilityBooking;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class DetailsDialog extends JPanel {

    FacilityBooking book;
    private JScrollPane scrollPane;
    private JButton btnBack;
    private JTextArea textDescription;
    private JPanel panelSur;
    private JButton btnCancelarEstaReserva;
    private JComboBox<String> bookingsBox;

    public DetailsDialog(FacilityBooking book) {
        setLayout(new BorderLayout());
        this.book = book;
        if (book == null) {
            add(getBookingsBox(), BorderLayout.WEST);
        }
        add(getScrollPane(), BorderLayout.CENTER);
        add(getPanelSur(), BorderLayout.SOUTH);
    }


    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setViewportView(getTextDescription());
        }
        return scrollPane;
    }

    private JTextArea getTextDescription() {
        if (textDescription == null) {
            textDescription = new JTextArea();
            if (book != null)
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
        }
        return panelSur;
    }

    private JButton getBtnCancelarEstaReserva() {
        if (btnCancelarEstaReserva == null) {
            btnCancelarEstaReserva = new JButton("Cancelar esta reserva");
            btnCancelarEstaReserva.addActionListener(e -> {
                AdministratorBookingCancelDialog.show(book);
            });
        }
        return btnCancelarEstaReserva;
    }

    private JComboBox getBookingsBox() {
        if (bookingsBox == null) {
            bookingsBox = new JComboBox<>();
            DefaultComboBoxModel<String> booksModel = new DefaultComboBoxModel<>();
            Database.getInstance().getFacilityBookings().forEach(a -> booksModel.addElement(a.toString()));
            bookingsBox.setModel(booksModel);
            bookingsBox.addActionListener(l -> {
                FacilityBooking fb = Database.getInstance().getFacilityBookings().get(bookingsBox.getSelectedIndex());
                textDescription.setText(fb.toStringFull());

            });
            book = Database.getInstance().getFacilityBookings().get(bookingsBox.getSelectedIndex());
        }
        return bookingsBox;
    }
}
