package view;

import assets.framework.Engine;
import assets.framework.Trace;
import assets.framework.Trace.Level;
import assets.model.OwnEngine;
import run.Specs;

import java.util.Scanner;

public class Main { //Tekstipohjainen
	public static void main(String[] args) {
		Trace.setTraceLevel(Level.INFO);
		Scanner scanner = new Scanner(System.in);
		Specs startingSpecs = new Specs();

		startingSpecs.setGarbageCanList();
		startingSpecs.setHowManyResidents();
		startingSpecs.setHowOftenTrashIsTakenOut();
		// startingSpecs.setDefaultGarbageCanList(); // For faster testing, can delete later (default = 1 of each)


		Engine m = new OwnEngine(startingSpecs.getGarbageCanList(), startingSpecs.getMeanArrivalRate());
		m.setSimulationTime(startingSpecs.getSimulationTime());
		m.execute();
		///
	}
}