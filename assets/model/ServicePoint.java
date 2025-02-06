package assets.model;

import assets.framework.*;
import java.util.LinkedList;
import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class ServicePoint {

	private final LinkedList<Customer> queue = new LinkedList<>(); // Tietorakennetoteutus
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved = false;


	public ServicePoint(ContinuousGenerator generator, EventList tapahtumalista, EventType tyyppi){
		this.eventList = tapahtumalista;
		this.generator = generator;
		this.scheduledEventType = tyyppi;
				
	}


	public void addToQue(Customer a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
		
	}


	public Customer getFromQue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll();
	}


	public void startService(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		
		Trace.out(Trace.Level.INFO, "Aloitetaan uusi palvelu asiakkaalle " + queue.peek().getId());
		
		reserved = true;
		double palveluaika = generator.sample();
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()+palveluaika));
	}



	public boolean isReserved(){
		return reserved;
	}



	public boolean isQueued(){
		return queue.size() != 0;
	}

}
