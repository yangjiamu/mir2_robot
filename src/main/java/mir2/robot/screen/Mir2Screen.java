package mir2.robot.screen;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import lombok.Getter;
import mir2.robot.Robot2;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yangwenjie on 16/11/3.
 */
@Getter
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

    private static final Point[] MIR2_SMALL_BAG_RELATIVE_POSITON = {new Point(400, 576), new Point(443, 576), new Point(487, 576),
            new Point(530, 576), new Point(574, 576), new Point(618, 576)};
    private static final Point MIR2_BAG_RELATIVE_POSITION = new Point(26, 17);



    private static final Point MIR2_HPMP_CICLE_START = new Point(43, 608);
    private static final int MIR2_HPMP_CICLE_WIDTH = 92;
    private static final int MIR2_HPMP_CICLE_HEIGHT = 90;
    private static final Point MIR2_COORDINATION_AREA_RELATIVE_START = new Point(13, 749);
    private static final int MIR2_COORDINATION_AREA_WIDTH = 156;
    private static final int MIR2_COORDINATION_AREA_HEIGHT = 17;
    private static final int MIR2_CHI_SIM_PIXEL_WIDTH = 14;
    private static final int MIR2_SMALL_MAP_AREA_WIDTH = 117;
    private static final int MIR2_SMALL_MAP_AREA_HEIGHT = 121;
    private static final Point MIR2_SMALL_MAP_AREA_START_POINT = new Point(MIR2_SCREEN_WIDTH-MIR2_SMALL_MAP_AREA_WIDTH, 0);

    private static Mir2Screen instance = new Mir2Screen();
    private static volatile boolean initialized = false;

    private Point mir2TitleBarLeftTop;
    private Point mir2TitleBarRightBottom;
    private Point mir2ScreenLeftTop;
    private Point mir2ScreenRightBottom;

    public static Point getMir2RoleRelativeStandCenter() {
        return MIR2_ROLE_RELATIVE_STAND_CENTER;
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

    public BufferedImage gameScreenImage(){
        return robot.captureScreen((int)mir2ScreenLeftTop.getX(), (int)mir2ScreenLeftTop.getY(), MIR2_SCREEN_WIDTH,MIR2_SCREEN_HEIGHT);
    }

    public BufferedImage getSmallMap(){
        return gameScreenImage().getSubimage((int)MIR2_SMALL_MAP_AREA_START_POINT.getX(),
                (int)MIR2_SMALL_MAP_AREA_START_POINT.getY(),
                (int)MIR2_SMALL_MAP_AREA_WIDTH,
                (int)MIR2_SMALL_MAP_AREA_HEIGHT);
    }
    public BufferedImage getScreenGridImage(ScreenGrid screenGrid){
        return gameScreenImage().getSubimage((screenGrid.getX()-1)*ScreenGrid.getWidthPerGrid(),
                (screenGrid.getY()-1)*ScreenGrid.getHeightPerGrid(),
                ScreenGrid.getWidthPerGrid(), ScreenGrid.getHeightPerGrid());
    }

    public BufferedImage getTitleBarScreen(){
        return robot.captureScreen((int)mir2TitleBarLeftTop.getX(),
                (int)mir2TitleBarLeftTop.getY(),
                (int)(mir2TitleBarRightBottom.getX() - mir2TitleBarLeftTop.getX()),
                (int)(mir2TitleBarRightBottom.getY() - mir2TitleBarLeftTop.getY()));
    }


    public BufferedImage[] getSmallBagImages(){
        BufferedImage[] smallBags = new BufferedImage[6];
        BufferedImage gameScreen = gameScreenImage();
        for (int i = 0; i < 6; i++) {
            smallBags[i] = gameScreen.getSubimage((int)MIR2_SMALL_BAG_RELATIVE_POSITON[i].getX(), (int)(MIR2_SMALL_BAG_RELATIVE_POSITON[i].getY()),
                Mir2ScreenRelativeConstant.SMALL_BAG_GRID_WIDTH, Mir2ScreenRelativeConstant.SMALL_BAG_GRID_HEIGHT);
        }
        return smallBags;
    }

    public BufferedImage[][] getBagImages(Point bagStart){
        BufferedImage[][] bagImages = new BufferedImage[5][8];
        BufferedImage gameScreen = gameScreenImage();
        for (int i = 0; i < 5; i++) {
            int extra = 0;
            for (int j = 0; j < 8; j++) {
                bagImages[i][j] = gameScreen.getSubimage((int) bagStart.getX() + j*Mir2ScreenRelativeConstant.BAG_GRID_WIDTH + extra,
                        (int) bagStart.getY() + i*Mir2ScreenRelativeConstant.BAG_GRID_HEIGHT,
                        Mir2ScreenRelativeConstant.BAG_GRID_WIDTH, Mir2ScreenRelativeConstant.BAG_GRID_HEIGHT);
                ++extra;
            }
        }
        return bagImages;
    }

    public Point smallBagGridClickPosition(int i){//0-5
        return new Point((int)(MIR2_SMALL_BAG_RELATIVE_POSITON[i].getX() + Mir2ScreenRelativeConstant.SMALL_BAG_GRID_WIDTH/2),
                (int)(MIR2_SMALL_BAG_RELATIVE_POSITON[i].getY() + Mir2ScreenRelativeConstant.BAG_GRID_HEIGHT/2));
    }


    public Point getOkButtonPosition(){
        return new Point((int) (gameScreenLeftTop().getX() + Mir2ScreenRelativeConstant.OK_BUTTON_X),
                (int) (gameScreenLeftTop().getY() + Mir2ScreenRelativeConstant.OK_BUTTON_Y));
    }

    public Point getMaxButtonPosition(){
        return new Point((int) (gameScreenLeftTop().getX() + Mir2ScreenRelativeConstant.MAX_BUTTON_X),
                (int) (gameScreenLeftTop().getY() + Mir2ScreenRelativeConstant.MAX_BUTTON_Y));
    }

    public BufferedImage getHPMPCicle(){
        return gameScreenImage().getSubimage((int)MIR2_HPMP_CICLE_START.getX(), (int)MIR2_HPMP_CICLE_START.getY(),
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
        return gameScreenImage().getSubimage((int) MIR2_COORDINATION_AREA_RELATIVE_START.getX(),
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

    public static Mir2Screen getInstance() {
        while (!initialized){
            instance.init();
        }
        return instance;
    }

    public Point gameScreenLeftTop() {
        return mir2ScreenLeftTop;
    }

    public Point gameScreenRightBottom(){
        return mir2ScreenRightBottom;
    }

    public Point stateInfoPlateOpenButtonPosition(){
        return addPoint(mir2ScreenLeftTop,
                Mir2ScreenRelativeConstant.STATE_INFO_PLATE_OPEN_BUTTON_X,
                Mir2ScreenRelativeConstant.STATE_INFO_PLATE_OPEN_BUTTON_Y);
    }

    public Point bagOpenButtonPosition(){
        return addPoint(mir2ScreenLeftTop,
                Mir2ScreenRelativeConstant.BAG_OPEN_BUTTON_X,
                Mir2ScreenRelativeConstant.BAG_OPEN_BUTTON_Y);
    }

    public Point skillInfoPlateOpenButtonPosition(){
        return addPoint(mir2ScreenLeftTop,
                Mir2ScreenRelativeConstant.SKILL_INFO_PLATE_OPEN_BUTTON_X,
                Mir2ScreenRelativeConstant.SKILL_INFO_PLATE_OPEN_BUTTON_Y);
    }

    public Point voiceOpenCloseButtonPosition(){
        return addPoint(mir2ScreenLeftTop,
                Mir2ScreenRelativeConstant.VOICE_OPEN_CLOSE_BUTTON_Y,
                Mir2ScreenRelativeConstant.VOICE_OPEN_CLOSE_BUTTON_Y);
    }

    private Point addPoint(Point point, int xDif, int yDif){
        return new Point(point.x + xDif, point.y + yDif);
    }
    private Point addPoint(Point point1, Point point2){
        return new Point(point1.x + point2.x, point1.y + point2.y);
    }
}
