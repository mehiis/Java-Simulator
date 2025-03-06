package application.view;

import application.assets.model.ApartmentType;
import application.assets.model.EventType;
import application.assets.model.GarbageCanType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Visuals2 extends Canvas implements IVisuals {
	
	private GraphicsContext gc;
	
	int asiakasLkm = 0;

	public Visuals2(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		clearDrawArea();
	}


	@Override
	public void setApartmentCounts(ApartmentType type, Integer count) {

	}

	@Override
	public void setTrashCanCounts(GarbageCanType type, Integer count) {

	}

	@Override
	public void constructSimuElementVisuals() {

	}

	@Override
	public void updateTrashPercentages(LinkedHashMap<GarbageCanType, ArrayList<Double>> percentages) {

	}

	public void clearDrawArea() {
		gc.setFill(Color.DARKGREY);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	@Override
	public void newResident(EventType eventType) {

	}

	public void uusiAsiakas() {
		
		asiakasLkm++;
		
		gc.setFill(Color.YELLOW);
		gc.fillRect(100,80, 100, 20);
		gc.setFill(Color.RED);
		gc.setFont(new Font(20));
		gc.fillText("Asiakas " + asiakasLkm, 100, 100);
		
	}
	

}
