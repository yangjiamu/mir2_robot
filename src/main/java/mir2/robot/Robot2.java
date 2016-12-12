package mir2.robot;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by yangwenjie on 16/10/25.
 */
public class Robot2 {
    private java.awt.Robot robot = null;

    private static int screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static int screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static Robot2 instance = new Robot2();
    private Robot2(){
        try {
            robot = new java.awt.Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static Robot2 getInstance(){
        return instance;
    }

    public void delay(int mils){
        robot.delay(mils);
    }

    public void mouseMove(int x, int y){
        robot.mouseMove(x, y);
    }

    public void mouseMove(Point point){
        robot.mouseMove((int)point.getX(), (int)point.getY());
    }

    public void pressMouseLeftButton(){
        robot.mousePress(InputEvent.BUTTON1_MASK);
    }


    public void releaseMouseLeftButton(){
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public void pressMouseRightButton(){
        robot.mousePress(InputEvent.BUTTON3_MASK);
    }

    public void releaseMouseRightButton(){
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    public void clickMouseLeftButton(){
        clickMouseLeftButton(25);
    }

    public void clickMouseLeftButton(int millSeconds){
        pressMouseLeftButton();
        robot.delay(millSeconds);
        releaseMouseLeftButton();
    }

    public void clickMouseLeftButton(Point point){
        mouseMove(point);
        clickMouseLeftButton();
    }

    public void clickMouseRightButton(){
        pressMouseRightButton();
        robot.delay(50);
        releaseMouseRightButton();
    }

    public void clickMouseRightButton(Point point){
        mouseMove(point);
        clickMouseRightButton();
    }

    public void pressKey(int keyCode){
        //digit or character
        robot.keyPress(keyCode);
        robot.delay(5);
        robot.keyRelease(keyCode);
    }

    public void pressKey(int keyCode1, int keyCode2){
        robot.keyPress(keyCode1);
        robot.delay(5);
        robot.keyPress(keyCode2);
        robot.delay(5);
        robot.keyRelease(keyCode1);
        robot.keyRelease(keyCode2);
    }



    public BufferedImage captureScreen(){
        return robot.createScreenCapture(new Rectangle(0, 0, screenWidth, screenHeight));
    }

    public BufferedImage captureScreen(int x, int y, int width, int height){
        return robot.createScreenCapture(new Rectangle(x, y, width, height));
    }

    public java.awt.Robot getRobot(){
        return this.robot;
    }
}
