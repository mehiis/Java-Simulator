package application.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class Resident {

    private DoubleProperty xLoc  = new SimpleDoubleProperty();
    private DoubleProperty yLoc  = new SimpleDoubleProperty();

    public double getxLoc() {
        return xLoc.get();
    }

    public double getyLoc() {
        return yLoc.get();
    }

    public Resident(double startPosY) {

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(xLoc, 125),
                        new KeyValue(yLoc, startPosY)
                ),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(xLoc, 400),
                        new KeyValue(yLoc, startPosY)
                )
        );

        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }
}
