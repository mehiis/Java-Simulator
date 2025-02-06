package assets.model;

import assets.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OwnEngine extends Engine {
	
	private ArrivalProcess saapumisprosessi;

	private ServicePoint[] palvelupisteet;

	public OwnEngine(){

		palvelupisteet = new ServicePoint[3];

		palvelupisteet[0]=new ServicePoint(new Normal(10,6), eventList, EventType.DEP1);
		palvelupisteet[1]=new ServicePoint(new Normal(10,10), eventList, EventType.DEP2);
		palvelupisteet[2]=new ServicePoint(new Normal(5,3), eventList, EventType.DEP3);

		saapumisprosessi = new ArrivalProcess(new Negexp(15,5), eventList, EventType.ARR1);

	}


	@Override
	protected void init() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void executeEvent(Event t){  // B-vaiheen tapahtumat

		Customer a;
		switch ((EventType)t.getType()){

			case ARR1: palvelupisteet[0].addToQue(new Customer());
				       saapumisprosessi.generoiSeuraava();
				break;
			case DEP1: a = (Customer)palvelupisteet[0].getFromQue();
				   	   palvelupisteet[1].addToQue(a);
				break;
			case DEP2: a = (Customer)palvelupisteet[1].getFromQue();
				   	   palvelupisteet[2].addToQue(a);
				break;
			case DEP3:
				       a = (Customer)palvelupisteet[2].getFromQue();
					   a.setExitTime(Clock.getInstance().getTime());
			           a.report();
		}
	}

	@Override
	protected void tryCtypeEvents(){
		for (ServicePoint p: palvelupisteet){
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
