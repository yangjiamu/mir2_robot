package mir2.screen;

import mir2.robot.Robot2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class ScreenCaptureTest {
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(1000 * 0);//wait for mir2 client to start and login
        Robot2 robot2 = new Robot2();
        BufferedImage bufferedImage = robot2.captureScreen();
        ImageIO.write(bufferedImage, "png", new File("C:\\Users\\yang\\Pictures\\screen_captured1.png"));

        System.out.println(Toolkit.getDefaultToolkit().getScreenSize().getWidth() + ":" + Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    }
}
