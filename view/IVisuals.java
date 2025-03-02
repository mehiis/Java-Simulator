package view;


import assets.model.ApartmentType;
import assets.model.GarbageCanType;
import javafx.scene.canvas.Canvas;

public interface IVisuals {

	public void setApartmentCounts(ApartmentType type, Integer count);

	public void setTrashCanCounts(GarbageCanType type, Integer count);

	public Canvas updateVisuals();

	public void tyhjennaNaytto();
	
	public void uusiAsiakas();
		
}

