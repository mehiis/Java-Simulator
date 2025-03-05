package application.view;


import application.assets.model.ApartmentType;
import application.assets.model.EventType;
import application.assets.model.GarbageCanType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;

public interface IVisuals {

	public void setApartmentCounts(ApartmentType type, Integer count);

	public void setTrashCanCounts(GarbageCanType type, Integer count);

	public Canvas updateVisuals();

	public void clearDrawArea();
	
	public void newResident(EventType eventType);
		
}

