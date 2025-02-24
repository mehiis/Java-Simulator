package controller;

public interface IControllerForModel {

    // Rajapinta, joka tarjotaan moottorille:

    double getMeanTrashPerThrowAmt();

    int getSingleAptAmt();
    int getDoubleAptAmt();
    int getTripleAptAmt();
    int getQuadAptAmt();

    public void naytaLoppuaika(double aika);
    public void visualisoiAsiakas();
}
