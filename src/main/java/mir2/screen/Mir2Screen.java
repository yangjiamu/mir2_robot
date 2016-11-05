package mir2.screen;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.xml.internal.bind.v2.runtime.output.SAXOutput;
import mir2.robot.Robot2;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2Screen {
    private static final String MIR2_WINDOW_TITLE = "Mir2online.Com";
    private static final int MAX_TITLE_LENGTH = 20;
    private static final int MIR2_SCREEN_WIDTH = 1024;
    private static final int MIR2_SCREEN_HEIGHT = 768;
    private static final Point MIR2_ROLE_RELATIVE_CENTER = new Point(502, 297);
    private static final Point MIR2_COORDINATION_RELATIVE_LEFT_TOP = new Point(55, 750);
    private static final Point MIR2_COORDINATION_RELATIVE_RIGHT_BOTTOM = new Point(100, 765);
    private static final int MIR2_SMALL_BAG_WIDTH = 30;
    private static final Point[] MIR2_SMALL_BAG_RELATIVE_POSITON = {new Point(400, 575), new Point(444, 575), new Point(488, 575),
            new Point(532, 575), new Point(576, 575), new Point(620, 575)};
    private static final int MIR2_HP_BAR_LEN = 93;
    private static final int MIR2_MP_BAR_LEN = 93;
    private static final Point MIR2_HP_CICLE_CENTER = new Point(85, 655);
    private static final int operationScreenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int operationScreenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private static Mir2Screen instance = new Mir2Screen();
    private static boolean initialized = false;

    private Point mir2TitleBarLeftTop;
    private Point mir2TitleBarRightBottom;
    private Point mir2ScreenLeftTop;
    private Point mir2ScreenRightBottom;

    public static Point getMir2RoleRelativeCenter() {
        return MIR2_ROLE_RELATIVE_CENTER;
    }

    public static Point getMir2CoordinationRelativeLeftTop() {
        return MIR2_COORDINATION_RELATIVE_LEFT_TOP;
    }

    public static Point getMir2CoordinationRelativeRightBottom() {
        return MIR2_COORDINATION_RELATIVE_RIGHT_BOTTOM;
    }

    public static int getMir2SmallBagWidth() {
        return MIR2_SMALL_BAG_WIDTH;
    }

    public static Point[] getMir2SmallBagRelativePositon() {
        return MIR2_SMALL_BAG_RELATIVE_POSITON;
    }

    public static int getMir2HpBarLen() {
        return MIR2_HP_BAR_LEN;
    }

    public static int getMir2MpBarLen() {
        return MIR2_MP_BAR_LEN;
    }

    public static Point getMir2HpCicleCenter() {
        return MIR2_HP_CICLE_CENTER;
    }

    private Point smallBagLeftTop;
    private Point smallBagRightBottom;
    private Mir2Screen() {
        init();
    }
    private Robot2 robot = Robot2.getInstance();
    public void init(){
        char [] title = new char[MAX_TITLE_LENGTH*2];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, title, MAX_TITLE_LENGTH);
        if(MIR2_WINDOW_TITLE.equals(new String(title).trim())){
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, rect);
            int smallBarWidth = (rect.right - rect.left - MIR2_SCREEN_WIDTH)/2;
            int titleBarHeight = rect.bottom - rect.top - MIR2_SCREEN_HEIGHT - smallBarWidth;
            mir2TitleBarLeftTop = new Point(rect.left, rect.top);
            mir2TitleBarRightBottom = new Point(rect.right, rect.top + titleBarHeight);
            mir2ScreenLeftTop = new Point(rect.left, rect.top + titleBarHeight);
            mir2ScreenRightBottom = new Point(rect.right, rect.bottom - smallBarWidth);
            initialized = true;
        }

    }

    public static synchronized Mir2Screen getMir2Screen(){
        while (!initialized) {
            instance.init();
        }
        return instance;
    }

    public BufferedImage getGameScreen(){
        return robot.captureScreen((int)mir2ScreenLeftTop.getX(), (int)mir2ScreenLeftTop.getY(), MIR2_SCREEN_WIDTH,MIR2_SCREEN_HEIGHT);
    }

    public BufferedImage getTitleBarScreen(){
        return robot.captureScreen((int)mir2TitleBarLeftTop.getX(),
                (int)mir2TitleBarLeftTop.getY(),
                (int)(mir2TitleBarRightBottom.getX() - mir2TitleBarLeftTop.getX()),
                (int)(mir2TitleBarRightBottom.getY() - mir2TitleBarLeftTop.getY()));
    }

    public BufferedImage getCoordinationImage(){
        return getGameScreen().getSubimage((int)MIR2_COORDINATION_RELATIVE_LEFT_TOP.getX(),
                (int)MIR2_COORDINATION_RELATIVE_LEFT_TOP.getY(),
                (int)(MIR2_COORDINATION_RELATIVE_RIGHT_BOTTOM.getX() - MIR2_COORDINATION_RELATIVE_LEFT_TOP.getX()),
                (int)(MIR2_COORDINATION_RELATIVE_RIGHT_BOTTOM.getY() - MIR2_COORDINATION_RELATIVE_LEFT_TOP.getY()));
    }

    public BufferedImage[] getSmallBagImages(){
        BufferedImage[] smallBags = new BufferedImage[6];
        BufferedImage gameScreen = getGameScreen();
        for (int i = 0; i < 6; i++) {
            smallBags[i] = gameScreen.getSubimage((int)MIR2_SMALL_BAG_RELATIVE_POSITON[i].getX(), (int)(MIR2_SMALL_BAG_RELATIVE_POSITON[i].getY()),
                MIR2_SMALL_BAG_WIDTH, MIR2_SMALL_BAG_WIDTH);
        }
        return smallBags;
    }

    public int getHPBarLen(){
        return MIR2_HP_BAR_LEN;
    }
    public int getMPBarLen(){
        return MIR2_MP_BAR_LEN;
    }
    public void locateHpBarLenAndCenter(){
        BufferedImage image = getGameScreen();
        Color color = new Color(255, 254, 54);
        Color color1 = new Color(163, 152, 79);
        int upMax = 0;
        int upMax1 = 0;
        int downMax = 0;
        int downMax1 = 0;
        int maxLen = 0;
        int maxLen1 = 0;
        int realCenterX = 0;

        int centerY = (int)(MIR2_HP_CICLE_CENTER.getY());
        for (int x = (int)(MIR2_HP_CICLE_CENTER.getX() - 20); x < (int)(MIR2_HP_CICLE_CENTER.getX() + 20); x++) {
            int upCur = 0;
            int upCur1 = 0;
            int downCur = 0;
            int downCur1 =0;

            int lenCur = 0;
            int lenCur1 = 0;
            //up
            int y = centerY;
            while (image.getRGB(x, y) != color1.getRGB()){
                --y;
            }
            ++y;
            upCur1 = centerY - y + 1;
            if(upMax1 < upCur1){
                upMax1 = upCur1;
            }
            y = centerY;
            while (image.getRGB(x, y) != color.getRGB()){
                --y;
            }
            ++y;
            upCur = centerY -y + 1;
            if(upMax < upCur){
                upMax = upCur;
            }
            //down
            y = centerY;
            while (image.getRGB(x, y) != color1.getRGB()){
                ++y;
            }
            --y;
            downCur1 = y - centerY + 1;
            if(downMax1 < downCur1){
                downMax1 = downCur1;
            }
            y = centerY;
            while (image.getRGB(x, y) != color.getRGB()){
                ++y;
            }
            --y;
            downCur = y - centerY + 1;
            if(downMax < downCur){
                downMax = downCur;
            }
            lenCur1 = upCur1 + downCur1;
            lenCur = upCur + downCur;
            if(maxLen < lenCur) {
                realCenterX = x;
                maxLen = lenCur;
            }
            if(maxLen1 < lenCur1) {
                realCenterX = x;
                maxLen1 = lenCur1;
            }
            System.out.println("up1: " + upCur1 + "     down1: " + downCur1 + "     len1: " + lenCur1);
            System.out.println("up:  " + upCur + "   down: " + downCur + "      len: " + lenCur);
            System.out.println("----------------------------------------------------------------------");
        }
        System.out.println("**********************************************************************");
        int y = centerY;
        while (image.getRGB(realCenterX, y) != color1.getRGB())--y;
        ++y;
        int upYMark = y;
        y = centerY;
        while (image.getRGB(realCenterX, y) != color1.getRGB())++y;
        --y;
        int downYMark = y;
        int realCenterY = upYMark + (downYMark - upYMark)/2;
        System.out.println("maxLen: " + maxLen + "      maxLen1: " + maxLen1);
        System.out.println("oldX: " + MIR2_HP_CICLE_CENTER.getX());
        System.out.println("oldY: " + MIR2_HP_CICLE_CENTER.getY());
        System.out.println("realCenterX: " + realCenterX);
        System.out.println("realCenterY: " + realCenterY);
    }

    public static int getMir2ScreenWidth() {
        return MIR2_SCREEN_WIDTH;
    }

    public static int getMir2ScreenHeight() {
        return MIR2_SCREEN_HEIGHT;
    }

    public static Mir2Screen getInstance() {
        return instance;
    }

    public static void setInstance(Mir2Screen instance) {
        Mir2Screen.instance = instance;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public int getOperationScreenWidth() {
        return operationScreenWidth;
    }

    public int getOperationScreenHeight() {
        return operationScreenHeight;
    }

    public Point getMir2TitleBarLeftTop() {
        return mir2TitleBarLeftTop;
    }

    public void setMir2TitleBarLeftTop(Point mir2TitleBarLeftTop) {
        this.mir2TitleBarLeftTop = mir2TitleBarLeftTop;
    }

    public Point getMir2TitleBarRightBottom() {
        return mir2TitleBarRightBottom;
    }

    public void setMir2TitleBarRightBottom(Point mir2TitleBarRightBottom) {
        this.mir2TitleBarRightBottom = mir2TitleBarRightBottom;
    }

    public Point getMir2ScreenLeftTop() {
        return mir2ScreenLeftTop;
    }

    public void setMir2ScreenLeftTop(Point mir2ScreenLeftTop) {
        this.mir2ScreenLeftTop = mir2ScreenLeftTop;
    }

    public Point getMir2ScreenRightBottom() {
        return mir2ScreenRightBottom;
    }

    public void setMir2ScreenRightBottom(Point mir2ScreenRightBottom) {
        this.mir2ScreenRightBottom = mir2ScreenRightBottom;
    }
}
