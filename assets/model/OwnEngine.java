package assets.model;

import assets.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OwnEngine extends Engine {
	
	private ArrivalProcess 		arrivalProcess;
	private GarbageShelter 	garbageShelters;

	public OwnEngine() {
		garbageShelters 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.THROW_TRASH, 0);
		arrivalProcess 		= new ArrivalProcess(new Negexp(15,5), 		eventList, EventType.ARRIVE_TO_SHELTER);
	}


	@Override
	protected void init() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat
		Apartment a;

		switch ((EventType)t.getType()){
			case ARRIVE_TO_SHELTER:
					garbageShelters.addToQueue(new Apartment());
					arrivalProcess.generateNext();
				break;
			case THROW_TRASH:
						garbageShelters.getFromQueue();
				   	   //servicePoints[1].addToQueue(a);
				break;
			/*case DEP3:
				       a = (Customer) servicePoints[2].getFromQueue();
					   a.setExitTime(Clock.getInstance().getTime());
			           a.report();*/
		}
	}

	@Override
	protected void tryCtypeEvents(){
			if (!garbageShelters.isReserved() && garbageShelters.isQueued()){
				garbageShelters.throwTrash();
			}
	}

	@Override
	protected void results() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
	}

	
}
