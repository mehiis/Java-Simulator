/**
 * This class does unit test for the getMeanArrivalRate method in the Specs class.
 */

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
        // Making sure no negative arrival rates are calculated.
        for (int i = -10000; i < 10000; i++) {
            double arrivalRate = Specs.getMeanArrivalRate(i, i, i);
            assertTrue(arrivalRate > 0);
            }

    }
}
