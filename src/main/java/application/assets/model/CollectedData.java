/**
 * @version 1.0
 * @since 8.March.2025
 * This object is created inside {@link GarbageShelter}, and is in charge of storing all collected data which is obtained from executed simulation.
 */
package application.assets.model;

import application.assets.framework.Clock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CollectedData {
    private final ArrayList<GarbageCan> garbageCans;
    private final double[] thrashTotalInLitres = new double[GarbageCanType.values().length];
    ;
    private final double[] thrashThrownInKg = new double[GarbageCanType.values().length];
    private final HashMap<GarbageCanType, LinkedList<Double>> shelterUsageRate = new HashMap<>() {
        {
            put(GarbageCanType.MIXED, new LinkedList<>());
            put(GarbageCanType.BIO, new LinkedList<>());
            put(GarbageCanType.CARDBOARD, new LinkedList<>());
            put(GarbageCanType.PLASTIC, new LinkedList<>());
            put(GarbageCanType.GLASS, new LinkedList<>());
            put(GarbageCanType.METAL, new LinkedList<>());
        }
    };

    private final HashMap<GarbageCanType, Integer> failedAttempts = new HashMap<>() {
        {
            put(GarbageCanType.MIXED, 0);
            put(GarbageCanType.BIO, 0);
            put(GarbageCanType.CARDBOARD, 0);
            put(GarbageCanType.PLASTIC, 0);
            put(GarbageCanType.GLASS, 0);
            put(GarbageCanType.METAL, 0);
        }
    };

    private int howManyTimeThrashThrown = 0;
    private int howManyTimesCarArrived = 0;

    private final HashMap<GarbageCanType, FullnessCalculator> howManyMinutesWasFull = new HashMap<>() {
        {
            put(GarbageCanType.MIXED, new FullnessCalculator());
            put(GarbageCanType.BIO, new FullnessCalculator());
            put(GarbageCanType.CARDBOARD, new FullnessCalculator());
            put(GarbageCanType.PLASTIC, new FullnessCalculator());
            put(GarbageCanType.GLASS, new FullnessCalculator());
            put(GarbageCanType.METAL, new FullnessCalculator());
        }
    };

    public CollectedData(ArrayList<GarbageCan> garbageCans) {
        this.garbageCans = garbageCans;
    }

    /**
     * Adds one to thrash counter, each time thrash has been thrown.
     */
    public void addThrownThrash() {
        howManyTimeThrashThrown++;
    }

    /**
     * Adds one to garbage car arrival counter, each time the garbage shelter has been cleared.
     */
    public void addGarbageCarArrival() {
        howManyTimesCarArrived++;
    }

    public LinkedHashMap<GarbageCanType, ArrayList<Double>> getGarbageCanCapacityPercentagesByType() {
        LinkedHashMap<GarbageCanType, ArrayList<Double>> result = new LinkedHashMap<>();

        ArrayList<Double> mixed = new ArrayList<>();
        ArrayList<Double> bio = new ArrayList<>();
        ArrayList<Double> card = new ArrayList<>();
        ArrayList<Double> plastic = new ArrayList<>();
        ArrayList<Double> glass = new ArrayList<>();
        ArrayList<Double> metal = new ArrayList<>();

        for (GarbageCan can : garbageCans) {
            switch (can.getType()) {
                case MIXED:
                    mixed.add((can.getCurrentCapacity() / can.getCapacity()) * 100);
                    break;
                case BIO:
                    bio.add((can.getCurrentCapacity() / can.getCapacity()) * 100);
                    break;
                case CARDBOARD:
                    card.add((can.getCurrentCapacity() / can.getCapacity()) * 100);
                    break;
                case PLASTIC:
                    plastic.add((can.getCurrentCapacity() / can.getCapacity()) * 100);
                    break;
                case GLASS:
                    glass.add((can.getCurrentCapacity() / can.getCapacity()) * 100);
                    break;
                case METAL:
                    metal.add((can.getCurrentCapacity() / can.getCapacity()) * 100);
                    break;
            }
        }
        result.put(GarbageCanType.MIXED, mixed);
        result.put(GarbageCanType.BIO, bio);
        result.put(GarbageCanType.CARDBOARD, card);
        result.put(GarbageCanType.PLASTIC, plastic);
        result.put(GarbageCanType.GLASS, glass);
        result.put(GarbageCanType.METAL, metal);
        return result;
    }

    public void calculateThrashAmountByType(GarbageCan can, double thrashAmtLitres) {
        switch (can.getType()) {
            case MIXED:
                thrashThrownInKg[0] += can.getWeight();
                thrashTotalInLitres[0] += thrashAmtLitres;
                break;
            case BIO:
                thrashThrownInKg[1] += can.getWeight();
                thrashTotalInLitres[1] += thrashAmtLitres;
                break;
            case CARDBOARD:
                thrashThrownInKg[2] += can.getWeight();
                thrashTotalInLitres[2] += thrashAmtLitres;
                break;
            case PLASTIC:
                thrashThrownInKg[3] += can.getWeight();
                thrashTotalInLitres[3] += thrashAmtLitres;
                break;
            case GLASS:
                thrashThrownInKg[4] += can.getWeight();
                thrashTotalInLitres[4] += thrashAmtLitres;
                break;
            case METAL:
                thrashThrownInKg[5] += can.getWeight();
                thrashTotalInLitres[5] += thrashAmtLitres;
                break;
        }
    }

    /**
     * This method is called each time when garbage shelter is cleared. It adds value to hashmap variable {@link #shelterUsageRate}, which is later used to calculate the average usage of thrash can or total.
     *
     * @param can GarbageCan object, method calculates 'how full is the garbage can'. (CURRENT_CAPACITY / MAX_CAPACITY)
     */
    public void calculateUsageRate(GarbageCan can) {
        double usageRate = (can.getDataCapacity() / can.getCapacity()) * 100;

        System.out.println("Usage rate of " + can.getType() + " can: " + usageRate + "%." + " Current capacity: " + can.getDataCapacity() + " l divided by " + can.getCapacity() + " l.");

        shelterUsageRate.get(can.getType()).add(usageRate);

        System.out.println("UR" + shelterUsageRate.get(can.getType()));
    }

    /**
     * This method calculates average usage rate of garbage cans by type.
     * <br><br>
     * e.g. We have two garbage cans of type MIXED {@link GarbageCanType}, where one is full and other is empty. Meaning the average usage of mixed waste cans is 50%.
     *
     * @param type Enum type which indicates which garbage can type to be observed.
     * @return Returns double value which indicates percent, how much of the garbage cans of specific type are used.
     */
    public double getAverageRateOfType(GarbageCanType type) {
        double totalUsageRate = 0;

        for (double rate : shelterUsageRate.get(type)) {
            totalUsageRate += rate;
        }

        return (double) Math.round((totalUsageRate / shelterUsageRate.get(type).size()));
    }

    /**
     * This method calculates average usage rate of all garbage cans. i.e. How much of the capacity of the garbage shelter is used.
     *
     * @return Returns double value which indicates percent(%) how much of the garbage cans of all garbage cans.
     */
    public double getAverageUsageRateTotal() {
        double totalUsageRate = 0;

        for (GarbageCan can : garbageCans) {
            totalUsageRate += getAverageRateOfType(can.getType());
        }

        return (double) Math.round(totalUsageRate / garbageCans.size());
    }

    public int getHowManyTimeThrashThrown() {
        return this.howManyTimeThrashThrown;
    }

    public int getGarbageCarArriveTimes() {
        return this.howManyTimesCarArrived;
    }

    public double getThrashTotalInLitres() {
        double totalAmount = 0;

        for (int i = 0; i < thrashTotalInLitres.length; i++) {
            totalAmount += thrashTotalInLitres[i];
        }

        return (double) (Math.round(totalAmount * 100) / 100); //round to one decimal place.
    }

    public double getThrashTotalInKg() {
        double totalAmount = 0;

        for (int i = 0; i < thrashThrownInKg.length; i++) {
            totalAmount += thrashThrownInKg[i];
        }

        return (double) (Math.round(totalAmount * 100) / 100); //round to one decimal place.
    }

    public double[] getThrashAmountByType(GarbageCanType type) {
        double[] data = new double[2];

        switch (type) {
            case MIXED:
                data[0] = thrashTotalInLitres[0];
                data[1] = thrashThrownInKg[0];
                break;
            case BIO:
                data[0] = thrashTotalInLitres[1];
                data[1] = thrashThrownInKg[1];
                break;
            case CARDBOARD:
                data[0] = thrashTotalInLitres[2];
                data[1] = thrashThrownInKg[2];
                break;
            case PLASTIC:
                data[0] = thrashTotalInLitres[3];
                data[1] = thrashThrownInKg[3];
                break;
            case GLASS:
                data[0] = thrashTotalInLitres[4];
                data[1] = thrashThrownInKg[4];
                break;
            case METAL:
                data[0] = thrashTotalInLitres[5];
                data[1] = thrashThrownInKg[5];
                break;
        }

        data[0] = (double) Math.round(data[0] * 100) / 100;
        data[1] = (double) Math.round(data[1] * 100) / 100;

        return data;
    }

    /**
     * @param type Enum type of {@link GarbageCanType}, which indicates which garbage type is referenced.
     *             This method sets start time of the calculation for garbage type being full. It is invoked when {@link GarbageShelter} notices garbage shelter being full.
     *             * Data is being stored in {@link FullnessCalculator} object.
     */
    public void startCalculatingGarbageFullTime(GarbageCanType type) {
        FullnessCalculator calculator = howManyMinutesWasFull.get(type);

        if (!calculator.isFull) {
            System.out.println(type + " is full starting calculations!");
            calculator.startTime = (int) Clock.getInstance().getTime();
            calculator.isFull = true;
        }
    }

    /**
     * @param type Enum type of {@link GarbageCanType}, which indicates which garbage type is referenced.
     *             This method sets end time of the calculation for garbage type being full. It is invoked when {@link GarbageShelter} clears all garbage cans i.e. Garbage truck comes and empties garbage cans.<br>
     *             Data is being stored in {@link FullnessCalculator} object.
     */
    public void stopCalculatingGarbageFullTime(GarbageCanType type) {
        FullnessCalculator calculator = howManyMinutesWasFull.get(type);

        if (calculator.isFull) {
            System.out.println(type + " was full... Calculating the time!");
            calculator.endTime = (int) Clock.getInstance().getTime();
            int time = calculator.endTime - calculator.startTime;

            calculator.timeBeingFull = time;
        }

        //Reset values.
        calculator.isFull = false;
    }

    /**
     * @param type Enum type of {@link GarbageCanType}, which indicates which garbage type is referenced.
     *             This method retrieves data by referencing correct {@link GarbageCanType} enum. <br>
     *             Data is accessed from {@link FullnessCalculator} object, which is stored by using methods {@link #startCalculatingGarbageFullTime(GarbageCanType)} and {@link #getGarbageCanCapacityPercentagesByType()}
     */
    public double getFullTimeCalculations(GarbageCanType type) {
        double days = howManyMinutesWasFull.get(type).timeBeingFull * 0.0166666667;
        return (double) Math.round(days * 100) / 100; //round to one decimal.
    }

    public void addFailedAttempt(GarbageCanType type) {
        failedAttempts.put(type, failedAttempts.get(type) + 1);
    }

    public int getFailedAttempts(GarbageCanType type) {
        return failedAttempts.get(type);
    }
}

class FullnessCalculator {
    boolean isFull = false;

    int startTime;
    int endTime;

    int timeBeingFull;
}