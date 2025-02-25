package controller;

public interface IControllerForModel {

    // Rajapinta, joka tarjotaan moottorille:

    double getMeanTrashPerThrowAmt();

    int getSingleAptAmt();
    int getDoubleAptAmt();
    int getTripleAptAmt();
    int getQuadAptAmt();

    public void naytaLoppuaika(double aika);

    // pass performance data to controller from engine
    void setMixedTotal(double amt);

    public void visualisoiAsiakas();
}
