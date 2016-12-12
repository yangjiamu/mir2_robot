package mir2.robot.enums;

import mir2.robot.navigation.Coordinate;
import mir2.robot.screen.Mir2Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangwenjie on 16/10/25.
 */
public enum DirectionEnum {
    NORTH(0, 0, -1, "北", Mir2Screen.getInstance().getAbsoluteNorthRunMousePoint(), Mir2Screen.getInstance().getAbsoluteNorthTurnMousePoint()),
    NORTH_EAST(1, 1, -1,  "东北", Mir2Screen.getInstance().getAbsoluteNorthEastRunMousePoint(), Mir2Screen.getInstance().getAbsoluteNorthEastTurnMousePoint()),
    EAST(2, 1, 0, "东", Mir2Screen.getInstance().getAbsoluteEastRunMousePoint(), Mir2Screen.getInstance().getAbsoluteEastTurnMousePoint()),
    SOUTH_EAST(3, 1, 1, "东南", Mir2Screen.getInstance().getAbsoluteSouthEastRunMousePoint(), Mir2Screen.getInstance().getAbsoluteSouthEastTurnMousePoint()),
    SOUTH(4, 0, 1, "南", Mir2Screen.getInstance().getAbsoluteSouthRunMousePoint(), Mir2Screen.getInstance().getAbsoluteSouthTurnMousePoint()),
    SOUTH_WEST(5, -1, 1, "西南", Mir2Screen.getInstance().getAbsoluteSouthWestRunMousePoint(), Mir2Screen.getInstance().getAbsoluteSouthWestTurnMousePoint()),
    WEST(6, -1, 0, "西", Mir2Screen.getInstance().getAbsoluteWestRunMousePoint(), Mir2Screen.getInstance().getAbsoluteWestTurnMousePoint()),
    NORTH_WEST(7, -1, -1, "西北", Mir2Screen.getInstance().getAbsoluteNorthWestRunMousePoint(), Mir2Screen.getInstance().getAbsoluteNorthWestTurnMousePoint()),
    STAND_POINT(8, 0, 0, "缺省中间", Mir2Screen.getInstance().getAbsoluteStandPoint(), Mir2Screen.getInstance().getAbsoluteStandPoint());

    private int code;
    private int x;
    private int y;
    private String desc;
    private Point runMousePoint;
    private Point turnAroundMousePoint;

    public Point getRunMousePoint() {
        return runMousePoint;
    }


    public Point getTurnAroundMousePoint() {
        return turnAroundMousePoint;
    }


    DirectionEnum(int code, int x, int y, String desc, Point runMousePoint, Point turnAroundMousePoint){
        this.code = code;
        this.x = x;
        this.y = y;
        this.desc = desc;
        this.runMousePoint = runMousePoint;
        this.turnAroundMousePoint = turnAroundMousePoint;
    }

    public static DirectionEnum fromCode(int code){
        for (DirectionEnum v : values()) {
            if(v.getCode() == code)
                return v;
        }
        return null;
    }

    public static DirectionEnum generateDirection(Coordinate from, Coordinate to){
        if(!Coordinate.isInLine(from, to)){
            return DirectionEnum.STAND_POINT;
        }
        int xDif = from.x - to.x;
        int yDif = from.y - to.y;
        if(from.x == to.x){
            if(from.x < to.x)
                return DirectionEnum.EAST;
            else return DirectionEnum.WEST;
        }
        if(from.y == to.y){
            if(from.x < to.x)
                return DirectionEnum.SOUTH;
            else return DirectionEnum.NORTH;
        }
        if(xDif < 0){
            if(yDif < 0)
                return DirectionEnum.SOUTH_EAST;
            else return NORTH_EAST;
        }
        else {
            if(yDif > 0)
                return NORTH_WEST;
            else return SOUTH_WEST;
        }
    }

    public static java.util.List<DirectionEnum> getAdjacentDirection(DirectionEnum directionEnum){
        List<DirectionEnum> adjacentDirections = new ArrayList<>();
        int a1 = (directionEnum.getCode()-1 + 8)%8;
        int a2 = (directionEnum.getCode()+1)%8;
        adjacentDirections.add(DirectionEnum.fromCode(a1));
        adjacentDirections.add(DirectionEnum.fromCode(a2));
        return adjacentDirections;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
