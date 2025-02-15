package view;


import assets.framework.Clock;
import assets.framework.Trace;
import controller.IControllerForView;
import controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
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

    // Käyttöliittymäkomponentit:
    private TextField time = new TextField();
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
    public double getTime() {
        return Double.parseDouble(time.getText());
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
        double horizontalLastPosition = 0.75;
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

        leftVBox.getChildren().add(time);

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
        VBox leftControl  = new VBox(new Label("Left Control"));
        return leftControl;
    }

    private Region bottom(){
        HBox bottomControl = new HBox(new Label("Bottom Control"));

        //MIKÄ TÄÄ ON OISKO JOKU VBOW VAI MITÄ EHMETTIÄ?!?!?!?
        return bottomControl;
    }
}