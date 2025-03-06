package application.controller;

import application.assets.framework.Clock;
import dao.InputParametersDao;
import entity.InputParameters;
import application.assets.model.ApartmentType;
import application.assets.model.EventType;
import application.assets.model.GarbageCanType;
import javafx.application.Platform;
import application.assets.framework.IEngine;
import application.assets.model.OwnEngine;
import application.view.ISimulatorGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
		saveInputsToDb();
		int daysToMinutes = (ui.getSimulationTimeValue() * 1440);
		engine = new OwnEngine(this); // luodaan uusi moottorisäie jokaista simulointia varten
		engine.setSimulationTime(daysToMinutes);

		engine.setMixedCanAmountValue(ui.getMixedCanAmountValue());
		engine.setBioCanAmountValue(ui.getBioCanAmountValue());
		engine.setPaperCanAmountValue(ui.getPaperCanAmountValue());
		engine.setGlassCanAmountValue(ui.getGlassCanAmountValue());
		engine.setMetalCanAmountValue(ui.getMetalCanAmountValue());
		engine.setPlasticCanAmountValue(ui.getPlasticCanAmountValue());

		//engine.setDelay(ui.getViive());

		// Construct visualization
		ui.getVisualisointi().setApartmentCounts(ApartmentType.YKSIO, ui.getSingleAptAmt());
		ui.getVisualisointi().setApartmentCounts(ApartmentType.KAKSIO, ui.getDoubleAptAmt());
		ui.getVisualisointi().setApartmentCounts(ApartmentType.KOLMIO, ui.getTripleAptAmt());
		ui.getVisualisointi().setApartmentCounts(ApartmentType.NELIO, ui.getQuadAptAmt());
		ui.getVisualisointi().constructSimuElementVisuals();

		((Thread)engine).start();
	}
	public void setDay() {
		int day;
		if (Math.floor((Clock.getInstance().getTime()/1440)) == 0) { // Just for the first day
			day = 1;
		} else {
			day = (int) ((Clock.getInstance().getTime()/1440));
		}
		
		Platform.runLater(() -> ui.setDay(day));
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


	public int getGarbageTruckArrivalInterval () {
		return ui.getGarbageTruckArrivalInterval() * 1440;
	}

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
	public void setTrashThrownTimes(int amt) {
		Platform.runLater(() -> ui.setTrashThrowTimes(amt));
	}

	@Override
	public void setShelterClearedTimes(int amt) {
		Platform.runLater(() -> ui.setShelterClearedTimes(amt));
	}

	@Override
	public void setTrashThrownTotal(double liters, double kg) {
		Platform.runLater(() -> ui.setTrashThrownTotalLiters(liters));
		Platform.runLater(() -> ui.setTrashThrownTotalKilos(kg));
	}

	public void setStartSimulationButtonAvailable() {
		Platform.runLater(() -> ui.setStartSimulationButtonAvailable());
	}

	@Override
	public void setMixedTotal(double amt) {
		Platform.runLater(() -> ui.setMixedTotal(amt));
	}

	@Override
	public void setBioTotal(double amt) {
		Platform.runLater(() -> ui.setBioTotal(amt));
	}

	@Override
	public void setCardboardTotal(double amt) {
		Platform.runLater(() -> ui.setCardboardTotal(amt));
	}

	@Override
	public void setPlasticTotal(double amt) {
		Platform.runLater(() -> ui.setPlasticTotal(amt));
	}

	@Override
	public void setGlassTotal(double amt) {
		Platform.runLater(() -> ui.setGlassTotal(amt));
	}

	@Override
	public void setMetalTotal(double amt) {
		Platform.runLater(() -> ui.setMetalTotal(amt));
	}

	@Override
	public void setMixedUsage(double amt) {
		Platform.runLater(() -> ui.setMixedUsage(amt));
	}

	@Override
	public void setBioUsage(double amt) {
		Platform.runLater(() -> ui.setBioUsage(amt));
	}

	@Override
	public void setCardboardUsage(double amt) {
		Platform.runLater(() -> ui.setCardboardUsage(amt));
	}

	@Override
	public void setPlasticUsage(double amt) {
		Platform.runLater(() -> ui.setPlasticUsage(amt));
	}

	@Override
	public void setGlassUsage(double amt) {
		Platform.runLater(() -> ui.setGlassUsage(amt));
	}

	@Override
	public void setMetalUsage(double amt) {
		Platform.runLater(() -> ui.setMetalUsage(amt));
	}

	@Override
	public void setMixedOverflow(double amt) {
		Platform.runLater(() -> ui.setMixedOverflow(amt));
	}

	@Override
	public void setBioOverflow(double amt) {
		Platform.runLater(() -> ui.setBioOverflow(amt));
	}

	@Override
	public void setCardboardOverflow(double amt) {
		Platform.runLater(() -> ui.setCardboardOverflow(amt));
	}

	@Override
	public void setPlasticOverflow(double amt) {
		Platform.runLater(() -> ui.setPlasticOverflow(amt));
	}

	@Override
	public void setGlassOverflow(double amt) {
		Platform.runLater(() -> ui.setGlassOverflow(amt));
	}

	@Override
	public void setMetalOverflow(double amt) {
		Platform.runLater(() -> ui.setMetalOverflow(amt));
	}
	
	@Override
	public void visualizeResident(EventType eventType, LinkedHashMap<GarbageCanType, ArrayList<Double>> percentages) {
		Platform.runLater(new Runnable(){
			public void run(){
				ui.getVisualisointi().newResident(eventType);
				ui.getVisualisointi().updateTrashPercentages(percentages);
			}});
	}

	public void loadInputParameters(int id) {
		try {
			InputParametersDao IPDao = new InputParametersDao();
			InputParameters input = IPDao.find(id);
			ui.setSimulationTimeValue(input.getSimulationTime());
			ui.setMeanTrashAmtPerThrow(input.getMeanTrashPerThrow());
			ui.setSingleAptAmt(input.getSingleAptAmount());
			ui.setDoubleAptAmt(input.getDoubleAptAmount());
			ui.setTripleAptAmt(input.getTripleAptAmount());
			ui.setQuadAptAmt(input.getQuadAptAmount());
			ui.setGarbageTruckArrivalInterval(input.getTruckArrivalInterval());
			ui.setMixedCanAmountValue(input.getMixedAmount());
			ui.setBioCanAmountValue(input.getBioAmount());
			ui.setPaperCanAmountValue(input.getPaperAmount());
			ui.setGlassCanAmountValue(input.getGlassAmount());
			ui.setMetalCanAmountValue(input.getMetalAmount());
			ui.setPlasticCanAmountValue(input.getPlasticAmount());
		} catch (Exception e) {
			System.out.println("LISTVIEW: Error loading input parameters: ");
			e.printStackTrace();
		}
	}

	public ObservableList<String> getInputHistory() {
		try {
			InputParametersDao IPDao = new InputParametersDao();
			List<InputParameters> inputs = IPDao.findAll();
			ObservableList<String> dates = FXCollections.observableArrayList();
			for (InputParameters input : inputs) {
				dates.add(input.getId() + "  |  " + input.getDate().toString());
			}
			return dates;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void saveInputsToDb() {
		try {
			InputParametersDao IPDao = new InputParametersDao();
			IPDao.persist(new InputParameters(
					LocalDate.now(),
					ui.getSimulationTimeValue(),
					ui.getMeanTrashAmtPerThrow(),
					ui.getSingleAptAmt(),
					ui.getDoubleAptAmt(),
					ui.getTripleAptAmt(),
					ui.getQuadAptAmt(),
					ui.getGarbageTruckArrivalInterval(),
					ui.getMixedCanAmountValue(),
					ui.getBioCanAmountValue(),
					ui.getPaperCanAmountValue(),
					ui.getGlassCanAmountValue(),
					ui.getMetalCanAmountValue(),
					ui.getPlasticCanAmountValue()));
		} catch (Exception e) {
			System.err.println("Error saving inputs to database: " + e.getMessage());
			e.printStackTrace();
		}
		ui.refreshHistoryList();
	}

	public void clearHistory() {
		try {
			InputParametersDao IPDao = new InputParametersDao();
			IPDao.deleteAll();
			Platform.runLater(() -> ui.refreshHistoryList());
		} catch (Exception e) {
			System.err.println("Error clearing history: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
