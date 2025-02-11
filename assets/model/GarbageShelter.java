package assets.model;

import assets.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageShelter {
	private final LinkedList<Apartment> queue = new LinkedList<>(); // Tietorakennetoteutus
	private ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved	 	= false;
	private boolean isFull 			= false;

	// Voidaan poistaa myöhemmin "default" constructor myöhemmin, säästetään nopeampia testauksia varten
	public GarbageShelter(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;

		garbageCans.add(new GarbageCan(true, GarbageCanType.MIXED));
		garbageCans.add(new GarbageCan(false, GarbageCanType.BIO));
		garbageCans.add(new GarbageCan(true, GarbageCanType.CARDBOARD));
		garbageCans.add(new GarbageCan(true, GarbageCanType.PLASTIC));
		garbageCans.add(new GarbageCan(false, GarbageCanType.GLASS));
		garbageCans.add(new GarbageCan(false, GarbageCanType.METAL));
	}

	// Constructor with custom amount of garbage cans
	public GarbageShelter(ContinuousGenerator generator, EventList eventList, EventType type, ArrayList<GarbageCan> garbageCans){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;
		this.garbageCans 			= garbageCans;
	}

	public void addToQueue(Apartment a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}

	public Apartment getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}


	public void throwTrash(){//Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana // parametrinä määrä + tyyppi?
		reserved = true;
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
		//double serviceTime = generator.sample();//+serviceTime));
		boolean thrashThrown = false;
		for (GarbageCan can : garbageCans){
			if (can.checkCapacity(100)){
				can.addGarbage(100);
				System.out.println("Added 100 l of thrash to " + can.getType() + " trash can.");
				System.out.println("Garbage can type: " + can.getType() + " has " + can.getCurrentCapacity() + " kg of trash.");
				thrashThrown = true;
				break;
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

}