package application.view;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.image.Image;
import javafx.util.Duration;

public class AnimatedIcon {

    private DoubleProperty xLoc  = new SimpleDoubleProperty();
    private DoubleProperty yLoc  = new SimpleDoubleProperty();
    private Image img;

    Timeline timeline;

    public double getxLoc() {
        return xLoc.get();
    }

    public double getyLoc() {
        return yLoc.get();
    }

    public AnimatedIcon(Image img, double startPosY) {
        this.img = img;
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(xLoc, 125),
                        new KeyValue(yLoc, startPosY)
                ),
                new KeyFrame(Duration.seconds(0.1),
                        new KeyValue(xLoc, 400),
                        new KeyValue(yLoc, 250)
                )
        );

        timeline.setAutoReverse(true);
        timeline.getOnFinished();
        //timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    public AnimatedIcon(Image img, double startPosX, double startPosY, double endPosX, double endPosY, double duration) {
        this.img = img;
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(xLoc, startPosX),
                        new KeyValue(yLoc, startPosY)
                ),
                new KeyFrame(Duration.seconds(duration),
                        new KeyValue(xLoc, endPosX),
                        new KeyValue(yLoc, endPosY)
                )
        );

        timeline.setAutoReverse(true);
        timeline.getOnFinished();
        //timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.play();
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public Image getImg() {
        return img;
    }
}
