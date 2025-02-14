package controller;

import assets.framework.Clock;
import javafx.application.Platform;
import assets.framework.IEngine;
import assets.model.OwnEngine;
import view.ISimulatorGUI;

public class Controller implements IControllerForModel, IControllerForView {   // UUSI
	private IEngine engine;
	private ISimulatorGUI ui;
	
	public Controller(ISimulatorGUI ui) {
		this.ui = ui;
	}
	
	// Moottorin ohjausta:
	@Override
	public void startSimulation() {

		System.out.println("Start simulation.");
		engine = new OwnEngine(this); // luodaan uusi moottorisäie jokaista simulointia varten
		engine.setSimulationTime(ui.getTime());
		//engine.setDelay(ui.getViive());
		//ui.getVisualisointi().tyhjennaNaytto();


		((Thread)engine).start();
	}
	
	@Override
	public void hidasta() { // hidastetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*1.10));
	}

	@Override
	public void nopeuta() { // nopeutetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*0.9));
	}
	

	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen:
		
	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(()->ui.setLoppuaika(aika)); 
	}

	
	@Override
	public void visualisoiAsiakas() {
		Platform.runLater(new Runnable(){
			public void run(){
				ui.getVisualisointi().uusiAsiakas();
			}
		});
	}
}
