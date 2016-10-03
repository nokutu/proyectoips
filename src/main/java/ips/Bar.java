package ips;

import ips.administrator.AdministratorMain;
import ips.member.MemberMain;

import javax.swing.*;

/**
 * Created by nokutu on 03/10/2016.
 */
public class Bar extends JMenuBar {

    public final static int MODE_ADMINISTRATION = 1;
    public final static int MODE_MEMBER = 2;
    private static Bar instance;

    private JMenu options;
    private JRadioButtonMenuItem member;

    private JRadioButtonMenuItem administration;

    public static Bar getInstance() {
        if (instance == null) {
            instance = new Bar();
        }
        return instance;
    }

    private Bar() {
        options = new JMenu("Options");
        add(options);

        fillOptions();
    }

    private void fillOptions() {
        JMenu change = new JMenu("Change mode");
        options.add(change);

        fillChange(change);
    }

    private void fillChange(JMenu change) {
        ButtonGroup g = new ButtonGroup();
        addMember(change, g);
        addAdministration(change, g);
    }

    private void addMember(JMenu change, ButtonGroup g) {
        member = new JRadioButtonMenuItem("Member");
        member.addActionListener((e) -> MainWindow.getInstance().setContent(new MemberMain()));
        g.add(member);
        change.add(member);
    }

    private void addAdministration(JMenu change, ButtonGroup g) {
        administration = new JRadioButtonMenuItem("Administration");
        administration.addActionListener((e) -> MainWindow.getInstance().setContent(new AdministratorMain()));
        g.add(administration);
        change.add(administration);
    }

    public void setMode(int mode) {
        if (mode == MODE_ADMINISTRATION) {
            administration.doClick();
        } else if (mode == MODE_MEMBER) {
            member.doClick();
        }
    }
}
