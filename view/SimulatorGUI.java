package view;


import assets.framework.Clock;
import assets.framework.Trace;
import controller.IControllerForView;
import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.DecimalFormat;

public class SimulatorGUI extends Application implements ISimulatorGUI {
    //Kontrollerin esittely (tarvitaan käyttöliittymässä)
    private IControllerForView controller;

    // settings
    //ASPECT RATIO 16:9. Possible sizes are: 800x450, 1280x720, 1600x900, 1920x1080
    private int width   = 1280;
    private int height  = 720;
    private final double defaultSimulationTime = 2500;

    // Käyttöliittymäkomponentit:
    // Left Box values
    private TextField simulationTimeValue = new TextField();
    private TextField apartmentAmountValue = new TextField();
    private TextField mixedCanAmountValue = new TextField();
    private TextField plasticCanAmountValue = new TextField();
    private TextField glassCanAmountValue = new TextField();
    private TextField paperCanAmountValue = new TextField();
    private TextField bioCanAmountValue = new TextField();
    private TextField metalCanAmountValue = new TextField();
    //
    private TextField delay;
    private Label result;
    private Label timeLabel;
    private Label delayLabel;
    private Label resultLabel;

    private Button startButton;
    private Button slowButton;
    private Button fastButton;

    private IVisuals view;


    @Override
    public void init() {

        Trace.setTraceLevel(Trace.Level.INFO);

        controller = new Controller(this);
    }

    @Override
    public void start(Stage stage) {
        // Käyttöliittymän rakentaminen
        try {
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });

            startButton = new Button();
            startButton.setText("Käynnistä simulointi");
            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    controller.startSimulation();
                    startButton.setDisable(true);
                }
            });
            stage.setTitle("Garbage Collection Simulator");

            stage.setScene(createScene());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public double getSimulationTimeValue() {
        if (simulationTimeValue.getText().isEmpty()) {
            return defaultSimulationTime;
        }
        return Double.parseDouble(simulationTimeValue.getText());
    }

    public int getMixedCanAmountValue() {
        if (mixedCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(mixedCanAmountValue.getText());
    }

    public int getPlasticCanAmountValue() {
        if (plasticCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(plasticCanAmountValue.getText());
    }

    public int getGlassCanAmountValue() {
        if (glassCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(glassCanAmountValue.getText());
    }

    public int getPaperCanAmountValue() {
        if (paperCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(paperCanAmountValue.getText());
    }

    public int getBioCanAmountValue() {
        if (bioCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(bioCanAmountValue.getText());
    }

    public int getMetalCanAmountValue() {
        if (metalCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        return Integer.parseInt(metalCanAmountValue.getText());
    }

    @Override
    public long getDelay() {
        return Long.parseLong(delay.getText());
    }

    @Override
    public void setLoppuaika(double aika) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        this.result.setText(formatter.format(aika));
        //startButton.setDisable(false); // enabloi käynnistä-nappula uudelleen
        Clock.getInstance().setTime(0.0); // reset time
    }


    @Override
    public IVisuals getVisualisointi() {
        return view;
    }

    private Scene createScene(){
        SplitPane horSplitPane = new SplitPane();
        SplitPane verSplitPane = new SplitPane();

        double horizontalFirstPosition = 0.2;
        double horizontalMiddlePosition = 0.8;
        double horizontalLastPosition = 0.8;
        double verticalPosition = 0.75;

        VBox leftVBox   = left();
        leftVBox.setId("leftVBox");

        VBox rightVBOX  = right();
        rightVBOX.setId("rightVBOX");

        horSplitPane.getItems().addAll(leftVBox, center(), rightVBOX);

        //SET DEFAULT SIZE OF TOP PANELS HORIZONTALLY
        horSplitPane.getDividers().getFirst().setPosition(horizontalFirstPosition);
        horSplitPane.getDividers().getLast().setPosition(horizontalMiddlePosition);
        horSplitPane.getDividers().get(1).setPosition(horizontalLastPosition);
        //END SETTING DEFAULT SIZES

        verSplitPane.getItems().addAll(horSplitPane, bottom(), startButton);
        verSplitPane.orientationProperty().setValue(Orientation.VERTICAL);
        verSplitPane.getDividers().getFirst().setPosition(verticalPosition); //SET DEFAULT SIZE OF TOP PANEL VERTICALLY. (0.75 = 75% of the screen).


        Scene scene = new Scene(verSplitPane, width, height);
        scene.getStylesheets().add("style.css");

        // Key event listeners
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case SPACE:
                    horSplitPane.getDividers().getFirst().setPosition(horizontalFirstPosition);
                    horSplitPane.getDividers().getLast().setPosition(horizontalLastPosition);
                    horSplitPane.getDividers().get(1).setPosition(horizontalMiddlePosition);
                    verSplitPane.getDividers().getFirst().setPosition(verticalPosition);
                    break;
                // Add more keys if needed
                default:
                    break;
            }
        });

        return scene;
    }

    private VBox center(){

        VBox midControl   = new VBox(new Label("Mid Control"));

        //ONKO TÄÄ NY VBOX VAI JOKU GRID SYSTEMI????
        return midControl;
    }

    private VBox right(){

        VBox leftControl  = new VBox(new Label("right Control"));
        return leftControl;
    }

    private VBox left(){
        final int TEXT_FIELD_WIDTH = 50;
        final Font FONT = new Font("Dubai Medium", 15);

        // Simulation time
        BorderPane simulationTime = new BorderPane();
        simulationTimeValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label simulationTimeLabel = new Label("Simulation Time");
        simulationTimeLabel.setFont(FONT);
        simulationTime.setLeft(simulationTimeLabel);
        simulationTime.setRight(simulationTimeValue);

        // Apartment amount
        BorderPane apartmentAmount = new BorderPane();
        apartmentAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label apartmentAmountLabel = new Label("Apartment Amount");
        apartmentAmountLabel.setFont(FONT);
        apartmentAmount.setLeft(apartmentAmountLabel);
        apartmentAmount.setRight(apartmentAmountValue);

        // Mixed can amount
        BorderPane mixedCanAmount = new BorderPane();
        mixedCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label mixedCanAmountLabel = new Label("Mixed");
        mixedCanAmountLabel.setFont(FONT);
        mixedCanAmount.setLeft(mixedCanAmountLabel);
        mixedCanAmount.setRight(mixedCanAmountValue);

        // Plastic can amount
        BorderPane plasticCanAmount = new BorderPane();
        plasticCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label plasticCanAmountLabel = new Label("Plastic");
        plasticCanAmountLabel.setFont(FONT);
        plasticCanAmount.setLeft(plasticCanAmountLabel);
        plasticCanAmount.setRight(plasticCanAmountValue);

        // Bio can amount
        BorderPane bioCanAmount = new BorderPane();
        bioCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label bioCanAmountLabel = new Label("Bio");
        bioCanAmountLabel.setFont(FONT);
        bioCanAmountLabel.setFont(FONT);
        bioCanAmount.setLeft(bioCanAmountLabel);
        bioCanAmount.setRight(bioCanAmountValue);

        // Glass can amount
        BorderPane glassCanAmount = new BorderPane();
        glassCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label glassCanAmountLabel = new Label("Glass");
        glassCanAmountLabel.setFont(FONT);
        glassCanAmount.setLeft(glassCanAmountLabel);
        glassCanAmount.setRight(glassCanAmountValue);

        // Paper can amount
        BorderPane paperCanAmount = new BorderPane();
        paperCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label paperCanAmountLabel = new Label("Paper");
        paperCanAmountLabel.setFont(FONT);
        paperCanAmount.setLeft(paperCanAmountLabel);
        paperCanAmount.setRight(paperCanAmountValue);

        // Metal can amount
        BorderPane metalCanAmount = new BorderPane();
        metalCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label metalCanAmountLabel = new Label("Metal");
        metalCanAmountLabel.setFont(FONT);
        metalCanAmount.setLeft(metalCanAmountLabel);
        metalCanAmount.setRight(metalCanAmountValue);

        VBox leftControl  = new VBox(simulationTime , apartmentAmount, mixedCanAmount, plasticCanAmount, bioCanAmount, glassCanAmount, paperCanAmount, metalCanAmount);
        leftControl.setSpacing(10); // Spacing for each component
        return leftControl;
    }

    private Region bottom(){
        HBox bottomControl = new HBox(new Label("Bottom Control"));

        //MIKÄ TÄÄ ON OISKO JOKU VBOW VAI MITÄ EHMETTIÄ?!?!?!?
        return bottomControl;
    }
}