package assets.model;

import assets.framework.*;

import java.util.*;

import eduni.distributions.ContinuousGenerator;

// TODO:
// Palvelupistekohtaiset toiminnallisuudet, laskutoimitukset (+ tarvittavat muuttujat) ja raportointi koodattava
public class GarbageShelter {
	private final LinkedList<Apartment> queue = new LinkedList<>(); // Tietorakennetoteutus
	private ArrayList<GarbageCan> garbageCans = new ArrayList<>();
	private TrashDistribution trashGenerator = new TrashDistribution();
	private final ContinuousGenerator generator;
	private final EventList eventList;
	private final EventType scheduledEventType;
	
	//JonoStartegia strategia; //optio: asiakkaiden järjestys
	
	private boolean reserved	 	= false;
	private boolean isFull 			= false;

	//data variables
	private double[] thrashTotalInLitres 			= new double[GarbageCanType.values().length];;
	private double[] tharshThrownInKg				= new double[GarbageCanType.values().length];
	private int howManyTimeThrashThrown 			= 0;
	private int howManyTimesCarArrived				= 0;
	private double[] thrashAccessibilityRateByType 	= new double[GarbageCanType.values().length];
	private List<Double> shelterUsageRate			= new LinkedList<>();


	// Constructor with custom amount of garbage cans
	public GarbageShelter(ContinuousGenerator generator, EventList eventList, EventType type){
		this.eventList 				= eventList;
		this.generator 				= generator;
		this.scheduledEventType 	= type;
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


	public void throwTrash(){//Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana // parametrinä määrä + tyyppi?
		reserved = true;
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
		//double serviceTime = generator.sample();//+serviceTime));
		boolean thrashThrown = false;
		// generate a trash distribution according to trash amt arg
		HashMap<GarbageCanType, Double> generatedTrash = trashGenerator.getTrash(6.0);
		for (GarbageCan can : garbageCans){
			// use trash can type to get amount of said trash
			Double trashAmt = generatedTrash.get(can.getType());
			generatedTrash.put(can.getType(), 0.0); // zero out trash in hashmap after getting it once, effectively simulating trash has been thrown to one of the cans only
			if (can.checkCapacity(trashAmt)){
				can.addGarbage(trashAmt);
				System.out.println("Added "+trashAmt+" l of thrash to " + can.getType() + " trash can.");
				System.out.println("Garbage can type: " + can.getType() + " has " + can.getCurrentCapacity() + " l of trash.");

				//Data collection
				howManyTimeThrashThrown++;
				calculateThrashAmountByType(can, trashAmt);

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
		System.out.println("GARBAGE TRUCK IS HERE TO EMPTY THE GARBAGE CANS!");
		howManyTimesCarArrived++;

		for(GarbageCan can: garbageCans) {
			can.empty();
		}

		isFull = false;
	}

	private void calculateThrashAmountByType(GarbageCan can, double thrashAmtLitres){
		switch (can.getType()){
			case MIXED:
				tharshThrownInKg[0] 	+= can.getWeight();
				thrashTotalInLitres[0] 	+= thrashAmtLitres;
				break;
			case BIO:
				tharshThrownInKg[1] 	+= can.getWeight();
				thrashTotalInLitres[1] 	+= thrashAmtLitres;
				break;
			case CARDBOARD:
				tharshThrownInKg[2] 	+= can.getWeight();
				thrashTotalInLitres[2] 	+= thrashAmtLitres;
				break;
			case PLASTIC:
				tharshThrownInKg[3] 	+= can.getWeight();
				thrashTotalInLitres[3] 	+= thrashAmtLitres;
				break;
			case GLASS:
				tharshThrownInKg[4] 	+= can.getWeight();
				thrashTotalInLitres[4] 	+= thrashAmtLitres;
				break;
			case METAL:
				tharshThrownInKg[5] 	+= can.getWeight();
				thrashTotalInLitres[5] 	+= thrashAmtLitres;
				break;
		}
	}

	public int getHowManyTimeThrashThrown(){
		return  this.howManyTimeThrashThrown;
	}

	public int getGarbageCarArriveTimes(){
		return  this.howManyTimesCarArrived;
	}

	public double getThrashTotalInLitres(){
		double totalAmount = 0;

		for (int i = 0; i < thrashTotalInLitres.length; i++){
			totalAmount += thrashTotalInLitres[i];
		}

		return (double)(Math.round(totalAmount * 100) / 100); //round to one decimal place.
	}

	public double getThrashTotalInKg(){
		double totalAmount = 0;

		for (int i = 0; i < tharshThrownInKg.length; i++){
			totalAmount += tharshThrownInKg[i];
		}

		return (double)(Math.round(totalAmount * 100) / 100); //round to one decimal place.
	}

	public double[] getTharshAmountByType(GarbageCanType type){
		double[] data = new double[2];

		switch (type){
			case MIXED:
				data[0] = thrashTotalInLitres[0];
				data[1] = tharshThrownInKg[0];
				break;
			case BIO:
				data[0] = thrashTotalInLitres[1];
				data[1] = tharshThrownInKg[1];
				break;
			case CARDBOARD:
				data[0] = thrashTotalInLitres[2];
				data[1] = tharshThrownInKg[2];
				break;
			case PLASTIC:
				data[0] = thrashTotalInLitres[3];
				data[1] = tharshThrownInKg[3];
				break;
			case GLASS:
				data[0] = thrashTotalInLitres[4];
				data[1] = tharshThrownInKg[4];
				break;
			case METAL:
				data[0] = thrashTotalInLitres[5];
				data[1] = tharshThrownInKg[5];
				break;
		}

		data[0] = (double) Math.round(data[0] * 100) / 100;
		data[1] = (double) Math.round(data[1] * 100) / 100;

		return data;
	}
}