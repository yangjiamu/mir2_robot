package mir2.role;

import mir2.DirectionEnum;
import mir2.robot.Robot2;
import mir2.screen.Mir2Screen;

import java.awt.*;

/**
 * Created by yang on 2016/11/5.
 */
public class GameRole {
    private static GameRole instance = new GameRole();
    private static final Robot2 robot = Robot2.getInstance();
    private String career;
    private Integer level;
    private Integer hp;
    private Integer maxHp;
    private Integer mp;
    private Integer maxMp;
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();


    public static GameRole getInstance(){
        return instance;
    }
    private GameRole() {
        career = "战士";
        level = 24;
        maxHp = 295;
        maxMp = 95;
        hp = maxHp;
        mp = maxMp;
    }

    public boolean walkTo(Point destination){
        return true;
    }

    public boolean walkOneStep(DirectionEnum direction){
        return true;
    }

    public boolean faceTo(DirectionEnum direction){
        return true;
    }

    //getter setter
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    public Integer getMp() {
        return mp;
    }

    public void setMp(Integer mp) {
        this.mp = mp;
    }

    public Integer getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(Integer maxMp) {
        this.maxMp = maxMp;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
}
