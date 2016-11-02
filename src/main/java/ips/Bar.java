package ips;

import ips.administrator.AdministratorMain;
import ips.administrator.AdministratorMainScreen;

import ips.member.MemberMainScreen;
import ips.monitor.MonitorMainScreen;
import ips.monitor.MonitorMainScreen;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;

/**
 * Created by nokutu on 03/10/2016.
 */
public class Bar extends JMenuBar {

	private static final long serialVersionUID = 8555548018850300676L;
	public final static int MODE_ADMINISTRATION = 1;
	public final static int MODE_MEMBER = 2;
	public static final int MODE_MONITOR = 3;
	private static final boolean DEBUG = false; // a true muestra el panel
												// clasico del SPRINT 1, para
												// pruebas solo

	private static Bar instance;

	private JMenu options;
	private JRadioButtonMenuItem member;
	private JRadioButtonMenuItem administration;
	private JRadioButtonMenuItem monitor;

	public static Bar getInstance() {
		if (instance == null) {
			instance = new Bar();
		}
		return instance;
	}

	private Bar() {
		options = new JMenu("Opciones");
		add(options);

		fillOptions();
	}

	private void fillOptions() {
		JMenu change = new JMenu("Cambiar modo");
		options.add(change);

		fillChange(change);
	}

	private void fillChange(JMenu change) {
		ButtonGroup g = new ButtonGroup();
		addAdministration(change, g);
		addMember(change, g);
		addMonitor(change, g);
	}

	private void addMember(JMenu change, ButtonGroup g) {
		member = new JRadioButtonMenuItem("Socio");
		member.addActionListener((e) -> MainWindow.getInstance().setContent(new MemberMainScreen()));
		g.add(member);
		change.add(member);
	}

	private void addAdministration(JMenu change, ButtonGroup g) {
		administration = new JRadioButtonMenuItem("Administraci\u00F3n");
		administration.addActionListener((e) -> {
			if (DEBUG)
				MainWindow.getInstance().setContent(new AdministratorMain());
			else
				MainWindow.getInstance().setContent(new AdministratorMainScreen());
		});
		g.add(administration);
		change.add(administration);
	}

	private void addMonitor(JMenu change, ButtonGroup g) {
		monitor = new JRadioButtonMenuItem("Monitor");
		monitor.addActionListener((e) -> MainWindow.getInstance().setContent(new MonitorMainScreen()));
		g.add(monitor);
		change.add(monitor);
	}

	public void setMode(int mode) {
		if (mode == MODE_ADMINISTRATION) {
			administration.doClick();
		} else if (mode == MODE_MEMBER) {
			member.doClick();
		} else if (mode == MODE_MONITOR) {
			monitor.doClick();
		}
	}
}
