package ips.administrator;

import ips.Utils;
import ips.database.Database;
import ips.database.FacilityBooking;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTreeUI.SelectionModelPropertyChangeHandler;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CurrentViewDialog extends JDialog {
    private JButton btnBack;
    private JTextPane textBookings;
    private JPanel panel;
    private JScrollPane scrollPane;
    private JList list;
    private JButton btnStartUse;
    private JButton btnEndUse;
    private JLabel lblUsed;

    private DefaultListModel model = new DefaultListModel();
    private List<FacilityBooking> books = new ArrayList<>();
    private FacilityBooking book = null;


    public CurrentViewDialog(JFrame owner) {
        super(owner, true);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().add(getBtnBack());
        getContentPane().add(getBtnStartUse());
        getContentPane().add(getBtnEndUse());
        getContentPane().add(getLblUsed());
        getContentPane().add(getScrollPane());
        getCurrentFacilities();

        this.setBounds(500, 350, 500, 350);
        setLocationRelativeTo(owner);
    }

    private JButton getBtnBack() {
        if (btnBack == null) {
            btnBack = new JButton("Atras");
            btnBack.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            btnBack.setBounds(170, 262, 89, 23);
        }
        return btnBack;
    }


    private void getCurrentFacilities() {
        String line = "";
        boolean found = false;
        Date date = new Date();
        Timestamp fecha = new Timestamp(date.getTime());
        List<FacilityBooking> bookings = Database.getInstance().getFacilityBookings();
        for (FacilityBooking f : bookings) {
            if (f.getTimeStart().before(Utils.getCurrentTime())&& f.getTimeEnd().after(Utils.getCurrentTime())) {
                found = true;
                line = f.toString();
                books.add(f);

                model.addElement(line);
                list.setModel(model);
            }
        }

        if (!found) {
        	
            lblUsed.setText("No hay reservas en la hora actual");
            lblUsed.setVisible(true);
        }

    }

    private JScrollPane getScrollPane() {
        if (scrollPane == null) {
            scrollPane = new JScrollPane();
            scrollPane.setBorder(new TitledBorder(null, "Instalaciones con reserva", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
            scrollPane.setBounds(80, 40, 280, 211);
            scrollPane.setViewportView(getList());
        }
        return scrollPane;
    }

    private JList getList() {
        if (list == null) {
            list = new JList();
            list.setModel(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.addListSelectionListener(new ListSelectionListener()
            {
              public void valueChanged(ListSelectionEvent e)
              {
                if (e.getValueIsAdjusting() == false)
                {

						book = books.get(list.getSelectedIndex());
						if (book.getEntrance() != null && book.getAbandon() == null) 
						{
							lblUsed.setVisible(true);
						} else 
						{
							lblUsed.setVisible(false);
						}

                    if (book.getEntrance() != null) {
                        btnStartUse.setEnabled(false);
                    } else {
                        btnStartUse.setEnabled(true);
                    }

                    if (book.getAbandon() != null) {
                        btnEndUse.setEnabled(false);
                    } else {
                        btnEndUse.setEnabled(true);
                    }
					}
				}

            });
            
        }
        return list;
    }

    private JButton getBtnStartUse() {
        if (btnStartUse == null) {
            btnStartUse = new JButton("Comenzar uso");
            btnStartUse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    Date date = new Date();
                    Timestamp fecha = new Timestamp(date.getTime());
                    book.setEntrance(fecha);
                    btnStartUse.setEnabled(false);
                    lblUsed.setVisible(true);
                    try {
                        book.update();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            btnStartUse.setBounds(370, 123, 114, 23);
            btnStartUse.setEnabled(false);
        }
        return btnStartUse;
    }

    private JButton getBtnEndUse() {
        if (btnEndUse == null) {
            btnEndUse = new JButton("Finalizar uso");
            btnEndUse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Date date = new Date();
                    Timestamp fecha = new Timestamp(date.getTime());
                    book.setAbandon(fecha);
                    btnEndUse.setEnabled(false);
                    lblUsed.setVisible(false);
                    try {
                        book.update();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            btnEndUse.setBounds(370, 152, 114, 23);
            btnEndUse.setEnabled(false);
        }
        return btnEndUse;
    }

    private JLabel getLblUsed() {
        if (lblUsed == null) {
            lblUsed = new JLabel("En uso actualmente");
            lblUsed.setFont(new Font("Tahoma", Font.BOLD, 12));
            lblUsed.setForeground(Color.RED);
            lblUsed.setBounds(80, 11, 280, 23);
            lblUsed.setVisible(false);
        }
        return lblUsed;
    }
}
