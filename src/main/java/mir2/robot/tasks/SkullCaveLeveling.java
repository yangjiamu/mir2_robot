package mir2.robot.tasks;

import mir2.robot.Mir2Robot;

/**
 * Created by yang on 2016/11/21.
 */
public class SkullCaveLeveling {
    public static void main(String[] args) {
        Mir2Robot mir2Robot = new Mir2Robot();
        mir2Robot.walkToMap("兽人古墓二层");
        //load coordinates from resource
        //walk to coordinate and kill monster
    }
}
