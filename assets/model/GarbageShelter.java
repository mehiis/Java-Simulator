package assets.model;

import assets.framework.*;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Negexp;

import java.util.*;
import java.util.stream.Collectors;
import java.util.function.*;

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
	private double[] thrashTotalInLitres 			= new double[GarbageCanType.values().length];;
	private double[] tharshThrownInKg				= new double[GarbageCanType.values().length];
	private int howManyTimeThrashThrown 			= 0;
	private int howManyTimesCarArrived				= 0;
	private double[] thrashAccessibilityRateByType 	= new double[GarbageCanType.values().length];
	private HashMap<GarbageCanType, LinkedList<Double>> shelterUsageRate = new HashMap<>() {
		{
			put(GarbageCanType.MIXED, new LinkedList<>());
			put(GarbageCanType.BIO, new LinkedList<>());
			put(GarbageCanType.CARDBOARD, new LinkedList<>());
			put(GarbageCanType.PLASTIC, new LinkedList<>());
			put(GarbageCanType.GLASS, new LinkedList<>());
			put(GarbageCanType.METAL, new LinkedList<>());
		}
	};

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

	// final trash placement into respective cans, separated this out to prevent nested ifs
	public void putTrash(GarbageCan can, double trashAmt, HashMap<GarbageCanType, Double> generatedTrash) {
		if (can.checkCapacity(trashAmt)){
			can.addGarbage(trashAmt);
			generatedTrash.put(can.getType(), 0.0); // zero out trash in hashmap after getting it once, effectively simulating trash has been thrown to one of the cans only
			System.out.println("Added "+trashAmt+" l of thrash to " + can.getType() + " trash can.");
			System.out.println("Garbage can type: " + can.getType() + " has " + can.getCurrentCapacity() + " l of trash.");

			//Data collection
			howManyTimeThrashThrown++;
			calculateThrashAmountByType(can, trashAmt);

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

	public void throwTrash(){//Aloitetaan uusi palvelu, asiakas on jonossa palvelun aikana // parametrinä määrä + tyyppi?
		reserved = true;
		eventList.add(new Event(scheduledEventType, Clock.getInstance().getTime()));
		//double serviceTime = generator.sample();//+serviceTime));

		// generate a trash distribution according to trash amt arg
		HashMap<GarbageCanType, Double> generatedTrash = trashGenerator.getTrash(trashThrowAmtMean);

		// form a list of types to prepare for duplicate checking
		List<GarbageCanType> garbageCanTypesList = new ArrayList<>();
		for (GarbageCan can: garbageCans) {
			garbageCanTypesList.add(can.getType());
		}

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
		howManyTimesCarArrived++;

		for(GarbageCan can: garbageCans) {
			calculateUsageRate(can);
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

	private void calculateUsageRate(GarbageCan can){
		double usageRate = (double) Math.round((can.getCurrentCapacity() / can.getCapacity()) * 100) / 100;

		System.out.println("Usage rate of " + can.getType() + " can: " + usageRate + "%." + " Current capacity: " + can.getCurrentCapacity() + " l divided by " + can.getCapacity() + " l.");

		shelterUsageRate.get(can.getType()).add(usageRate);
	}

	public double getAverageUsageRateTotal(){
		double totalUsageRate = 0;

		for (GarbageCanType type: GarbageCanType.values()){
			totalUsageRate += getAverageRateOfType(type);
		}

		return (double) Math.round((totalUsageRate / GarbageCanType.values().length) * 100) / 100;
	}

	public double getAverageRateOfType(GarbageCanType type){
		double totalUsageRate = 0;

		for (double rate: shelterUsageRate.get(type)){
			totalUsageRate += rate;
		}

		return (double) Math.round((totalUsageRate / shelterUsageRate.get(type).size()) * 100) / 100;
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

	public double[] getThrashAmountByType(GarbageCanType type){
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


	public double getOverflowTrash(GarbageCanType type) {
		if (overflowTrash.isEmpty()) return 0; // if there is not any overflow

		return overflowTrash.get(type);
	}
}