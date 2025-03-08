package application.run;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpecsGetMeanArrivalRateTest {
    @BeforeAll
    static void beforeAll() {

    }
    @Test
    void checkArrivalRateRangeNotNegative() {
    for (int i = 0; i < 100; i++) {
        double arrivalRate = Specs.getMeanArrivalRate(i, i, i);
        assertTrue(arrivalRate > 0);
        }

    }
}
