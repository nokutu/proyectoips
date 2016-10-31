package ips.administrator;

import ips.MainWindow;
import ips.database.ActivityMember;
import ips.database.Database;
import ips.database.Member;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nokutu on 25/10/2016.
 */
public class AdministratorActivitiesDialog extends JDialog {

    private JList<String> memberList;
    private List<Member> membersInSession;

    public AdministratorActivitiesDialog() {
        super(MainWindow.getInstance(), true);

        setLayout(new BorderLayout());

        createLeftPanel();
        createBottomPanel();
        // TODO añadir el panel central
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
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 10, 2, 5);

        leftPanel.add(new JLabel("Actividad:"), c);

        c.gridx++;

        JComboBox<String> activities = new JComboBox<>();
        DefaultComboBoxModel<String> activitiesModel = new DefaultComboBoxModel<>();
        Database.getInstance().getActivities().forEach(a -> activitiesModel.addElement(a.getActivityName()));
        activities.setModel(activitiesModel);
        leftPanel.add(activities, c);

        c.gridx = 0;
        c.gridy++;

        leftPanel.add(new JLabel("Sesión:"), c);

        c.gridx++;

        JComboBox<String> sessions = new JComboBox<>();
        activities.addActionListener(l -> {
            DefaultComboBoxModel<String> sessionsModel = new DefaultComboBoxModel<>();
            Database.getInstance().getActivityBookings().stream()
                    .filter(ab -> ab.getActivityName().equals(Database.getInstance().getActivities().get(activities.getSelectedIndex()).getActivityName()))
                    .map(ab -> new SimpleDateFormat().format(ab.getFacilityBooking().getTimeStart()))
                    .forEach(sessionsModel::addElement);
            sessions.setModel(sessionsModel);
        });
        activities.setSelectedIndex(0);

        sessions.addActionListener(l -> {
            // TODO update central list
        	DefaultListModel<String> model=new DefaultListModel();
        	membersInSession=new ArrayList<>();
        	for(ActivityMember am :Database.getInstance().getActivityMembers())
        	{
        		if(am.getActivityName().equals(activities.getSelectedItem()))
        		{
        			Member member= Database.getInstance().getMemberById(am.getMemberId());
        			if(am.isAssistance())
        			{
        				//la lista contiene a los miembros asistentes
        				membersInSession.add(member);
        			}
        			model.addElement(member.getMemberName());
        		}
        	}
        	memberList.setModel(model);
        	memberList.setCellRenderer(new CheckboxCellRenderer());
        });

        leftPanel.add(sessions, c);
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        bottomPanel.add(new JLabel("Socios apuntados:"));
        JLabel asistanceLabel = new JLabel("0/25");
        bottomPanel.add(asistanceLabel);


        JButton addMember = new JButton("Apuntar a socio");
        bottomPanel.add(addMember);


    }

    private void createCenterPanel() {
        memberList = new JList<>();
        DefaultListModel<String> model=new DefaultListModel();
        memberList.setModel(model);
        memberList.setCellRenderer(new CheckboxCellRenderer());
        JLabel colorlabel=new JLabel();
        colorlabel.setText("Asistio/va a asistir: verde \n"
        		+ "No asistio: rojo \n");

        
        add(colorlabel,BorderLayout.NORTH);
        add(memberList, BorderLayout.CENTER);
    }
    protected class CheckboxCellRenderer implements ListCellRenderer 
    {

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			
			 JCheckBox checkbox = new JCheckBox((String)value);
			 boolean found=false;
			 for(Member m : membersInSession)
			 {
				 
				 if(m.getMemberName().equals((String)value))
				 {
					 found = true;
					 checkbox.setBackground(Color.green);
				 }			
			
			 }
			 
			 if(!found)
			 {
				 checkbox.setBackground(Color.red);
			 }
			return checkbox;
		}
    	
    }
}
