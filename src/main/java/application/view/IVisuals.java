package application.view;


import application.assets.model.ApartmentType;
import application.assets.model.GarbageCanType;
import javafx.scene.canvas.Canvas;

public interface IVisuals {

	public void setApartmentCounts(ApartmentType type, Integer count);

	public void setTrashCanCounts(GarbageCanType type, Integer count);

	public Canvas updateVisuals();

	public void emptyScreen();
	
	public void uusiAsiakas();
		
}

