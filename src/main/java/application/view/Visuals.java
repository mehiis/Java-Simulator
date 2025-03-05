package application.view;

import application.assets.model.ApartmentType;
import application.assets.model.GarbageCanType;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Visuals extends Canvas implements IVisuals {

	private final GraphicsContext gc;
	
	double i = 0;
	double j = 10;

	private LinkedHashMap<GarbageCanType, Integer> trashCanCounts = new LinkedHashMap<>();
	private Image mixedImg;
	private Image bioImg;
	private Image cardboardImg;
	private Image plasticImg;
	private Image glasswasteImg;
	private Image metalImg;

	private LinkedHashMap<ApartmentType, Integer> apartmentCounts = new LinkedHashMap<>();
	private Image yksioImg;
	private Image kaksioImg;
	private Image kolmioImg;
	private Image nelioImg;

	private Image personImg;

	private ArrayList<Resident> residentRenderList;
	
	public Visuals(int w, int h) {
		super(w, h);
		gc = this.getGraphicsContext2D();
		clearDrawArea();

		// get counts to display from controller and gui
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

		// Set text properties
		gc.setFont(new Font("Dubai", 24)); // Set font and size

		// size of apartment images
		int houseImgSizeX =  (int) (289*0.28);
		int houseImgSizeY =  (int) (512*0.28);

		// size of trashcan images
		int trashImgSize = (int) (512 * 0.15);

		bioImg = loadImage("src/main/resources/biowaste.png", trashImgSize, trashImgSize);
		cardboardImg = loadImage("src/main/resources/cardboardwaste.png", trashImgSize, trashImgSize);
		glasswasteImg = loadImage("src/main/resources/glasswaste.png",trashImgSize, trashImgSize);
		kaksioImg = loadImage("src/main/resources/kaksio.png", houseImgSizeX, houseImgSizeY);
		kolmioImg = loadImage("src/main/resources/kolmio.png", houseImgSizeX, houseImgSizeY);
		metalImg = loadImage("src/main/resources/metalwaste.png", trashImgSize, trashImgSize);
		mixedImg = loadImage("src/main/resources/mixedwaste.png", trashImgSize, trashImgSize);
		nelioImg = loadImage("src/main/resources/nelio.png", houseImgSizeX, houseImgSizeY);
		personImg = loadImage("src/main/resources/person.png", trashImgSize, trashImgSize);
		plasticImg = loadImage("src/main/resources/plasticwaste.png", trashImgSize, trashImgSize);
		yksioImg = loadImage("src/main/resources/yksio.png", houseImgSizeX, houseImgSizeY);

		// init resident render list
		residentRenderList = new ArrayList<>();

		// Discrete time steps using timer / ANIMATION RENDERING LOOP
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				// clearing screen for animation frames is done here!
				clearDrawArea();
				for (Resident resident: residentRenderList) {
					System.out.println(resident.getyLoc());
					gc.drawImage(personImg, resident.getxLoc(), resident.getyLoc());
				}
			}
		};
		timer.start();

	}

	// image load with draw size args
	private Image loadImage(String filepath, int sizeX, int sizeY) {
		try {
			FileInputStream inputStream = new FileInputStream(filepath);
			return new Image(inputStream, sizeX, sizeY, false, false);
		} catch (IOException e) {
			System.err.println(e);
		}
		return null;
	}

	// image load without draw size args
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

	private void constructAptList() {
		gc.setFill(Color.BLUE); // Set text color to get rid of yellow fill in emptyScreen()

		double apartmentImgVSize = yksioImg.getHeight();
		double apartmentImgHSize = yksioImg.getWidth();

		double yLoc = 5;

		for (ApartmentType type: apartmentCounts.keySet()) {
			double xLoc = 20;

			switch (type) {
				case YKSIO:
					gc.drawImage(yksioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + apartmentImgVSize);
					break;
				case KAKSIO:
					gc.drawImage(kaksioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + apartmentImgVSize);
					break;
				case KOLMIO:
					gc.drawImage(kolmioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + apartmentImgVSize);
					break;
				case NELIO:
					gc.drawImage(nelioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + apartmentImgVSize);
					break;

			}
			yLoc += apartmentImgVSize + 5; // increment y loc by image height plus padding
		}
	}

	private void constructGarbageCanList() {
		gc.setFill(Color.BLUE); // Set text color to get rid of yellow fill in emptyScreen()

		double garbageImgVSize = mixedImg.getHeight();
		double garbageImgHSize = mixedImg.getWidth();

		double yLoc = 50;

		for (GarbageCanType type: trashCanCounts.keySet()) {
			double xLoc = this.getWidth() - garbageImgHSize - 30;

			switch (type) {
				case MIXED:
					gc.drawImage(mixedImg, xLoc, yLoc);
					gc.fillText(String.valueOf((trashCanCounts.get(type))), xLoc + garbageImgHSize + 5, yLoc + garbageImgVSize);
					break;
				case BIO:
					gc.drawImage(bioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((trashCanCounts.get(type))), xLoc + garbageImgHSize + 5, yLoc + garbageImgVSize);
					break;
				case CARDBOARD:
					gc.drawImage(cardboardImg, xLoc, yLoc);
					gc.fillText(String.valueOf((trashCanCounts.get(type))), xLoc + garbageImgHSize + 5, yLoc + garbageImgVSize);
					break;
				case PLASTIC:
					gc.drawImage(plasticImg, xLoc, yLoc);
					gc.fillText(String.valueOf((trashCanCounts.get(type))), xLoc + garbageImgHSize + 5, yLoc + garbageImgVSize);
					break;
				case GLASS:
					gc.drawImage(glasswasteImg, xLoc, yLoc);
					gc.fillText(String.valueOf((trashCanCounts.get(type))), xLoc + garbageImgHSize + 5, yLoc + garbageImgVSize);
					break;
				case METAL:
					gc.drawImage(metalImg, xLoc, yLoc);
					gc.fillText(String.valueOf((trashCanCounts.get(type))), xLoc + garbageImgHSize + 5, yLoc + garbageImgVSize);
					break;

			}
			yLoc += garbageImgVSize + 5; // increment y loc by image height plus padding
		}
	}

	@Override
	public Canvas updateVisuals() {
		constructAptList();
		constructGarbageCanList();
		newResident(0.0);
		newResident(100.0);
		newResident(200.0);

		return this;
	}

	@Override
	public void clearDrawArea() {
		gc.setFill(Color.WHITESMOKE);
		gc.fillRect(135, 0, this.getWidth() - 135*2, this.getHeight());
	}

	public void newResident(double startYPos) {
		// y loc should be get from apartment type somehow
		residentRenderList.add(new Resident(startYPos));
	}
}