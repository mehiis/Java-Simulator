package controller;

public interface IControllerForModel {

    // Rajapinta, joka tarjotaan moottorille:

    public int getSingleAptAmt();
    public int getDoubleAptAmt();
    public int getTripleAptAmt();
    public int getQuadAptAmt();

    public void naytaLoppuaika(double aika);
    public void visualisoiAsiakas();
}
