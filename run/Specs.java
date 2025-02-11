package run;
import assets.model.GarbageCan;
import assets.model.GarbageCanType;
import java.util.ArrayList;
import java.util.Scanner;

public class Specs {
    private final ArrayList<GarbageCan> garbageCans = new ArrayList<>();
    private int simulationTime = 2500;

    public ArrayList<GarbageCan> getGarbageCanList() {
        return garbageCans;
    }

    public int getSimulationTime() {
        return simulationTime;
    }

    // For faster testing, can delete later
    public void setDefaultGarbageCanList() {
        garbageCans.add(new GarbageCan(true, GarbageCanType.MIXED));
        garbageCans.add(new GarbageCan(false, GarbageCanType.BIO));
        garbageCans.add(new GarbageCan(true, GarbageCanType.CARDBOARD));
        garbageCans.add(new GarbageCan(true, GarbageCanType.PLASTIC));
        garbageCans.add(new GarbageCan(false, GarbageCanType.GLASS));
        garbageCans.add(new GarbageCan(false, GarbageCanType.METAL));
    }

    public void setGarbageCanList() {
        boolean isBig;
        Scanner scanner = new Scanner(System.in);
        for (GarbageCanType type : GarbageCanType.values()) {
            if (type == GarbageCanType.MIXED || type == GarbageCanType.CARDBOARD || type == GarbageCanType.PLASTIC) {
                isBig = true;
            } else {isBig = false;}

            System.out.println("How many " + type + " garbage cans do you want?");
            int amount = scanner.nextInt();
            for (int i = 0; i < amount; i++) {
                garbageCans.add(new GarbageCan(isBig, type));
            }
        }
    }

    public void setSimulationTime() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How long do you want the simulation to run?");
        int time = scanner.nextInt();
        if (time > 0) {
            simulationTime = time;
        } else {
            System.out.println("Invalid input, simulation time set to default (2500)");
        }
    }
}
