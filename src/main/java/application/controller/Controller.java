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
import java.util.LinkedHashMap;

/**
 * This class is responsible for the communication between the model and the view.
 */
public class Controller implements IControllerForModel, IControllerForView {   // UUSI
	private IEngine engine;
	private ISimulatorGUI ui;

	public Controller(ISimulatorGUI ui) {
		this.ui = ui;
	}

	/**
	 * This method is called when the Start Simulation button is pressed.
	 * It starts the simulation and sets the input parameters to the engine.
	 * User inputs are saved to the database.
	 * The simulation time is converted from days to minutes.
	 */
	@Override
	public void startSimulation() {

		System.out.println("Start simulation.");
		saveInputsToDb();
		int daysToMinutes = (ui.getSimulationTimeValue() * 1440);
		engine = new OwnEngine(this); // luodaan uusi moottorisäie jokaista simulointia varten
		engine.setSimulationTime(daysToMinutes);

		engine.setMixedCanAmountValue(ui.getMixedCanAmountValue());
		engine.setBioCanAmountValue(ui.getBioCanAmountValue());
		engine.setPaperCanAmountValue(ui.getCardboardCanAmountValue());
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

	/**
	 * Passes the current day from the engine to the UI.
	 */
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
	public void getSimulationSpeed() {
		double temp = engine.getDelay();
		ui.setSimulationSpeed((int)temp);
	}

	/**
	 * Slows down the engine.
	 * Called from the UI when the Slow Down button is pressed.
	 */
	@Override
	public void slowDown() { // hidastetaan moottorisäiettä
		long newDelay = (long)(engine.getDelay() * 1.10);

		if(newDelay > 1000)// min delay 1s
			newDelay = 1000;

		engine.setDelay((long)(newDelay));

		getSimulationSpeed();
	}

	@Override
	public void pause() {
		engine.pressPauseButton();
	}

	/**
	 * Speeds up the engine.
	 * Called from the UI when the Speed Up button is pressed.
	 */
	@Override
	public void speedUp() { // nopeutetaan moottorisäiettä
		engine.setDelay((long)(engine.getDelay()*0.9));
		getSimulationSpeed();
	}

	/**
	 * Passes the truck arrival interval from the UI to the engine.
	 * @return the truck arrival interval in minutes
	 */
	public int getGarbageTruckArrivalInterval () {
		return ui.getGarbageTruckArrivalInterval() * 1440;
	}

	/**
	 * Passes the mean trash amount per throw from the UI to the engine.
	 * @return the mean trash amount per throw.
	 */
	@Override
	public double getMeanTrashPerThrowAmt() {
		return ui.getMeanTrashAmtPerThrow();
	}

	/**
	 * Passes the amount of single apartments from the UI to the engine.
	 * @return the amount of single apartments.
	 */
	@Override
	public int getSingleAptAmt() {
		return ui.getSingleAptAmt();
	}

	/**
	 * Passes the amount of double apartments from the UI to the engine.
	 * @return the amount of double apartments.
	 */
	@Override
	public int getDoubleAptAmt() {
		return ui.getDoubleAptAmt();
	}

	/**
	 * Passes the amount of triple apartments from the UI to the engine.
	 * @return the amount of triple apartments.
	 */
	@Override
	public int getTripleAptAmt() {
		return ui.getTripleAptAmt();
	}

	/**
	 * Passes the amount of quadruple apartments from the UI to the engine.
	 * @return the amount of quadruple apartments.
	 */
	@Override
	public int getQuadAptAmt() {
		return ui.getQuadAptAmt();
	}

	/**
	 * Passes the end simulation time to the UI.
	 * @param aika Time from the engine.
	 */
	@Override
	public void naytaLoppuaika(double aika) {
		Platform.runLater(()->ui.setLoppuaika(aika)); 
	}

	/**
	 * Passes the amount of trash thrown during the simulation to the UI.
	 * @param amt Amount of times trash was thrown.
	 */
	@Override
	public void setTrashThrownTimes(int amt) {
		Platform.runLater(() -> ui.setTrashThrowTimes(amt));
	}

	/**
	 * Passes the amount of times garbage shelter was emptied to the UI.
	 * @param amt Amount of times shelter was emptied.
	 */
	@Override
	public void setShelterClearedTimes(int amt) {
		Platform.runLater(() -> ui.setShelterClearedTimes(amt));
	}

	@Override
	public void setShelterUsagePercent(double amt) {
		Platform.runLater(() -> ui.setShelterUsagePercent(amt));
	}

	/**
	 * Passes the total amount of trash thrown during the simulation to the UI.
	 * @param liters Amount of trash thrown in liters.
	 * @param kg Amount of trash thrown in kilograms.
	 */
	@Override
	public void setTrashThrownTotal(double liters, double kg) {
		Platform.runLater(() -> ui.setTrashThrownTotalLiters(liters));
		Platform.runLater(() -> ui.setTrashThrownTotalKilos(kg));
	}

	/**
	 * Passes the total amount of mixed trash to the UI.
	 * @param amt Amount of mixed trash generated during the simulation.
	 */
	@Override
	public void setMixedTotal(double amt) {
		Platform.runLater(() -> ui.setMixedTotal(amt));
	}

	/**
	 * Passes the total amount of bio trash to the UI.
	 * @param amt Amount of bio trash generated during the simulation.
	 */
	@Override
	public void setBioTotal(double amt) {
		Platform.runLater(() -> ui.setBioTotal(amt));
	}

	/**
	 * Passes the total amount of cardboard trash to the UI.
	 * @param amt Amount of cardboard trash generated during the simulation.
	 */
	@Override
	public void setCardboardTotal(double amt) {
		Platform.runLater(() -> ui.setCardboardTotal(amt));
	}

	/**
	 * Passes the total amount of plastic trash to the UI.
	 * @param amt Amount of plastic trash generated during the simulation.
	 */
	@Override
	public void setPlasticTotal(double amt) {
		Platform.runLater(() -> ui.setPlasticTotal(amt));
	}

	/**
	 * Passes the total amount of glass trash to the UI.
	 * @param amt Amount of glass trash generated during the simulation.
	 */
	@Override
	public void setGlassTotal(double amt) {
		Platform.runLater(() -> ui.setGlassTotal(amt));
	}

	/**
	 * Passes the total amount of metal trash to the UI.
	 * @param amt Amount of metal trash generated during the simulation.
	 */
	@Override
	public void setMetalTotal(double amt) {
		Platform.runLater(() -> ui.setMetalTotal(amt));
	}

	/**
	 * Passes the percentage use of mixed trash cans to the UI.
	 * @param amt Percentage of mixed trash can usage.
	 */
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
	public void setMixedAccessTime(double amt) {
		Platform.runLater(() -> ui.setMixedAcessTime(amt));
	}

	@Override
	public void setBioAccessTime(double amt) {
		Platform.runLater(() -> ui.setBioAcessTime(amt));
	}

	@Override
	public void setCardboardAccessTime(double amt) {
		Platform.runLater(() -> ui.setCardboardAcessTime(amt));
	}

	@Override
	public void setPlasticAccessTime(double amt) {
		Platform.runLater(() -> ui.setPlasticAcessTime(amt));
	}

	@Override
	public void setGlassAccessTime(double amt) {
		Platform.runLater(() -> ui.setGlassAcessTime(amt));
	}

	@Override
	public void setMetalAccessTime(double amt) {
		Platform.runLater(() -> ui.setMetalAcessTime(amt));
	}

	@Override
	public void visualizeResident(EventType eventType, LinkedHashMap<GarbageCanType, ArrayList<Double>> percentages) {
		Platform.runLater(new Runnable(){
			public void run(){
				ui.getVisualisointi().newResident(eventType);
				ui.getVisualisointi().updateTrashPercentages(percentages);
			}});
	}

	/**
	 * Gets the input parameters from the database.
	 * Called when a date is selected from the history list.
	 * @param id The id of the input parameters to be loaded.
	 */
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
			ui.setCardBoardCanAmountValue(input.getPaperAmount());
			ui.setGlassCanAmountValue(input.getGlassAmount());
			ui.setMetalCanAmountValue(input.getMetalAmount());
			ui.setPlasticCanAmountValue(input.getPlasticAmount());
		} catch (Exception e) {
			System.out.println("LISTVIEW: Error loading input parameters: ");
			e.printStackTrace();
		}
	}

	/**
	 * Gets the input history from the database.
	 * @return ObservableList of the InputParameters objects.
	 */
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

	/**
	 * Gets the inputs from the UI and passes them to the dao to be saved to the database.
	 * Called when the Start Simulation button is pressed.
	 */
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
					ui.getCardboardCanAmountValue(),
					ui.getGlassCanAmountValue(),
					ui.getMetalCanAmountValue(),
					ui.getPlasticCanAmountValue()));
		} catch (Exception e) {
			System.err.println("Error saving inputs to database: " + e.getMessage());
			e.printStackTrace();
		}
		ui.refreshHistoryList();
	}

	/**
	 * Calls the dao to delete all input parameters from the database.
	 * Called when the Clear History button is pressed.
	 */
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
