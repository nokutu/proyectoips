package ips;


import javax.swing.*;
import java.awt.*;

/**
 * Created by nokutu on 3/10/16.
 */
public class MainWindow extends JFrame {

    private static MainWindow instance;

    private JPanel mainPanel;

    private JPanel center;

    private MainWindow() {
        super();
        setPreferredSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(Bar.getInstance(), BorderLayout.NORTH);

        setContentPane(mainPanel);

        setContent(new ChoosePanel());

        pack();
    }

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    /**
     * Changes the view in the center area.
     * @param p The {@link JPanel} that is going to be shown.
     */
    public void setContent(JPanel p) {
        if (center != null) {
            mainPanel.remove(center);
        }
        mainPanel.add(p, BorderLayout.CENTER);
        center = p;
        repaint();
    }
}
