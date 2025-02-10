package assets.model.condominium;

import assets.framework.*;

import java.util.ArrayList;
import java.util.LinkedList;
import assets.model.EventType;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageShelter {
	public int howManyTimesThrashThrown = 0;
	public ArrivalProcess 		arrivalProcess;

	private static int id = 0;
	private int thisId;

	private final LinkedList<Integer> queue = new LinkedList<>(); // Tietorakennetoteutus
	private final ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;
	private final int peopleAmt;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved = false;

	private double thrashAmount = 0;


	public GarbageShelter(ContinuousGenerator generator, EventList eventList, EventType type, int peopleAmt){
		thisId = id;
		id++;

		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;
		// do some fancy math for people amount to acquire Nexexp mean
		this.peopleAmt 				= peopleAmt;

		arrivalProcess 		= new ArrivalProcess(new Negexp(peopleAmt,(int) (Math.random() * 1000000)), 		eventList, EventType.ARRIVE_TO_SHELTER, this);
	}

	public void addToQueue(Integer a){   // Jonon 1. asiakas aina palvelussa
		queue.add(a);
	}


	public Integer getFromQueue(){  // Poistetaan palvelussa ollut
		reserved = false;
		return queue.poll(); //delete the first element
	}

	public void emptyAllThrash(){
		this.thrashAmount = 0;
	}

	public void throwTrash(){  //Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana
		thrashAmount += 0.5;

		Trace.out(Trace.Level.INFO, "[Condominium "  + thisId +"]: Resident throws thrash 0.5 kg, " + thrashAmount + " kg thrash in the shelter. ");// + queue.peek().getId());
		
		reserved = true;
		//double serviceTime = generator.sample();
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime(), this)); //+serviceTime));
	}

	public int getId(){
		return thisId;
	}



	public boolean isReserved(){
		return reserved;
	}

	public boolean isQueued(){
		return queue.size() != 0;
	}

}