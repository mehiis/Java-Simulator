package assets.framework;
import assets.model.condominium.GarbageShelter;
import eduni.distributions.*;

public class ArrivalProcess {
	
	private ContinuousGenerator generaattori;
	private EventList tapahtumalista;
	private IEventType tyyppi;
	private GarbageShelter shelter;

	public ArrivalProcess(ContinuousGenerator g, EventList tl, IEventType tyyppi, GarbageShelter shelter){
		this.generaattori = g;
		this.tapahtumalista = tl;
		this.tyyppi = tyyppi;
		this.shelter = shelter;
	}

	public void generateNext(){
		Event t = new Event(tyyppi, Clock.getInstance().getTime()+generaattori.sample(), shelter);
		tapahtumalista.add(t);
		System.out.println("Uusi tapahtuma " + shelter.getId() + " shelteriin ");
	}

}
