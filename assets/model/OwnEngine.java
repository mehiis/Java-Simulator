package assets.model;

import assets.framework.*;
import assets.model.condominium.GarbageCar;
import assets.model.condominium.GarbageShelter;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OwnEngine extends Engine {
	private ArrivalProcess 		garbageCarArrivalProcess;
	private GarbageShelter[] 	garbageShelters;
	private GarbageCar[] 		garbageCar;

	public OwnEngine(int howManyGarbageShelters) {
		garbageShelters 	= new GarbageShelter[howManyGarbageShelters];

		garbageShelters[0] 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.THROW_TRASH, 100);
		garbageShelters[1] 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.THROW_TRASH, 300);
		garbageShelters[2] 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.THROW_TRASH, 150);
		garbageShelters[3] 	= new GarbageShelter(new Normal(10, 6), 	eventList, EventType.THROW_TRASH, 200);

		garbageCar = new GarbageCar[3];

		garbageCar[0] = new GarbageCar(new Negexp(10, 100), eventList, EventType.CAR_ARRIVE);
		garbageCar[1] = new GarbageCar(new Negexp(10, 100), eventList, EventType.CAR_MOVE);
		garbageCar[2] = new GarbageCar(new Negexp(10, 100), eventList, EventType.CAR_COLLECT);

		garbageCarArrivalProcess 		= new ArrivalProcess(new Negexp(1000,(int) (Math.random() * 1000000)), 		eventList, EventType.CAR_ARRIVE, garbageShelters[0]); //init
	}

	@Override
	protected void init() {
		garbageCarArrivalProcess.generateNext();

		for (GarbageShelter shelter : garbageShelters) {
			shelter.arrivalProcess.generateNext();
		}
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat
		GarbageCar a;

		switch ((EventType)t.getType())
		{
			case ARRIVE_TO_SHELTER:
				GarbageShelter shelter = (GarbageShelter) t.getData(); // Retrieve the correct shelter
				shelter.addToQueue(0);
				shelter.arrivalProcess.generateNext();
				break;

			case THROW_TRASH:
				GarbageShelter throwingShelter = (GarbageShelter) t.getData();
				throwingShelter.getFromQueue();
				break;

			case CAR_ARRIVE:
				a = (GarbageCar) garbageCar[0].getFromQueue();
				garbageCar[1].addToQueue(a);

				garbageCarArrivalProcess.generateNext();
				break;

			case CAR_COLLECT:
				//collect from the shelter which it is now set to collect from and move
				a = (GarbageCar) garbageCar[1].getFromQueue();
				garbageCar[2].addToQueue(a);
				break;

			case CAR_MOVE:
				//if garbage car is over the index of the shelters, remove it completely if not add +1 index and MOVE to next shelter.
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

		for (GarbageCar car: garbageCar){
			if (!car.isReserved() && car.isQueued()){
				car.startService();
			}
		}
	}

	@Override
	protected void results() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
	}
}