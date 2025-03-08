package application.assets.framework;

import application.eduni.distributions.ContinuousGenerator;

public class ArrivalProcess {
	
	private ContinuousGenerator generaattori;
	private EventList tapahtumalista;
	private IEventType tyyppi;

	public ArrivalProcess(ContinuousGenerator g, EventList tl, IEventType tyyppi){
		this.generaattori = g;
		this.tapahtumalista = tl;
		this.tyyppi = tyyppi;
	}

	public void generateNext(){
		Event t = new Event(tyyppi, Clock.getInstance().getTime()+generaattori.sample());
		tapahtumalista.add(t);
	}



}