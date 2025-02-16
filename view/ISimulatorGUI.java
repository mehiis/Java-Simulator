package view;

public interface ISimulatorGUI {
	
	// Kontrolleri tarvitsee syötteitä, jotka se välittää Moottorille
	public double getSimulationTimeValue();

	public int getMixedCanAmountValue();
	public int getPlasticCanAmountValue();
	public int getGlassCanAmountValue();
	public int getPaperCanAmountValue();
	public int getBioCanAmountValue();
	public int getMetalCanAmountValue();

	public long getDelay();
	
	//Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa 
	public void setLoppuaika(double aika);
	
	// Kontrolleri tarvitsee  
	public IVisuals getVisualisointi();

}
