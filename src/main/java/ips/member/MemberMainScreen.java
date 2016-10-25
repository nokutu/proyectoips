package ips.member;

import ips.database.Database;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.BorderLayout;

/**
 * Created by nokutu on 24/10/2016.
 */
public class MemberMainScreen extends JPanel {

    public static int userID;

    private final JTextField userIDTextField;
    private JPanel rightPanel;
    private JPanel upperPanel;

    public MemberMainScreen() {
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());

        add(northPanel, BorderLayout.NORTH);

        upperPanel = new JPanel();
        northPanel.add(upperPanel, BorderLayout.CENTER);

        JPanel userIDPanel = new JPanel();
        northPanel.add(userIDPanel, BorderLayout.NORTH);

        userIDPanel.add(new JLabel("ID de socio:"));
        userIDTextField = new JTextField(10);
        userIDPanel.add(userIDTextField);
        userIDTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateUser(userIDTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateUser(userIDTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateUser(userIDTextField.getText());
            }
        });
        // TODO añadir el panel de Tony a center y los listeners que llamen a setRightPanel

        JButton activitiesButton = new JButton("Ver actividades");
        activitiesButton.addActionListener(l -> {
            if (userID != 0) {
                new MemberActivitiesDialog().setVisible(true);
            }
        });
        upperPanel.add(activitiesButton);

        setRightPanel(new MemberBookPanel());
    }

    public void setRightPanel(JPanel panel) {
        if (rightPanel != null) {
            remove(rightPanel);
        }
        add(panel, BorderLayout.EAST);
        rightPanel = panel;
    }

    private void updateUser(String id) {
        if (checkUser(id)) {
            userID = Integer.parseInt(id);
        } else {
            userID = 0;
        }
    }

    private boolean checkUser(String id) {
        try {
            return Database.getInstance().getMembers().stream().filter(m -> m.getMemberId() == Integer.parseInt(id)).findAny().isPresent();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
