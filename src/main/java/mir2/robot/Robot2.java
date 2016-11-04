package mir2.robot;

import mir2.Coordinate;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * Created by yangwenjie on 16/10/25.
 */
public class Robot2 {
    private java.awt.Robot robot = null;
    private static int letterKeyCodeTable[] = {KeyEvent.VK_A,KeyEvent.VK_B,KeyEvent.VK_C,KeyEvent.VK_D,KeyEvent.VK_E,KeyEvent.VK_F,KeyEvent.VK_G,
            KeyEvent.VK_H,KeyEvent.VK_I,KeyEvent.VK_J,KeyEvent.VK_K,KeyEvent.VK_L,KeyEvent.VK_M,KeyEvent.VK_N,KeyEvent.VK_O,KeyEvent.VK_P,
            KeyEvent.VK_Q, KeyEvent.VK_R,KeyEvent.VK_S,KeyEvent.VK_T,KeyEvent.VK_U,KeyEvent.VK_V,KeyEvent.VK_W,KeyEvent.VK_X,KeyEvent.VK_Y,
            KeyEvent.VK_Z};
    private static int digitKeyCodeTable[] = {KeyEvent.VK_0,KeyEvent.VK_1,KeyEvent.VK_2,KeyEvent.VK_3,KeyEvent.VK_4,KeyEvent.VK_5,KeyEvent.VK_6,
            KeyEvent.VK_7,KeyEvent.VK_8,KeyEvent.VK_9};
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

    public void mouseMove(Coordinate coordinate){
        robot.mouseMove(coordinate.getX(), coordinate.getY());
    }

    public void pressMouseLeftButton(){
        robot.mousePress(InputEvent.BUTTON1_MASK);
    }

    public void pressMouseLeftButton(Coordinate coordinate){
        mouseMove(coordinate);
        pressMouseLeftButton();
    }

    public void releaseMouseLeftButton(){
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    public void pressMouseRightButton(){
        robot.mousePress(InputEvent.BUTTON3_MASK);
    }

    public void pressMouseRightButton(Coordinate coordinate){
        mouseMove(coordinate);
        pressMouseRightButton();
    }

    public void releaseMouseRightButton(){
        robot.mouseRelease(InputEvent.BUTTON3_MASK);
    }

    public void clickMouseLeftButton(){
        pressMouseLeftButton();
        robot.delay(50);
        releaseMouseLeftButton();
    }

    public void clickMouseLeftButton(Coordinate coordinate){
        mouseMove(coordinate);
        clickMouseLeftButton();
    }

    public void clickMouseRightButton(){
        pressMouseRightButton();
        robot.delay(50);
        releaseMouseRightButton();
    }

    public void clickMouseRightButton(Coordinate coordinate){
        mouseMove(coordinate);
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

    public int characterToKeyCode(char ch){
        if (Character.isDigit(ch)) {
            return digitKeyCodeTable[ch - '0'];
        }
        else if(Character.isLetter(ch)){
            if(Character.isUpperCase(ch))
                return letterKeyCodeTable[ch - 'A'];
            else
                return letterKeyCodeTable[ch - 'a'];
        }

        return 0;//
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
