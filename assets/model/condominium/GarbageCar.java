package assets.model.condominium;

import assets.framework.*;
import assets.model.EventType;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;

import java.util.ArrayList;
import java.util.LinkedList;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageCar {
	private final LinkedList<GarbageCar> queue = new LinkedList<>(); // Tietorakennetoteutus
	private final ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;

	//JonoStartegia strategia; //optio: asiakkaiden järjestys

	private boolean reserved = false;

	private int whichShelter = 0;


	public GarbageCar(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;

		whichShelter = 0;
	}

	public void addToQueue(GarbageCar a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}


	public GarbageCar getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}


	public void startService(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana


		Trace.out(Trace.Level.INFO, "[Garbage car]: Collecting garbage from shelter " + whichShelter + " .");// + queue.peek().getId());
		
		reserved = true;
		double serviceTime = generator.sample();
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime() + serviceTime, this));

		whichShelter++;
	}

	public boolean isReserved(){
		return reserved;
	}

	public boolean isQueued(){
		return queue.size() != 0;
	}

}