package assets.model.condominium;

import assets.framework.*;
import assets.model.EventType;
import eduni.distributions.ContinuousGenerator;

import java.util.ArrayList;
import java.util.LinkedList;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageCarManager {
	private final LinkedList<GarbageCar> queue = new LinkedList<>(); // Tietorakennetoteutus
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;

	public int servedShelters = 0;

	//JonoStartegia strategia; //optio: asiakkaiden järjestys

	private boolean reserved = false;


	public GarbageCarManager(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;
	}

	public void addToQueue(GarbageCar car){   // Jonon 1. asiakas aina palvelussa
		queue.add(car);
	}


	public GarbageCar getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}


	public void startService(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		Trace.out(Trace.Level.INFO, "[Garbage car]: Collecting garbage from shelter...");// + queue.peek().getId());
		
		reserved = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime, this));
	}

	public boolean isReserved(){
		return reserved;
	}

	public boolean isQueued(){
		return queue.size() != 0;
	}

}