package application.view;

public interface ISimulatorGUI {

    public int getSimulationTimeValue();

    double getMeanTrashAmtPerThrow();

    int getGarbageTruckArrivalInterval();

    int getSingleAptAmt();

    int getDoubleAptAmt();

    int getTripleAptAmt();

    int getQuadAptAmt();

    public int getMixedCanAmountValue();

    public int getPlasticCanAmountValue();

    public int getGlassCanAmountValue();

    public int getCardboardCanAmountValue();

    public int getBioCanAmountValue();

    public int getMetalCanAmountValue();

    public long getDelay();

    //Kontrolleri antaa käyttöliittymälle tuloksia, joita Moottori tuottaa
    public void setLoppuaika(double aika);

    void setShelterUsagePercent(double amt);

    void setTrashThrowTimes(int amt);

    void setShelterClearedTimes(int amt);

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

    void setMixedAcessTime(double value);

    void setBioAcessTime(double value);

    void setCardboardAcessTime(double value);

    void setPlasticAcessTime(double value);

    void setGlassAcessTime(double value);

    void setMetalAcessTime(double value);

    // Kontrolleri tarvitsee
    public IVisuals getVisualisointi();

    void setDay(int day);

    void setSimulationSpeed(int speed);

    void setSimulationTimeValue(int simulationTime);

    void setMeanTrashAmtPerThrow(double meanTrashPerThrow);

    void setSingleAptAmt(int singleAptAmount);

    void setDoubleAptAmt(int doubleAptAmount);

    void setTripleAptAmt(int tripleAptAmount);

    void setQuadAptAmt(int quadAptAmount);

    void setGarbageTruckArrivalInterval(int truckArrivalInterval);

    void setMixedCanAmountValue(int mixedAmount);

    void setBioCanAmountValue(int bioAmount);

    void setCardBoardCanAmountValue(int paperAmount);

    void setGlassCanAmountValue(int glassAmount);

    void setMetalCanAmountValue(int metalAmount);

    void setPlasticCanAmountValue(int plasticAmount);

    void refreshHistoryList();

}
