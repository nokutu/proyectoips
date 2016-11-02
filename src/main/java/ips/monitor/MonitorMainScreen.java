package ips.monitor;

import ips.Utils;
import ips.database.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class MonitorMainScreen extends JPanel {

	JPanel centerPanel;
	private CheckBoxList memberList = new CheckBoxList();

	private List<ActivityBooking> sessionsList;
	private List<Member> membersInSession;
	private List<ActivityMember> activityMembersInSession;
	private List<JCheckBox> activityMembersInSessionChk;

	private JLabel assistanceLabel = new JLabel("");
	private JComboBox<String> activities;
	private JButton addMember = new JButton("Apuntar a socio");
	private JComboBox<String> sessions;

	private JComboBox<Monitor> monitorIDTextField;

	public static int monitorID;

	public MonitorMainScreen() {
		super();

		setLayout(new BorderLayout());

		createLeftPanel();
		createBottomPanel();
		createCenterPanel();
		createRightPanel();
		createNorthPanel();
		
	}

	private void createNorthPanel() {
		JPanel monitorIDPanel = new JPanel();
		add(monitorIDPanel, BorderLayout.NORTH);

		monitorIDPanel.add(new JLabel("ID de Monitor:"));
		
		monitorIDTextField = new JComboBox<>();
		DefaultComboBoxModel<Monitor> monitorsModel = new DefaultComboBoxModel<>();
		Database.getInstance().getMonitors().forEach(a -> monitorsModel.addElement(a));
		monitorIDTextField.setModel(monitorsModel);		
		monitorIDTextField.addActionListener(l -> update((Monitor)monitorIDTextField.getSelectedItem()));
		monitorIDPanel.add(monitorIDTextField);
	}

	private void createRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;

		/*JButton remove = new JButton("Marcar asistencia");
		remove.addActionListener(l -> {
			for (int i = 0; i < membersInSession.size(); i++) {
				if (activityMembersInSessionChk.get(i).isSelected() && activityMembersInSessionChk.get(i).isEnabled())
				{
					try {
						activityMembersInSession.get(i).setAssistance(true);
						activityMembersInSession.get(i).update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			refreshCentralPanelList();
		});
		rightPanel.add(remove, c);*/

		add(rightPanel, BorderLayout.EAST);
	}

	 private void update(Monitor member) {
	        if (checkMonitor(member.getMonitorId())) {
	            monitorID = member.getMonitorId();
	        } else {
	            monitorID = 0;
	        }
	    }

	    private boolean checkMonitor(int i) {
	        try {
	            return Database.getInstance().getMonitors().stream().filter(m -> m.getMonitorId() == i).findAny().isPresent();
	        } catch (NumberFormatException e) {
	            return false;
	        }
	    }
	    
	private void createLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(leftPanel, BorderLayout.WEST);

		leftPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(2, 10, 2, 5);

		leftPanel.add(new JLabel("Actividad:"), c);

		c.gridx++;

		activities = new JComboBox<>();
		DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
		Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
		activities.setModel(activitiesModel);
		leftPanel.add(activities, c);

		c.gridx = 0;
		c.gridy++;

		leftPanel.add(new JLabel("Sesión:"), c);

		c.gridx++;

		sessions = new JComboBox<>();
		activities.addActionListener(l -> {
			memberList.removeAll();
			DefaultComboBoxModel<String> sessionsModel = new DefaultComboBoxModel<>();
			sessionsList = Database.getInstance().getActivityBookings().stream()
					.filter(ab -> ab.getActivityId() == getSelectedActivity().getActivityId())
					.collect(Collectors.toList());
			sessionsList.stream().map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
					.forEach(sessionsModel::addElement);
			sessions.setModel(sessionsModel);

			if (sessions.getModel().getSize() > 0) {
				sessions.setSelectedIndex(0);
			}
			refreshAssistanceCount();
		});
		activities.setSelectedIndex(0);

		sessions.addActionListener(l -> {
			refreshCentralPanelList();
		});
		if (sessions.getModel().getSize() > 0) {
			sessions.setSelectedIndex(0);
		}

		leftPanel.add(sessions, c);
	}

	private void refreshCentralPanelList() {
		memberList.removeAll();
		membersInSession = new ArrayList<>();
		activityMembersInSession = new ArrayList<>();
		activityMembersInSessionChk = new ArrayList<>();
		for (ActivityMember am : Database.getInstance().getActivityMembers()) {
			if (am.getActivityId() == getSelectedActivity().getActivityId()
					&& am.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId()
					&& !am.isDeleted()) {
				activityMembersInSession.add(am);

				Member member = Database.getInstance().getMemberById(am.getMemberId());
				membersInSession.add(member);

				JCheckBox chk = new JCheckBox(member.getMemberName());
				if(am.isAssistance())
				{
					chk.setSelected(true);
				}
				if(!sessionsList.get(sessions.getSelectedIndex()).getFacilityBooking().getTimeStart().before(Utils.getCurrentTime())||!sessionsList.get(sessions.getSelectedIndex()).getFacilityBooking().getTimeEnd().after(Utils.getCurrentTime()))
				{
					chk.setEnabled(false);
				}
				chk.addActionListener(l->{
					if (chk.isSelected() && chk.isEnabled())
					{
					try 
					{
						am.setAssistance(true);
						am.update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					}
					if (!chk.isSelected() && chk.isEnabled())
					{
						try 
						{
							am.setAssistance(false);
							am.update();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}});
				activityMembersInSessionChk.add(chk);
				memberList.addLine(chk);
			}
			refreshAssistanceCount();
		}

		// Force the panel to refresh
		SwingUtilities.invokeLater(() -> {
			memberList.setVisible(false);
			memberList.setVisible(true);
		});
	}

	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		add(bottomPanel, BorderLayout.SOUTH);

		bottomPanel.add(new JLabel("Socios apuntados:"));
		bottomPanel.add(assistanceLabel);

		bottomPanel.add(addMember);
	}

	private void createCenterPanel() {
		centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());

		JLabel colorlabel = new JLabel();
		colorlabel.setText("Lista de socios apuntados:");

		centerPanel.add(colorlabel, BorderLayout.NORTH);
		centerPanel.add(memberList, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);
	}

	private void refreshAssistanceCount() {
		Optional<Integer> assistantsOptional = Database.getInstance().getActivityMembers().parallelStream()
				.filter(am -> am.getActivityId() == getSelectedActivity().getActivityId() && !am.isDeleted() && am
						.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId())
				.map(am -> am.isAssistance() ? 1 : 0).reduce(Integer::sum);
		Optional<Activity> activityOptional = Database.getInstance().getActivities().parallelStream()
				.filter(a -> a.getActivityName().equals(activities.getSelectedItem())).findAny();
		if (activityOptional.isPresent()) {
			if (assistantsOptional.isPresent()) {
				// n assistants
				assistanceLabel.setText(assistantsOptional.get() + "/" + activityOptional.get().getAssistantLimit());
				addMember.setEnabled(assistantsOptional.get() < activityOptional.get().getAssistantLimit());
			} else {
				// 0 assistants
				assistanceLabel.setText("0/" + activityOptional.get().getAssistantLimit());
				addMember.setEnabled(true);
			}
		} else {
			// no activity selected
			assistanceLabel.setText("");
			addMember.setEnabled(false);
		}
	}

	private Activity getSelectedActivity() {
		return Database.getInstance().getActivities().get(activities.getSelectedIndex());
	}

	private class CheckBoxList extends JPanel {

		private GridBagConstraints c;

		private JLabel endLabel = new JLabel();

		public CheckBoxList() {
			setLayout(new GridBagLayout());
			c = new GridBagConstraints();
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.anchor = GridBagConstraints.NORTHWEST;
			setBackground(Color.white);

			c.weighty = 1;
			add(endLabel, c);
			c.weighty = 0;
		}

		public void addLine(JCheckBox ch) {
			remove(endLabel);

			ch.setBackground(Color.white);
			add(ch, c);
			c.gridy++;

			c.weighty = 1;
			add(endLabel, c);
			c.weighty = 0;
		}
	}
}