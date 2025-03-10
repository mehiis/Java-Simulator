package application.assets.framework;

public abstract class Engine extends Thread implements IEngine{
	
	private double simulationTime = 0;
	private long delayTime = 250; // Adjust later

	private Clock clock;
	
	protected EventList eventList;
	protected boolean paused = false;

	public Engine(){

		clock = Clock.getInstance(); // Otetaan kello muuttujaan yksinkertaistamaan koodia
		
		eventList = new EventList();

		clock.setTime(0); // Asetetaan kello alkuarvoon

		// Palvelupisteet luodaan simu.model-pakkauksessa Moottorin aliluokassa
	}

	public void setSimulationTime(double time) {
		simulationTime = time;
	}

	public void setDelay(long time) {
		delayTime = time;
	}

	public long getDelay() {
		return delayTime;
	}
	
	public void run(){
		init(); // luodaan mm. ensimmäinen tapahtuma
		while (simulation()) {

			if (!paused) {
				delay();

				Trace.out(Trace.Level.INFO, "\nA-Stage: simulation time is " + currentTime() + " minutes.");
				clock.setTime(currentTime());

				Trace.out(Trace.Level.INFO, "\nB-Stage:");
				executeBtypeEvents();

				Trace.out(Trace.Level.INFO, "\nC-Stage:");
				tryCtypeEvents();

			} else {
				Trace.out(Trace.Level.INFO, "\nPaused.");
			}
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

	protected abstract void results();// Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	public abstract void pressPauseButton(); // Määritellään simu.model-pakkauksessa Moottorin aliluokassa

	private void delay() { // UUSI
		Trace.out(Trace.Level.INFO, "delay " + delayTime + " ms.");
		try {
			sleep(delayTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}