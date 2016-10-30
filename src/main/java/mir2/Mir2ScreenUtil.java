package mir2;

import mir2.robot.Robot2;

import java.awt.image.BufferedImage;

/**
 * Created by yangwenjie on 16/10/27.
 */
public class Mir2ScreenUtil {
    private static final Robot2 robot = new Robot2();
    private static final int LEFT_TOP_X = 0;
    private static final int LEFT_TOP_Y = 0;
    private static final int MIR2_WINDOW_WIDTH = 1;
    private static final int MIR2_WINDOW_HEIGHT = 1;
    public static BufferedImage mir2WindowScreen(){
        BufferedImage screen = robot.captureScreen();
        BufferedImage mir2Window = screen.getSubimage(LEFT_TOP_X, LEFT_TOP_Y, MIR2_WINDOW_WIDTH, MIR2_WINDOW_HEIGHT);
        return mir2Window;
    }
}
