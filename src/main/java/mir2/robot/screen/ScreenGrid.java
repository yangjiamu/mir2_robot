package mir2.robot.screen;

import lombok.Data;

import java.awt.*;

/**
 * Created by yang on 2016/11/12.
 */
@Data
public class ScreenGrid {
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private Point mir2ScreenLeftTop = mir2Screen.gameScreenLeftTop();
    private static final int widthPerGrid = 48;
    private static final int heightPerGrid = 32;
    private static final int numOfGridHorizontal = 21;
    private static final int numOfGridVertical = 20;
    private int x;
    private int y;
    private Point absoluteScreenGridClickPoint;
    public ScreenGrid(){
        this(0, 0);
    }
    public ScreenGrid(int x, int y){
        this.x = x;
        this.y = y;
        absoluteScreenGridClickPoint = new Point((int)(mir2ScreenLeftTop.getX() + (x-1)*widthPerGrid + widthPerGrid/2),
                (int)(mir2ScreenLeftTop.getY() + (y-1)*heightPerGrid));
    }

    public static boolean isValidScreenGrid(ScreenGrid screenGrid){
        return screenGrid.getX()>0 && screenGrid.getX()<numOfGridHorizontal
                && screenGrid.getY()>0 && screenGrid.getY()<numOfGridVertical;
    }
    public static int getWidthPerGrid() {
        return widthPerGrid;
    }

    public static int getHeightPerGrid() {
        return heightPerGrid;
    }

    public static int getNumOfGridHorizontal() {
        return numOfGridHorizontal;
    }

    public static int getNumOfGridVertical() {
        return numOfGridVertical;
    }

    @Override
    public String toString() {
        return "ScreenGrid[x=" + x +
                ", y=" + y + "]";
    }
}
