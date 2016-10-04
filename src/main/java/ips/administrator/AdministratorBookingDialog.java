package ips.administrator;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Date;

/**
 * Created by nokutu on 03/10/2016.
 */
public class AdministratorBookingDialog extends JDialog 
{

	private Date date;
	private int hourStart;
	private int hourEnd;

	public AdministratorBookingDialog(JFrame owner, Date date, int hourStart, int hourEnd) 
    {
        // TODOpublic BookingDialog(JFrame owner, Date date, int hourStart, int hourEnd) {
        super(owner);
        setResizable(false);

        this.date = date;
        this.hourStart = hourStart;
        this.hourEnd = hourEnd;

    }
}
