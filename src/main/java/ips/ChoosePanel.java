package ips;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Created by nokutu on 3/10/16.
 */
public class ChoosePanel extends JPanel {


    private static final long serialVersionUID = -8043605828508774926L;
    private JButton administration;
    private JButton member;

    public ChoosePanel() {
        setLayout(new GridBagLayout());
        administration = new JButton("Administration");
        member = new JButton("Member");

        administration.addActionListener((e) -> Bar.getInstance().setMode(Bar.MODE_ADMINISTRATION));
        member.addActionListener((e) -> Bar.getInstance().setMode(Bar.MODE_MEMBER));

        JPanel cont = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        add(cont, c);

        cont.setLayout(new GridBagLayout());
        cont.add(administration, c);
        c.gridy = 1;
        c.insets = new Insets(20, 0, 0, 0);
        cont.add(member, c);
    }
}
