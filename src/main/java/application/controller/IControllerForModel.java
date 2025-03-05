package application.controller;

import application.assets.model.EventType;

public interface IControllerForModel {

    // Rajapinta, joka tarjotaan moottorille:

    double getMeanTrashPerThrowAmt();

    int getSingleAptAmt();
    int getDoubleAptAmt();
    int getTripleAptAmt();
    int getQuadAptAmt();

    public void naytaLoppuaika(double aika);

    void setTrashThrownTimes(int amt);

    void setShelterClearedTimes(int amt);

    void setTrashThrownTotal(double liters, double kg);

    // pass performance data to controller from engine
    void setMixedTotal(double amt);

    void setBioTotal(double amt);

    void setCardboardTotal(double amt);

    void setPlasticTotal(double amt);

    void setGlassTotal(double amt);

    void setMetalTotal(double amt);

    void setMixedUsage(double amt);

    void setBioUsage(double amt);

    void setCardboardUsage(double amt);

    void setPlasticUsage(double amt);

    void setGlassUsage(double amt);

    void setMetalUsage(double amt);

    void setMixedOverflow(double amt);

    void setBioOverflow(double amt);

    void setCardboardOverflow(double amt);

    void setPlasticOverflow(double amt);

    void setGlassOverflow(double amt);

    void setMetalOverflow(double amt);

    public void visualizeResident(EventType eventType);
}
