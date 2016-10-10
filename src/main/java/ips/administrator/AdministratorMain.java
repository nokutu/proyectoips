package ips.administrator;

import ips.MainWindow;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Main panel for administration
 */
public class AdministratorMain extends JPanel {
    /**
     *
     */
    private static final long serialVersionUID = -4556204503732370163L;
    private JButton btnBookActivity;

    public AdministratorMain() {
        JButton book = new JButton("Book for a member");
        book.addActionListener((e) -> new BookForMemberDialog(MainWindow.getInstance()).setVisible(true));
        add(book);
        add(getBtnBookActivity());
    }

    private JButton getBtnBookActivity() {
        if (btnBookActivity == null) {
            btnBookActivity = new JButton("Book for the center");
            btnBookActivity.addActionListener(e -> {
                BookForCenterDialog adminbook = new BookForCenterDialog(MainWindow.getInstance().setVisible(true));
                adminbook.setVisible(true);
            });
        }
        return btnBookActivity;
    }
}
