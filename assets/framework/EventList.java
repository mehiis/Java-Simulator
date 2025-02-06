package assets.framework;

import java.util.PriorityQueue;

public class EventList {
	private PriorityQueue<Event> lista = new PriorityQueue<Event>();
	
	public EventList(){
	 
	}
	
	public Event poista(){
		Trace.out(Trace.Level.INFO,"Tapahtumalistasta poisto " + lista.peek().getTyyppi() + " " + lista.peek().getAika() );
		return lista.remove();
	}
	
	public void lisaa(Event t){
		Trace.out(Trace.Level.INFO,"Tapahtumalistaan lisätään uusi " + t.getTyyppi() + " " + t.getAika());
		lista.add(t);
	}
	
	public double getSeuraavanAika(){
		return lista.peek().getAika();
	}
	
	
}
