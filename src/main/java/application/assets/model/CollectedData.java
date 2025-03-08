/**
 * @version 1.0
 * @since 8.March.2025
 * This object is created inside {@link #GarbageShelter}, and is in charge of storing all collected data which is obtained from executed simulation.
 */
package application.assets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CollectedData {
    private final ArrayList<GarbageCan> garbageCans;
    private final double[] thrashTotalInLitres 			    = new double[GarbageCanType.values().length];;
    private final double[] thrashThrownInKg                 = new double[GarbageCanType.values().length];
    //private final double[] thrashAccessibilityRateByType 	= new double[GarbageCanType.values().length];
    private final HashMap<GarbageCanType, LinkedList<Double>> shelterUsageRate = new HashMap<>() {
        {
            put(GarbageCanType.MIXED,       new LinkedList<>());
            put(GarbageCanType.BIO,         new LinkedList<>());
            put(GarbageCanType.CARDBOARD,   new LinkedList<>());
            put(GarbageCanType.PLASTIC,     new LinkedList<>());
            put(GarbageCanType.GLASS,       new LinkedList<>());
            put(GarbageCanType.METAL,       new LinkedList<>());
        }
    };

    private int howManyTimeThrashThrown 			= 0;
    private int howManyTimesCarArrived				= 0;

    public CollectedData(ArrayList<GarbageCan> garbageCans){
        this.garbageCans = garbageCans;
    }

    /**
     * Adds one to thrash counter, each time thrash has been thrown.
     */
    public void addThrownThrash(){
        howManyTimeThrashThrown++;
    }
    /**
     * Adds one to garbage car arrival counter, each time the garbage shelter has been cleared.
     */
    public void addGarbageCarArrival(){
        howManyTimesCarArrived++;
    }

    public LinkedHashMap<GarbageCanType, ArrayList<Double>> getGarbageCanCapacityPercentagesByType() {
        LinkedHashMap<GarbageCanType, ArrayList<Double>> result = new LinkedHashMap<>();

        ArrayList<Double> mixed     = new ArrayList<>();
        ArrayList<Double> bio       = new ArrayList<>();
        ArrayList<Double> card      = new ArrayList<>();
        ArrayList<Double> plastic   = new ArrayList<>();
        ArrayList<Double> glass     = new ArrayList<>();
        ArrayList<Double> metal     = new ArrayList<>();

        for (GarbageCan can: garbageCans) {
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

    public void calculateThrashAmountByType(GarbageCan can, double thrashAmtLitres){
        switch (can.getType()){
            case MIXED:
                thrashThrownInKg[0] 	+= can.getWeight();
                thrashTotalInLitres[0] 	+= thrashAmtLitres;
                break;
            case BIO:
                thrashThrownInKg[1] 	+= can.getWeight();
                thrashTotalInLitres[1] 	+= thrashAmtLitres;
                break;
            case CARDBOARD:
                thrashThrownInKg[2] 	+= can.getWeight();
                thrashTotalInLitres[2] 	+= thrashAmtLitres;
                break;
            case PLASTIC:
                thrashThrownInKg[3] 	+= can.getWeight();
                thrashTotalInLitres[3] 	+= thrashAmtLitres;
                break;
            case GLASS:
                thrashThrownInKg[4] 	+= can.getWeight();
                thrashTotalInLitres[4] 	+= thrashAmtLitres;
                break;
            case METAL:
                thrashThrownInKg[5] 	+= can.getWeight();
                thrashTotalInLitres[5] 	+= thrashAmtLitres;
                break;
        }
    }

    /**
     * This method is called each time when garbage shelter is cleared. It adds normalized value 0-1 to hashmap variable {@link #shelterUsageRate}, which is later used to calculate the average usage of thrash can or total.
     * @param can GarbageCan object, method calculates 'how full is the garbage can'. (CURRENT_CAPACITY / MAX_CAPACITY)
     */
    public void calculateUsageRate(GarbageCan can){
        double usageRate = (double) Math.round((can.getCurrentCapacity() / can.getCapacity()) * 100) / 100; //calculation gives normalized number between 0-1.

        System.out.println("Usage rate of " + can.getType() + " can: " + usageRate*100 + "%." + " Current capacity: " + can.getCurrentCapacity() + " l divided by " + can.getCapacity() + " l.");

        shelterUsageRate.get(can.getType()).add(usageRate);
    }

    /**
     * This method calculates average usage rate of garbage cans by type.
     * <br><br>
     * e.g. We have two garbage cans of type MIXED {@link GarbageCanType}, where one is full and other is empty. Meaning the average usage of mixed waste cans is 50%.
     * @param type Enum type which indicates which garbage can type to be observed.
     * @return Returns double value which indicates percent, how much of the garbage cans of specific type are used.
     */
    public double getAverageRateOfType(GarbageCanType type){
        double totalUsageRate = 0;

        for (double rate: shelterUsageRate.get(type)){
            totalUsageRate += rate;
        }

        return (double) Math.round((totalUsageRate / shelterUsageRate.get(type).size()) * 100);
    }

    /**
     * This method calculates average usage rate of all garbage cans. i.e. How much of the capacity of the garbage shelter is used.
     * @return Returns double value which indicates percent(%) how much of the garbage cans of all garbage cans.
     */
    public double getAverageUsageRateTotal(){
        double totalUsageRate = 0;

        for(GarbageCan can: garbageCans){
            totalUsageRate += getAverageRateOfType(can.getType());
        }

        return (double) Math.round(totalUsageRate / garbageCans.size());
    }

    public int getHowManyTimeThrashThrown(){
        return  this.howManyTimeThrashThrown;
    }

    public int getGarbageCarArriveTimes(){
        return  this.howManyTimesCarArrived;
    }

    public double getThrashTotalInLitres(){
        double totalAmount = 0;

        for (int i = 0; i < thrashTotalInLitres.length; i++){
            totalAmount += thrashTotalInLitres[i];
        }

        return (double)(Math.round(totalAmount * 100) / 100); //round to one decimal place.
    }

    public double getThrashTotalInKg(){
        double totalAmount = 0;

        for (int i = 0; i < thrashThrownInKg.length; i++){
            totalAmount += thrashThrownInKg[i];
        }

        return (double)(Math.round(totalAmount * 100) / 100); //round to one decimal place.
    }

    public double[] getThrashAmountByType(GarbageCanType type){
        double[] data = new double[2];

        switch (type){
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
}