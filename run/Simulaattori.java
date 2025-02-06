package run;

import assets.framework.Moottori;
import assets.framework.Trace;
import assets.framework.Trace.Level;
import assets.model.OmaMoottori;

public class Simulaattori { //Tekstipohjainen

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);
		Moottori m = new OmaMoottori();
		m.setSimulointiaika(1000);
		m.aja();
		///
	}
}
