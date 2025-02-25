package controller;

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
		double daysToMinutes = (ui.getSimulationTimeValue() * 1440);
		engine = new OwnEngine(this); // luodaan uusi moottorisäie jokaista simulointia varten
		engine.setSimulationTime(daysToMinutes);

		engine.setMixedCanAmountValue(ui.getMixedCanAmountValue());
		engine.setBioCanAmountValue(ui.getBioCanAmountValue());
		engine.setPaperCanAmountValue(ui.getPaperCanAmountValue());
		engine.setGlassCanAmountValue(ui.getGlassCanAmountValue());
		engine.setMetalCanAmountValue(ui.getMetalCanAmountValue());
		engine.setPlasticCanAmountValue(ui.getPlasticCanAmountValue());

		//engine.setDelay(ui.getViive());
		//ui.getVisualisointi().tyhjennaNaytto();


		((Thread)engine).start();
	}
	
	@Override
	public void slowDown() { // hidastetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*1.10));
	}

	@Override
	public void speedUp() { // nopeutetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*0.9));
	}
	

	// Simulointitulosten välittämistä käyttöliittymään.
	// Koska FX-ui:n päivitykset tulevat moottorisäikeestä, ne pitää ohjata JavaFX-säikeeseen:

	@Override
	public double getMeanTrashPerThrowAmt() {
		return ui.getMeanTrashAmtPerThrow();
	}

	@Override
	public int getSingleAptAmt() {
		return ui.getSingleAptAmt();
	}

	@Override
	public int getDoubleAptAmt() {
		return ui.getDoubleAptAmt();
	}

	@Override
	public int getTripleAptAmt() {
		return ui.getTripleAptAmt();
	}

	@Override
	public int getQuadAptAmt() {
		return ui.getQuadAptAmt();
	}

	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(()->ui.setLoppuaika(aika)); 
	}

	@Override
	public void setMixedTotal(double amt) {
		Platform.runLater(()->ui.setMixedTotal(amt));
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
