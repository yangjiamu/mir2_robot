package mir2.screen;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.WinDef.RECT;
import mir2.robot.Robot2;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
/**
 * Created by yangwenjie on 16/10/30.
 */
public class ScreenCaptureTest {
    private static final int MAX_TITLE_LENGTH = 1024;
    public static void main(String[] args) throws IOException, InterruptedException {
        Thread.sleep(1000 * 20);//wait for mir2 client to start and login
        Robot2 robot2 = new Robot2();
        BufferedImage bufferedImage = robot2.captureScreen();
        ImageIO.write(bufferedImage, "png", new File("C:/screen.png"));
    }

    @Test
    public void getCurrentWindowTest(){
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, buffer, MAX_TITLE_LENGTH);
        System.out.println("Active window title: " + Native.toString(buffer));
        RECT rect = new RECT();
        User32.INSTANCE.GetWindowRect(hwnd, rect);
        System.out.println("rect = " + rect);
    }
}
