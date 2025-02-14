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
    private TextField time;
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
        horSplitPane.getItems().addAll(left(), center(), right());

        //SET DEFAULT SIZE OF TOP PANELS HORIZONTALLY
        horSplitPane.getDividers().getFirst().setPosition(0.2);
        horSplitPane.getDividers().getLast().setPosition(0.25);
        horSplitPane.getDividers().get(1).setPosition(0.8);
        //END SETTING DEFAULT SIZES


        verSplitPane.getItems().addAll(horSplitPane, bottom());
        verSplitPane.orientationProperty().setValue(Orientation.VERTICAL);
        verSplitPane.getDividers().getFirst().setPosition(0.75); //SET DEFAULT SIZE OF TOP PANEL VERTICALLY. (0.75 = 75% of the screen).

        Scene scene = new Scene(verSplitPane, width, height);
        scene.getStylesheets().add("style.css");

        return scene;
    }

    private Region center(){
        Region center = new Region();
        center.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));


        VBox midControl   = new VBox(new Label("Mid Control"));
        return midControl;
    }

    private Region right(){
        VBox leftControl  = new VBox(new Label("Left Control"));
        return leftControl;
    }

    private Region left(){
        VBox leftControl  = new VBox(new Label("Left Control"));
        return leftControl;
    }

    private Region bottom(){
        HBox bottomControl = new HBox(new Label("Bottom Control"));
        return bottomControl;
    }
}