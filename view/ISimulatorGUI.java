package view;

public interface ISimulatorGUI {
	
	// Kontrolleri tarvitsee syötteitä, jotka se välittää Moottorille
	public double getSimulationTimeValue();

	double getMeanTrashAmtPerThrow();

	int getSingleAptAmt();
	int getDoubleAptAmt();
	int getTripleAptAmt();
	int getQuadAptAmt();

	public int getMixedCanAmountValue();
	public int getPlasticCanAmountValue();
	public int getGlassCanAmountValue();
	public int getPaperCanAmountValue();
	public int getBioCanAmountValue();
	public int getMetalCanAmountValue();

	public long getDelay();
	
	//Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa 
	public void setLoppuaika(double aika);

	void setTrashThrowTimes(double amt);
	void setShelterClearedTimes(double amt);
	void setTrashThrownTotalLiters(double liters);
	void setTrashThrownTotalKilos(double kg);

	// setters for results that will be passed from controller to GUI
	void setMixedTotal(double value);
	void setBioTotal(double value);
	void setCardboardTotal(double value);
	void setPlasticTotal(double value);
	void setGlassTotal(double value);
	void setMetalTotal(double value);
	void setMixedUsage(double value);
	void setBioUsage(double value);
	void setCardboardUsage(double value);
	void setPlasticUsage(double value);
	void setGlassUsage(double value);
	void setMetalUsage(double value);
	void setMixedOverflow(double value);
	void setBioOverflow(double value);
	void setCardboardOverflow(double value);
	void setPlasticOverflow(double value);
	void setGlassOverflow(double value);
	void setMetalOverflow(double value);
	
	// Kontrolleri tarvitsee  
	public IVisuals getVisualisointi();

}
