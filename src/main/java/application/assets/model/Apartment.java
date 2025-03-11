package application.assets.model;

import application.assets.framework.Clock;
import application.assets.framework.Trace;

/**
 * Class for the apartment. The apartment is used to store the arrival and exit time of the customer.
 * Apartments queue at the garbage shelter.
 */
public class Apartment {
    private double arrivalTime;
    private int id;
    private static int i = 1;

    public Apartment() {
        id = i++;

        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "Apartment " + id + " arrives at " + arrivalTime + " minutes.");
    }
}