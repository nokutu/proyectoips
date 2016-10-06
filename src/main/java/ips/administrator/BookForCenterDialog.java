package ips.administrator;

import javax.swing.*;
import java.util.Date;

/**
 * Created by nokutu on 03/10/2016.
 */
public class BookForCenterDialog extends JDialog {

    private Date date;
    private int hourStart;
    private int hourEnd;

    public BookForCenterDialog(JFrame owner, Date date, int hourStart, int hourEnd) {
        super(owner, true);
        setResizable(false);

        this.date = date;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;

    }
}
