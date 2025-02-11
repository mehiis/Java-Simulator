package assets.model;

import assets.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

import java.util.ArrayList;

public class OwnEngine extends Engine {
	private ArrivalProcess 		arrivalProcess;
	private GarbageShelter 	garbageShelters;

	public OwnEngine(ArrayList<GarbageCan> garbageCans){
		garbageShelters 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.EXIT, garbageCans);
		arrivalProcess 		= new ArrivalProcess(new Negexp(15,(int)(Math.random() * 10000)), eventList, EventType.ARRIVE_TO_SHELTER);
	}


	@Override
	protected void init() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
		garbageShelters.printThrashCans(); // !for testing purposes! //
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat
		Apartment apartment;

		switch ((EventType)t.getType()){
			case ARRIVE_TO_SHELTER:
					garbageShelters.addToQueue(new Apartment());
					arrivalProcess.generateNext();
				break;
			case EXIT:
						garbageShelters.getFromQueue(); //TRASH HAS BEEN THROWN! :)
				break;
		}
	}

	@Override
	protected void tryCtypeEvents(){
			if (!garbageShelters.isReserved() && garbageShelters.isQueued() && !garbageShelters.isFull()){
				garbageShelters.throwTrash();
			}
	}

	@Override
	protected void results() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
	}
}
