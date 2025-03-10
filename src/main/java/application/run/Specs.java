package application.run;
import application.assets.model.GarbageCan;
import application.assets.model.GarbageCanType;
import application.eduni.distributions.ContinuousGenerator;
import application.eduni.distributions.Normal;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * This class contains the specifications for the arrival process of residents to the shelter.
 * It calculates the mean arrival rate for the residents based on the amount of people living in the apartments and the amount of trash they produce.
 * In its own class to keep OwnEngine class clean.
 */
public class Specs {
    private static final double avgTrashAmountPerYearPerPerson = 307.7954819; // in kg
    private static double howOftenTrashIsTakenOut = 3.15; // default is in every 3.15 days

    public static double getMeanArrivalRate(int peopleAmt, int apartmentAmt, double meanTrashThrowAmt) {
        if (peopleAmt <= 0 || apartmentAmt <= 0 || meanTrashThrowAmt <= 0) {
            System.out.println("Invalid input. Please enter positive values.");
            return 0.1;
        } else {
            ContinuousGenerator generatorHowMuchPeopleTakeTrashOutAtOnceUsually = new Normal(meanTrashThrowAmt, 0.2); // an educated guess
            howOftenTrashIsTakenOut = 365 / ((avgTrashAmountPerYearPerPerson * peopleAmt) / generatorHowMuchPeopleTakeTrashOutAtOnceUsually.sample()); // in days
            double disposalsPerDay = 1 / howOftenTrashIsTakenOut;
            double meanArrivalRate = (1440 / disposalsPerDay) / apartmentAmt; // how often trash is taken, converted from days to in minutes. number of apartments of given size is taken into account here.
            System.out.println("mean arrival rate at shelter for apt. of "+peopleAmt+" people is: "+meanArrivalRate+" minutes.");
            return Math.abs(Math.max(0.1, meanArrivalRate));
        }
    }
}
