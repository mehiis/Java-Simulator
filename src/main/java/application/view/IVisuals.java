package application.view;


import application.assets.model.ApartmentType;
import application.assets.model.EventType;
import application.assets.model.GarbageCanType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface IVisuals {

	void setApartmentCounts(ApartmentType type, Integer count);

	void setTrashCanCounts(GarbageCanType type, Integer count);

	// this sets up apartment/garbage can icons and amounts
	void constructSimuElementVisuals();

	void updateTrashPercentages(LinkedHashMap<GarbageCanType, ArrayList<Double>> percentages);

	void newResident(EventType eventType);
		
}

