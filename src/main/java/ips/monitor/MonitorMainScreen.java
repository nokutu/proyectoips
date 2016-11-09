package ips.monitor;

import ips.MainWindow;
import ips.Utils;
import ips.database.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nokutu on 25/10/2016.
 */
public class MonitorMainScreen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CheckBoxList memberList = new CheckBoxList();

	private List<ActivityBooking> sessionsList;
	private List<ActivityMember> activityMembersInSession;

	private JLabel assistanceLabel = new JLabel("");
	private JComboBox<String> activities;
	private JButton addMember = new JButton("Apuntar al socio:");
	private TextField addMemberTextField = new TextField("Numero de Socio a a\u00F1adir");
	private JComboBox<String> sessions;
	DefaultComboBoxModel<String> sessionsModel;
	DefaultComboBoxModel<String> activitiesModel;
	// private List<ActivityBooking> activityBookingsList;

	private JComboBox<Monitor> monitorIDComboBox;
	public static int monitorID;
	private List<Activity> activitiesList;

	public MonitorMainScreen() {
		super();

		setLayout(new BorderLayout());

		createNorthPanel();
		createLeftPanel();
		createBottomPanel();
		createCenterPanel();

		setMinimumSize(new Dimension(320, 180));
	}

	private void createNorthPanel() {
		JPanel monitorIDPanel = new JPanel();
		add(monitorIDPanel, BorderLayout.NORTH);

		monitorIDPanel.add(new JLabel("Monitor:"));

		monitorIDComboBox = new JComboBox<>();
		DefaultComboBoxModel<Monitor> monitorsModel = new DefaultComboBoxModel<>();
		Database.getInstance().getMonitors().forEach(a -> monitorsModel.addElement(a));
		monitorIDComboBox.setModel(monitorsModel);
		monitorIDComboBox.setSelectedIndex(0);
		monitorID = ((Monitor) monitorIDComboBox.getSelectedItem()).getMonitorId();
		// refreshLeftCombos(); //MARK
		monitorIDComboBox.addActionListener(l -> {
			monitorID = ((Monitor) monitorIDComboBox.getSelectedItem()).getMonitorId();
			refreshLeftCombos();
			refreshCentralPanelList();
			//createLeftPanel();
			//createCenterPanel();
			// TODO actualizar el sur
		});
		monitorIDPanel.add(monitorIDComboBox);
	}
	private void refreshLeftCombos() { // TODO
		System.err.println("actualizo");
		updateActivitiesCombo();
		updateSessionsCombo();
	}

	private void updateActivitiesCombo() {

		activitiesModel = new DefaultComboBoxModel<>();
		activitiesList = Database.getInstance().getActivities().stream()
				.filter(a -> a.getActivityBookings().stream().filter(ab -> ab.getMonitorId() == monitorID).findAny().isPresent())
				.collect(Collectors.toList());
		activitiesList.forEach(a -> activitiesModel.addElement(a.getActivityName()));
		activities.setModel(activitiesModel);

		// Force the panel to refresh
		SwingUtilities.invokeLater(() -> {
			memberList.setVisible(false);
			memberList.setVisible(true);
		});

	}
	private void updateSessionsCombo(){
		sessionsModel = new DefaultComboBoxModel<>();
		sessionsList = Database.getInstance()
				.getActivityBookings().stream().filter(ab -> ab.getActivityId() == getSelectedActivity().getActivityId() && ab.getMonitorId() == monitorID)
				.collect(Collectors.toList());
		sessionsList.stream().map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
				.forEach(sessionsModel::addElement);
		sessions.setModel(sessionsModel);
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
		activitiesModel = new DefaultComboBoxModel<>();
		updateActivitiesCombo();
		leftPanel.add(activities, c);

		c.gridx = 0;
		c.gridy++;

		leftPanel.add(new JLabel("Sesi\u00F3n:"), c);

		c.gridx++;

		sessions = new JComboBox<>();
		activities.addActionListener(l -> {
			memberList.removeAll();
			sessionsModel = new DefaultComboBoxModel<>();
			sessionsList = Database.getInstance().getActivityBookings().stream()
					.filter(ab -> ab.getActivityId() == getSelectedActivity().getActivityId())
					.collect(Collectors.toList());
			sessionsList.stream().map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
					.forEach(sessionsModel::addElement);
			sessions.setModel(sessionsModel);

			if (sessions.getModel().getSize() > 0) {
				sessions.setSelectedIndex(0);
			}
			refreshCentralPanelList(); // TODO poner esto y aqui pero en el de
										// activity??
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
		activityMembersInSession = new ArrayList<>();
		for (ActivityMember am : Database.getInstance().getActivityMembers()) {
			if (am.getActivityId() == getSelectedActivity().getActivityId()
					&& am.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId()
					&& !am.isDeleted()) {
				activityMembersInSession.add(am);

				Member member = Database.getInstance().getMemberById(am.getMemberId());

				JCheckBox chk = new JCheckBox(member.getMemberName());
				chk.setSelected(am.isAssistance());
				chk.addActionListener(l -> {
					am.setAssistance(chk.isSelected());
					try {
						am.update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
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

	private Component getThis() {
		return this;
	}

	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		add(bottomPanel, BorderLayout.SOUTH);

		bottomPanel.add(new JLabel("Socios apuntados:"));
		bottomPanel.add(assistanceLabel);

		addMember.addActionListener(l -> { // MARK
			try {// primero objenemos el numero del socio

				int memberId = Integer.valueOf(addMemberTextField.getText()); // peta
																				// aqui,
																				// numberFormatEx;
				if (Database.getInstance().getMembers().stream().filter(m -> m.getMemberId() == memberId)
						.toArray().length == 0) {
					JOptionPane.showMessageDialog(getThis(),
							"No se ha encontrado un cliente con el numero de socio introducido");
					return;
				}
				// segundo obtenemos la actividad
				int activityId = Database.getInstance().getActivities().get(activities.getSelectedIndex())
						.getActivityId();
				// tercero obtenemos la facilitybooking
				FacilityBooking facilityBooking = sessionsList.get(sessions.getSelectedIndex()).getFacilityBooking();
				// cuarto obtenemos el numero actual de invitados y el maximo
				Optional<Integer> assistantsOptional = getAssistantsOptional();
				Optional<Activity> activityOptional = getActivityOptional();
				int numeroMaximoApuntados = activityOptional.get().getAssistantLimit();
				int numeroActualApuntados = assistantsOptional.isPresent() ? assistantsOptional.get() : 0;

				ActivityMember newActivityMember = new ActivityMember(activityId,
						facilityBooking.getFacilityBookingId(), memberId);
				// CONDICIONES PARA AÑADIR:
				// COMO MONITOR: cupo no lleno Y entre 5 minutos antes de
				// empezar y 10 despues de haberlo hecho
				if (numeroActualApuntados >= numeroMaximoApuntados)
					JOptionPane.showMessageDialog(getThis(),
							"Error, la transaccion no se puede llevar a cabo porque la actividad ya esta completa de socios",
							"Error", JOptionPane.ERROR_MESSAGE, null);
				else if (!Utils.getCurrentTime().before(facilityBooking.getTimeStart())) {
					JOptionPane.showMessageDialog(getThis(),
							"Error, la transaccion no se puede llevar a cabo porque la actividad ya está en curso",
							"Error", JOptionPane.ERROR_MESSAGE, null);
				} else {
					newActivityMember.create(); // peta aqui, sqlEx
					Database.getInstance().getActivityMembers().add(newActivityMember);
					JOptionPane.showMessageDialog(getThis(), "A\u00F1adido correctamente", "Correcto",
							JOptionPane.INFORMATION_MESSAGE, null);
				}

			} catch (SQLException sql) {
				JOptionPane.showMessageDialog(getThis(),
						"Error, la transaccion no se ha llevado a cabo\nEl socio ya está en la lista de apuntados a la atividad",
						"Error", JOptionPane.ERROR_MESSAGE, null);
			} catch (NumberFormatException ex1) {
				JOptionPane.showMessageDialog(this, "Por favor, introduzca un número de socio");
			} finally {
				refreshCentralPanelList();
			}

		});

		bottomPanel.add(addMember);

		addMemberTextField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				if (addMemberTextField.getText().equals("Numero de Socio a a\u00F1adir"))
					addMemberTextField.setText("");
			}
		});
		bottomPanel.add(addMemberTextField);
	}

	private void createCenterPanel() {
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());

		JLabel colorlabel = new JLabel();
		colorlabel.setText("Lista de socios apuntados:");

		centerPanel.add(colorlabel, BorderLayout.NORTH);
		centerPanel.add(memberList, BorderLayout.CENTER);

		add(centerPanel, BorderLayout.CENTER);
	}

	private Optional<Integer> getAssistantsOptional() {
		return Database.getInstance().getActivityMembers().parallelStream()
				.filter(am -> am.getActivityId() == getSelectedActivity().getActivityId() && !am.isDeleted() && am
						.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId())
				.map(am -> am.isAssistance() ? 1 : 0).reduce(Integer::sum);
	}

	private Optional<Activity> getActivityOptional() {
		return Database.getInstance().getActivities().parallelStream()
				.filter(a -> a.getActivityName().equals(activities.getSelectedItem())).findAny();
	}

	private void refreshAssistanceCount() {
		Optional<Integer> assistantsOptional = getAssistantsOptional();
		Optional<Activity> activityOptional = getActivityOptional();
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
		return activitiesList.get(activities.getSelectedIndex());
	}

	private class CheckBoxList extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
