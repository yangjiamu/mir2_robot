package mir2;

/**
 * Created by yangwenjie on 16/10/25.
 */
public enum DirectionEnum {
    EAST(0, "东"),
    SOUTH(1, "南"),
    WEST(2, "西"),
    NORTH(3, "北"),
    SOUTH_EAST(4, "东南"),
    SOUTH_WEST(5, "西南"),
    NORTH_WEST(6, "西北"),
    NORTH_EAST(7, "东北");
    private int code;
    private String desc;
    private Coordinate coordination;

    DirectionEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
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

    public Coordinate getCoordination() {
        return coordination;
    }

    public void setCoordination(Coordinate coordination) {
        this.coordination = coordination;
    }
}
