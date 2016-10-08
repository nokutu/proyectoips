package ips.administrator;

import com.toedter.calendar.JDateChooser;
import ips.gui.AutocompleteJTextField;
import ips.gui.Form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nokutu on 3/10/16.
 */
public class BookForMemberDialog extends JDialog {

    private Date date;
    private int hourStart;
    private int hourEnd;

    private AutocompleteJTextField memberField;
    private JLabel memberLabel;

    private JLabel paymentLabel;
    private JComboBox<String> paymentCombo;

    private JButton confirm;
    private JButton cancel;
    private JDateChooser dateChooser;

    public BookForMemberDialog(JFrame owner) {
        this(owner, null, -1, -1);
    }

    public BookForMemberDialog(JFrame owner, Date date, int hourStart, int hourEnd) {
        super(owner, true);
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

        Form form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);

        addForm(form, date == null);
        addButtons(content);

        pack();
        setLocationRelativeTo(owner);
    }

    private void addButtons(JPanel content) {
        GridBagConstraints c;

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

    private void addForm(Form form, boolean addDate) {

        if (addDate) {
            dateChooser = new JDateChooser("dd/MM/yyyy", "", '_');
            dateChooser.setCalendar(Calendar.getInstance());
            form.addLine(new JLabel("Date:"), dateChooser);
        }

        form.addLine(memberLabel, memberField);
        form.addLine(paymentLabel, paymentCombo);
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
        memberField = new AutocompleteJTextField(20, Arrays.asList("uoo245115", "uoooo124444"));
    }

    private void confirm(ActionEvent actionEvent) {
        // TODO add data to the database
        dispose();
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }
}
