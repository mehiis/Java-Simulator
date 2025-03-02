package view;

import assets.model.ApartmentType;
import assets.model.GarbageCanType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;

public class Visuals extends Canvas implements IVisuals {

	private final GraphicsContext gc;
	
	double i = 0;
	double j = 10;

	// define the allowed areas for both types of images, calculated in constructor
	double[] trashCanImagesArea = new double[2];
	double[] apartmentImagesArea = new double[2];

	private HashMap<GarbageCanType, Integer> trashCanCounts = new HashMap<>();
	private Image mixedImg;
	private Image bioImg;
	private Image cardboardImg;
	private Image plasticImg;
	private Image glasswasteImg;
	private Image metalImg;

	private HashMap<ApartmentType, Integer> apartmentCounts = new HashMap<>();
	private Image yksioImg;
	private Image kaksioImg;
	private Image kolmioImg;
	private Image nelioImg;

	private Image personImg;

	
	public Visuals(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		tyhjennaNaytto();

		// initializing hashmaps to have 1 of each
		trashCanCounts.put(GarbageCanType.MIXED, 1);
		trashCanCounts.put(GarbageCanType.BIO, 1);
		trashCanCounts.put(GarbageCanType.CARDBOARD, 1);
		trashCanCounts.put(GarbageCanType.PLASTIC, 1);
		trashCanCounts.put(GarbageCanType.GLASS, 1);
		trashCanCounts.put(GarbageCanType.METAL, 1);

		apartmentCounts.put(ApartmentType.YKSIO, 1);
		apartmentCounts.put(ApartmentType.KAKSIO, 1);
		apartmentCounts.put(ApartmentType.KOLMIO, 1);
		apartmentCounts.put(ApartmentType.NELIO, 1);

		apartmentImagesArea[0] = this.getWidth();
		apartmentImagesArea[1] = this.getHeight() / 3;
		trashCanImagesArea[0] = this.getWidth();
		trashCanImagesArea[1] = this.getHeight() / 3;

		bioImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\biowaste.png");
		cardboardImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\cardboardwaste.png");
		glasswasteImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\glasswaste.png");
		kaksioImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\kaksio.png");
		kolmioImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\kolmio.png");
		metalImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\metalwaste.png");
		mixedImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\mixedwaste.png");
		nelioImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\nelio.png");
		personImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\person.png");
		plasticImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\plasticwaste.png");
		yksioImg = loadImage("C:\\Users\\D\\IdeaProjects\\Java-Simulator\\src\\main\\resources\\yksio.png");
	}

	private Image loadImage(String filepath) {
		try {
			FileInputStream inputStream = new FileInputStream(filepath);
			return new Image(inputStream);
		} catch (IOException e) {
			System.err.println(e);
		}
		return null;
	}

	@Override
	public void setApartmentCounts(ApartmentType type, Integer count) {
		apartmentCounts.put(type, count);
	}

	@Override
	public void setTrashCanCounts(GarbageCanType type, Integer count) {
		trashCanCounts.put(type, count);
	}

	@Override
	public void updateVisuals() {
		gc.drawImage(bioImg, 0, 0);
	}

	@Override
	public void tyhjennaNaytto() {
		gc.setFill(Color.YELLOW);
		gc.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void uusiAsiakas() {
		gc.setFill(Color.RED);
		gc.fillOval(i,j,10,10);
		
		i = (i + 10) % this.getWidth();
		//j = (j + 12) % this.getHeight();
		if (i==0) j+=10;			
	}
}