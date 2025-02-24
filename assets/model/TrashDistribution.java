package assets.model;

import eduni.distributions.Normal;

import java.util.HashMap;

public class TrashDistribution {

    public HashMap<GarbageCanType, Double> getTrash(double trashAmtMean) {
        final int TRASHITERATIONS = 1000;
        // generate randomness to trash amount with normal distribution
        final double dividedTrash = new Normal(trashAmtMean, 2.2).sample() / TRASHITERATIONS;

        // parallel arrays with matched indices: cumulative percentages and trash types
        double[] cumulativePercentages = {61.01, 76.11, 90.66, 93.51, 97.28, 100.0};
        GarbageCanType[] types = {
                GarbageCanType.MIXED,
                GarbageCanType.CARDBOARD,
                GarbageCanType.BIO,
                GarbageCanType.GLASS,
                GarbageCanType.METAL,
                GarbageCanType.PLASTIC
        };

        HashMap<GarbageCanType, Double> generatedTrash = new HashMap<>();

        // initialize generatedTrash with keys and values
        for (GarbageCanType type : types) {
            generatedTrash.put(type, 0.0);
        }

        // generate trash according to the distribution
        for (int i = 1; i <= TRASHITERATIONS; i++) {
            double x = Math.min(100.0, Math.max(1.0, Math.round(Math.random() * 100) + 1));
            for (int j = 0; j < cumulativePercentages.length; j++) {
                if (x <= cumulativePercentages[j]) {
                    generatedTrash.put(types[j], generatedTrash.get(types[j]) + dividedTrash);
                    break;
                }
            }
        }

        return generatedTrash;
    }
}