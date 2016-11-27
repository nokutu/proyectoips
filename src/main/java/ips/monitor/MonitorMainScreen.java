package ips.monitor;

import ips.utils.Utils;
import ips.database.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by nokutu on 25/10/2016.
 */
public class MonitorMainScreen extends JPanel {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static int monitorID;

	private CheckBoxList memberList = new CheckBoxList();
	private List<ActivityBooking> sessionsList;

	private List<ActivityMember> activityMembersInSession;
	private JLabel assistanceLabel = new JLabel("");
	private JComboBox<String> activities;
	private JButton addMember = new JButton("Apuntar al socio:");
	private TextField addMemberTextField = new TextField("Numero de Socio a a\u00F1adir");

	private JComboBox<Monitor> monitorIDComboBox;
	private List<Activity> activitiesList;

	private JTextField txtActividadnombre;
	private JTextField txtFechaInicio;
	private int facilitybooking_id;

	private int activity_id;

	private JPanel panel = new JPanel();

	private JLabel lblPrximaActividad;

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
		// refreshLeftCombos(); 
		monitorIDComboBox.addActionListener(l -> {
			monitorID = ((Monitor) monitorIDComboBox.getSelectedItem()).getMonitorId();
			updateLeftPanel();
			refreshCentralPanelList();
			refreshAssistanceCount();
			//createLeftPanel();
			//createCenterPanel();
			
		});
		monitorIDPanel.add(monitorIDComboBox);
	}
		
	private void createLeftPanel() {
		panel.setLayout(new GridLayout(1, 2, 0, 0));
		add(panel, BorderLayout.CENTER);

		JPanel leftPanel = new JPanel();
		panel.add(leftPanel);
		leftPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));

		GridBagLayout gbl_leftPanel = new GridBagLayout();
		gbl_leftPanel.columnWeights = new double[] { 0.0, 1.0 };
		leftPanel.setLayout(gbl_leftPanel);

		lblPrximaActividad = new JLabel("Proxima actividad:");
		GridBagConstraints gbc_lblPrximaActividad = new GridBagConstraints();
		gbc_lblPrximaActividad.insets = new Insets(0, 0, 5, 0);
		gbc_lblPrximaActividad.gridx = 1;
		gbc_lblPrximaActividad.gridy = 0;
		leftPanel.add(lblPrximaActividad, gbc_lblPrximaActividad);

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(2, 10, 5, 5);

		JLabel label = new JLabel("Actividad:");
		leftPanel.add(label, c);

		txtActividadnombre = new JTextField();
		txtActividadnombre.setEditable(false);
		GridBagConstraints gbc_txtActividadnombre = new GridBagConstraints();
		gbc_txtActividadnombre.insets = new Insets(0, 0, 5, 0);
		gbc_txtActividadnombre.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtActividadnombre.gridx = 1;
		gbc_txtActividadnombre.gridy = 1;
		leftPanel.add(txtActividadnombre, gbc_txtActividadnombre);
		txtActividadnombre.setColumns(10);

		GridBagConstraints E = new GridBagConstraints();
		E.gridx = 0;
		E.gridy = 2;
		E.anchor = GridBagConstraints.EAST;
		E.insets = new Insets(2, 10, 0, 5);
		E.gridx = 0;

		JLabel label_1 = new JLabel("Sesi\u00F3n:");
		leftPanel.add(label_1, E);

		txtFechaInicio = new JTextField();
		txtFechaInicio.setEditable(false);
		GridBagConstraints gbc_txtFechaInicio = new GridBagConstraints();
		gbc_txtFechaInicio.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFechaInicio.gridx = 1;
		gbc_txtFechaInicio.gridy = 2;
		leftPanel.add(txtFechaInicio, gbc_txtFechaInicio);
		txtFechaInicio.setColumns(10);
		
		updateLeftPanel();
		

	}
	
	private void updateLeftPanel() {
		String queryCurrentActivity = "SELECT * FROM ACTIVITYBOOKING,ACTIVITY, FACILITYBOOKING WHERE ACTIVITY.ACTIVITY_ID=ACTIVITYBOOKING.ACTIVITY_ID AND FACILITYBOOKING.FACILITYBOOKING_ID=ACTIVITYBOOKING.FACILITYBOOKING_ID AND DELETED=FALSE AND STATE='Valid' AND MONITOR_ID=? and current_timestamp() between time_start and time_end order by TIME_START asc";
		String queryNextActivity = "SELECT * FROM ACTIVITYBOOKING,ACTIVITY, FACILITYBOOKING WHERE ACTIVITY.ACTIVITY_ID=ACTIVITYBOOKING.ACTIVITY_ID AND FACILITYBOOKING.FACILITYBOOKING_ID=ACTIVITYBOOKING.FACILITYBOOKING_ID AND DELETED=FALSE AND MONITOR_ID=? and time_start>current_timestamp() order by TIME_START asc;";
		PreparedStatement pst;
		try {
			pst = Database.getInstance().getConnection().prepareStatement(queryCurrentActivity);
			pst.setInt(1, ((Monitor) monitorIDComboBox.getSelectedItem()).getMonitorId());
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				activity_id = rs.getInt("activity.activity_id");
				facilitybooking_id = rs.getInt("facilitybooking_id");
				txtActividadnombre.setText(rs.getString("activity_name"));
				txtFechaInicio.setText(new SimpleDateFormat().format(rs.getTimestamp("time_start"))
						+" - "
						+new SimpleDateFormat().format(rs.getTimestamp("time_end")));
				lblPrximaActividad.setText("Actividad Actual:");
				addMember.setEnabled(true);

			} else {
				PreparedStatement pst2;
				pst2 = Database.getInstance().getConnection().prepareStatement(queryNextActivity);
				pst2.setInt(1, ((Monitor) monitorIDComboBox.getSelectedItem()).getMonitorId());
				ResultSet rs2 = pst2.executeQuery();
				if (rs2.next()) {
					activity_id = rs2.getInt("activity.activity_id");
					facilitybooking_id = rs2.getInt("facilitybooking_id");
					txtActividadnombre.setText(rs2.getString("activity_name"));
					txtFechaInicio.setText(new SimpleDateFormat().format(rs2.getTimestamp("time_start"))
							+" - "
							+new SimpleDateFormat().format(rs2.getTimestamp("time_end")));
					lblPrximaActividad.setText("Próxima actividad:");
					addMember.setEnabled(true);
				} else {
					txtActividadnombre.setText("No hay ninguna actividad planificada");
					txtFechaInicio.setText(" - ");
					lblPrximaActividad.setText("Próxima actividad:");
					addMember.setEnabled(false);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			
		}

	}
	

	private void refreshCentralPanelList() {
		memberList.removeAll();
		activityMembersInSession = new ArrayList<>();
		for (ActivityMember am : Database.getInstance().getActivityMembers()) {
			if (am.getActivityId() == activity_id
					&& am.getFacilityBookingId() == facilitybooking_id
					&& !am.isDeleted()) {
				activityMembersInSession.add(am);

				Member member = Database.getInstance().getMemberById(am.getMemberId());

				JCheckBox chk = new JCheckBox(member.getMemberName());
				chk.setSelected(am.isAssistance());
				if(am.getFacilityBooking().getTimeStart().after(Utils.getCurrentTime())||am.getFacilityBooking().getTimeEnd().before(Utils.getCurrentTime()))
					chk.setEnabled(false);
				chk.addActionListener(l -> {
					am.setAssistance(chk.isSelected());
					try {
						am.update();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				});
				memberList.addLine(chk);
				refreshAssistanceCount();
			}
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
				int activityId = activity_id;
				// tercero obtenemos la facilitybooking
				FacilityBooking facilityBooking = Database.getInstance().getFacilityBookingById(facilitybooking_id); // XXX
				// cuarto obtenemos el numero actual de invitados y el maximo
				Optional<Integer> assistantsOptional = getAssistantsOptional();
				Optional<Activity> activityOptional = getActivityOptional();
				int numeroMaximoApuntados = activityOptional.get().getAssistantLimit();
				int numeroActualApuntados = assistantsOptional.isPresent() ? assistantsOptional.get() : 0;

				ActivityMember newActivityMember = new ActivityMember(activityId,
						facilityBooking.getFacilityBookingId(), memberId);
				// CONDICIONES PARA AÑADIR:
				// COMO MONITOR: desde 5 minutos antes Y num socios apuntados no exceda
				// el maximo posible Y no esté ya en otra reserva o actividad
				if (numeroMaximoApuntados!=-1 && numeroActualApuntados >= numeroMaximoApuntados)
					JOptionPane.showMessageDialog(getThis(),
							"Error, la transaccion no se puede llevar a cabo porque la actividad ya esta completa de socios",
							"Error", JOptionPane.ERROR_MESSAGE, null);
				else if (Utils.getCurrentTime().before(Utils.addMinutesToHour(facilityBooking.getTimeStart(), -5))) {
					JOptionPane.showMessageDialog(getThis(),
							"Error, Solo se puede apuntar a partir de los 5 minutos antes de comenzar la clase",
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
		panel.add(centerPanel);
		centerPanel.setLayout(new BorderLayout());

		JLabel colorlabel = new JLabel();
		colorlabel.setText("Lista de socios apuntados:");

		centerPanel.add(colorlabel, BorderLayout.NORTH);
		centerPanel.add(memberList, BorderLayout.CENTER);
		
		refreshCentralPanelList();
	}

	private Optional<Integer> getAssistantsOptional() {
		//Object[] aux = Database.getInstance().getActivityMembers().parallelStream().filter(am -> am.getActivityId() == activity_id && !am.isDeleted() && am.getFacilityBookingId() == facilitybooking_id).toArray();
		return Database.getInstance().getActivityMembers().parallelStream()
				.filter(am -> am.getActivityId() == activity_id && !am.isDeleted() && am
						.getFacilityBookingId() == facilitybooking_id)
				.map(am -> am.isAssistance() ? 1 : 0).reduce(Integer::sum);
	}

	private Optional<Activity> getActivityOptional() {
		return Database.getInstance().getActivities().parallelStream()
				.filter(a -> a.getActivityId()==activity_id).findAny();
	}
	private void refreshAssistanceCount() {
        if (txtFechaInicio.getText().equals(" - ")) {
            assistanceLabel.setText("-");
            return;
        }
        Optional<Integer> assistantsOptional = getAssistantsOptional();
        Optional<Activity> activityOptional = getActivityOptional();
        if (activityOptional.isPresent()) {
            if (assistantsOptional.isPresent()) {
                // n assistants
                assistanceLabel.setText(assistantsOptional.get() + (activityOptional.get().getAssistantLimit() == -1 ?
                        " (sin limitede plazas)"
                        : "/" + activityOptional.get().getAssistantLimit()));
                addMember.setEnabled(activityOptional.get().getAssistantLimit() == -1 || assistantsOptional.get() < activityOptional.get().getAssistantLimit());
            } else {
                // 0 assistants
                assistanceLabel.setText("0" + (activityOptional.get().getAssistantLimit() == -1 ?
                        " (sin limitede plazas)"
                        : "/" + activityOptional.get().getAssistantLimit()));
                addMember.setEnabled(true);
            }
        } else {
            // no activity selected
            assistanceLabel.setText("");
            addMember.setEnabled(false);
        }
    }

	/*private void refreshAssistanceCount() {
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
	}*/

	private Activity getSelectedActivity() {
		return Database.getInstance().getActivityById(activity_id);
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
