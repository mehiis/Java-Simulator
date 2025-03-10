package application.assets.model;

import application.assets.framework.Clock;
import application.assets.framework.Trace;
import application.assets.framework.*;

// TODO:
// Asiakas koodataan simulointimallin edellyttämällä tavalla (data!)
/**
 * Class for the apartment. The apartment is used to store the arrival and exit time of the customer.
 * Apartments queue at the garbage shelter.
 */
public class Apartment {
	private double arrivalTime;
	private double exitTime;
	private int id;
	private static int i = 1;
	private static long sum = 0;
	
	public Apartment(){
	    id = i++;
	    
		arrivalTime = Clock.getInstance().getTime();
		Trace.out(Trace.Level.INFO, "Uusi asiakas nro " + id + " saapui klo "+ arrivalTime);
	}

	public double getExitTime() {
		return exitTime;
	}

	public void setExitTime(double exitTime) {
		this.exitTime = exitTime;
	}

	public double getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(double saapumisaika) {
		this.arrivalTime = saapumisaika;
	}


	public int getId() {
		return id;
	}
	
	public void report(){
		Trace.out(Trace.Level.INFO, 	"\nAsiakas "+id+ " valmis! 	"								);
		Trace.out(Trace.Level.INFO, 	"Asiakas "	+id+ " saapui: 	" + arrivalTime					);
		Trace.out(Trace.Level.INFO,		"Asiakas "	+id+ " poistui: " + exitTime					);
		Trace.out(Trace.Level.INFO,		"Asiakas "	+id+ " viipyi: 	" +	(exitTime - arrivalTime)	);

		sum += (exitTime - arrivalTime);
		double average = sum/id;
		System.out.println("Asiakkaiden läpimenoaikojen keskiarvo tähän asti "+ average);
	}

}
