package view;

import javafx.scene.image.Image;

import java.util.LinkedHashMap;
import java.util.List;

/*
This class is currently not in use, idea was to have an ordered list for images to hang out in but idk.
 */

public class IconContainer {
    private LinkedHashMap<String, List<Image>> groupedList;

    public IconContainer() {
        groupedList = new LinkedHashMap<>();
    }

    public void updateImagesToGroupedList(List<Image> images, String group) {
        groupedList.put(group, images);
    }

    public LinkedHashMap<String, List<Image>> getGroupedList() {
        return groupedList;
    }
}
