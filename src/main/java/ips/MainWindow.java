package ips;


import ips.database.Database;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/**
 * Created by nokutu on 3/10/16.
 */
public class MainWindow extends JFrame {

    /**
     *
     */
    private static final long serialVersionUID = -2273433528921048086L;

    private static MainWindow instance;

    private JPanel mainPanel;

    private JPanel center;

    private MainWindow() {
        super();
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        /*try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }*/
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(Bar.getInstance(), BorderLayout.NORTH);

        setContentPane(mainPanel);

        setContent(new ChoosePanel());

        pack();
        setLocationRelativeTo(null);
    }

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    /**
     * Changes the view in the center area.
     *
     * @param p The {@link JPanel} that is going to be shown.
     */
    public void setContent(JPanel p) {
        if (center != null) {
            mainPanel.remove(center);
        }
        mainPanel.add(p, BorderLayout.CENTER);
        center = p;

        // Tell the window to redraw the contents
        repaint();
        revalidate();
    }

    @Override
    public void dispose() {
        Database.getInstance().close();
        super.dispose();
    }
}
