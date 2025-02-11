package assets.model;

enum GargeCanType {MIXED, BIO, CARDBOARD, PLASTIC, GLASS, METAL};

public class GarbageCan {
    private double currentCapacity;
    private double totalCapacity;

    private double weightMultiplier;

    private GargeCanType type;

    public GarbageCan(boolean isBig, GargeCanType type){
        if(isBig) totalCapacity = 660; else totalCapacity = 240;
        this.type = type;
    }

    public boolean addGarbage(double amount){
        double temp = this.currentCapacity + amount;

        if(temp > totalCapacity)
            return false;

        this.currentCapacity = temp;
        return true;
    }

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
            case null, default:
                System.out.println("WHAT IS GOING OOON??!?!?!?!?!!?!?!? THIS SHOULD NOT HAPPEN?????????");
        }

        return currentCapacity * weightMultiplier;
    }

    public void empty(){this.currentCapacity = 0;}
}