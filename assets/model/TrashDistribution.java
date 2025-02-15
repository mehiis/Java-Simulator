package assets.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class TrashDistribution {
    private double paperAndCardboardPercentage;
    private double bioPercentage;
    private double glassPercentage;
    private double metalPercentage;
    private double plasticPercentage;



    public HashMap<GarbageCanType, Double> getTrash(double trashAmt) {
        final int TRASHITERATIONS = 1000;
        final double dividedTrash = trashAmt / TRASHITERATIONS;
        final int MAXAGE = 35;
        LinkedHashMap<Double, GarbageCanType> trashDistribution = new LinkedHashMap<>();

        trashDistribution.put(61.01, GarbageCanType.MIXED);             // 61.01% mixed waste
        trashDistribution.put(76.11, GarbageCanType.CARDBOARD);         // 15.01% cardboard/paper waste
        trashDistribution.put(90.66, GarbageCanType.BIO);               // etc...
        trashDistribution.put(93.51, GarbageCanType.GLASS);
        trashDistribution.put(97.28, GarbageCanType.METAL);
        trashDistribution.put(100.0, GarbageCanType.PLASTIC);

        ArrayList<Double> keyList = new ArrayList<>(trashDistribution.keySet()); // store percentage keys as a list for iteration

        HashMap<GarbageCanType, Double> generatedTrash = new HashMap<>();
        // initialize generatedTrash
        for (GarbageCanType type: trashDistribution.values()) {
            generatedTrash.put(type, 0.0);
        }

        // Generate ages according to the distribution:
        for (int i = 1; i <= TRASHITERATIONS; i++){
            GarbageCanType type;
            // min and max to make ABSOLUTELY sure the values stay withing 1.0 ... 100.0
            double x = Math.min(100.0, Math.max(1.0, Math.round(Math.random()*100)+1)); 	// generate a random number 1..100 -> we get the row which gives the trash type
            int j = 0;
            while (x > keyList.get(j)) { // search for the correct row to get the matching garbage type
                j++;
            }
            type = trashDistribution.get(keyList.get(j));
            generatedTrash.put(type, generatedTrash.get(type) + dividedTrash); // put some of the divided trash amt to correct garbage type
        }
        return generatedTrash;
    }

}
