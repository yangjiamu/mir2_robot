package mir2;


import mir2.robot.Robot2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Created by yangwenjie on 16/10/25.
 */
public class RobotControlMir2Test {
    private static final Robot2 robot = Robot2.getInstance();

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000 * 60);
        say("hello");
    }

    public static void say(String str) {
        robot.pressKey((int) KeyEvent.VK_ENTER);
        for (int i = 0; i < str.length(); i++) {
            robot.pressKey(robot.characterToKeyCode(str.charAt(i)));
        }
        robot.pressKey((int) KeyEvent.VK_ENTER);
    }

    public static void randomWalk(){
        int randomDirectionCode = new Random().nextInt(7);
        WalkDirectionEnum randomDirection = WalkDirectionEnum.fromCode(randomDirectionCode);
        prepareMouseForWalk(randomDirection);

    }

    public static void prepareMouseForWalk(WalkDirectionEnum directionEnum){
        robot.mouseMove(directionEnum.getCoordination());
    }
}
