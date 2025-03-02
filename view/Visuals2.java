package view;

import assets.model.ApartmentType;
import assets.model.GarbageCanType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Visuals2 extends Canvas implements IVisuals {
	
	private GraphicsContext gc;
	
	int asiakasLkm = 0;

	public Visuals2(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		tyhjennaNaytto();
	}


	@Override
	public void setApartmentCounts(ApartmentType type, Integer count) {

	}

	@Override
	public void setTrashCanCounts(GarbageCanType type, Integer count) {

	}

	@Override
	public void updateVisuals() {

	}

	public void tyhjennaNaytto() {
		gc.setFill(Color.YELLOW);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
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
