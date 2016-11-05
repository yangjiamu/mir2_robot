package mir2.robot;

import mir2.screen.Mir2Screen;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by yang on 2016/11/5.
 */
public class Test{
    public static void main(String[] args) throws InterruptedException, AWTException {
        /*Robot2 robot2 = Robot2.getInstance();
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        Point mir2ScreenLeftTop = mir2Screen.getMir2ScreenLeftTop();
        Point mir2RoleRelativeCenter = Mir2Screen.getMir2RoleRelativeCenter();
        System.out.println(mir2ScreenLeftTop);
        System.out.println(mir2RoleRelativeCenter);

        Thread.sleep(1000 * 5);
        robot2.mouseMove(1000, 800);
        Thread.sleep(1000);*/

        Thread.sleep(1000*3);
        Robot robot = new Robot();
        robot.mouseMove(1000, 800);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
    @org.junit.Test
    public void testMouse() throws InterruptedException {
        Thread.sleep(1000* 5);
        Robot2 robot = Robot2.getInstance();
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        Point mir2ScreenLeftTop = mir2Screen.getMir2ScreenLeftTop();
        Point mir2RoleRelativeCenter = Mir2Screen.getMir2RoleRelativeCenter();
        System.out.println(mir2ScreenLeftTop);
        System.out.println(mir2RoleRelativeCenter);
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
            /*robot.mouseMove((int) (mir2ScreenLeftTop.getX() + mir2RoleRelativeCenter.getX()),
                    (int) (mir2ScreenLeftTop.getY() + mir2RoleRelativeCenter.getY()));*/
            robot.mouseMove(0, 0);
        }
        //robot.pressKey(robot.characterToKeyCode('d'));
        /*Random random = new Random();
        while (true){
            int x = random.nextInt()%Mir2Screen.getMir2ScreenWidth();
            int y = random.nextInt()%Mir2Screen.getMir2ScreenHeight();
            robot.mouseMove(x, y);
            Thread.sleep(1000);
        }*/
    }
}
