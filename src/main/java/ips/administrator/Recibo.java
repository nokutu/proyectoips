package ips.administrator;

import java.io.*;
import java.sql.Timestamp;
import java.util.Date;

import ips.database.FacilityBooking;

public class Recibo 
{
	FacilityBooking done ;
	String nombreFichero="recibo";

	public Recibo(FacilityBooking done) 
	{
		super();
		this.done = done;
	}
	
	public void grabarRecibo()
	{
		try {
		        BufferedWriter fichero = new BufferedWriter(new FileWriter("files/" + nombreFichero + ".dat"));
		        String linea =done.toString();
		        
		        Date date= new Date();
		        linea+="Was paid at "+new Timestamp(date.getTime())+" \n";
		 
		        fichero.write(linea);
		        fichero.close();
		        System.out.println("Guardado con exito");
			}

		catch (FileNotFoundException fnfe) {
		      System.out.println("El archivo no se ha podido guardar");
		    }
		catch (IOException ioe) {
		      new RuntimeException("Error de entrada/salida");
		}
	  }
	
	
	
}
