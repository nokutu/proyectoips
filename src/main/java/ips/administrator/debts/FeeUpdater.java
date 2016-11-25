package ips.administrator.debts;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ips.MainWindow;
import ips.database.Database;
import ips.database.FacilityBooking;
import ips.database.Fee;
import ips.database.Member;
import ips.utils.Utils;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * <p>
 * Implementa la funcionalidad de la historia de usuario #34.
 * </p>
 * 
 * <p>
 * Como contable quiero pasar los pagos por las reservas realizadas por los
 * socios a su cuota mensual para solventar las deudas acumuladas por las
 * reservas
 * </p>
 * 
 * <p>
 * El contable incrementará la cuota mensual de un socio para el siguiente mes
 * con el importe de las reservas de instalaciones pendientes de liquidar
 * (porque el socio ha seleccionado el pago a través de su cuota mensual o no se
 * ha registrado el pago en efectivo en su momento). Las reservas que se pasan a
 * la cuota serán desde el 20 del mes anterior hasta el 19 del actual y se
 * acumulan a la cuota del mes siguiente.
 * </p>
 * 
 * <p>
 * <em>En esta historia no se gestionarán las reservas de los clientes dados de
 * baja. Se dejará para más adelante de manera que se puedan obtener el listado
 * de los socios que se han dado de baja con pagos de reservas pendientes para
 * que se les avise vía telefónica y pasen a abonar el importe de manera
 * presencial.</em>
 * </p>
 * 
 * @author Sergio Florez
 *
 */
public class FeeUpdater extends JDialog {

	List<Member> sociosDeBajaConPagosPendientes = new ArrayList<Member>();
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JScrollPane scrollPane_east;
	private JScrollPane scrollPane_west;
	private JTextArea textArea_west;
	private JTextArea textArea_east;

	public FeeUpdater() {

		update();

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.5 };
		getContentPane().setLayout(gridBagLayout);
		GridBagConstraints gbc_scrollPane_west = new GridBagConstraints();
		gbc_scrollPane_west.gridwidth = 2;
		gbc_scrollPane_west.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_west.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_west.gridx = 0;
		gbc_scrollPane_west.gridy = 0;
		getContentPane().add(getScrollPane_west(), gbc_scrollPane_west);
		GridBagConstraints gbc_scrollPane_east = new GridBagConstraints();
		gbc_scrollPane_east.gridwidth = 2;
		gbc_scrollPane_east.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_east.gridx = 0;
		gbc_scrollPane_east.gridy = 1;
		getContentPane().add(getScrollPane_east(), gbc_scrollPane_east);

		setModalityType(ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(MainWindow.getInstance());
	}

	/**
	 * 
	 * @return una lista con los clientes que se han dado de baja y hay que
	 *         avisarles por telefono de que tienen pagos pendientes
	 */
	public List<Member> update() {
		Date now = Utils.getCurrentDate();

		// now
		Calendar cNow = Calendar.getInstance();
		cNow.setTime(now);

		// previous
		Calendar cPrevious = Calendar.getInstance();
		cPrevious.setTime(now);
		cPrevious.add(Calendar.MONTH, -1); // mes anterior

		// next
		Calendar cNext = Calendar.getInstance();
		cPrevious.setTime(now);
		cPrevious.add(Calendar.MONTH, 1); // mes siguiente

		List<FacilityBooking> pagosPendientes = Database.getInstance().getFacilityBookings().stream().filter(
				fb -> fb.getMemberId() != 0 && fb.getState().equals(FacilityBooking.STATE_VALID) && !fb.isPaid())
				.collect(Collectors.toList());

		for (FacilityBooking pago : pagosPendientes) {
			// try { pago.toString(); } catch (Exception ex) { }
			if (pago.getPaymentMethod().equals("Fee")
					|| (pago.getPaymentMethod().equals("Cash") && pago.getTimeStart().before(now))) {
				// si no borrada ni pagada o si ya ha pasado la hora pero el
				// socio no ha pagado, se le cobra

				Calendar cPago = Calendar.getInstance();
				cPago.setTime(pago.getTimeEnd());

				// pago entre el 20 del mes anterior y el 19 de este
				if ((cPago.get(Calendar.MONTH) == cNow.get(Calendar.MONTH) && cPago.get(Calendar.DAY_OF_MONTH) <= 19)
						|| (cPago.get(Calendar.MONTH) == cPrevious.get(Calendar.MONTH)
								&& cPago.get(Calendar.DAY_OF_MONTH) >= 20)) {
					Fee theFee = Fee.getOrCreate(pago.getMember(), new Timestamp(cNext.getTime().getTime()));
					// Fee thatFee =
					// Database.getInstance().getFeeByMonth(pago.getMemberId(),
					// nextMonth);
					// if exists a fee for that month
					double cost = pago.getFacility().getPrice();

					assert theFee.getMemberId() == pago.getMemberId(); // TODO
																		// eliminar
																		// esto

					theFee.addFeeItem(cost,
							pago.getPaymentMethod().equals("Fee") ? "Pago por cuota" : "Pago por deuda acumulada");

					pago.setPayed(true); // payed
					try {
						pago.update();
						getTextArea_west().setText(getTextArea_west().getText() + "\n"
								+ (pago.getPaymentMethod().equals("Fee") ? "Pago por cuota"
										: "Pago por deuda acumulada") + " del Socio: "
								+ pago.getMember().getMemberName() + "\n\tInstalacion: "
								+ pago.getFacility().getFacilityName() + " (" + pago.getFacility().getPrice() + "€)"
								+ "\n\tEstado: " + pago.getState());
					} catch (SQLException ex) {
						ex.printStackTrace();
					}

					Member socio = pago.getMember();
					if (!socio.isSubscribed()) { // si esta de baja
						sociosDeBajaConPagosPendientes.add(socio);
						getTextArea_east().setText("\n"+getTextArea_east().getText() + socio.getMemberName()+"(id:)"+socio.getMemberId()/*"\n"
								+ (pago.getPaymentMethod().equals("Fee") ? "Pago por cuota"
										: "Pago por deuda acumulada")
								+ " del  "\n\tInstalacion: "
								+ pago.getFacility().getFacilityName() + " (" + pago.getFacility().getPrice() + "€)"
								+ "\n\tEstado: " + pago.getState() + "\n\tDatos del socio: " + socio.getMemberName()*/);
					}
				} // end if month filter
			} // end main if
		} // end for
		if (pagosPendientes.size() > 0)
			this.setVisible(true);
		else
			JOptionPane.showMessageDialog(this, "No hay pagos pendientes de liquidar este mes");
		return sociosDeBajaConPagosPendientes;
	}

	private JScrollPane getScrollPane_east() {
		if (scrollPane_east == null) {
			scrollPane_east = new JScrollPane();
			scrollPane_east.setBorder(new TitledBorder(null, "Socios de baja con pagos pendientes",
					TitledBorder.LEADING, TitledBorder.TOP, null, null));
			scrollPane_east.setViewportView(getTextArea_east());
		}
		return scrollPane_east;
	}

	private JScrollPane getScrollPane_west() {
		if (scrollPane_west == null) {
			scrollPane_west = new JScrollPane();
			scrollPane_west.setBorder(
					new TitledBorder(null, "Deudas solventadas", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			scrollPane_west.setViewportView(getTextArea_west());
		}
		return scrollPane_west;
	}

	private JTextArea getTextArea_west() {
		if (textArea_west == null) {
			textArea_west = new JTextArea();
		}
		return textArea_west;
	}

	private JTextArea getTextArea_east() {
		if (textArea_east == null) {
			textArea_east = new JTextArea();
		}
		return textArea_east;
	}
}
