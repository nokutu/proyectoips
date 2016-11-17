package ips.member;

import ips.MainWindow;
import ips.utils.Utils;
import ips.database.Activity;
import ips.database.ActivityBooking;
import ips.database.ActivityMember;
import ips.database.Database;
import ips.database.FacilityBooking;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class MemberActivitiesDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ActivityBooking> activityBookings;
	private JList<String> activitiesList;
	JComboBox<String> activitiesComboBox;
	private List<ActivityMember> activityMembers;
	private JButton remove;
	private JComboBox<String> sessions;
	private List<ActivityBooking> sessionsList;
	private DefaultComboBoxModel<String> sessionsModel;

	public MemberActivitiesDialog() {
		super(MainWindow.getInstance(), true);

		setLayout(new BorderLayout());

		createLeftPanel();
		createRightPanel();
		createCenterPanel();

		setMinimumSize(new Dimension(320, 180));
		pack();
		setLocationRelativeTo(MainWindow.getInstance());
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
		c.insets = new Insets(2, 10, 2, 0);

		leftPanel.add(new JLabel("Actividad:"), c);

		c.gridx++;

		activitiesComboBox = new JComboBox<>();
		DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
		Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
		activitiesComboBox.setModel(activitiesModel);
		leftPanel.add(activitiesComboBox, c);

		c.gridx = 0;
		c.gridy++;

		leftPanel.add(new JLabel("Sesi\u00F3n:"), c);

		c.gridx++;

		sessions = new JComboBox<>();
		updateSessions();
		//activitiesComboBox.setModel(activitiesModel);
		activitiesComboBox.addActionListener(l -> {
			updateSessions();

		});
		if (sessions.getModel().getSize() > 0) {
			sessions.setSelectedIndex(0);
		}
		

		leftPanel.add(sessions, c);

		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth = 2;
		c.gridy++;
		c.gridx = 0;

		JButton join = new JButton("Apuntarse");
		join.addActionListener(l -> {
			try {
				// primero obtenemos el numero del socio
				int memberId = MemberMainScreen.userID;

				// segundo obtenemos la actividad
				int activityId = Database.getInstance().getActivities().get(activitiesComboBox.getSelectedIndex())
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
				// COMO SOCIO: cupo no lleno Y entre 24 horas antes de
				// empezar y 1 hora despues de la hora de hacerlo
				if (numeroActualApuntados >= numeroMaximoApuntados)
					JOptionPane.showMessageDialog(getThis(), "Lo sentimos, la actividad no tiene mas plazas libres",
							"Error", JOptionPane.ERROR_MESSAGE, null);
				else if (Utils.getCurrentTime().before(Utils.addHourToDay(facilityBooking.getTimeStart(), -24))) {
					JOptionPane.showMessageDialog(getThis(),
							"Lo sentimos, ud. aun no puede apuntarse a esta actividad. Solo 24 horas antes", "Error",
							JOptionPane.ERROR_MESSAGE, null);
				} else if (Utils.getCurrentTime().after(Utils.addHourToDay(facilityBooking.getTimeStart(), -1)))
					JOptionPane.showMessageDialog(getThis(),
							"Lo sentimos, ud. ya no puede apuntarse a esta actividad.\nEl plazo finaliza 1 hora antes del inicio de la actividad", "Error",
							JOptionPane.ERROR_MESSAGE, null);
				else {
					newActivityMember.create(); // peta aqui, sqlEx
					Database.getInstance().getActivityMembers().add(newActivityMember);
					JOptionPane.showMessageDialog(getThis(), "A\u00F1adido correctamente", "Correcto",
							JOptionPane.INFORMATION_MESSAGE, null);
				}

			} catch (SQLException sql) {
				JOptionPane.showMessageDialog(getThis(),
						"Error, la transaccion no se ha llevado a cabo\nUd. ya está apuntado a la atividad selecccionada",
						"Error", JOptionPane.ERROR_MESSAGE, null);
				sql.printStackTrace();
				return;
			}

		});

		leftPanel.add(join, c);
	}
	private void updateSessions(){
		sessionsModel = new DefaultComboBoxModel<>();
		sessionsList = Database.getInstance()
				.getActivityBookings().stream().filter(ab -> ab.getActivityId() == Database.getInstance()
						.getActivities().get(activitiesComboBox.getSelectedIndex()).getActivityId())
				.collect(Collectors.toList());
		sessionsList.stream().map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
				.forEach(sessionsModel::addElement);
		sessions.setModel(sessionsModel);
	}

	private Component getThis() {
		return this;
	}

	private Activity getSelectedActivity() {
		return Database.getInstance().getActivities().get(activitiesComboBox.getSelectedIndex());
	}

	private Optional<Integer> getAssistantsOptional() {
		return Database.getInstance().getActivityMembers().parallelStream()
				.filter(am -> am.getActivityId() == getSelectedActivity().getActivityId() && !am.isDeleted() && am
						.getFacilityBookingId() == sessionsList.get(sessions.getSelectedIndex()).getFacilityBookingId())
				.map(am -> am.isAssistance() ? 1 : 0).reduce(Integer::sum);
	}

	private Optional<Activity> getActivityOptional() {
		return Database.getInstance().getActivities().parallelStream()
				.filter(a -> a.getActivityName().equals(activitiesComboBox.getSelectedItem())).findAny();
	}

	private void createRightPanel() {
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		add(rightPanel, BorderLayout.EAST);

		rightPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.LINE_START;
		c.insets = new Insets(2, 5, 2, 10);

		remove = new JButton("Borrarse");

		remove.addActionListener(l -> {

			ActivityMember am = activityMembers.get(activitiesList.getSelectedIndex());
			if (!am.isDeleted()) {
				am.setDeleted(true);
				try {
					am.update();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				refreshActivitiesList();

			} else {
				JOptionPane.showMessageDialog(this, "Ya has sido desapuntado de esta actividad");
			}
		});
		rightPanel.add(remove, c);
	}

	private void createCenterPanel() {
		activitiesList = new JList<>();
		add(activitiesList, BorderLayout.CENTER);
		refreshActivitiesList();
	}

	private void refreshActivitiesList() {
		DefaultListModel<String> m = new DefaultListModel<>();
		activityMembers = Database.getInstance().getActivityMembers().stream()
				.filter(am -> !am.isDeleted() && am.getMemberId() == MemberMainScreen.userID
						&& am.getFacilityBooking().getTimeStart().after(Utils.getCurrentTime()))
				.collect(Collectors.toList());
		if (activityMembers.size() == 0) {
			remove.setEnabled(false);
		}
		activityMembers.stream().map(am -> am.getActivity().getActivityName() + " - "
				+ new SimpleDateFormat().format(am.getFacilityBooking().getTimeStart())).forEach(m::addElement);
		activitiesList.setModel(m);
	}
}
