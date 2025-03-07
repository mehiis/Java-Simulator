package application.assets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class CollectedData {

    private ArrayList<GarbageCan> garbageCans = new ArrayList<>();

    private double[] thrashTotalInLitres 			= new double[GarbageCanType.values().length];;
    private double[] tharshThrownInKg				= new double[GarbageCanType.values().length];
    private int howManyTimeThrashThrown 			= 0;
    private int howManyTimesCarArrived				= 0;
    private double[] thrashAccessibilityRateByType 	= new double[GarbageCanType.values().length];
    private HashMap<GarbageCanType, LinkedList<Double>> shelterUsageRate = new HashMap<>() {
        {
            put(GarbageCanType.MIXED, new LinkedList<>());
            put(GarbageCanType.BIO, new LinkedList<>());
            put(GarbageCanType.CARDBOARD, new LinkedList<>());
            put(GarbageCanType.PLASTIC, new LinkedList<>());
            put(GarbageCanType.GLASS, new LinkedList<>());
            put(GarbageCanType.METAL, new LinkedList<>());
        }
    };

    public CollectedData(ArrayList<GarbageCan> garbageCans){
        this.garbageCans = garbageCans;
    }

    public void addThrownThrash(){
        howManyTimeThrashThrown++;
    }

    public void addGarbageCarArrival(){
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
                tharshThrownInKg[0] 	+= can.getWeight();
                thrashTotalInLitres[0] 	+= thrashAmtLitres;
                break;
            case BIO:
                tharshThrownInKg[1] 	+= can.getWeight();
                thrashTotalInLitres[1] 	+= thrashAmtLitres;
                break;
            case CARDBOARD:
                tharshThrownInKg[2] 	+= can.getWeight();
                thrashTotalInLitres[2] 	+= thrashAmtLitres;
                break;
            case PLASTIC:
                tharshThrownInKg[3] 	+= can.getWeight();
                thrashTotalInLitres[3] 	+= thrashAmtLitres;
                break;
            case GLASS:
                tharshThrownInKg[4] 	+= can.getWeight();
                thrashTotalInLitres[4] 	+= thrashAmtLitres;
                break;
            case METAL:
                tharshThrownInKg[5] 	+= can.getWeight();
                thrashTotalInLitres[5] 	+= thrashAmtLitres;
                break;
        }
    }

    public void calculateUsageRate(GarbageCan can){
        double usageRate = (double) Math.round((can.getCurrentCapacity() / can.getCapacity()) * 100) / 100;

        System.out.println("Usage rate of " + can.getType() + " can: " + usageRate + "%." + " Current capacity: " + can.getCurrentCapacity() + " l divided by " + can.getCapacity() + " l.");

        shelterUsageRate.get(can.getType()).add(usageRate);
    }

    public double getAverageUsageRateTotal(){
        double totalUsageRate = 0;

        for (GarbageCanType type: GarbageCanType.values()){
            totalUsageRate += getAverageRateOfType(type);
        }

        return (double) Math.round((totalUsageRate / GarbageCanType.values().length) * 100);
    }

    public double getAverageRateOfType(GarbageCanType type){
        double totalUsageRate = 0;

        for (double rate: shelterUsageRate.get(type)){
            totalUsageRate += rate;
        }

        return (double) Math.round((totalUsageRate / shelterUsageRate.get(type).size()) * 100);
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

        for (int i = 0; i < tharshThrownInKg.length; i++){
            totalAmount += tharshThrownInKg[i];
        }

        return (double)(Math.round(totalAmount * 100) / 100); //round to one decimal place.
    }

    public double[] getThrashAmountByType(GarbageCanType type){
        double[] data = new double[2];

        switch (type){
            case MIXED:
                data[0] = thrashTotalInLitres[0];
                data[1] = tharshThrownInKg[0];
                break;
            case BIO:
                data[0] = thrashTotalInLitres[1];
                data[1] = tharshThrownInKg[1];
                break;
            case CARDBOARD:
                data[0] = thrashTotalInLitres[2];
                data[1] = tharshThrownInKg[2];
                break;
            case PLASTIC:
                data[0] = thrashTotalInLitres[3];
                data[1] = tharshThrownInKg[3];
                break;
            case GLASS:
                data[0] = thrashTotalInLitres[4];
                data[1] = tharshThrownInKg[4];
                break;
            case METAL:
                data[0] = thrashTotalInLitres[5];
                data[1] = tharshThrownInKg[5];
                break;
        }

        data[0] = (double) Math.round(data[0] * 100) / 100;
        data[1] = (double) Math.round(data[1] * 100) / 100;

        return data;
    }

}
