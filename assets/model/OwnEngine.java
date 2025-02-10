package assets.model;

import assets.framework.*;
import assets.model.condominium.GarbageShelter;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OwnEngine extends Engine {
	private ArrivalProcess 		arrivalProcess;
	private GarbageShelter[] 	garbageShelters;

	public OwnEngine(int howManyGarbageShelters) {
		garbageShelters 	= new GarbageShelter[howManyGarbageShelters];

		garbageShelters[0] 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.THROW_TRASH, 0);
		arrivalProcess 		= new ArrivalProcess(new Negexp(15,5), 		eventList, EventType.ARRIVE_TO_SHELTER);
	}

	@Override
	protected void init() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat
		int a;

		switch ((EventType)t.getType()){
			case ARRIVE_TO_SHELTER:
					garbageShelters[0].addToQueue(0);
					arrivalProcess.generateNext();
				break;
			case THROW_TRASH:
						garbageShelters[0].getFromQueue();
				break;
		}
	}

	@Override
	protected void tryCtypeEvents(){
		for (GarbageShelter shelter: garbageShelters){
			if (!shelter.isReserved() && shelter.isQueued()){
				shelter.throwTrash();
			}
		}
	}

	@Override
	protected void results() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
	}
}
