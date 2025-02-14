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
    public void start(Stage primaryStage) {
        // Käyttöliittymän rakentaminen
        try {

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent t) {
                    Platform.exit();
                    System.exit(0);
                }
            });


            primaryStage.setTitle("Garbage Collection Simulator");

            startButton = new Button();
            startButton.setText("Start");
            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    controller.startSimulation();
                    //startButton.setDisable(true);
                }
            });

            slowButton = new Button();
            slowButton.setText("Slow simulation");
            slowButton.setOnAction(e -> System.out.println("biip")/*kontrolleri.hidasta()*/);

            fastButton = new Button();
            fastButton.setText("Fasten simulation");
            fastButton.setOnAction(e -> System.out.println("boop")/*kontrolleri.nopeuta()*/);

            timeLabel = new Label("Simulation time:");
            timeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            time = new TextField("2000");
            time.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            time.setPrefWidth(150);

            delayLabel = new Label("Simulation speed:");
            delayLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            delay = new TextField("150");
            delay.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            delay.setPrefWidth(150);

            resultLabel = new Label("Time simulated: ");
            resultLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            result = new Label();
            result.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
            result.setPrefWidth(150);

            HBox hBox = new HBox();
            hBox.setPadding(new Insets(15, 12, 15, 12)); // marginaalit ylä, oikea, ala, vasen
            hBox.setSpacing(10);   // noodien välimatka 10 pikseliä

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setVgap(10);
            grid.setHgap(5);

            grid.add(timeLabel, 0, 0);   // sarake, rivi
            grid.add(time, 1, 0);          // sarake, rivi
            grid.add(delayLabel, 0, 1);      // sarake, rivi
            grid.add(delay, 1, 1);           // sarake, rivi
            grid.add(resultLabel, 0, 2);      // sarake, rivi
            grid.add(result, 1, 2);           // sarake, rivi
            grid.add(startButton, 0, 3);  // sarake, rivi
            grid.add(fastButton, 0, 4);   // sarake, rivi
            grid.add(slowButton, 1, 4);   // sarake, rivi

            view = new Visuals(400, 200);

            hBox.getChildren().addAll(grid, (Canvas) view);

            Scene scene = new Scene(hBox);
            primaryStage.setScene(scene);
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Käyttöliittymän rajapintametodit (kutsutaan kontrollerista)

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
}




