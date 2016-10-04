package ips.administrator;

import ips.Autocomplete;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by nokutu on 3/10/16.
 */
public class BookingDialog extends JDialog {

    private Date date;
    private int hourStart;
    private int hourEnd;

    private Autocomplete memberAutocomplete;
    private JTextField memberField;
    private JLabel memberLabel;

    private JLabel paymentLabel;
    private JComboBox<String> paymentCombo;

    private JButton confirm;
    private JButton cancel;


    public BookingDialog(JFrame owner, Date date, int hourStart, int hourEnd) {
        super(owner);
        setResizable(false);

        this.date = date;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;

        createMember();
        createPayment();

        createButtons();

        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        content.add(form, BorderLayout.CENTER);

        addForm(form);
        addButtons(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addButtons(JPanel content) {
        GridBagConstraints c = new GridBagConstraints();

        JPanel bottom = new JPanel();
        bottom.setLayout(new BorderLayout());
        content.add(bottom, BorderLayout.SOUTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(20, 0, 10, 5);
        bottom.add(buttons, BorderLayout.EAST);
        buttons.add(confirm, c);
        c.insets = new Insets(20, 5, 10, 10);
        c.gridx = 1;
        buttons.add(cancel, c);
    }

    private void addForm(JPanel form) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        // First line
        c.insets = new Insets(10, 10, 2, 10);
        form.add(memberLabel, c);
        c.gridx = 1;
        form.add(memberField, c);

        // Middle lines
        c.insets = new Insets(2, 10, 2, 10);
        c.gridx = 0;
        c.gridy = 1;
        form.add(paymentLabel, c);
        c.gridx = 1;
        form.add(paymentCombo, c);
    }

    private void createButtons() {
        confirm = new JButton("Confirm");
        confirm.addActionListener(this::confirm);
        cancel = new JButton("Cancel");
        cancel.addActionListener(this::cancel);
    }

    private void createPayment() {
        paymentLabel = new JLabel("Payment type:");
        paymentCombo = new JComboBox<>();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(new String[]{"Cash", "Fee"});
        paymentCombo.setModel(model);
    }

    private void createMember() {
        memberLabel = new JLabel("Member ID:");
        memberField = new JTextField(20);
        memberAutocomplete = new Autocomplete(memberField, Arrays.asList("uoo245115", "uoooo124444"));
        memberField.getDocument().addDocumentListener(memberAutocomplete);
    }

    private void confirm(ActionEvent actionEvent) {
        // TODO add data to the database
        dispose();
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }
}
