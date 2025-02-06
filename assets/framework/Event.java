package assets.framework;

public class Event implements Comparable<Event> {

	private IEventType tyyppi;
	private double aika;
	
	public Event(IEventType tyyppi, double aika){
		this.tyyppi = tyyppi;
		this.aika = aika;
	}
	
	public void setTyyppi(IEventType tyyppi) {
		this.tyyppi = tyyppi;
	}
	public IEventType getTyyppi() {
		return tyyppi;
	}
	public void setAika(double aika) {
		this.aika = aika;
	}
	public double getAika() {
		return aika;
	}

	@Override
	public int compareTo(Event arg) {
		if (this.aika < arg.aika) return -1;
		else if (this.aika > arg.aika) return 1;
		return 0;
	}

}
