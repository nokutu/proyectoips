package ips.administrator;

import ips.database.FacilityBooking;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class Recibo {
    FacilityBooking done;
    String nombreFichero = "recibo";

    public Recibo(FacilityBooking done) {
        super();
        this.done = done;
    }

    public void grabarRecibo() {
        try {
            BufferedWriter fichero = new BufferedWriter(new FileWriter("files/" + nombreFichero + ".dat"));
            String linea = done.toStringFull();

            Date date = new Date();
            linea += "Fue pagado en " + new Timestamp(date.getTime()) + " \n";

            fichero.write(linea);
            fichero.close();
            System.out.println("Guardado con exito");
        } catch (FileNotFoundException fnfe) {
            System.out.println("El archivo no se ha podido guardar");
        } catch (IOException ioe) {
            throw new RuntimeException("Error de entrada/salida");
        }
    }
}
