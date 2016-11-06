package mir2.robot;

/**
 * Created by yang on 2016/11/5.
 */
public enum  RoleCurrentStatusEnum {
    IDLE(0, "空闲"),
    RUNNING(1, "跑路");
    private int code;
    private String desc;
    private RoleCurrentStatusEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
