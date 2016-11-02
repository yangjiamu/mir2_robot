package mir2.crawl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class Monster implements Serializable{
    private String monsterName;
    private String monsterUrl;
    private List<DropObject> dropObjects;
    private List<String> occuredMapNames;

    public Monster() {
        dropObjects = new ArrayList<>();
        occuredMapNames = new ArrayList<>();
    }

    public Monster(List<DropObject> dropObjects, List<String> occuredMapNames) {
        this.dropObjects = dropObjects;
        this.occuredMapNames = occuredMapNames;
    }

    public List<DropObject> getDropObjects() {
        return dropObjects;
    }

    public void setDropObjects(List<DropObject> dropObjects) {
        this.dropObjects = dropObjects;
    }

    public List<String> getOccuredMapNames() {
        return occuredMapNames;
    }

    public void setOccuredMapNames(List<String> occuredMapNames) {
        this.occuredMapNames = occuredMapNames;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public String getMonsterUrl() {
        return monsterUrl;
    }

    public void setMonsterUrl(String monsterUrl) {
        this.monsterUrl = monsterUrl;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "monsterName='" + monsterName + '\'' +
                ", dropObjects=" + dropObjects +
                ", occuredMapNames=" + occuredMapNames +
                '}';
    }
}
