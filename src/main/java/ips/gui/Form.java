package ips.gui;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nokutu on 08/10/2016.
 */
public class Form {

    private JPanel panel;

    private int line = 0;

    public Form() {
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
    }

    public void addLine(Component a, JTextField b) {
        doAddLine(a, b);
    }

    public void addLine(Component a, JDateChooser b) {
        doAddLine(a, b);
    }

    public void addLine(Component a, JComboBox<String> b) {
        doAddLine(a, b);
    }

    private void doAddLine(Component a, Component b) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = line;
        c.insets = new Insets(2, 10, 2, 10);

        c.gridx = 0;
        panel.add(a, c);

        c.gridx = 1;
        panel.add(b, c);

        line++;
    }

    public JPanel getPanel() {
        return panel;
    }
}
