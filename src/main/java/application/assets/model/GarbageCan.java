package application.assets.model;

/**
 * Class for garbage cans. The garbage cans are used to store trash in the garbage shelter.
 */
public class GarbageCan {
    private double currentCapacity;
    private double totalCapacity;
    private double weightMultiplier;
    private GarbageCanType type;

    //DATA COLLECTION ONLY
    private double dataCapcity;

    public GarbageCan(boolean isBig, GarbageCanType type){
        if(isBig) totalCapacity = 660; else totalCapacity = 240;
        this.type = type;
    }

    /**
     * Adds garbage to the garbage can.
     * @param amount
     * @return
     */
    public boolean addGarbage(double amount){
        double temp = this.currentCapacity + amount;

        if(temp > totalCapacity)
            return false;

        this.currentCapacity = temp;
        return true;
    }

    /**
     * Garbage data collection.
     * @param amount
     */
    public void addGarbageForData(double amount){
        this.dataCapcity += amount;
    }

    public double getCapacity(){
        return totalCapacity;
    }

    public boolean checkCapacity(double thrashAmount){
        return (currentCapacity + thrashAmount) <= totalCapacity;
    }

    /**
     * Returns the weight of trash of given bin type, multiplied by a trash volume multiplier.
     * @return
     */
    public double getWeight(){
        switch (this.type){
            case MIXED:
                this.weightMultiplier = 1.0;
                break;
            case BIO:
                this.weightMultiplier = 1.0;
                break;
            case CARDBOARD:
                this.weightMultiplier = 0.15;
                break;
            case PLASTIC:
                this.weightMultiplier = 0.05;
                break;
            case METAL:
                this.weightMultiplier = 0.75;
                break;
            case GLASS:
                this.weightMultiplier = 1.25;
                break;
            default:
                System.out.println("WHAT IS GOING OOON??!?!?!?!?!!?!?!? THIS SHOULD NOT HAPPEN?????????");
        }

        return currentCapacity * weightMultiplier;
    }

    public double getCurrentCapacity(){return this.currentCapacity;}

    public double getDataCapacity(){return this.dataCapcity;}

    public GarbageCanType getType(){return this.type;}

    /**
     * Empties the garbage can.
     */
    public void empty(){this.currentCapacity = 0;}
}