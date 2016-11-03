package mir2.screen;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

import java.awt.*;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2Screen {
    private static final String MIR2_WINDOW_TITLE = "Mir2online.Com";
    private static final int MAX_TITLE_LENGTH = 20;
    private static final int MIR2_SCREEN_WIDTH = 1024;
    private static final int MIR2_SCREEN_HEIGHT = 768;

    private static Mir2Screen instance = null;
    private static boolean windowExist = false;

    private int operationScreenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private int operationScreenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private Point mir2TitleBarLeftTop;
    private Point mir2TitleBarRightBottom;
    private Point mir2ScreenLeftTop;
    private Point mir2ScreenRightBottom;

    private Mir2Screen() {
        char [] title = new char[MAX_TITLE_LENGTH*2];
        WinDef.HWND hwnd = User32.INSTANCE.GetForegroundWindow();
        User32.INSTANCE.GetWindowText(hwnd, title, MAX_TITLE_LENGTH);
        if(MIR2_WINDOW_TITLE.equals(new String(title).trim())){
            windowExist = true;
            WinDef.RECT rect = new WinDef.RECT();
            User32.INSTANCE.GetWindowRect(hwnd, rect);
            mir2TitleBarLeftTop = new Point(rect.left, rect.top);
            mir2TitleBarRightBottom = new Point(rect.right, rect.bottom);
            int titleBarWidth = rect.right - rect.left;
            int smallBarWidth = (titleBarWidth - MIR2_SCREEN_WIDTH)/2;
            int titleBarHeight = rect.bottom - rect.top - MIR2_SCREEN_HEIGHT - smallBarWidth;
            mir2TitleBarLeftTop = new Point(rect.left, rect.top);
            mir2TitleBarRightBottom = new Point(rect.right, rect.top + titleBarHeight);
            mir2ScreenLeftTop = new Point(rect.left, rect.top + titleBarHeight);
            mir2ScreenRightBottom = new Point(rect.right, rect.bottom - smallBarWidth);
        }
    }

    public static synchronized Mir2Screen getMir2Screen(){
        if(instance == null){
            instance = new Mir2Screen();
        }
        return instance;
    }

    public static String getMir2WindowTitle() {
        return MIR2_WINDOW_TITLE;
    }

    public static int getMaxTitleLength() {
        return MAX_TITLE_LENGTH;
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

    public static boolean isWindowExist() {
        return windowExist;
    }

    public static void setWindowExist(boolean windowExist) {
        Mir2Screen.windowExist = windowExist;
    }

    public int getOperationScreenWidth() {
        return operationScreenWidth;
    }

    public void setOperationScreenWidth(int operationScreenWidth) {
        this.operationScreenWidth = operationScreenWidth;
    }

    public int getOperationScreenHeight() {
        return operationScreenHeight;
    }

    public void setOperationScreenHeight(int operationScreenHeight) {
        this.operationScreenHeight = operationScreenHeight;
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
