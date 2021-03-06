import mir2.robot.Robot2;
import mir2.robot.screen.Mir2Screen;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/5.
 */
public class Robot2Test {
    public static void main(String[] args) throws InterruptedException, AWTException {
        Robot2 robot2 = Robot2.getInstance();
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        Point mir2ScreenLeftTop = mir2Screen.gameScreenLeftTop();
        Point mir2RoleRelativeCenter = Mir2Screen.getMir2RoleRelativeStandCenter();
        System.out.println(mir2ScreenLeftTop);
        System.out.println(mir2RoleRelativeCenter);

        int absoluteX = (int) (mir2ScreenLeftTop.getX() + mir2RoleRelativeCenter.getX());
        int absoluteY = (int)(mir2ScreenLeftTop.getY() + mir2RoleRelativeCenter.getY());
        System.out.println(absoluteX + " " + absoluteY);
        for (int i = 0; i < 100; i++) {
            robot2.mouseMove(absoluteX, absoluteY);
            Thread.sleep((long) (1000 * 1.5));
        }
    }
    @org.junit.Test
    public void testMouse() throws InterruptedException {

    }

    @org.junit.Test
    public void testRoleRun(){
        Robot2 robot2 = Robot2.getInstance();
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        //south
        robot2.mouseMove(mir2Screen.getAbsoluteSouthRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();
        //north
        robot2.mouseMove(mir2Screen.getAbsoluteNorthRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();
        //west
        robot2.mouseMove(mir2Screen.getAbsoluteWestRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();
        //east
        robot2.mouseMove(mir2Screen.getAbsoluteEastRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();

        //north west
        robot2.mouseMove(mir2Screen.getAbsoluteNorthWestRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();
        //south east
        robot2.mouseMove(mir2Screen.getAbsoluteSouthEastRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();
        //south west
        robot2.mouseMove(mir2Screen.getAbsoluteSouthWestRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();
        //north east
        robot2.mouseMove(mir2Screen.getAbsoluteNorthEastRunMousePoint());
        robot2.pressMouseRightButton();
        robot2.delay(1000*3);
        robot2.releaseMouseRightButton();

    }

    @org.junit.Test
    public void testTurnAround(){
        Robot2 robot2 = Robot2.getInstance();
        robot2.delay(1000*3);
        //north
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        robot2.mouseMove(mir2Screen.getAbsoluteNorthTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //south
        robot2.mouseMove(mir2Screen.getAbsoluteSouthTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //west
        robot2.mouseMove(mir2Screen.getAbsoluteWestTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //east
        robot2.mouseMove(mir2Screen.getAbsoluteEastTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //north west
        robot2.mouseMove(mir2Screen.getAbsoluteNorthWestTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //north east
        robot2.mouseMove(mir2Screen.getAbsoluteNorthEastTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //south west
        robot2.mouseMove(mir2Screen.getAbsoluteSouthWestTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
        //south east
        robot2.mouseMove(mir2Screen.getAbsoluteSouthEastTurnMousePoint());
        robot2.clickMouseRightButton();
        robot2.delay(1000*3);
    }

    @org.junit.Test
    public void testCoordination() throws IOException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        BufferedImage image = mir2Screen.getNavigationArea();
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\coordination.png"));
    }

    @org.junit.Test
    public void testScale() throws IOException {
        BufferedImage oldImag = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\coordination.png"));
        BufferedImage newImage = new BufferedImage(oldImag.getWidth()*2, oldImag.getHeight()*2, BufferedImage.TYPE_INT_RGB);

        Graphics g = newImage.createGraphics();
        g.drawImage(oldImag, 0, 0, oldImag.getWidth(), oldImag.getHeight(), null);
        g.dispose();
        ImageIO.write(newImage , "png", new File("C:\\Users\\yang\\Pictures\\coordination_2scale.png"));
    }

    @org.junit.Test
    public void testScale1() throws IOException {
        BufferedImage oldImag = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\coordination.png"));
        BufferedImage newImage = Scalr.resize(oldImag, Scalr.Method.BALANCED, oldImag.getWidth()*2, oldImag.getHeight()*2);
        ImageIO.write(newImage , "png", new File("C:\\Users\\yang\\Pictures\\coordination_2scale.png"));
    }

}
