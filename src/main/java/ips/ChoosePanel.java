package ips;

import ips.administrator.AdministratorMain;
import ips.member.MemberMain;

import javax.swing.*;
import java.awt.*;

/**
 * Created by nokutu on 3/10/16.
 */
public class ChoosePanel extends JPanel {

    private JButton administration;
    private JButton member;

    public ChoosePanel() {
        administration = new JButton("Administration");
        member = new JButton("Member");

        administration.addActionListener((e) -> Bar.getInstance().setMode(Bar.MODE_ADMINISTRATION));
        member.addActionListener((e) -> Bar.getInstance().setMode(Bar.MODE_MEMBER));

        JPanel cont = new JPanel();
        add(cont);

        cont.add(administration);
        cont.add(member);
    }
}
