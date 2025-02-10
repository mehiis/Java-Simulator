package run;

import assets.framework.Engine;
import assets.framework.Trace;
import assets.framework.Trace.Level;
import assets.model.OwnEngine;

public class Simulaattori { //Tekstipohjainen

	public static void main(String[] args) {
		
		Trace.setTraceLevel(Level.INFO);

		Engine m = new OwnEngine(1);
		m.setSimulationTime(1000);
		m.execute();
		///
	}
}
