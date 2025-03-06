package application.controller;

import javafx.collections.ObservableList;

public interface IControllerForView {

    // Rajapinta, joka tarjotaan  käyttöliittymälle:

    public void startSimulation();
    public void speedUp();
    public void slowDown();
    public ObservableList<String> getInputHistory();

    void loadInputParameters(int selectedId);

    void clearHistory();
}
