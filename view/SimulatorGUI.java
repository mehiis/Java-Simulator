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
import javafx.geometry.Pos;
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
    private final double defaultSimulationTime = 14400; // 10 days

    // Käyttöliittymäkomponentit:
    // Left Box values
    private TextField simulationTimeValue = new TextField();

    private TextField meanThrashAmountPerThrowValue = new TextField();

    private TextField singleAptAmountValue = new TextField();
    private TextField doubleAptAmountValue = new TextField();
    private TextField tripleAptAmountValue = new TextField();
    private TextField quadAptAmountValue = new TextField();

    private TextField mixedCanAmountValue = new TextField();
    private TextField plasticCanAmountValue = new TextField();
    private TextField glassCanAmountValue = new TextField();
    private TextField paperCanAmountValue = new TextField();
    private TextField bioCanAmountValue = new TextField();
    private TextField metalCanAmountValue = new TextField();
    //
    private TextField delay;
    private Label endTime = new Label("");
    private Label timeLabel;
    private Label delayLabel;
    private Label resultLabel;

    // Performance variables

    private Label trashThrownTotalKilos = new Label("");
    private Label trashThrownTotalLiters = new Label("");

    private Label mixedTotal = new Label("");
    private Label bioTotal = new Label("");
    private Label cardboardTotal = new Label("");
    private Label plasticTotal = new Label("");
    private Label glassTotal = new Label("");
    private Label metalTotal = new Label("");

    private Label mixedUsage = new Label("");
    private Label bioUsage = new Label("");
    private Label cardboardUsage = new Label("");
    private Label plasticUsage = new Label("");
    private Label glassUsage = new Label("");
    private Label metalUsage = new Label("");

    private Label mixedOverflow = new Label("");
    private Label bioOverflow = new Label("");
    private Label cardboardOverflow = new Label("");
    private Label plasticOverflow = new Label("");
    private Label glassOverflow = new Label("");
    private  Label metalOverflow = new Label("");

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
    public double getSimulationTimeValue() {
        if (simulationTimeValue.getText().isEmpty()) {
            return defaultSimulationTime;
        }
        else {
            try {
                return Double.parseDouble(simulationTimeValue.getText());
            } catch (NumberFormatException e) {
                return defaultSimulationTime;
            }
        }
    }

    @Override
    public double getMeanTrashAmtPerThrow() {
        if (meanThrashAmountPerThrowValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Double.parseDouble(meanThrashAmountPerThrowValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    @Override
    public int getSingleAptAmt() {
        if (singleAptAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(singleAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    @Override
    public int getDoubleAptAmt() {
        if (doubleAptAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(doubleAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    @Override
    public int getTripleAptAmt() {
        if (tripleAptAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(tripleAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    @Override
    public int getQuadAptAmt() {
        if (quadAptAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(quadAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    @Override
    public void setMixedTotal(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        mixedTotal.setText("Mixed trash: " + df.format(value) + " kg");
    }

    @Override
    public void setBioTotal(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        bioTotal.setText("Bio trash: " + df.format(value) + " kg");
    }

    @Override
    public void setCardboardTotal(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        cardboardTotal.setText("Cardboard trash: " + df.format(value) + " kg");
    }

    @Override
    public void setPlasticTotal(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        plasticTotal.setText("Plastic trash: " + df.format(value) + " kg");
    }

    @Override
    public void setGlassTotal(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        glassTotal.setText("Glass trash: " + df.format(value) + " kg");
    }

    @Override
    public void setMetalTotal(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        metalTotal.setText("Metal trash: " + df.format(value) + " kg");
    }

    @Override
    public void setMixedUsage(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        mixedUsage.setText("Mixed usage: " + df.format(value) + "%");
    }

    @Override
    public void setBioUsage(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        bioUsage.setText("Bio usage: " + df.format(value) + "%");
    }

    @Override
    public void setCardboardUsage(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        cardboardUsage.setText("Cardboard usage: " + df.format(value) + "%");
    }

    @Override
    public void setPlasticUsage(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        plasticUsage.setText("Plastic usage: " + df.format(value) + "%");
    }

    @Override
    public void setGlassUsage(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        glassUsage.setText("Glass usage: " + df.format(value) + "%");
    }

    @Override
    public void setMetalUsage(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        metalUsage.setText("Metal usage: " + df.format(value) + "%");
    }

    @Override
    public void setMixedOverflow(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        mixedOverflow.setText("Mixed overflow: " + df.format(value) + " kg");
    }

    @Override
    public void setBioOverflow(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        bioOverflow.setText("Bio overflow: " + df.format(value) + " kg");
    }

    @Override
    public void setCardboardOverflow(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        cardboardOverflow.setText("Cardboard overflow: " + df.format(value) + " kg");
    }

    @Override
    public void setPlasticOverflow(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        plasticOverflow.setText("Plastic overflow: " + df.format(value) + " kg");
    }

    @Override
    public void setGlassOverflow(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        glassOverflow.setText("Glass overflow: " + df.format(value) + " kg");
    }

    @Override
    public void setMetalOverflow(double value) {
        DecimalFormat df = new DecimalFormat("#0.00");
        metalOverflow.setText("Metal overflow: " + df.format(value) + " kg");
    }

    @Override
    public void setTrashThrowTimes(double amt) {

    }

    @Override
    public void setShelterClearedTimes(double amt) {

    }

    @Override
    public void setTrashThrownTotalLiters(double liters) {
        DecimalFormat df = new DecimalFormat("#0.00");
        trashThrownTotalLiters.setText("Total trash thrown in liters: " + df.format(liters) + " l");
    }

    @Override
    public void setTrashThrownTotalKilos(double kg) {
        DecimalFormat df = new DecimalFormat("#0.00");
        trashThrownTotalKilos.setText("Total trash thrown in kilograms: " + df.format(kg) + " kg");
    }

    public int getMixedCanAmountValue() {
        if (mixedCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(mixedCanAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    public int getPlasticCanAmountValue() {
        if (plasticCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(plasticCanAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    public int getGlassCanAmountValue() {
        if (glassCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(glassCanAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    public int getPaperCanAmountValue() {
        if (paperCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(paperCanAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    public int getBioCanAmountValue() {
        if (bioCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(bioCanAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    public int getMetalCanAmountValue() {
        if (metalCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(metalCanAmountValue.getText());
            } catch (NumberFormatException e) {
                return 1;
            }
        }
    }

    @Override
    public long getDelay() {
        return Long.parseLong(delay.getText());
    }

    @Override
    public void setLoppuaika(double aika) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        this.endTime.setText("Simulation end time: "+formatter.format(aika)+" minutes, "+formatter.format(aika / 1440)+" days.");
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
        double verticalPosition = 0.90;

        VBox leftVBox   = left();
        leftVBox.setId("leftVBox");

        ScrollPane rightPanel  = right();
        rightPanel.setId("rightPanel");

        horSplitPane.getItems().addAll(leftVBox, center(), rightPanel);

        //SET DEFAULT SIZE OF TOP PANELS HORIZONTALLY
        horSplitPane.getDividers().getFirst().setPosition(horizontalFirstPosition);
        horSplitPane.getDividers().getLast().setPosition(horizontalMiddlePosition);
        horSplitPane.getDividers().get(1).setPosition(horizontalLastPosition);
        //END SETTING DEFAULT SIZES

        verSplitPane.getItems().addAll(horSplitPane, bottom());
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

    private ScrollPane right(){
        final int TEXT_FIELD_WIDTH = 50;
        final Font FONT = new Font("Dubai Medium", 15);

        Label collectedDataTitle = new Label("COLLECTED DATA: ");
        collectedDataTitle.setFont(FONT);

        Label trashThrownTotals = new Label("Trash thrown: ");
        trashThrownTotals.setFont(FONT);

        Label shelterUsageRates = new Label("Shelter usage rates: ");
        shelterUsageRates.setFont(FONT);

        Label trashOverflows = new Label("Trash overflow: ");
        trashOverflows.setFont(FONT);

        VBox rightControl  = new VBox(
                collectedDataTitle,
                trashThrownTotals, trashThrownTotalKilos, trashThrownTotalLiters, mixedTotal, bioTotal, cardboardTotal, plasticTotal, glassTotal, metalTotal,
                shelterUsageRates, mixedUsage, bioUsage, cardboardUsage, plasticUsage, glassUsage, metalUsage,
                trashOverflows, mixedOverflow, bioOverflow, cardboardOverflow, plasticOverflow, glassOverflow, metalOverflow, endTime
        );
        rightControl.setSpacing(8.0);

        ScrollPane scrollPane = new ScrollPane(rightControl);

        return scrollPane;
    }

    private VBox left(){
        final int TEXT_FIELD_WIDTH = 50;
        final Font FONT = new Font("Dubai Medium", 15);

        // Simulation time
        BorderPane simulationTime = new BorderPane();
        simulationTimeValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label simulationTimeLabel = new Label("Simulation Time (Days)");
        simulationTimeLabel.setFont(FONT);
        simulationTime.setLeft(simulationTimeLabel);
        simulationTime.setRight(simulationTimeValue);

        // meanThrashAmountPerThrow
        meanThrashAmountPerThrowValue.setText("6.0"); // set default
        BorderPane meanThrashAmountPerThrow = new BorderPane();
        meanThrashAmountPerThrowValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label meanTrashAmountPerThrowLabel = new Label("Mean Trash Amount per Throw");
        meanTrashAmountPerThrowLabel.setFont(FONT);
        meanThrashAmountPerThrow.setLeft(meanTrashAmountPerThrowLabel);
        meanThrashAmountPerThrow.setRight(meanThrashAmountPerThrowValue);

        // Apartment amounts
        BorderPane singleAptAmount = new BorderPane();
        BorderPane doubleAptAmount = new BorderPane();
        BorderPane tripleAptAmount = new BorderPane();
        BorderPane quadAptAmount = new BorderPane();

        singleAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label singleAptAmountLabel = new Label("Single Apartment Amount");
        singleAptAmountLabel.setFont(FONT);
        singleAptAmount.setLeft(singleAptAmountLabel);
        singleAptAmount.setRight(singleAptAmountValue);

        doubleAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label doubleAptAmountLabel = new Label("Double Apartment Amount");
        doubleAptAmountLabel.setFont(FONT);
        doubleAptAmount.setLeft(doubleAptAmountLabel);
        doubleAptAmount.setRight(doubleAptAmountValue);

        tripleAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label tripleAptAmountLabel = new Label("Triple Apartment Amount");
        tripleAptAmountLabel.setFont(FONT);
        tripleAptAmount.setLeft(tripleAptAmountLabel);
        tripleAptAmount.setRight(tripleAptAmountValue);

        quadAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        Label quadAptAmountLabel = new Label("Quad Apartment Amount");
        quadAptAmountLabel.setFont(FONT);
        quadAptAmount.setLeft(quadAptAmountLabel);
        quadAptAmount.setRight(quadAptAmountValue);

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

        VBox leftControl  = new VBox(simulationTime, meanThrashAmountPerThrow, singleAptAmount, doubleAptAmount, tripleAptAmount, quadAptAmount, mixedCanAmount, plasticCanAmount, bioCanAmount, glassCanAmount, paperCanAmount, metalCanAmount);
        leftControl.setSpacing(10); // Spacing for each component
        return leftControl;
    }

    private Region bottom(){
        startButton = new Button();
        startButton.setText("Käynnistä simulointi");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.startSimulation();
                //startButton.setDisable(true); //SHOULD DISABLE SIMU BUTTON AND ENABLE IT AFTER
            }
        });

        slowButton = new Button();
        slowButton.setText("Hidasta");
        slowButton.setOnAction(e -> controller.slowDown());

        fastButton = new Button();
        fastButton.setText("Nopeuta");
        fastButton.setOnAction(e -> controller.speedUp());

        HBox bottomControl = new HBox(slowButton, startButton, fastButton);
        bottomControl.setAlignment(Pos.CENTER);
        bottomControl.setSpacing(10);

        //MIKÄ TÄÄ ON OISKO JOKU VBOW VAI MITÄ EHMETTIÄ?!?!?!?
        return bottomControl;
    }
}