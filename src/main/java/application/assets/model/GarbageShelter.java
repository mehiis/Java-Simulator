package application.assets.model;

import application.assets.framework.Clock;
import application.assets.framework.Event;
import application.assets.framework.EventList;
import application.assets.framework.*;
import application.eduni.distributions.ContinuousGenerator;
import application.eduni.distributions.Negexp;

import java.util.*;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageShelter {
	private final LinkedList<Apartment> queue = new LinkedList<>(); // Tietorakennetoteutus
	private ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private TrashDistribution trashGenerator = new TrashDistribution();
	private final EventList eventList;
	private final EventType scheduledEventType;
	private final double trashThrowAmtMean;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved	 	= false;
	private boolean isFull 			= false;

	//data variables
	private HashMap<GarbageCanType, Double> overflowTrash = new HashMap<>();
	public CollectedData data = new CollectedData(garbageCans);


	// Constructor with custom amount of garbage cans
	public GarbageShelter(EventList eventList, EventType type, double meanTrashThrowAmt){
		this.eventList 				= eventList;
		this.scheduledEventType 	= type;
		// trash throw amt is passed from GUI -> controller -> engine -> here -> throwTrash()/trash distribution
		this.trashThrowAmtMean = meanTrashThrowAmt;
	}

	public void addToQueue(Apartment a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}

	public void addGarbageCan(GarbageCanType type, int amount){
		for (int i = 0; i < amount; i++) {
			garbageCans.add(new GarbageCan(true , type));
		}
	}

	public Apartment getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}

	// SORRY ABOUT THE MESS
	// final trash placement into respective cans, separated this out to prevent nested ifs
	public void putTrash(GarbageCan can, double trashAmt, HashMap<GarbageCanType, Double> generatedTrash) {
		if (can.checkCapacity(trashAmt)){
			can.addGarbage(trashAmt);
			generatedTrash.put(can.getType(), 0.0); // zero out trash in hashmap after getting it once, effectively simulating trash has been thrown to one of the cans only
			System.out.println("Added "+trashAmt+" l of thrash to " + can.getType() + " trash can.");
			System.out.println("Garbage can type: " + can.getType() + " has " + can.getCurrentCapacity() + " l of trash.");

			//Data collection
			data.calculateThrashAmountByType(can, trashAmt);
		}
		else {
			// get type
			GarbageCanType type = can.getType();
			// Init entry if absent
			overflowTrash.putIfAbsent(type, 0.0);
			// add trash to the entry value
			overflowTrash.put(type, overflowTrash.get(type) + trashAmt);

			System.out.println("OVERFLOW IN " + type + " CAN! Added " + trashAmt + " to overflow" );

		}
	}

	public void throwTrash(){
		//Data collection, do this here because it needs to be incremented only once
		data.addThrownThrash();

		reserved = true;
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
		//double serviceTime = generator.sample();//+serviceTime));

		// generate a trash distribution according to trash amt arg
		HashMap<GarbageCanType, Double> generatedTrash = trashGenerator.getTrash(trashThrowAmtMean);

		for (GarbageCanType currentCanType : generatedTrash.keySet()) {
			Double trashAmt = generatedTrash.get(currentCanType);

			// find all cans of the current type
			List<GarbageCan> cansOfType = garbageCans.stream().filter(can -> can.getType() == currentCanType).toList();

			if (cansOfType.size() > 1) {
				// if there are multiple cans of the same type, use Negexp to choose one
				ContinuousGenerator chooseCan = new Negexp(1.0);
				//math.min() to constrain chosen index to available indices
				int chosenIndex = (int) Math.min(cansOfType.size() - 1, chooseCan.sample() * cansOfType.size());
				GarbageCan chosenCan = cansOfType.get(chosenIndex);
				// place trash
				putTrash(chosenCan, trashAmt, generatedTrash);
			} else {
				if (cansOfType.size() == 1) {
					// if only one can of this type, throw trash into it
					GarbageCan chosenCan = cansOfType.get(0);
					putTrash(chosenCan, trashAmt, generatedTrash);
				}
			}
		}
	}

	// testing purposes
	public void printThrashCans(){
		System.out.println("Garbage cans in the shelter:");
		for(GarbageCan can : garbageCans){
			System.out.println(can.getType());
		}
	}

	public boolean isReserved(){
		return reserved;
	}

	public boolean isQueued(){
		return queue.size() != 0;
	}

	public boolean isFull(){return  isFull;}

	public void garbageCanStates(){
		for(GarbageCan can: garbageCans)
			System.out.println(can.getType() + ": " + can.getCurrentCapacity() + "/"  + can.getCapacity() + ". Is full?: " + !can.checkCapacity(can.getCurrentCapacity()));
	}

	public void clearGarbageCans(){
		System.out.println("GARBAGE TRUCK IS HERE TO EMPTY THE GARBAGE CANS!");
		data.addGarbageCarArrival();

		for(GarbageCan can: garbageCans) {
			data.calculateUsageRate(can);
			can.empty();
		}

		isFull = false;
	}

	public double getOverflowTrash(GarbageCanType type) {
		return overflowTrash.getOrDefault(type, 0.0); // return default if no overflow occurred in the can
	}
}