package assets.model;

import assets.framework.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageShelter {
	private final LinkedList<Resident> queue = new LinkedList<>(); // Tietorakennetoteutus
	private ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private TrashDistribution trashGenerator = new TrashDistribution();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved	 	= false;
	private boolean isFull 			= false;

	// Constructor with custom amount of garbage cans
	public GarbageShelter(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;
	}

	public void addToQueue(Resident a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}

	public void addGarbageCan(GarbageCanType type, int amount){
		for (int i = 0; i < amount; i++) {
			garbageCans.add(new GarbageCan(true , type));
		}
	}

	public Resident getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}


	public void throwTrash(){//Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana // parametrinä määrä + tyyppi?
		reserved = true;
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
		//double serviceTime = generator.sample();//+serviceTime));
		boolean thrashThrown = false;
		// generate a trash distribution according to trash amt arg
		HashMap<GarbageCanType, Double> generatedTrash = trashGenerator.getTrash(4.0);
		for (GarbageCan can : garbageCans){
			// use trash can type to get amount of said trash
			Double trashAmt = generatedTrash.get(can.getType());
			if (can.checkCapacity(trashAmt)){
				can.addGarbage(trashAmt);
				System.out.println("Added "+trashAmt+" l of thrash to " + can.getType() + " trash can.");
				System.out.println("Garbage can type: " + can.getType() + " has " + can.getCurrentCapacity() + " kg of trash.");
				thrashThrown = true;
			}
		}
		if (!thrashThrown){
			isFull = true;
			System.out.println("All of the cans are full!");
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
		for(GarbageCan can: garbageCans) {
			can.empty();
		}

		isFull = false;
	}
}