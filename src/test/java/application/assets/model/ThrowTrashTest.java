package application.assets.model;

import application.assets.framework.EventList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class ThrowTrashTest {
    private GarbageShelter garbageShelter;
    private TrashDistribution trashGenerator;
    @BeforeEach
    void setUp() {
        garbageShelter = new GarbageShelter(new EventList(), EventType.EXIT, 6.0);
        trashGenerator = new TrashDistribution();
    }

    @Test
    void testGenerateTrash() {
        // setting up needed percentages and types, and the generated trash itself
        HashMap<GarbageCanType, Double> generatedTrash = trashGenerator.getTrash(6.0, 10000);
        double[] cumulativePercentages = {61.01, 76.11, 90.66, 93.51, 97.28, 100.0};
        GarbageCanType[] types = {
                GarbageCanType.MIXED,
                GarbageCanType.CARDBOARD,
                GarbageCanType.BIO,
                GarbageCanType.GLASS,
                GarbageCanType.METAL,
                GarbageCanType.PLASTIC
        };

        // calculate total weight of generated trash to check if percentages are correct in the end
        double totalTrashWeight= 0.0;
        for (GarbageCanType type: types) {
            totalTrashWeight += generatedTrash.get(type);
        }

        // get trash amout of type, divide it by total weight of generated trash, multiply it by a hundred to get percentages, now check it against distribution percentages with a delta
        int loopCounter = 0;
        for (GarbageCanType type: types) {
            Assertions.assertEquals((generatedTrash.get(type) / totalTrashWeight) * 100, cumulativePercentages[loopCounter], 5);
            loopCounter ++;
        }
    }
}
