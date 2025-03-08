package application.assets.model;

import application.assets.framework.ArrivalProcess;
import application.assets.framework.Clock;
import application.assets.framework.Engine;
import application.assets.framework.Event;
import application.assets.framework.*;
import application.controller.Controller;
import application.eduni.distributions.Negexp;
import application.eduni.distributions.Normal;
import application.run.Specs;

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
		yksioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(1, controller.getSingleAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.YKSIO_ARRIVE_TO_SHELTER);
		kaksioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(2, controller.getDoubleAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.KAKSIO_ARRIVE_TO_SHELTER);
		kolmioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(3, controller.getTripleAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.KOLMIO_ARRIVE_TO_SHELTER);
		nelioArrivalProcess = new ArrivalProcess(new Negexp(Specs.getMeanArrivalRate(4, controller.getQuadAptAmt(), meanTrashThrowAmt),(int)(Math.random() * 10000)), eventList, EventType.NELIO_ARRIVE_TO_SHELTER);

		clearProcess 		= new ArrivalProcess(new Normal(controller.getGarbageTruckArrivalInterval(),1), eventList, EventType.CLEAR_GARBAGE_FROM_SHELTER);
	}


	@Override
	protected void init() {
		// start needed arrival processes as defined in GUI as params
		yksioArrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
		kaksioArrivalProcess.generateNext();
		kolmioArrivalProcess.generateNext();
		nelioArrivalProcess.generateNext();
		clearProcess.generateNext();
		garbageShelter.printThrashCans();// !for testing purposes! //
	}

	@Override
	protected void executeEvent(Event t){// B-vaiheen tapahtumat

		controller.setDay();

		switch ((EventType)t.getType()){
			case YKSIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					yksioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.YKSIO_ARRIVE_TO_SHELTER, garbageShelter.data.getGarbageCanCapacityPercentagesByType());
					break;
			case KAKSIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					kaksioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.KAKSIO_ARRIVE_TO_SHELTER, garbageShelter.data.getGarbageCanCapacityPercentagesByType());
					break;
			case KOLMIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					kolmioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.KOLMIO_ARRIVE_TO_SHELTER, garbageShelter.data.getGarbageCanCapacityPercentagesByType());
					break;
			case NELIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					nelioArrivalProcess.generateNext();
					controller.visualizeResident(EventType.NELIO_ARRIVE_TO_SHELTER, garbageShelter.data.getGarbageCanCapacityPercentagesByType());
					break;
			case EXIT:
					garbageShelter.getFromQueue();//TRASH HAS BEEN THROWN! :)
					break;
			case CLEAR_GARBAGE_FROM_SHELTER:
					garbageShelter.clearGarbageCans();
					clearProcess.generateNext();
					break;
		}
	}

	@Override
	protected void tryCtypeEvents(){
			if (!garbageShelter.isReserved() && garbageShelter.isQueued() && !garbageShelter.isFull()){
				garbageShelter.throwTrash();
			}

			garbageShelter.garbageCanStates();
	}

	@Override
	protected void results() {
		controller.naytaLoppuaika(Clock.getInstance().getTime());

		controller.setTrashThrownTimes(garbageShelter.data.getHowManyTimeThrashThrown());
		controller.setShelterClearedTimes(garbageShelter.data.getGarbageCarArriveTimes());

		controller.setTrashThrownTotal(garbageShelter.data.getThrashTotalInKg(), garbageShelter.data.getThrashTotalInLitres());

		controller.setMixedTotal(garbageShelter.data.getThrashAmountByType(GarbageCanType.MIXED)[0]);
		controller.setBioTotal(garbageShelter.data.getThrashAmountByType(GarbageCanType.BIO)[0] );
		controller.setCardboardTotal(garbageShelter.data.getThrashAmountByType(GarbageCanType.CARDBOARD)[0]);
		controller.setPlasticTotal(garbageShelter.data.getThrashAmountByType(GarbageCanType.PLASTIC)[0]);
		controller.setGlassTotal(garbageShelter.data.getThrashAmountByType(GarbageCanType.GLASS)[0] );
		controller.setMetalTotal(garbageShelter.data.getThrashAmountByType(GarbageCanType.METAL)[0]);

		controller.setMixedUsage(garbageShelter.data.getAverageRateOfType(GarbageCanType.MIXED) );
		controller.setBioUsage(garbageShelter.data.getAverageRateOfType(GarbageCanType.BIO) );
		controller.setCardboardUsage(garbageShelter.data.getAverageRateOfType(GarbageCanType.CARDBOARD));
		controller.setPlasticUsage(garbageShelter.data.getAverageRateOfType(GarbageCanType.PLASTIC));
		controller.setGlassUsage(garbageShelter.data.getAverageRateOfType(GarbageCanType.GLASS));
		controller.setMetalUsage(garbageShelter.data.getAverageRateOfType(GarbageCanType.METAL));

		controller.setMixedOverflow(garbageShelter.getOverflowTrash(GarbageCanType.MIXED));
		controller.setBioOverflow(garbageShelter.getOverflowTrash(GarbageCanType.BIO));
		controller.setCardboardOverflow(garbageShelter.getOverflowTrash(GarbageCanType.CARDBOARD));
		controller.setPlasticOverflow(garbageShelter.getOverflowTrash(GarbageCanType.PLASTIC));
		controller.setGlassOverflow(garbageShelter.getOverflowTrash(GarbageCanType.GLASS));
		controller.setMetalOverflow(garbageShelter.getOverflowTrash(GarbageCanType.METAL));

		System.out.println("\n\n### SIMULATION ENDED###\nSimulation lasted for " + (double)(Math.round(Clock.getInstance().getTime()*100)/100) + " minutes.");
		System.out.println(
						"\nCOLLECTED DATA PRINT:\n" +
						"Thrash thrown " + garbageShelter.data.getHowManyTimeThrashThrown() + " times.\n" +
						"Garbage truck arrived " + garbageShelter.data.getGarbageCarArriveTimes() + " times.\n\n" +

						"Thrash thrown in total: " + garbageShelter.data.getThrashTotalInLitres() + " litres/" + garbageShelter.data.getThrashTotalInKg() +" kg. \n" +
								"MIXED: " 		+ garbageShelter.data.getThrashAmountByType(GarbageCanType.MIXED)[0] 	+ "l/" + garbageShelter.data.getThrashAmountByType(GarbageCanType.MIXED)[1] 		+ "kg.\n" +
								"BIO: " 		+ garbageShelter.data.getThrashAmountByType(GarbageCanType.BIO)[0] 		+ "l/" + garbageShelter.data.getThrashAmountByType(GarbageCanType.BIO)[1] 		+ "kg.\n" +
								"CARDBOARD: " 	+ garbageShelter.data.getThrashAmountByType(GarbageCanType.CARDBOARD)[0] + "l/" + garbageShelter.data.getThrashAmountByType(GarbageCanType.CARDBOARD)[1] 	+ "kg.\n" +
								"PLASTIC: " 	+ garbageShelter.data.getThrashAmountByType(GarbageCanType.PLASTIC)[0] 	+ "l/" + garbageShelter.data.getThrashAmountByType(GarbageCanType.PLASTIC)[1] 	+ "kg.\n" +
								"GLASS: " 		+ garbageShelter.data.getThrashAmountByType(GarbageCanType.GLASS)[0] 	+ "l/" + garbageShelter.data.getThrashAmountByType(GarbageCanType.GLASS)[1] 		+ "kg.\n" +
								"METAL: " 		+ garbageShelter.data.getThrashAmountByType(GarbageCanType.METAL)[0] 	+ "l/" + garbageShelter.data.getThrashAmountByType(GarbageCanType.METAL)[1] 		+ "kg.\n\n" +

						"Shelter usage rate: " + garbageShelter.data.getAverageUsageRateTotal() + "%.\n" +
								"MIXED: " 		+ garbageShelter.data.getAverageRateOfType(GarbageCanType.MIXED) 	+ "%.\n" +
								"BIO: " 		+ garbageShelter.data.getAverageRateOfType(GarbageCanType.BIO) 	+ "%.\n" +
								"CARDBOARD: " 	+ garbageShelter.data.getAverageRateOfType(GarbageCanType.CARDBOARD) 	+ "%.\n" +
								"PLASTIC: " 	+ garbageShelter.data.getAverageRateOfType(GarbageCanType.PLASTIC) 	+ "%.\n" +
								"GLASS: " 		+ garbageShelter.data.getAverageRateOfType(GarbageCanType.GLASS) 	+ "%.\n" +
								"METAL: " 		+ garbageShelter.data.getAverageRateOfType(GarbageCanType.METAL) 	+ "%.\n\n" +

						"Trash overflows: \n" +
								" MIXED: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.MIXED)) + " l\n" +
								" BIO: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.BIO)) + " l\n" +
								" CARDBOARD: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.CARDBOARD)) + " l\n" +
								" PLASTIC: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.PLASTIC)) + " l\n" +
								" GLASS: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.GLASS)) + " l\n" +
								" METAL: " + String.format("%.0f", garbageShelter.getOverflowTrash(GarbageCanType.METAL)) + " l\n"
		);
	}

	@Override
	public void setMixedCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.MIXED, amount);}

	@Override
	public void setPlasticCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.PLASTIC, amount);}

	@Override
	public void setGlassCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.GLASS, amount);}

	@Override
	public void setPaperCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.CARDBOARD, amount);}

	@Override
	public void setBioCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.BIO, amount);}

	@Override
	public void setMetalCanAmountValue(int amount) {garbageShelter.addGarbageCan(GarbageCanType.METAL, amount);}
}
