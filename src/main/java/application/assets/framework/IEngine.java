package application.assets.framework;

import entity.InputParameters;

public interface IEngine { // UUSI

    // Kontrolleri käyttää tätä rajapintaa

    public void setMixedCanAmountValue(int amount);

    public void setPlasticCanAmountValue(int amount);

    public void setGlassCanAmountValue(int amount);

    public void setPaperCanAmountValue(int amount);

    public void setBioCanAmountValue(int amount);

    public void setMetalCanAmountValue(int amount);

    public void setSimulationTime(double time);

    public void setDelay(long time);

    public long getDelay();

    public void pressPauseButton();

    public void saveInputsToDb();

    public void clearHistory();

    public void startSimulation();
}
