package ips.administrator;

import ips.gui.Form;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

/**
 * Created by nokutu on 17/10/2016.
 */
public class CreateActivityDialog extends JDialog {

    private Form form;
    private JPanel content;

    private JButton confirm;
    private JButton cancel;

    public CreateActivityDialog(JFrame owner) {
        super(owner, true);

        content = new JPanel();
        content.setLayout(new BorderLayout());
        setContentPane(content);

        form = new Form();
        content.add(form.getPanel(), BorderLayout.CENTER);

        createButtons();
        addButtons(content);
        fillForm();

        pack();
        setLocationRelativeTo(owner);
    }

    private void fillForm() {
        form.addLine(new JLabel("Nombre:"), new JTextField(20));
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

    private void createButtons() {
        confirm = new JButton("OK");
        confirm.addActionListener(this::confirm);
        cancel = new JButton("Cancelar");
        cancel.addActionListener(this::cancel);
    }

    private void cancel(ActionEvent actionEvent) {
        dispose();
    }

    private void confirm(ActionEvent actionEvent) {
        // TODO
    }
}
