package assets.model.condominium;

import java.util.ArrayList;

public class Condominium {
    private GarbageShelter garbageShelter;
    private ArrayList<Apartment> apartments = new ArrayList<>();


    public Condominium(int aparmentAmount) {
        this.garbageShelter = new GarbageShelter();

        for (int i = 0; i < aparmentAmount; i++) {
            apartments.add(new Apartment());
        }
    }
}