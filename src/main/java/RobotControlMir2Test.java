import com.xnx3.robot.Robot;

import java.awt.event.KeyEvent;

/**
 * Created by yangwenjie on 16/10/25.
 */
public class RobotControlMir2Test {
    private static final Robot robot = new Robot();

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(1000 * 1);
        say("hello");
    }

    public static void say(String str) {
        robot.press((int) KeyEvent.KEY_EVENT_MASK);
        for (int i = 0; i < str.length(); i++) {
            System.out.println(i);
            robot.press(robot.StringToKey(str.substring(i, i + 1)));
        }
        robot.press((int) KeyEvent.KEY_EVENT_MASK);
    }
}
