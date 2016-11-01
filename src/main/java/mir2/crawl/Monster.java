package mir2.crawl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class Monster {
    private List<String> dropObjects;
    private List<String> occuredMapNames;

    public Monster() {
        dropObjects = new ArrayList<>();
        occuredMapNames = new ArrayList<>();
    }

    public Monster(List<String> treasures, List<String> occuredMapNames) {
        this.dropObjects = treasures;
        this.occuredMapNames = occuredMapNames;
    }

    public List<String> getDropObjects() {
        return dropObjects;
    }

    public void setDropObjects(List<String> dropObjects) {
        this.dropObjects = dropObjects;
    }

    public List<String> getOccuredMapNames() {
        return occuredMapNames;
    }

    public void setOccuredMapNames(List<String> occuredMapNames) {
        this.occuredMapNames = occuredMapNames;
    }
}
