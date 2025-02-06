package assets.model;

import assets.framework.*;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;

public class OwnEngine extends Engine {
	
	private ArrivalProcess saapumisprosessi;

	private ServicePoint[] palvelupisteet;

	public OwnEngine(){

		palvelupisteet = new ServicePoint[3];

		palvelupisteet[0]=new ServicePoint(new Normal(10,6), tapahtumalista, EventType.DEP1);
		palvelupisteet[1]=new ServicePoint(new Normal(10,10), tapahtumalista, EventType.DEP2);
		palvelupisteet[2]=new ServicePoint(new Normal(5,3), tapahtumalista, EventType.DEP3);

		saapumisprosessi = new ArrivalProcess(new Negexp(15,5), tapahtumalista, EventType.ARR1);

	}


	@Override
	protected void alustukset() {
		saapumisprosessi.generoiSeuraava(); // Ensimmäinen saapuminen järjestelmään
	}

	@Override
	protected void suoritaTapahtuma(Event t){  // B-vaiheen tapahtumat

		Customer a;
		switch ((EventType)t.getTyyppi()){

			case ARR1: palvelupisteet[0].lisaaJonoon(new Customer());
				       saapumisprosessi.generoiSeuraava();
				break;
			case DEP1: a = (Customer)palvelupisteet[0].otaJonosta();
				   	   palvelupisteet[1].lisaaJonoon(a);
				break;
			case DEP2: a = (Customer)palvelupisteet[1].otaJonosta();
				   	   palvelupisteet[2].lisaaJonoon(a);
				break;
			case DEP3:
				       a = (Customer)palvelupisteet[2].otaJonosta();
					   a.setPoistumisaika(Clock.getInstance().getAika());
			           a.raportti();
		}
	}

	@Override
	protected void yritaCTapahtumat(){
		for (ServicePoint p: palvelupisteet){
			if (!p.onVarattu() && p.onJonossa()){
				p.aloitaPalvelu();
			}
		}
	}

	@Override
	protected void tulokset() {
		System.out.println("Simulointi päättyi kello " + Clock.getInstance().getAika());
		System.out.println("Tulokset ... puuttuvat vielä");
	}

	
}
