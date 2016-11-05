package mir2.robot;

import com.vnetpublishing.java.suapp.SU;
import com.vnetpublishing.java.suapp.SuperUserApplication;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by yang on 2016/11/5.
 */
public class TestAdmin extends SuperUserApplication{
    public static void main(String[] args) {
        SU.run(new TestAdmin(), args);
    }
    @Override
    public int run(String[] strings) {
        System.out.println("RUN AS ADMIN! YAY!");
        try {
            Thread.sleep(1000* 4);
            Robot robot = new Robot();
            robot.mouseMove(1000, 800);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        } catch (AWTException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
