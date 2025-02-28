package view;

import javafx.scene.image.Image;

import java.util.LinkedHashMap;
import java.util.List;

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
