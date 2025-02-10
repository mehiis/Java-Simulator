package assets.model;

import assets.framework.*;
import assets.model.condominium.Condominium;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OwnEngine extends Engine {
	
	private ArrivalProcess arrivalProcess;
	private ServicePoint[] servicePoints;

	private Condominium[] condominiums;

	public OwnEngine(int howManyCondominiums){
		condominiums = new Condominium[howManyCondominiums];

		for (int i = 0; i < howManyCondominiums; i++){
			condominiums[i] = new Condominium(10); //fixed amount of apartments
		}

		servicePoints 		= new ServicePoint[1];

		servicePoints[0]	= new ServicePoint(new Normal(10,6), eventList, EventType.THROW_TRASH);
		arrivalProcess 		= new ArrivalProcess(new Negexp(15,5), eventList, EventType.ARRIVE_TO_SHELTER);

	}


	@Override
	protected void init() {
		arrivalProcess.generateNext(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat

		Customer a;
		switch ((EventType)t.getType()){

			case ARRIVE_TO_SHELTER: servicePoints[0].addToQueue(new Customer());
				       arrivalProcess.generateNext();
				break;
			case THROW_TRASH: a = (Customer) servicePoints[0].getFromQueue();
				   	   servicePoints[1].addToQueue(a);
				break;
			case DEP2: a = (Customer) servicePoints[1].getFromQueue();
				   	   servicePoints[2].addToQueue(a);
				break;
			case DEP3:
				       a = (Customer) servicePoints[2].getFromQueue();
					   a.setExitTime(Clock.getInstance().getTime());
			           a.report();
		}
	}

	@Override
	protected void tryCtypeEvents(){
		for (ServicePoint p: servicePoints){
			if (!p.isReserved() && p.isQueued()){
				p.startService();
			}
		}
	}

	@Override
	protected void results() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getTime());
		System.out.println("Tulokset ... puuttuvat vielä");
	}

	
}
