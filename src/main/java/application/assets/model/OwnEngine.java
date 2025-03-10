package application.assets.model;

import application.assets.framework.ArrivalProcess;
import application.assets.framework.Clock;
import application.assets.framework.Engine;
import application.assets.framework.Event;
import application.controller.Controller;
import application.eduni.distributions.Negexp;
import application.eduni.distributions.Normal;
import application.run.Specs;
/**
 * Child of Engine-class, contains the customized main working logic of the simulator.
 */
public class OwnEngine extends Engine {
	private ArrivalProcess yksioArrivalProcess;
	private ArrivalProcess kaksioArrivalProcess;
	private ArrivalProcess kolmioArrivalProcess;
	private ArrivalProcess nelioArrivalProcess;
	private ArrivalProcess 		clearProcess;
	private GarbageShelter 		garbageShelter;
	private Controller 			controller;
	private long 				delayTime = 0;
	private double meanTrashThrowAmt;

	public OwnEngine(Controller controller){
		this.controller = controller;

		meanTrashThrowAmt = controller.getMeanTrashPerThrowAmt();

		garbageShelter = new GarbageShelter(eventList, EventType.EXIT, meanTrashThrowAmt);
		// GUI should be read; which arrival processes to create and use. GUI should also decide apt amt.
		// people amt is basically a multiplier for the base amount of trash a specific type of apt generates.
		if (controller.getSingleAptAmt() > 0) {
			yksioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(1, controller.getSingleAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.YKSIO_ARRIVE_TO_SHELTER);
		}
		if (controller.getDoubleAptAmt() > 0) {
			kaksioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(2, controller.getDoubleAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.KAKSIO_ARRIVE_TO_SHELTER);
		}
		if (controller.getTripleAptAmt() > 0) {
			kolmioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(3, controller.getTripleAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.KOLMIO_ARRIVE_TO_SHELTER);
		}
		if (controller.getQuadAptAmt() > 0) {
			nelioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(4, controller.getQuadAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.NELIO_ARRIVE_TO_SHELTER);
		}

		clearProcess 		= new ArrivalProcess(new Normal(controller.getGarbageTruckArrivalInterval(),1), eventList, EventType.CLEAR_GARBAGE_FROM_SHELTER);
	}

	/**
	 * Initializes engine by generating the necessary first B-type events.
	 * Starts the loop of wanted resident type-arrivals and garbage truck arrival.
	 */
	@Override
	protected void init() {
		// start needed arrival processes as defined in GUI as params
		if (yksioArrivalProcess != null) { yksioArrivalProcess.generateNext();}
		if (kaksioArrivalProcess != null) {kaksioArrivalProcess.generateNext();}
		if (kolmioArrivalProcess != null) {kolmioArrivalProcess.generateNext();}
		if (nelioArrivalProcess != null) {nelioArrivalProcess.generateNext();}
		clearProcess.generateNext();
		garbageShelter.printThrashCans();// !for testing purposes! //
	}

	/**
	 * Booked B-stage of the simulation model. Arrival of residents to garbage shelter,
	 * generation of new arrival events and visualization of residents.
	 * @param t
	 */
	@Override
	protected void executeEvent(Event t){// B-vaiheen tapahtumat

		controller.setDay();

		switch ((EventType)t.getType()){
			case YKSIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					yksioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.YKSIO_ARRIVE_TO_SHELTER, garbageShelter.getData().getGarbageCanCapacityPercentagesByType());
					break;
			case KAKSIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					kaksioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.KAKSIO_ARRIVE_TO_SHELTER, garbageShelter.getData().getGarbageCanCapacityPercentagesByType());
					break;
			case KOLMIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					kolmioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.KOLMIO_ARRIVE_TO_SHELTER, garbageShelter.getData().getGarbageCanCapacityPercentagesByType());
					break;
			case NELIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					nelioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.NELIO_ARRIVE_TO_SHELTER, garbageShelter.getData().getGarbageCanCapacityPercentagesByType());
					break;
			case EXIT:
					garbageShelter.getFromQueue();//TRASH HAS BEEN THROWN! :)
					break;
			case CLEAR_GARBAGE_FROM_SHELTER:
					garbageShelter.clearGarbageCans();
					clearProcess.generateNext();
					controller.visualizeGarbageTruck(garbageShelter.getData().getGarbageCanCapacityPercentagesByType());
					break;
		}
	}
	/**
	 * Conditional C-stage of the simulation model. Tries all C-type events (can trash be thrown?).
	 */
	@Override
	protected void tryCtypeEvents(){
			if (!garbageShelter.isReserved() && garbageShelter.isQueued() && !garbageShelter.isFull()){
				garbageShelter.throwTrash();
			}

			garbageShelter.garbageCanStates();
	}

	/**
	 * Display simulation results. Passes data gathered from model to controller to GUI for displaying.
	 */
	@Override
	protected void results() {
		controller.naytaLoppuaika(Clock.getInstance().getTime());

		controller.setTrashThrownTimes(garbageShelter.getData().getHowManyTimeThrashThrown());
		controller.setShelterClearedTimes(garbageShelter.getData().getGarbageCarArriveTimes());
		controller.setShelterUsagePercent(garbageShelter.getData().getAverageUsageRateTotal());

		controller.setTrashThrownTotal(garbageShelter.getData().getThrashTotalInKg(), garbageShelter.getData().getThrashTotalInLitres());

		controller.setMixedTotal(garbageShelter.getData().getThrashAmountByType(GarbageCanType.MIXED)[0]);
		controller.setBioTotal(garbageShelter.getData().getThrashAmountByType(GarbageCanType.BIO)[0] );
		controller.setCardboardTotal(garbageShelter.getData().getThrashAmountByType(GarbageCanType.CARDBOARD)[0]);
		controller.setPlasticTotal(garbageShelter.getData().getThrashAmountByType(GarbageCanType.PLASTIC)[0]);
		controller.setGlassTotal(garbageShelter.getData().getThrashAmountByType(GarbageCanType.GLASS)[0] );
		controller.setMetalTotal(garbageShelter.getData().getThrashAmountByType(GarbageCanType.METAL)[0]);

		controller.setMixedUsage(garbageShelter.getData().getAverageRateOfType(GarbageCanType.MIXED) );
		controller.setBioUsage(garbageShelter.getData().getAverageRateOfType(GarbageCanType.BIO) );
		controller.setCardboardUsage(garbageShelter.getData().getAverageRateOfType(GarbageCanType.CARDBOARD));
		controller.setPlasticUsage(garbageShelter.getData().getAverageRateOfType(GarbageCanType.PLASTIC));
		controller.setGlassUsage(garbageShelter.getData().getAverageRateOfType(GarbageCanType.GLASS));
		controller.setMetalUsage(garbageShelter.getData().getAverageRateOfType(GarbageCanType.METAL));

		controller.setMixedOverflow(garbageShelter.getOverflowTrash(GarbageCanType.MIXED));
		controller.setBioOverflow(garbageShelter.getOverflowTrash(GarbageCanType.BIO));
		controller.setCardboardOverflow(garbageShelter.getOverflowTrash(GarbageCanType.CARDBOARD));
		controller.setPlasticOverflow(garbageShelter.getOverflowTrash(GarbageCanType.PLASTIC));
		controller.setGlassOverflow(garbageShelter.getOverflowTrash(GarbageCanType.GLASS));
		controller.setMetalOverflow(garbageShelter.getOverflowTrash(GarbageCanType.METAL));

		controller.setMixedAccessTime(		garbageShelter.getData().getFullTimeCalculations(GarbageCanType.MIXED		));
		controller.setBioAccessTime(		garbageShelter.getData().getFullTimeCalculations(GarbageCanType.BIO			));
		controller.setCardboardAccessTime(	garbageShelter.getData().getFullTimeCalculations(GarbageCanType.CARDBOARD	));
		controller.setPlasticAccessTime(	garbageShelter.getData().getFullTimeCalculations(GarbageCanType.PLASTIC		));
		controller.setGlassAccessTime(		garbageShelter.getData().getFullTimeCalculations(GarbageCanType.GLASS		));
		controller.setMetalAccessTime(		garbageShelter.getData().getFullTimeCalculations(GarbageCanType.METAL		));

		System.out.println("\n\n### SIMULATION ENDED###\nSimulation lasted for " + (double)(Math.round(Clock.getInstance().getTime()*100)/100) + " minutes.");
		System.out.println(
						"\nCOLLECTED DATA PRINT:\n" +
						"Thrash thrown " + garbageShelter.getData().getHowManyTimeThrashThrown() + " times.\n" +
						"Garbage truck arrived " + garbageShelter.getData().getGarbageCarArriveTimes() + " times.\n\n" +

						"Thrash thrown in total: " + garbageShelter.getData().getThrashTotalInLitres() + " litres/" + garbageShelter.getData().getThrashTotalInKg() +" kg. \n" +
								"MIXED: " 		+ garbageShelter.getData().getThrashAmountByType(GarbageCanType.MIXED)[0] 	+ "l/" + garbageShelter.getData().getThrashAmountByType(GarbageCanType.MIXED)[1] 		+ "kg.\n" +
								"BIO: " 		+ garbageShelter.getData().getThrashAmountByType(GarbageCanType.BIO)[0] 		+ "l/" + garbageShelter.getData().getThrashAmountByType(GarbageCanType.BIO)[1] 		+ "kg.\n" +
								"CARDBOARD: " 	+ garbageShelter.getData().getThrashAmountByType(GarbageCanType.CARDBOARD)[0] + "l/" + garbageShelter.getData().getThrashAmountByType(GarbageCanType.CARDBOARD)[1] 	+ "kg.\n" +
								"PLASTIC: " 	+ garbageShelter.getData().getThrashAmountByType(GarbageCanType.PLASTIC)[0] 	+ "l/" + garbageShelter.getData().getThrashAmountByType(GarbageCanType.PLASTIC)[1] 	+ "kg.\n" +
								"GLASS: " 		+ garbageShelter.getData().getThrashAmountByType(GarbageCanType.GLASS)[0] 	+ "l/" + garbageShelter.getData().getThrashAmountByType(GarbageCanType.GLASS)[1] 		+ "kg.\n" +
								"METAL: " 		+ garbageShelter.getData().getThrashAmountByType(GarbageCanType.METAL)[0] 	+ "l/" + garbageShelter.getData().getThrashAmountByType(GarbageCanType.METAL)[1] 		+ "kg.\n\n" +

						"Shelter usage rate: " + garbageShelter.getData().getAverageUsageRateTotal() + "%.\n" +
								"MIXED: " 		+ garbageShelter.getData().getAverageRateOfType(GarbageCanType.MIXED) 	+ "%.\n" +
								"BIO: " 		+ garbageShelter.getData().getAverageRateOfType(GarbageCanType.BIO) 	+ "%.\n" +
								"CARDBOARD: " 	+ garbageShelter.getData().getAverageRateOfType(GarbageCanType.CARDBOARD) 	+ "%.\n" +
								"PLASTIC: " 	+ garbageShelter.getData().getAverageRateOfType(GarbageCanType.PLASTIC) 	+ "%.\n" +
								"GLASS: " 		+ garbageShelter.getData().getAverageRateOfType(GarbageCanType.GLASS) 	+ "%.\n" +
								"METAL: " 		+ garbageShelter.getData().getAverageRateOfType(GarbageCanType.METAL) 	+ "%.\n\n" +

						"Trash overflows: \n" +
								" MIXED: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.MIXED)) + " l\n" +
								" BIO: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.BIO)) + " l\n" +
								" CARDBOARD: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.CARDBOARD)) + " l\n" +
								" PLASTIC: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.PLASTIC)) + " l\n" +
								" GLASS: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.GLASS)) + " l\n" +
								" METAL: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.METAL)) + " l\n"
		);

		System.out.println("Mixed cans were full for " + garbageShelter.getData().getFullTimeCalculations(GarbageCanType.MIXED) + " hours.");
		System.out.println("Bio cans were full for " + garbageShelter.getData().getFullTimeCalculations(GarbageCanType.BIO) + " hours.");
		System.out.println("Cardboard cans were full for " + garbageShelter.getData().getFullTimeCalculations(GarbageCanType.CARDBOARD) + " hours.");
		System.out.println("Plastic cans were full for " + garbageShelter.getData().getFullTimeCalculations(GarbageCanType.PLASTIC) + " hours.");
		System.out.println("Glass cans were full for " + garbageShelter.getData().getFullTimeCalculations(GarbageCanType.GLASS) + " hours.");
		System.out.println("Metal cans were full for " + garbageShelter.getData().getFullTimeCalculations(GarbageCanType.METAL) + " hours.");

		System.out.println("Mixed cans failed attempts: " + garbageShelter.getData().getFailedAttempts(GarbageCanType.MIXED));
		System.out.println("Mixed cans failed attempts: " + garbageShelter.getData().getFailedAttempts(GarbageCanType.BIO));
		System.out.println("Mixed cans failed attempts: " + garbageShelter.getData().getFailedAttempts(GarbageCanType.CARDBOARD));
		System.out.println("Mixed cans failed attempts: " + garbageShelter.getData().getFailedAttempts(GarbageCanType.PLASTIC));
		System.out.println("Mixed cans failed attempts: " + garbageShelter.getData().getFailedAttempts(GarbageCanType.GLASS));
		System.out.println("Mixed cans failed attempts: " + garbageShelter.getData().getFailedAttempts(GarbageCanType.METAL));
	}

	/**
	 * Interface method for controller to pause model.
	 */
	@Override
	public void pressPauseButton() { this.paused = !this.paused; }

	/**
	 * Interface method for controller to pass user input garbage bin amounts to model.
	 * @param amount
	 */
	@Override
	public void setMixedCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.MIXED, amount);}
	/**
	 * Interface method for controller to pass user input garbage bin amounts to model.
	 * @param amount
	 */
	@Override
	public void setPlasticCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.PLASTIC, amount);}
	/**
	 * Interface method for controller to pass user input garbage bin amounts to model.
	 * @param amount
	 */
	@Override
	public void setGlassCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.GLASS, amount);}
	/**
	 * Interface method for controller to pass user input garbage bin amounts to model.
	 * @param amount
	 */
	@Override
	public void setPaperCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.CARDBOARD, amount);}
	/**
	 * Interface method for controller to pass user input garbage bin amounts to model.
	 * @param amount
	 */
	@Override
	public void setBioCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.BIO, amount);}
	/**
	 * Interface method for controller to pass user input garbage bin amounts to model.
	 * @param amount
	 */
	@Override
	public void setMetalCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.METAL, amount);}
}
