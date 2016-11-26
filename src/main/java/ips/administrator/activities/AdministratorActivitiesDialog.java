package ips.administrator.activities;

import ips.utils.Utils;
import ips.database.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class AdministratorActivitiesDialog extends JDialog {

	private CheckBoxList memberList = new CheckBoxList();

	private List<ActivityBooking> sessionsList;
	private List<Member> membersInSession;
	private List<ActivityMember> activityMembersInSession;
	private List<Activity> activitiesInComboboxList=new ArrayList<>();
	private List<JCheckBox> activityMembersInSessionChk;

	private JLabel assistanceLabel = new JLabel("");
	private JComboBox<String> activities;
	private JButton addMember = new JButton("Apuntar al socio:");
	private TextField addMemberTextField = new TextField("Numero de Socio a a\u00F1adir");
	private JComboBox<String> sessions;
	// private List<ActivityBooking> activityBookingsList;

	public AdministratorActivitiesDialog(JDialog owner) {
		super(owner, true);

		setLayout(new BorderLayout());

		createLeftPanel();
		createBottomPanel();
		createCenterPanel();
		createRightPanel();

		setMinimumSize(new Dimension(320, 180));
		pack();
		setLocationRelativeTo(owner);
	}

	private void createRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.CENTER;

		JButton remove = new JButton("Borrar seleccionados");
		remove.addActionListener(l -> {
			for (int i = 0; i < membersInSession.size(); i++) {
				if (activityMembersInSessionChk.get(i).isSelected()) {
					try {
						activityMembersInSession.get(i).setDeleted(true);
						activityMembersInSession.get(i).update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			refreshCentralPanelList();
		});
		rightPanel.add(remove, c);

		add(rightPanel, BorderLayout.EAST);
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
		Database.getInstance().getActivities().stream().filter(a->!a.isDeleted()).forEach(a ->{
			activitiesInComboboxList.add(a);
			activitiesModel.addElement(a.getActivityName());
		});
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
			
			/*sessionsList = Database.getInstance().getActivityBookings().stream()
					.filter(ab -> ab.getActivityId() == getSelectedActivity().getActivityId())
					.collect(Collectors.toList());
			*/
			sessionsList = Database.getInstance().getActivityBookings().stream().filter(ab -> {
				Date hora_actual = new Date();
				Date hora_inicio = ab.getFacilityBooking().getTimeStart();

				return ab.getActivityId() == getSelectedActivity().getActivityId()
						&& hora_actual.before(hora_inicio);
			}).collect(Collectors.toList());
			
			sessionsList.stream().map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
					.forEach(sessionsModel::addElement);
			sessions.setModel(sessionsModel);

			if (sessions.getModel().getSize() > 0) {
				sessions.setSelectedIndex(0);
			}
			if(sessions.getSelectedIndex()==-1)addMember.setEnabled(false);
			else addMember.setEnabled(true);
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

	private AdministratorActivitiesDialog getThis() {
		return this;
	}

	private void createBottomPanel() {
		JPanel bottomPanel = new JPanel();
		add(bottomPanel, BorderLayout.SOUTH);

		bottomPanel.add(new JLabel("Socios apuntados:"));
		bottomPanel.add(assistanceLabel);

		addMember.addActionListener(l -> {
			if(sessions.getSelectedIndex()==-1){
				JOptionPane.showMessageDialog(this, "No hay sesiones disponibles");
				return;
			}
				
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
				int activityId = activitiesInComboboxList.get(activities.getSelectedIndex())
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
				// COMO ADMIN: 24 horas antes Y num socios apuntados no exceda
				// maximo posible Y no este ya en otra reserva o actividad
				if (numeroMaximoApuntados!=-1 && numeroActualApuntados >= numeroMaximoApuntados)
					JOptionPane.showMessageDialog(getThis(),
							"Error, la transaccion no se puede llevar a cabo porque la actividad ya esta completa de socios",
							"Error", JOptionPane.ERROR_MESSAGE, null);
				else if (!Utils.getCurrentTime().before(Utils.addHourToDay(facilityBooking.getTimeStart(), 24))) {
					JOptionPane.showMessageDialog(getThis(),
							"Error, Un socio solo puede apuntarse a una actividad 24 horas antes de su inicio", // TODO
																												// probar
							"Error", JOptionPane.ERROR_MESSAGE, null);
					if (Database.getInstance().getFacilityBookings().stream()
							.filter(fb -> (fb.getMemberId() == newActivityMember.getMemberId()
									&& (fb.getTimeStart().after(newActivityMember.getFacilityBooking().getTimeStart())
											|| fb.getTimeStart()
													.equals(newActivityMember.getFacilityBooking().getTimeStart()))))
							.findAny().isPresent()) {
						JOptionPane.showMessageDialog(getThis(),
								"Error, El socio ya tiene una reserva para ese momento",
								"Error", JOptionPane.ERROR_MESSAGE, null);

					}
					if (Database.getInstance().getActivityMembers().stream()
							.filter(am -> (am.getMemberId() == newActivityMember.getMemberId()
									&& (am.getFacilityBooking().getTimeStart().after(newActivityMember.getFacilityBooking().getTimeStart())
											|| am.getFacilityBooking().getTimeStart()
													.equals(newActivityMember.getFacilityBooking().getTimeStart()))))
							.findAny().isPresent()) {
						JOptionPane.showMessageDialog(getThis(),
								"Error, El socio ya tiene una reserva para ese momento",
								"Error", JOptionPane.ERROR_MESSAGE, null);

					}
				} else {
					Optional<ActivityMember> a = Database.getInstance().getActivityMembers().stream()
							.filter(am -> am.isDeleted() && am.equals(newActivityMember)).findAny();
					if (a.isPresent()) {
						ActivityMember b = a.get();
						b.setDeleted(false);
						b.update();
					} else {
						newActivityMember.create(); // peta aqui, sqlEx
						Database.getInstance().getActivityMembers().add(newActivityMember);
					}
					JOptionPane.showMessageDialog(getThis(), "A\u00F1adido correctamente", "Correcto",
							JOptionPane.INFORMATION_MESSAGE, null);
				}

			} catch (SQLException sql) {
				JOptionPane.showMessageDialog(getThis(),
						"Error, la transacción no se ha llevado a cabo\nEl socio ya está en la lista de apuntados a la atividad",
						"Error", JOptionPane.ERROR_MESSAGE, null);
				return;
			} catch (NumberFormatException ex1) {
				JOptionPane.showMessageDialog(this, "Por favor, introduzca un número de socio");
				return;
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
		if(sessions.getSelectedIndex()==-1){
			assistanceLabel.setText("-");
			return;
		}
		Optional<Integer> assistantsOptional = getAssistantsOptional();
		Optional<Activity> activityOptional = getActivityOptional();
		if (activityOptional.isPresent()) {
			if (assistantsOptional.isPresent()) {
				// n assistants
				assistanceLabel.setText(assistantsOptional.get() + (activityOptional.get().getAssistantLimit()==-1?
						" (sin limitede plazas)"
						: "/" + activityOptional.get().getAssistantLimit()));
				addMember.setEnabled(assistantsOptional.get() < activityOptional.get().getAssistantLimit());
			} else {
				// 0 assistants
				assistanceLabel.setText("0/" + (activityOptional.get().getAssistantLimit()==-1?
						" (sin limitede plazas)"
						: "/" + activityOptional.get().getAssistantLimit()));
				addMember.setEnabled(true);
			}
		} else {
			// no activity selected
			assistanceLabel.setText("");
			addMember.setEnabled(false);
		}
		pack();
	}

	private Activity getSelectedActivity() {
		return activitiesInComboboxList.get(activities.getSelectedIndex());
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
