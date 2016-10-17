package ips.member;

import ips.MainWindow;
import ips.database.Database;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/**
 * Main panel for members.
 *
 * @since 03/10/2016
 */
public class MemberMain extends JPanel {

    public static int userID;

    private JTextField userIDField;
    private boolean userValid = false;
    private JPanel center;

    public MemberMain() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        userIDField = new JTextField(20);

        userIDField.setMaximumSize(userIDField.getPreferredSize());

        topPanel.add(new JLabel("ID de socio:"));
        topPanel.add(userIDField);
        add(topPanel, BorderLayout.NORTH);

        center = new JPanel();
        add(center, BorderLayout.CENTER);
        center.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        JButton bookForMember = new JButton("Reservar");
        bookForMember.addActionListener(l -> {
            if (checkUser(userIDField.getText())) {
                userID = Integer.parseInt(userIDField.getText());
                userValid = true;
            } else {
                userValid = false;
            }
            if (userValid) {
                new MemberBookingDialog(MainWindow.getInstance()).setVisible(true);
            }
        });
        center.add(bookForMember);
    }

    public boolean checkUser(String id) {
        try {
            return Database.getInstance().getMembers().stream().filter(m -> m.getMemberId() == Integer.parseInt(id)).findAny().isPresent();
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
