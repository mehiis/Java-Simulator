package application.assets.model;

import application.assets.framework.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

class CollectedDataTest {
    private ArrayList<GarbageCan> garbageCans;
    private CollectedData d;

    @BeforeEach
    void setUp() {
        garbageCans = new ArrayList<>(); //Setup clear list before every test.
        d           = new CollectedData(garbageCans);
    }

    @Test
    void getAverageUsageRateTotal() {
        //step 1: test empty
        assertEquals(0, d.getAverageUsageRateTotal());

        //step 2: add one full mixed waste garbage can
        GarbageCan mixed = new GarbageCan(true, GarbageCanType.MIXED);
        mixed.addGarbageForData(mixed.getCapacity());
        garbageCans.add(mixed);
        d.calculateUsageRate(mixed);
        assertEquals(100, d.getAverageUsageRateTotal());

        //step 3: add one empty bio waste garbage can
        GarbageCan bio = new GarbageCan(false, GarbageCanType.BIO);
        garbageCans.add(bio);
        d.calculateUsageRate(bio);
        assertEquals(50, d.getAverageUsageRateTotal());

        //step 4: add one empty metal waste garbage can
        GarbageCan metal = new GarbageCan(false, GarbageCanType.METAL);
        garbageCans.add(metal);
        d.calculateUsageRate(metal);
        assertEquals(33.0, d.getAverageUsageRateTotal());

        //step 5: add one empty plastic waste garbage can
        GarbageCan plastic = new GarbageCan(true, GarbageCanType.PLASTIC);
        garbageCans.add(plastic);
        d.calculateUsageRate(plastic);
        assertEquals(25.0, d.getAverageUsageRateTotal());

        //step 6: add one full glass waste garbage can
        GarbageCan glass = new GarbageCan(false, GarbageCanType.GLASS);
        glass.addGarbageForData(glass.getCapacity());
        garbageCans.add(glass);
        d.calculateUsageRate(glass);
        assertEquals(40, d.getAverageUsageRateTotal()); //100 * (2/5) -> 2 full & 3 empty = 40% used capacity

        //step 7: add one more full mixed waste can
        GarbageCan mixed2 = new GarbageCan(true, GarbageCanType.MIXED);
        mixed2.addGarbageForData(mixed2.getCapacity());
        garbageCans.add(mixed2);
        d.calculateUsageRate(mixed2);
        assertEquals(50, d.getAverageUsageRateTotal()); //100 * (3/6) -> 3 full & 3 empty = 50% used capacity
    }

    @Test
    void getAverageRateOfType() {
        //TEST 1: test empty
        assertEquals(0, d.getAverageRateOfType(GarbageCanType.MIXED));

        //TEST 2: fill mixed 100%
        GarbageCan mixed = new GarbageCan(true, GarbageCanType.MIXED);
        mixed.addGarbageForData(mixed.getCapacity());
        garbageCans.add(mixed);
        d.calculateUsageRate(mixed);

        assertEquals(100.0, d.getAverageRateOfType(GarbageCanType.MIXED));

        //TEST 3: fill plastic container 50%
        GarbageCan plastic = new GarbageCan(true, GarbageCanType.PLASTIC);
        plastic.addGarbageForData(plastic.getCapacity()/2);
        garbageCans.add(plastic);
        d.calculateUsageRate(plastic);

        assertEquals(50.0, d.getAverageRateOfType(GarbageCanType.PLASTIC));

        //TEST 4: add second mixed waste can and let it be empty, now we have one empty and one full garbage can type MIXED
        GarbageCan mixed2 = new GarbageCan(true, GarbageCanType.MIXED);
        mixed2.addGarbageForData(0);
        garbageCans.add(mixed2);
        d.calculateUsageRate(mixed2);

        assertEquals(50.0, d.getAverageRateOfType(GarbageCanType.MIXED));

        //TEST 4.2: add third mixed waste can and let it be empty, now we have two empty and one full garbage can type MIXED
        GarbageCan mixed3 = new GarbageCan(true, GarbageCanType.MIXED);
        mixed3.addGarbageForData(0);
        garbageCans.add(mixed3);
        d.calculateUsageRate(mixed3);

        assertEquals(33.0, d.getAverageRateOfType(GarbageCanType.MIXED));
    }

    @Test
    void getFullTimeCalculations(){
        int minutesInADay = 60;

        //Test 1: Declare that mixed waste is full and forward time by 60 minute(1 hour) and "collect the thrash".
        d.startCalculatingGarbageFullTime(GarbageCanType.MIXED);
        Clock.getInstance().setTime(minutesInADay);
        d.stopCalculatingGarbageFullTime(GarbageCanType.MIXED);

        assertEquals(1.0, d.getFullTimeCalculations(GarbageCanType.MIXED), 0.0001); //Mixed waste should be full for one day.

        //Test 2: Declare that bio waste is full and forward time by additional 2*60 minute(2 hours) and "collect the thrash".
        d.startCalculatingGarbageFullTime(GarbageCanType.BIO);
        Clock.getInstance().setTime(Clock.getInstance().getTime() + (2 * minutesInADay));
        d.stopCalculatingGarbageFullTime(GarbageCanType.BIO);

        assertEquals(2.0, d.getFullTimeCalculations(GarbageCanType.BIO), 0.0001); //Bio waste should be full for two days.
    }
}