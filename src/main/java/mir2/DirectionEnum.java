package mir2;

import mir2.screen.Mir2Screen;

import java.awt.*;

/**
 * Created by yangwenjie on 16/10/25.
 */
public enum DirectionEnum {
    EAST(0, "东", Mir2Screen.getInstance().getAbsoluteEastRunMousePoint(), Mir2Screen.getInstance().getAbsoluteEastTurnMousePoint()),
    SOUTH(1, "南", Mir2Screen.getInstance().getAbsoluteSouthRunMousePoint(), Mir2Screen.getInstance().getAbsoluteSouthTurnMousePoint()),
    WEST(2, "西", Mir2Screen.getInstance().getAbsoluteWestRunMousePoint(), Mir2Screen.getInstance().getAbsoluteWestTurnMousePoint()),
    NORTH(3, "北", Mir2Screen.getInstance().getAbsoluteNorthRunMousePoint(), Mir2Screen.getInstance().getAbsoluteNorthTurnMousePoint()),
    SOUTH_EAST(4, "东南", Mir2Screen.getInstance().getAbsoluteSouthEastRunMousePoint(), Mir2Screen.getInstance().getAbsoluteSouthEastTurnMousePoint()),
    SOUTH_WEST(5, "西南", Mir2Screen.getInstance().getAbsoluteSouthWestRunMousePoint(), Mir2Screen.getInstance().getAbsoluteSouthWestTurnMousePoint()),
    NORTH_WEST(6, "西北", Mir2Screen.getInstance().getAbsoluteNorthWestRunMousePoint(), Mir2Screen.getInstance().getAbsoluteNorthWestTurnMousePoint()),
    NORTH_EAST(7, "东北", Mir2Screen.getInstance().getAbsoluteNorthEastRunMousePoint(), Mir2Screen.getInstance().getAbsoluteNorthEastTurnMousePoint()),
    STAND_POINT(8, "缺省中间", Mir2Screen.getInstance().getAbsoluteStandPoint(), Mir2Screen.getInstance().getAbsoluteStandPoint());

    private int code;
    private String desc;
    private Point runMousePoint;
    private Point turnAroundMousePoint;

    public Point getRunMousePoint() {
        return runMousePoint;
    }

    public void setRunMousePoint(Point runMousePoint) {
        this.runMousePoint = runMousePoint;
    }

    public Point getTurnAroundMousePoint() {
        return turnAroundMousePoint;
    }

    public void setTurnAroundMousePoint(Point turnAroundMousePoint) {
        this.turnAroundMousePoint = turnAroundMousePoint;
    }

    DirectionEnum(int code, String desc, Point runMousePoint, Point turnAroundMousePoint){
        this.code = code;
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
}
