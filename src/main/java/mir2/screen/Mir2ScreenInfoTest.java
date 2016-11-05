package mir2.screen;

import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2ScreenInfoTest {
    @Test
    public void testMir2ScreenInfo() throws InterruptedException {
        Thread.sleep(1000 * 2);
        Mir2Screen mir2Screen  = Mir2Screen.getMir2Screen();
        System.out.println(mir2Screen.getMir2TitleBarLeftTop());
        System.out.println(mir2Screen.getMir2TitleBarRightBottom());
        System.out.println(mir2Screen.getMir2ScreenLeftTop());
        System.out.println(mir2Screen.getMir2ScreenRightBottom());

        Assert.assertEquals(1024.0f, mir2Screen.getMir2ScreenRightBottom().getX() - mir2Screen.getMir2ScreenLeftTop().getX());
        Assert.assertEquals(768.0f, mir2Screen.getMir2ScreenRightBottom().getY() - mir2Screen.getMir2ScreenLeftTop().getY());
    }

    @Test
    public void testCaptureGameWindow() throws InterruptedException, IOException {
        Mir2Screen mir2Screen = Mir2Screen.getMir2Screen();
        BufferedImage image = mir2Screen.getGameScreen();
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\game_window.png"));
        image = mir2Screen.getTitleBarScreen();
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\game_titlebar.png"));
    }

    @Test
    public void testHPMP(){
        Mir2Screen mir2Screen = Mir2Screen.getMir2Screen();
        mir2Screen.locateHpBarLenAndCenter();
    }

    @Test
    public void locateHpBarLenAndCenter() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\game_window_test_hpbar.png"));
        Color color = new Color(255, 254, 54);
        //Color color1 = new Color(163, 152, 79);
        int upMax = 0;
        int downMax = 0;
        int maxLen = 0;
        int realCenterX = 0;

        int centerY = (int)(Mir2Screen.getMir2HpCicleCenter().getY());
        for (int x = (int)(Mir2Screen.getMir2HpCicleCenter().getX() - 20); x < (int)(Mir2Screen.getMir2HpCicleCenter().getX() + 20); x++) {
            System.out.println("x:" + x);
            int upCur = 0;
            int downCur = 0;
            int lenCur = 0;
            //up
            int y = centerY;
            while (image.getRGB(x, y) != color.getRGB()){
                System.out.println("y:" + y);
                --y;
            }
            ++y;
            upCur = centerY -y + 1;
            if(upMax < upCur){
                upMax = upCur;
            }
            //down
            y = centerY;
            while (image.getRGB(x, y) != color.getRGB()){
                ++y;
            }
            --y;
            downCur = y - centerY + 1;
            if(downMax < downCur){
                downMax = downCur;
            }
            lenCur = upCur + downCur;
            if(maxLen < lenCur) {
                realCenterX = x;
                maxLen = lenCur;
            }
            System.out.println("up:  " + upCur + "   down: " + downCur + "      len: " + lenCur);
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("**********************************************************************");
        int y = centerY;
        while (image.getRGB(realCenterX, y) != color.getRGB())--y;
        ++y;
        int upYMark = y;
        y = centerY;
        while (image.getRGB(realCenterX, y) != color.getRGB())++y;
        --y;
        int downYMark = y;
        int realCenterY = upYMark + (downYMark - upYMark)/2;
        System.out.println("maxLen: " + maxLen);
        System.out.println("oldX: " + Mir2Screen.getMir2HpCicleCenter().getX());
        System.out.println("oldY: " + Mir2Screen.getMir2HpCicleCenter().getY());
        System.out.println("realCenterX: " + realCenterX);
        System.out.println("realCenterY: " + realCenterY);
    }
}
