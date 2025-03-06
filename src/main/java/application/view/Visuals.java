package application.view;

import application.assets.model.Apartment;
import application.assets.model.ApartmentType;
import application.assets.model.EventType;
import application.assets.model.GarbageCanType;
import application.controller.IControllerForModel;
import application.controller.IControllerForView;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class Visuals extends Canvas implements IVisuals {
	private final GraphicsContext gc;
	
	double i = 0;
	double j = 10;

	private LinkedHashMap<GarbageCanType, ArrayList<Double>> trashCanPercentages;

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

		// get counts to display from controller and gui
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

		// Set text properties
		gc.setFont(new Font("Dubai", 16)); // Set font and size

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
				clearDrawArea(135, 0, 600 - 135*2, 600);
				for (Resident resident: residentRenderList) {
					resident.getTimeline().setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent actionEvent) {
							residentRenderList.remove(resident);
						}
					});
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
		clearDrawArea(0, 0, 135, 600);
		// Set text properties
		gc.setFont(new Font("Dubai", 16)); // Set font and size
		gc.setFill(Color.BLUE); // Set text color to get rid of yellow fill in emptyScreen()

		double apartmentImgVSize = yksioImg.getHeight();
		double apartmentImgHSize = yksioImg.getWidth();

		double yLoc = 5;

		for (ApartmentType type: apartmentCounts.keySet()) {
			double xLoc = 20;

			switch (type) {
				case YKSIO:
					gc.drawImage(yksioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + (apartmentImgVSize/2));
					break;
				case KAKSIO:
					gc.drawImage(kaksioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + (apartmentImgVSize/2));
					break;
				case KOLMIO:
					gc.drawImage(kolmioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + (apartmentImgVSize/2));
					break;
				case NELIO:
					gc.drawImage(nelioImg, xLoc, yLoc);
					gc.fillText(String.valueOf((apartmentCounts.get(type))), xLoc + apartmentImgHSize + 5, yLoc + (apartmentImgVSize/2));
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
			double xLoc = this.getWidth() - garbageImgHSize - 60;

			switch (type) {
				case MIXED:
					gc.drawImage(mixedImg, xLoc, yLoc);
					break;
				case BIO:
					gc.drawImage(bioImg, xLoc, yLoc);
					break;
				case CARDBOARD:
					gc.drawImage(cardboardImg, xLoc, yLoc);
					break;
				case PLASTIC:
					gc.drawImage(plasticImg, xLoc, yLoc);
					break;
				case GLASS:
					gc.drawImage(glasswasteImg, xLoc, yLoc);
					break;
				case METAL:
					gc.drawImage(metalImg, xLoc, yLoc);
					break;

			}
			yLoc += garbageImgVSize + 5; // increment y loc by image height plus padding
		}
	}

	@Override
	public void constructSimuElementVisuals() {
		constructAptList();
		constructGarbageCanList();
	}

	@Override
	public void updateTrashPercentages(LinkedHashMap<GarbageCanType, ArrayList<Double>> percentages) {
		trashCanPercentages = percentages;

		gc.setFont(new Font("Dubai", 8)); // Set font and size smaller

		double garbageImgVSize = mixedImg.getHeight();
		double garbageImgHSize = mixedImg.getWidth();

		double yLoc = 100;
		double xLoc = this.getWidth() - 40;

		for (GarbageCanType type: trashCanPercentages.keySet()) {
			double secondaryYoffset = 0.0;

			clearDrawArea((int) xLoc, (int) (yLoc - 50), 50, 80);
			gc.setFill(Color.BLUE); // have to set fill color after clearing again

			for (int i = 0; i < trashCanPercentages.get(type).size(); i++) {

				secondaryYoffset += 9;

				gc.fillText("#"+(i+1)+": "+String.format( "%.2f", trashCanPercentages.get(type).get(i))+"%", xLoc, (yLoc + secondaryYoffset) - 30);
			}

			yLoc += garbageImgVSize + 5; // increment y loc by image height plus padding
		}
	}

	// middle clear area offset: x = 135
	public void clearDrawArea(int posX, int posY, int width, int height) {
		gc.setFill(Color.WHITESMOKE);
		gc.fillRect(posX, posY, width, height);
	}

	public void newResident(EventType eventType) {
		// y loc should be get from apartment type somehow
		double apartmentImgVSize = yksioImg.getHeight();
		double offset = 50.0;
		double startYPos = 0.0;
		switch (eventType){
			case YKSIO_ARRIVE_TO_SHELTER:
				startYPos = 0.0 + offset;
				break;
			case KAKSIO_ARRIVE_TO_SHELTER:
				startYPos = apartmentImgVSize + offset;
				break;
			case KOLMIO_ARRIVE_TO_SHELTER:
				startYPos = apartmentImgVSize * 2 + offset;
				break;
			case NELIO_ARRIVE_TO_SHELTER:
				startYPos = apartmentImgVSize * 3 + offset;
				break;
		}
		residentRenderList.add(new Resident(startYPos));
	}
}