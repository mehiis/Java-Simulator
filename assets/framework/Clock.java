package assets.framework;

public class Clock {

	private double aika;
	private static Clock instanssi;
	
	private Clock(){
		aika = 0;
	}
	
	public static Clock getInstance(){
		if (instanssi == null){
			instanssi = new Clock();
		}
		return instanssi;
	}
	
	public void setAika(double aika){
		this.aika = aika;
	}

	public double getAika(){
		return aika;
	}
}
