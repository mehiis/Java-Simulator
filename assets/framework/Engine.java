package assets.framework;

public abstract class Engine {
	
	private double simulationTime = 0;
	
	private Clock clock;
	
	protected EventList eventList;

	public Engine(){

		clock = Clock.getInstance(); // Otetaan kello muuttujaan yksinkertaistamaan koodia
		
		eventList = new EventList();
		
		// Palvelupisteet luodaan simu.model-pakkauksessa Moottorin aliluokassa 
		
		
	}

	public void setSimulationTime(double time) {
		simulationTime = time;
	}
	
	
	public void execute(){
		init(); // luodaan mm. ensimmäinen tapahtuma
		while (simulation()){
			
			Trace.out(Trace.Level.INFO, "\nA-vaihe: kello on " + currentTime());
			clock.setTime(currentTime());
			
			Trace.out(Trace.Level.INFO, "\nB-vaihe:" );
			executeBtypeEvents();
			
			Trace.out(Trace.Level.INFO, "\nC-vaihe:" );
			tryCtypeEvents();

		}
		results();
		
	}
	
	private void executeBtypeEvents(){
		while (eventList.getNextTime() == clock.getTime()){
			executeEvent(eventList.delete());
		}
	}

	private double currentTime(){
		return eventList.getNextTime();
	}
	
	private boolean simulation(){
		return clock.getTime() < simulationTime;
	}

	protected abstract void executeEvent(Event t);  // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	protected abstract void tryCtypeEvents();	// Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void init(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	protected abstract void results(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa
	
}