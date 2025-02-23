package assets.model;

import assets.framework.*;
import controller.Controller;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import run.Specs;

public class OwnEngine extends Engine {
	private ArrivalProcess yksioArrivalProcess;
	private ArrivalProcess kaksioArrivalProcess;
	private ArrivalProcess kolmioArrivalProcess;
	private ArrivalProcess nelioArrivalProcess;
	private ArrivalProcess 		clearProcess;
	private GarbageShelter 		garbageShelter;
	private Controller 			controller;
	private long 				delayTime = 0;


	public OwnEngine(Controller controller){
		this.controller = controller;

		// added starting specs here for now! these should come from GUI simulation parameters panel in the end.
		Specs startingSpecs = new Specs();

		garbageShelter = new GarbageShelter(new Normal(10, 6), 	eventList, EventType.EXIT);
		// GUI should be read; which arrival processes to create and use. GUI should also decide apt amt.
		// people amt is basically a multiplier for the base amount of trash a specific type of apt generates.
		yksioArrivalProcess = new ArrivalProcess(new Negexp(startingSpecs.getMeanArrivalRate(1, 6),(int)(Math.random() * 10000)), eventList, EventType.YKSIO_ARRIVE_TO_SHELTER);
		kaksioArrivalProcess = new ArrivalProcess(new Negexp(startingSpecs.getMeanArrivalRate(2, 3),(int)(Math.random() * 10000)), eventList, EventType.KAKSIO_ARRIVE_TO_SHELTER);
		kolmioArrivalProcess = new ArrivalProcess(new Negexp(startingSpecs.getMeanArrivalRate(3, 2),(int)(Math.random() * 10000)), eventList, EventType.KOLMIO_ARRIVE_TO_SHELTER);
		nelioArrivalProcess = new ArrivalProcess(new Negexp(startingSpecs.getMeanArrivalRate(4, 1),(int)(Math.random() * 10000)), eventList, EventType.NELIO_ARRIVE_TO_SHELTER);

		// clearing happens every week which is 10080 minutes
		clearProcess 		= new ArrivalProcess(new Normal(10080,1), eventList, EventType.CLEAR_GARBAGE_FROM_SHELTER);
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
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat
		switch ((EventType)t.getType()){
			case YKSIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					yksioArrivalProcess.generateNext();
				break;
			case KAKSIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					kaksioArrivalProcess.generateNext();
				break;
			case KOLMIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					kolmioArrivalProcess.generateNext();
				break;
			case NELIO_ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					nelioArrivalProcess.generateNext();
				break;
			case EXIT:
					garbageShelter.getFromQueue(); //TRASH HAS BEEN THROWN! :)
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
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
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
