package ips;

import java.util.Date;

import ips.database.Database;
import ips.database.FacilityBooking;
import ips.database.Fee;
import ips.database.FeeItem;

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
public class FeeUpdater {

	@SuppressWarnings("deprecation")
	public static void update() {
		Date now = new Date();
		int previousMonth = now.getMonth() == 0 ? 11 : now.getMonth() - 1; // el anterior a enero es diciembre

		for (FacilityBooking pago : Database.getInstance().getFacilityBookings()) {
			if (!pago.isDeletedFlag() && !pago.isPaid() && pago.getPaymentMethod().equals("Fee")) { // si no borrada
																									// ni pagada

				if ((pago.getTimeEnd().getMonth() == now.getMonth() && pago.getTimeEnd().getDate() <= 19)
						|| (pago.getTimeEnd().getMonth() == previousMonth && pago.getTimeEnd().getDate() >= 20)) {

					now.setDate(1);
					int nextMonth = now.getMonth() == 11 ? 0 : now.getMonth() + 1;
					try {
						Fee thatFee = Database.getInstance().getFeeByMonth(pago.getMemberId(), nextMonth);
						// if exists a fee for that month
						int cost = Database.getInstance().getFacilityById(pago.getFacilityId()).getPrice();
						thatFee.getFeeItems().add(new FeeItem(cost, pago.getMemberId()));
						try {
							thatFee.update();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					} catch (RuntimeException e) {
						// else, dont exists a fee for that month
						now.setMonth(nextMonth);
						java.sql.Date now2 = new java.sql.Date(now.getTime());
						Fee newFee = new Fee(pago.getMemberId(), now2);
						Database.getInstance().getFees().add(newFee);
						try {
							newFee.create();
						} catch (Exception e3) {
							e3.printStackTrace();
						}
					} finally {
						pago.setPayed(true); // payed
					}

				} // end if month filter
			} // end main if
		} // end for
	}
}
