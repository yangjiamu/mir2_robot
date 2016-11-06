package mir2.role;

import mir2.DirectionEnum;
import mir2.robot.Robot2;
import mir2.robot.RoleCurrentStatusEnum;
import mir2.screen.Mir2Screen;
import org.junit.Test;

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
    private Point currentCoordination;
    private RoleCurrentStatusEnum currentStatus;
    private int currentMapNo;
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();


    public static GameRole getInstance(){
        return instance;
    }
    public GameRole() {
        career = "战士";
        level = 24;
        maxHp = 295;
        maxMp = 95;
        hp = maxHp;
        mp = maxMp;
    }

    public boolean walkTo(Point destination){
        //a new Thread to do that???
        return true;
    }

    public boolean walkToMap(int mapNo){
        return true;
    }

    public void walkOneStep(DirectionEnum direction){
        //safety check
        robot.clickMouseLeftButton(direction.getTurnAroundMousePoint());
        robot.delay(1000);
    }

    public void faceTo(DirectionEnum direction){
        robot.clickMouseLeftButton(direction.getTurnAroundMousePoint());
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

    @Test
    public void testWalk(){
        GameRole gameRole = GameRole.getInstance();
        gameRole.walkOneStep(DirectionEnum.EAST);
        gameRole.walkOneStep(DirectionEnum.WEST);
        gameRole.walkOneStep(DirectionEnum.NORTH);
        gameRole.walkOneStep(DirectionEnum.SOUTH);
        gameRole.walkOneStep(DirectionEnum.NORTH_EAST);
        gameRole.walkOneStep(DirectionEnum.NORTH_WEST);
        gameRole.walkOneStep(DirectionEnum.SOUTH_EAST);
        gameRole.walkOneStep(DirectionEnum.SOUTH_WEST);
    }
}
