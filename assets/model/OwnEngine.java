package assets.model;

import assets.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

import java.util.ArrayList;

public class OwnEngine extends Engine {
	private ArrivalProcess 		arrivalProcess;
	private ArrivalProcess 		clearProcess;
	private GarbageShelter 		garbageShelter;

	public OwnEngine(ArrayList<GarbageCan> garbageCans, double arrivalRate){
		garbageShelter = new GarbageShelter(new Normal(10, 6), 	eventList, EventType.EXIT, garbageCans);
		arrivalProcess 		= new ArrivalProcess(new Negexp(arrivalRate,(int)(Math.random() * 10000)), eventList, EventType.ARRIVE_TO_SHELTER);
		clearProcess 		= new ArrivalProcess(new Normal(1000,1), eventList, EventType.CLEAR_GARBAGE_FROM_SHELTER);
	}


	@Override
	protected void init() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
		clearProcess.generateNext();
		garbageShelter.printThrashCans(); // !for testing purposes! //
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat
		switch ((EventType)t.getType()){
			case ARRIVE_TO_SHELTER:
					garbageShelter.addToQueue(new Apartment());
					arrivalProcess.generateNext();
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
}
