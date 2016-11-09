package mir2.screen;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
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
    private static final int operationScreenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int operationScreenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    //relative attribute
    private static final Point MIR2_ROLE_RELATIVE_STAND_CENTER = new Point(504, 327);
    private static final Point MIR2_WEST_RUN_RELATIVE = new Point(300, 327);
    private static final Point MIR2_EAST_RUN_RELATIVE = new Point(720, 327);
    private static final Point MIR2_NORTH_RUN_RELATIVE = new Point(505, 200);
    private static final Point MIR2_SOUTH_RUN_RELATIVE = new Point(505, 420);
    private static final Point MIR2_NORTH_WEST_RUN_RELATIVE = new Point(300, 200);
    private static final Point MIR2_NORTH_EAST_RUN_RELATIVE = new Point(720, 200);
    private static final Point MIR2_SOUTH_WEST_RUN_RELATIVE = new Point(300, 420);
    private static final Point MIR2_SOUTH_EAST_RUN_RELATIVE = new Point(720, 420);
    private static final int MIR2_SMALL_BAG_WIDTH = 30;
    private static final Point[] MIR2_SMALL_BAG_RELATIVE_POSITON = {new Point(400, 575), new Point(444, 575), new Point(488, 575),
            new Point(532, 575), new Point(576, 575), new Point(620, 575)};
    private static final Point MIR2_HPMP_CICLE_START = new Point(43, 608);
    private static final int MIR2_HPMP_CICLE_WIDTH = 92;
    private static final int MIR2_HPMP_CICLE_HEIGHT = 90;
    private static final Point MIR2_COORDINATION_AREA_RELATIVE_START = new Point(13, 749);
    private static final int MIR2_COORDINATION_AREA_WIDTH = 156;
    private static final int MIR2_COORDINATION_AREA_HEIGHT = 17;
    private static final int MIR2_CHI_SIM_PIXEL_WIDTH = 14;

    private static Mir2Screen instance = new Mir2Screen();
    private static volatile boolean initialized = false;

    private Point mir2TitleBarLeftTop;
    private Point mir2TitleBarRightBottom;
    private Point mir2ScreenLeftTop;
    private Point mir2ScreenRightBottom;

    public static Point getMir2RoleRelativeStandCenter() {
        return MIR2_ROLE_RELATIVE_STAND_CENTER;
    }
    public static int getMir2SmallBagWidth() {
        return MIR2_SMALL_BAG_WIDTH;
    }

    public static Point[] getMir2SmallBagRelativePositon() {
        return MIR2_SMALL_BAG_RELATIVE_POSITON;
    }

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

    public BufferedImage getGameScreen(){
        return robot.captureScreen((int)mir2ScreenLeftTop.getX(), (int)mir2ScreenLeftTop.getY(), MIR2_SCREEN_WIDTH,MIR2_SCREEN_HEIGHT);
    }

    public BufferedImage getTitleBarScreen(){
        return robot.captureScreen((int)mir2TitleBarLeftTop.getX(),
                (int)mir2TitleBarLeftTop.getY(),
                (int)(mir2TitleBarRightBottom.getX() - mir2TitleBarLeftTop.getX()),
                (int)(mir2TitleBarRightBottom.getY() - mir2TitleBarLeftTop.getY()));
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

    public BufferedImage getHPMPCicle(){
        return getGameScreen().getSubimage((int)MIR2_HPMP_CICLE_START.getX(), (int)MIR2_HPMP_CICLE_START.getY(),
                MIR2_HPMP_CICLE_WIDTH, MIR2_HPMP_CICLE_HEIGHT);
    }

    public BufferedImage getHPCicle(){
        BufferedImage hpmpCicle = getHPMPCicle();
        return hpmpCicle.getSubimage(0, 0, hpmpCicle.getWidth()/2, hpmpCicle.getHeight());
    }

    public BufferedImage getMPCicle(){
        BufferedImage hpmpCicle = getHPMPCicle();
        return hpmpCicle.getSubimage(0, hpmpCicle.getWidth()/2, hpmpCicle.getWidth()/2, hpmpCicle.getHeight());
    }

    public BufferedImage getNavigationArea(){
        return getGameScreen().getSubimage((int) MIR2_COORDINATION_AREA_RELATIVE_START.getX(),
                (int) MIR2_COORDINATION_AREA_RELATIVE_START.getY(),
                MIR2_COORDINATION_AREA_WIDTH, MIR2_COORDINATION_AREA_HEIGHT);
    }

    public BufferedImage getMapNameArea(String mapName){
        BufferedImage mapAndCoordinationArea = getNavigationArea();
        return mapAndCoordinationArea.getSubimage(0, 0, mapName.length()*MIR2_CHI_SIM_PIXEL_WIDTH,
                mapAndCoordinationArea.getHeight());
    }

    public BufferedImage getCoordinationArea(String mapName){
        BufferedImage mapAndCoordinationArea = getNavigationArea();
        return mapAndCoordinationArea.getSubimage(mapName.length()*MIR2_CHI_SIM_PIXEL_WIDTH, 0,
                mapAndCoordinationArea.getWidth() - mapName.length()*MIR2_CHI_SIM_PIXEL_WIDTH,
                mapAndCoordinationArea.getHeight());
    }

    public Point getAbsoluteStandPoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_ROLE_RELATIVE_STAND_CENTER);
    }

    public Point getAbsoluteWestRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_WEST_RUN_RELATIVE);
    }

    public Point getAbsoluteEastRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_EAST_RUN_RELATIVE);
    }

    public Point getAbsoluteNorthRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_NORTH_RUN_RELATIVE);
    }

    public Point getAbsoluteSouthRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_SOUTH_RUN_RELATIVE);
    }

    public Point getAbsoluteNorthWestRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_NORTH_WEST_RUN_RELATIVE);
    }

    public Point getAbsoluteNorthEastRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_NORTH_EAST_RUN_RELATIVE);
    }

    public Point getAbsoluteSouthWestRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_SOUTH_WEST_RUN_RELATIVE);
    }

    public Point getAbsoluteSouthEastRunMousePoint(){
        return addPoint(mir2ScreenLeftTop, MIR2_SOUTH_EAST_RUN_RELATIVE);
    }

    public Point getAbsoluteNorthTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(0, -50));
    }

    public Point getAbsoluteSouthTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(0, 50));
    }

    public Point getAbsoluteWestTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(-50, 0));
    }

    public Point getAbsoluteEastTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(50, 0));
    }

    public Point getAbsoluteNorthWestTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(-50, -50));
    }
    public Point getAbsoluteNorthEastTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(50, -50));
    }

    public Point getAbsoluteSouthWestTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(-50, 50));
    }

    public Point getAbsoluteSouthEastTurnMousePoint(){
        return addPoint(getAbsoluteStandPoint(), new Point(50, 50));
    }
    public static int getMir2ScreenWidth() {
        return MIR2_SCREEN_WIDTH;
    }

    public static int getMir2ScreenHeight() {
        return MIR2_SCREEN_HEIGHT;
    }

    public static Mir2Screen getInstance() {
        while (!initialized){
            instance.init();
        }
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

    private Point addPoint(Point point1, Point point2){
        return new Point((int)(point1.getX() + point2.getX()), (int)(point1.getY() + point2.getY()));
    }
}
