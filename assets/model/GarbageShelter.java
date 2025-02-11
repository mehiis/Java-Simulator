package assets.model;

import assets.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;

import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageShelter {
	private final LinkedList<Apartment> queue = new LinkedList<>(); // Tietorakennetoteutus
	private final ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved	 	= false;
	private boolean isFull 			= false;

	public static double thrashAmount = 0; //poista tää ku roskikset rokkaa placeholderitäsvaamoi:)


	public GarbageShelter(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;

		//TO-DO: CREATE GARBAGE CANS!!!!
	}

	public void addToQueue(Apartment a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}

	public Apartment getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}


	public void throwTrash(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		thrashAmount += 0.5;

		Trace.out(Trace.Level.INFO, "Resident throws thrash 0.5 kg, " + thrashAmount + " kg thrash in the shelter." + queue.peek().getId());
		
		reserved = true;
		//double serviceTime = generator.sample();
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime())); //+serviceTime));
	}

	public boolean isReserved(){
		return reserved;
	}

	public boolean isQueued(){
		return queue.size() != 0;
	}

	public boolean isFull(){return  isFull;}

}