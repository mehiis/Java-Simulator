package application.view;
import application.assets.framework.Clock;
import application.assets.framework.Trace;
import application.controller.IControllerForView;
import application.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * User interface for the simulator
 */
public class SimulatorGUI extends Application implements ISimulatorGUI {
    //Kontrollerin esittely (tarvitaan käyttöliittymässä)
    private IControllerForView controller;

    // settings
    //ASPECT RATIO 16:9. Possible sizes are: 800x450, 1280x720, 1600x900, 1920x1080
    private  int width   = 1280;
    private  int height  = 720;
    private final int defaultSimulationTime = 14400 / 1440;// 10 days

    // Käyttöliittymäkomponentit:
    // Left Box values
    private final TextField simulationTimeValue = new TextField();

    private final TextField meanThrashAmountPerThrowValue = new TextField();

    private final TextField garbageTruckArrivalValue = new TextField();

    private final TextField singleAptAmountValue = new TextField();
    private final TextField doubleAptAmountValue = new TextField();
    private final TextField tripleAptAmountValue = new TextField();
    private final TextField quadAptAmountValue = new TextField();

    private final TextField mixedCanAmountValue = new TextField();
    private final TextField plasticCanAmountValue = new TextField();
    private final TextField glassCanAmountValue = new TextField();
    private final TextField cardBoardCanAmountValue = new TextField();
    private final TextField bioCanAmountValue = new TextField();
    private final TextField metalCanAmountValue = new TextField();


    private final Color blackColor      = Color.BLACK;
    private final Color redColor        = Color.RED;
    private final Color yellowColor     = Color.rgb(186, 142, 35);

    // Middle Control
    private final Label dayLabel = new Label("Day: 1");

    // Right box
    private boolean resultWindowOpen = true;
    private ListView<String> historyWindow;
    private ScrollPane resultsWindow;

    // Bottom Control
    private Button historyButton;
    private Button clearHistoryButton;

    private TextField delay;
    private Label endTime = new Label("");
    private Label timeLabel;
    private Label delayLabel;
    private Label resultLabel;

    // Performance variables
    private final Label trashThrownTimes    = new Label("");
    private final Label trashClearedTimes   = new Label("");
    private final Label shelterUsagePercent = new Label("");

    private final Label trashThrownTotalKilos = new Label("");
    private final Label trashThrownTotalLiters = new Label("");

    private final Label mixedTotal = new Label("");
    private final Label bioTotal = new Label("");
    private final Label cardboardTotal = new Label("");
    private final Label plasticTotal = new Label("");
    private final Label glassTotal = new Label("");
    private final Label metalTotal = new Label("");

    private final Label mixedUsage = new Label("");
    private final Label bioUsage = new Label("");
    private final Label cardboardUsage = new Label("");
    private final Label plasticUsage = new Label("");
    private final Label glassUsage = new Label("");
    private final Label metalUsage = new Label("");

    private final Label mixedOverflow = new Label("");
    private final Label bioOverflow = new Label("");
    private final Label cardboardOverflow = new Label("");
    private final Label plasticOverflow = new Label("");
    private final Label glassOverflow = new Label("");
    private final Label metalOverflow = new Label("");

    private final Label mixedAccessTime     = new Label("");
    private final Label bioAccessTime       = new Label("");
    private final Label cardboardAccessTime = new Label("");
    private final Label plasticAccessTime   = new Label("");
    private final Label glassAccessTime     = new Label("");
    private final Label metalAccessTime     = new Label("");
    private final double MAX_ACCESS_TIME = 100.0;
    private final double MIN_ACCESS_TIME = 10.0;


    private Button startButton;
    private Button slowButton;
    private Button fastButton;
    private Button pauseButton;

    private IVisuals view;

    private Visuals canvas;

    /**
     * Init method for the application
     */
    @Override
    public void init() {

        Trace.setTraceLevel(Trace.Level.INFO);

        controller = new Controller(this);
    }

    /**
     * Start method for the application.
     * Calls createScene method to create the scene for the window.
     * @param stage Window for the application.
     */
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
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/biowaste.png"))));

            stage.setScene(createScene());
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Visualizes the simulation time.
     * @param day Current day passed from the model.
     */
    public void setDay(int day) {
        dayLabel.setText("Day: " + day);
    }

    @Override
    public int getSimulationTimeValue() {
        if (simulationTimeValue.getText().isEmpty()) {
            return defaultSimulationTime;
        }
        else {
            try {
                return Integer.parseInt(simulationTimeValue.getText());
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

    public int getGarbageTruckArrivalInterval() {
        if (garbageTruckArrivalValue.getText().isEmpty()) {
            return 7;
        }
        else {
            try {
                return Integer.parseInt(garbageTruckArrivalValue.getText());
            } catch (NumberFormatException e) {
                return 7;
            }
        }
    }

    @Override
    public int getSingleAptAmt() {
        if (singleAptAmountValue.getText().isEmpty()) {
            return 10;
        }
        else {
            try {
                return Integer.parseInt(singleAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 10;
            }
        }
    }

    @Override
    public int getDoubleAptAmt() {
        if (doubleAptAmountValue.getText().isEmpty()) {
            return 8;
        }
        else {
            try {
                return Integer.parseInt(doubleAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 8;
            }
        }
    }

    @Override
    public int getTripleAptAmt() {
        if (tripleAptAmountValue.getText().isEmpty()) {
            return 6;
        }
        else {
            try {
                return Integer.parseInt(tripleAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 6;
            }
        }
    }

    @Override
    public int getQuadAptAmt() {
        if (quadAptAmountValue.getText().isEmpty()) {
            return 4;
        }
        else {
            try {
                return Integer.parseInt(quadAptAmountValue.getText());
            } catch (NumberFormatException e) {
                return 4;
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
        mixedUsage.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.00");
        mixedUsage.setText("Mixed usage: " + df.format(value) + "%");

        if(value > MAX_ACCESS_TIME)
            mixedUsage.setTextFill(redColor);
        else if(value < MIN_ACCESS_TIME)
            mixedUsage.setTextFill(yellowColor);
    }

    @Override
    public void setBioUsage(double value) {
        bioUsage.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.00");
        bioUsage.setText("Bio usage: " + df.format(value) + "%");

        if(value > MAX_ACCESS_TIME)
            bioUsage.setTextFill(redColor);
        else if(value < MIN_ACCESS_TIME)
            bioUsage.setTextFill(yellowColor);
    }

    @Override
    public void setCardboardUsage(double value) {
        cardboardUsage.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.00");
        cardboardUsage.setText("Cardboard usage: " + df.format(value) + "%");

        if(value > MAX_ACCESS_TIME)
            cardboardUsage.setTextFill(redColor);
        else if(value < MIN_ACCESS_TIME)
            cardboardUsage.setTextFill(yellowColor);
    }

    @Override
    public void setPlasticUsage(double value) {
        plasticUsage.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.00");
        plasticUsage.setText("Plastic usage: " + df.format(value) + "%");

        if(value > MAX_ACCESS_TIME)
            plasticUsage.setTextFill(redColor);
        else if(value < MIN_ACCESS_TIME)
            plasticUsage.setTextFill(yellowColor);
    }

    @Override
    public void setGlassUsage(double value) {
        glassUsage.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.00");
        glassUsage.setText("Glass usage: " + df.format(value) + "%");

        if(value > MAX_ACCESS_TIME)
            glassUsage.setTextFill(redColor);
        else if(value < MIN_ACCESS_TIME)
            glassUsage.setTextFill(yellowColor);
    }

    @Override
    public void setMetalUsage(double value) {
        metalUsage.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.00");
        metalUsage.setText("Metal usage: " + df.format(value) + "%");

        if(value > MAX_ACCESS_TIME)
            metalUsage.setTextFill(redColor);
        else if(value < MIN_ACCESS_TIME)
            metalUsage.setTextFill(yellowColor);
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
    public void setMixedAcessTime(double value) {
        mixedAccessTime.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.0");
        mixedAccessTime.setText("Mixed waste for " + df.format(value) + " hours");

        if(value > 24)
            mixedAccessTime.setText("Mixed waste for " + df.format(value) + " hours/" + df.format(value/24) + " days");

        if(value > 24)
            mixedAccessTime.setTextFill(redColor);
    }

    @Override
    public void setBioAcessTime(double value) {
        bioAccessTime.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.0");
        bioAccessTime.setText("Bio waste: " + df.format(value) + " hours");

        if(value > 24)
            bioAccessTime.setText("Mixed waste for " + df.format(value) + " hours/" + df.format(value/24) + " days");

        if(value > 24)
            bioAccessTime.setTextFill(redColor);
    }

    @Override
    public void setCardboardAcessTime(double value) {
        cardboardAccessTime.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.0");
        cardboardAccessTime.setText("Cardboard waste: " + df.format(value) + " hours");

        if(value > 24)
            cardboardAccessTime.setText("Mixed waste for " + df.format(value) + " hours/" + df.format(value/24) + " days");

        if(value > 24)
            cardboardAccessTime.setTextFill(redColor);
    }

    @Override
    public void setPlasticAcessTime(double value) {
        plasticAccessTime.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.0");
        plasticAccessTime.setText("Plastic waste: " + df.format(value) + " hours");

        if(value > 24)
            plasticAccessTime.setText("Mixed waste for " + df.format(value) + " hours/" + df.format(value/24) + " days");

        if(value > 24)
            plasticAccessTime.setTextFill(redColor);
    }

    @Override
    public void setGlassAcessTime(double value) {
        glassAccessTime.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.0");
        glassAccessTime.setText("Glass waste: " + df.format(value) + " hours");

        if(value > 24)
            glassAccessTime.setText("Mixed waste for " + df.format(value) + " hours/" + df.format(value/24) + " days");

        if(value > 24)
            glassAccessTime.setTextFill(redColor);
    }

    @Override
    public void setMetalAcessTime(double value) {
        metalAccessTime.setTextFill(blackColor);

        DecimalFormat df = new DecimalFormat("#0.0");
        metalAccessTime.setText("Metal waste: " + df.format(value) + " hours");

        if(value > 24)
            metalAccessTime.setText("Mixed waste for " + df.format(value) + " hours/" + df.format(value/24) + " days");

        if(value > 24)
            metalAccessTime.setTextFill(redColor);
    }

    @Override
    public void setTrashThrowTimes(int amt) {
        trashThrownTimes.setText("Trash was thrown "+amt+" times");
    }
    public void setShelterUsagePercent(double amt) {
        shelterUsagePercent.setTextFill(blackColor);
        shelterUsagePercent.setText("Average shelter usage rate: "+amt+" %");

        if(amt <= MIN_ACCESS_TIME)
            shelterUsagePercent.setTextFill(yellowColor);
        else if(amt > MAX_ACCESS_TIME)
            shelterUsagePercent.setTextFill(redColor);
    }

    @Override
    public void setShelterClearedTimes(int amt) {
        trashClearedTimes.setText("Trash was cleared by truck "+amt+" times");
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

    public int getCardboardCanAmountValue() {
        if (cardBoardCanAmountValue.getText().isEmpty()) {
            return 1;
        }
        else {
            try {
                return Integer.parseInt(cardBoardCanAmountValue.getText());
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

    public void setSimulationTimeValue (int time) {
        simulationTimeValue.setText(Integer.toString(time));

    }

    public void setMeanTrashAmtPerThrow (double amount) {
        meanThrashAmountPerThrowValue.setText(Double.toString(amount));
    }

    public void setGarbageTruckArrivalInterval (int interval) {
        garbageTruckArrivalValue.setText(Integer.toString(interval));
    }

    public void setSingleAptAmt (int amount) {
        singleAptAmountValue.setText(Integer.toString(amount));
    }

    public void setDoubleAptAmt (int amount) {
        doubleAptAmountValue.setText(Integer.toString(amount));
    }

    public void setTripleAptAmt (int amount) {
        tripleAptAmountValue.setText(Integer.toString(amount));
    }

    public void setQuadAptAmt (int amount) {
        quadAptAmountValue.setText(Integer.toString(amount));
    }

    public void setMixedCanAmountValue (int amount) {
        mixedCanAmountValue.setText(Integer.toString(amount));
    }

    public void setPlasticCanAmountValue (int amount) {
        plasticCanAmountValue.setText(Integer.toString(amount));
    }

    public void setGlassCanAmountValue (int amount) {
        glassCanAmountValue.setText(Integer.toString(amount));
    }

    public void setCardBoardCanAmountValue(int amount) {
        cardBoardCanAmountValue.setText(Integer.toString(amount));
    }

    public void setBioCanAmountValue (int amount) {
        bioCanAmountValue.setText(Integer.toString(amount));
    }

    public void setMetalCanAmountValue (int amount) {
        metalCanAmountValue.setText(Integer.toString(amount));
    }

    @Override
    public long getDelay() {
        return Long.parseLong(delay.getText());
    }

    /**
     * Method to set simulation time to the results window.
     * Also enables the start button after the simulation has ended.
     * @param aika time from the model.
     */
    @Override
    public void setLoppuaika(double aika) {
        DecimalFormat formatter = new DecimalFormat("#0.00");
        this.endTime.setText("Simulation time: " + formatter.format(aika / 1440)+" days.");
        startButton.setDisable(false);
        pauseButton.setDisable(true);
        slowButton.setDisable(true);
        fastButton.setDisable(true);
        Clock.getInstance().setTime(0.0);
    }

    /**
     * Method called from the Start() to create the scene for the window.
     * It sets up the layout and configuration of the various UI components in the scene.
     * @return The created Scene object.
     */
    private Scene createScene(){
        SplitPane horSplitPane = new SplitPane();
        SplitPane verSplitPane = new SplitPane();

        double horizontalFirstPosition = 0.2;
        double horizontalMiddlePosition = 0.8;
        double horizontalLastPosition = 0.8;
        double verticalPosition = 0.90;

        VBox leftVBox   = left();
        leftVBox.setId("leftVBox");

        ScrollPane resultsWindow  = right();
        ListView historyWindow = historyWindow();

        StackPane rightPanel = new StackPane(resultsWindow, historyWindow);
        historyWindow.setVisible(false);

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

    @Override
    public IVisuals getVisualisointi() {
        return canvas;
    }

    /**
     * Method called from the createScene() to create middle section of the window.
     * @return The VBox object containing the middle section of the window.
     */
    private VBox center(){

        // INITIALIZING VISUALS HERE!!!
        // controller can access canvas with getVisualisointi()
        canvas = new Visuals(600, 600);

        BorderPane rootPane = new BorderPane();

        rootPane.setCenter(canvas);
        dayLabel.setFont(new Font("Dubai Medium", 20));
        dayLabel.setVisible(false);

        VBox midControl   = new VBox(dayLabel, rootPane);

        //ONKO TÄÄ NY VBOX VAI JOKU GRID SYSTEMI????
        return midControl;
    }

    /**
     * Method to toggle between the history and results windows.
     */
    public void toggleRightPanel() {
        if (resultWindowOpen) {
            resultsWindow.setVisible(false);
            historyWindow.setVisible(true);
            clearHistoryButton.setVisible(true);
            historyButton.setText("Results");
            resultWindowOpen = false;
        } else {
            resultsWindow.setVisible(true);
            historyWindow.setVisible(false);
            clearHistoryButton.setVisible(false);
            historyButton.setText("History");
            resultWindowOpen = true;
        }
    }

    /**
     * Method for fetching fresh data for the history list from the database.
     */
    public void refreshHistoryList() {
        if (historyWindow != null) {
            historyWindow.setItems(controller.getInputHistory());
        }
    }

    /**
     * Method called from the createScene() to create the history window.
     * @return The ListView object containing the history window.
     */
    private ListView historyWindow() {
        historyWindow = new ListView<>();
        refreshHistoryList();
        historyWindow.setOnMouseClicked(event -> {
            String selectedItem = historyWindow.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                int selectedId = Integer.parseInt(selectedItem.split(" \\| ")[0].trim());
                controller.loadInputParameters(selectedId);
            }
        });

        return historyWindow;
    }

    /**
     * Method called from the createScene() to create the result section of the window.
     * @return The ScrollPane object containing the result window.
     */
    private ScrollPane right(){
        final int TEXT_FIELD_WIDTH = 50;
        final Font FONT = new Font("Dubai Medium", 15);

        Label collectedDataTitle = new Label("General data: ");
        collectedDataTitle.setFont(FONT);

        Label trashThrownTotals = new Label("Trash thrown: ");
        trashThrownTotals.setFont(FONT);

        Label shelterUsageRates = new Label("Shelter usage rates: ");
        shelterUsageRates.setFont(FONT);

        Label trashOverflows = new Label("Trash overflow: ");
        trashOverflows.setFont(FONT);

        Label accessTime = new Label("Time unavailable: ");
        accessTime.setFont(FONT);

        VBox rightControl  = new VBox(
                collectedDataTitle,
                endTime, trashThrownTimes, trashClearedTimes, shelterUsagePercent,
                trashThrownTotals, trashThrownTotalKilos, trashThrownTotalLiters, mixedTotal, bioTotal, cardboardTotal, plasticTotal, glassTotal, metalTotal,
                shelterUsageRates, mixedUsage, bioUsage, cardboardUsage, plasticUsage, glassUsage, metalUsage,
                trashOverflows, mixedOverflow, bioOverflow, cardboardOverflow, plasticOverflow, glassOverflow, metalOverflow,
                accessTime, mixedAccessTime, bioAccessTime, cardboardAccessTime, plasticAccessTime, glassAccessTime, metalAccessTime

        );

        // set padding to all Labels in rightControl
        ObservableList<Node> controls = rightControl.getChildren();
        for (Node node: controls) {
            if (node.getClass() == Label.class) {
                ((Label) node).setPadding(new Insets(8, 8, 8, 8));
            }
        }
        rightControl.setSpacing(6.0);

        resultsWindow = new ScrollPane(rightControl);

        return resultsWindow;
    }

    /**
     * Method called from the createScene() to create input section of the window.
     * @return The VBox object containing the input section of the window.
     */
    private VBox left(){
        final int TEXT_FIELD_WIDTH = 50;
        final Font FONT = new Font("Dubai Medium", 15);

        // Simulation time
        BorderPane simulationTime = new BorderPane();
        simulationTimeValue.setPrefWidth(TEXT_FIELD_WIDTH);
        simulationTimeValue.setPromptText("10");
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

        // Garbage truck emptying interval
        BorderPane garbageTruckTime = new BorderPane();
        garbageTruckArrivalValue.setPrefWidth(TEXT_FIELD_WIDTH);
        garbageTruckArrivalValue.setPromptText("7");
        Label garbageTruckTimeLabel = new Label("Garbage Truck Arrival Interval (Days)");
        garbageTruckTimeLabel.setFont(FONT);
        garbageTruckTime.setLeft(garbageTruckTimeLabel);
        garbageTruckTime.setRight(garbageTruckArrivalValue);

        // Apartment amounts
        BorderPane singleAptAmount = new BorderPane();
        BorderPane doubleAptAmount = new BorderPane();
        BorderPane tripleAptAmount = new BorderPane();
        BorderPane quadAptAmount = new BorderPane();

        singleAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        singleAptAmountValue.setPromptText("10");
        Label singleAptAmountLabel = new Label("Single Apartment Amount");
        singleAptAmountLabel.setFont(FONT);
        singleAptAmount.setLeft(singleAptAmountLabel);
        singleAptAmount.setRight(singleAptAmountValue);

        doubleAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        doubleAptAmountValue.setPromptText("8");
        Label doubleAptAmountLabel = new Label("Double Apartment Amount");
        doubleAptAmountLabel.setFont(FONT);
        doubleAptAmount.setLeft(doubleAptAmountLabel);
        doubleAptAmount.setRight(doubleAptAmountValue);

        tripleAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        tripleAptAmountValue.setPromptText("6");
        Label tripleAptAmountLabel = new Label("Triple Apartment Amount");
        tripleAptAmountLabel.setFont(FONT);
        tripleAptAmount.setLeft(tripleAptAmountLabel);
        tripleAptAmount.setRight(tripleAptAmountValue);

        quadAptAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        quadAptAmountValue.setPromptText("4");
        Label quadAptAmountLabel = new Label("Quad Apartment Amount");
        quadAptAmountLabel.setFont(FONT);
        quadAptAmount.setLeft(quadAptAmountLabel);
        quadAptAmount.setRight(quadAptAmountValue);

        // Mixed can amount
        BorderPane mixedCanAmount = new BorderPane();
        mixedCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        mixedCanAmountValue.setPromptText("1");
        Label mixedCanAmountLabel = new Label("Mixed");
        mixedCanAmountLabel.setFont(FONT);
        mixedCanAmount.setLeft(mixedCanAmountLabel);
        mixedCanAmount.setRight(mixedCanAmountValue);

        // Plastic can amount
        BorderPane plasticCanAmount = new BorderPane();
        plasticCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        plasticCanAmountValue.setPromptText("1");
        Label plasticCanAmountLabel = new Label("Plastic");
        plasticCanAmountLabel.setFont(FONT);
        plasticCanAmount.setLeft(plasticCanAmountLabel);
        plasticCanAmount.setRight(plasticCanAmountValue);

        // Bio can amount
        BorderPane bioCanAmount = new BorderPane();
        bioCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        bioCanAmountValue.setPromptText("1");
        Label bioCanAmountLabel = new Label("Bio");
        bioCanAmountLabel.setFont(FONT);
        bioCanAmountLabel.setFont(FONT);
        bioCanAmount.setLeft(bioCanAmountLabel);
        bioCanAmount.setRight(bioCanAmountValue);

        // Glass can amount
        BorderPane glassCanAmount = new BorderPane();
        glassCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        glassCanAmountValue.setPromptText("1");
        Label glassCanAmountLabel = new Label("Glass");
        glassCanAmountLabel.setFont(FONT);
        glassCanAmount.setLeft(glassCanAmountLabel);
        glassCanAmount.setRight(glassCanAmountValue);

        // CardBOARD can amount
        BorderPane cardBoardCanAmount = new BorderPane();
        cardBoardCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        cardBoardCanAmountValue.setPromptText("1");
        Label cardBoardCanAmountLabel = new Label("Cardboard");
        cardBoardCanAmountLabel.setFont(FONT);
        cardBoardCanAmount.setLeft(cardBoardCanAmountLabel);
        cardBoardCanAmount.setRight(cardBoardCanAmountValue);

        // Metal can amount
        BorderPane metalCanAmount = new BorderPane();
        metalCanAmountValue.setPrefWidth(TEXT_FIELD_WIDTH);
        metalCanAmountValue.setPromptText("1");
        Label metalCanAmountLabel = new Label("Metal");
        metalCanAmountLabel.setFont(FONT);
        metalCanAmount.setLeft(metalCanAmountLabel);
        metalCanAmount.setRight(metalCanAmountValue);

        VBox leftControl  = new VBox(simulationTime, meanThrashAmountPerThrow, garbageTruckTime,  singleAptAmount, doubleAptAmount, tripleAptAmount, quadAptAmount, mixedCanAmount, plasticCanAmount, bioCanAmount, glassCanAmount, cardBoardCanAmount, metalCanAmount);
        leftControl.setSpacing(10); // Spacing for each component
        return leftControl;
    }

    /**
     * Method called from the createScene() to create the bottom section of the window.
     * @return The Region object containing the bottom section of the window.
     */
    private Region bottom(){
        startButton = new Button();
        startButton.setText("Start Simulation");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dayLabel.setVisible(true);
                controller.startSimulation();
                startButton.setDisable(true);
                pauseButton.setDisable(false);
                slowButton.setDisable(false);
                fastButton.setDisable(false);
            }
        });

        pauseButton = new Button();
        pauseButton.setDisable(true);
        pauseButton.setText("Pause");
        pauseButton.setOnAction(e -> {
            controller.pause();
            if(Objects.equals(pauseButton.getText(), "Pause"))
                pauseButton.setText("Resume");
            else
                pauseButton.setText("Pause");
        });

        slowButton = new Button();
        slowButton.setText("Slow Down");
        slowButton.setDisable(true);
        slowButton.setOnAction(e -> controller.slowDown());

        fastButton = new Button();
        fastButton.setText("Speed Up");
        fastButton.setDisable(true);
        fastButton.setOnAction(e -> controller.speedUp());

        historyButton = new Button();
        historyButton.setText("History");
        historyButton.setOnAction(e -> toggleRightPanel());

        clearHistoryButton = new Button();
        clearHistoryButton.setText("Clear History");
        clearHistoryButton.setVisible(false);
        clearHistoryButton.setOnAction(e -> {
                if (!historyWindow.getItems().isEmpty()) {
                    controller.clearHistory();
                }
                else {
                    System.out.println("History is already empty");
                }
        });

        HBox bottomControl = new HBox(slowButton, startButton, fastButton, pauseButton, historyButton, clearHistoryButton);
        bottomControl.setAlignment(Pos.CENTER);
        bottomControl.setSpacing(10);

        return bottomControl;
    }
}