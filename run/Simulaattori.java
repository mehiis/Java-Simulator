package run;

import assets.framework.Engine;
import assets.framework.Trace;
import assets.framework.Trace.Level;
import assets.model.OwnEngine;

public class Simulaattori { //Tekstipohjainen

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);

		Engine m = new OwnEngine(4);
		m.setSimulationTime(2500);
		m.execute();
		///
	}
}
