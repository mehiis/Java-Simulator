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

		apartmentCounts.put(ApartmentType.YKSIO, 8);
		apartmentCounts.put(ApartmentType.KAKSIO, 1);
		apartmentCounts.put(ApartmentType.KOLMIO, 4);
		apartmentCounts.put(ApartmentType.NELIO, 1);

		apartmentImagesArea[0] = this.getWidth();
		apartmentImagesArea[1] = this.getHeight() / 3;
		trashCanImagesArea[0] = this.getWidth();
		trashCanImagesArea[1] = this.getHeight() / 3;

		bioImg = loadImage("src/main/resources/biowaste.png", 50, 50);
		cardboardImg = loadImage("src/main/resources/cardboardwaste.png", 50, 50);
		glasswasteImg = loadImage("src/main/resources/glasswaste.png",50, 50);
		kaksioImg = loadImage("src/main/resources/kaksio.png", 50, 50);
		kolmioImg = loadImage("src/main/resources/kolmio.png", 50, 50);
		metalImg = loadImage("src/main/resources/metalwaste.png", 50, 50);
		mixedImg = loadImage("src/main/resources/mixedwaste.png", 50, 50);
		nelioImg = loadImage("src/main/resources/nelio.png", 50, 50);
		personImg = loadImage("src/main/resources/person.png", 50, 50);
		plasticImg = loadImage("src/main/resources/plasticwaste.png", 50, 50);
		yksioImg = loadImage("src/main/resources/yksio.png", 50, 50);
	}

	private Image loadImage(String filepath, int sizeX, int sizeY) {
		try {
			FileInputStream inputStream = new FileInputStream(filepath);
			return new Image(inputStream, sizeX, sizeY, false, false);
		} catch (IOException e) {
			System.err.println(e);
		}
		return null;
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
	public Canvas updateVisuals() {
		double apartmentImgVSize = yksioImg.getHeight();
		double apartmentImgHSize = yksioImg.getWidth();

		double yLoc = 0;

		for (ApartmentType type: apartmentCounts.keySet()) {
			double xLoc = 0;

			switch (type) {
				case YKSIO:
					for (int i = 0; i < apartmentCounts.get(ApartmentType.YKSIO); i++) {
						gc.drawImage(yksioImg, xLoc, yLoc);
						xLoc += apartmentImgHSize + 5; // increment x loc by image width plus padding
					}
					break;
				case KAKSIO:
					for (int i = 0; i < apartmentCounts.get(ApartmentType.KAKSIO); i++) {
						gc.drawImage(kaksioImg, xLoc, yLoc);
						xLoc += apartmentImgHSize + 5; // increment x loc by image width plus padding
					}
					break;
				case KOLMIO:
					for (int i = 0; i < apartmentCounts.get(ApartmentType.KOLMIO); i++) {
						gc.drawImage(kolmioImg, xLoc, yLoc);
						xLoc += apartmentImgHSize + 5; // increment x loc by image width plus padding
					}
					break;
				case NELIO:
					for (int i = 0; i < apartmentCounts.get(ApartmentType.NELIO); i++) {
						gc.drawImage(nelioImg, xLoc, yLoc);
						xLoc =+ apartmentImgHSize + 5; // increment x loc by image width plus padding
					}
					break;

			}
			yLoc += apartmentImgVSize + 5; // increment y loc by image height plus padding
		}

		gc.drawImage(bioImg, 0, 0);
		return this;
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