package mir2.robot.enums;

import java.awt.*;

/**
 * Created by yang on 2016/11/13.
 */
public enum ColorMappingEnum {
    RED((byte)0xf9, new Color(255, 0, 0), "红色"),
    DARK_RED((byte)0x01, new Color(132, 0, 0), "暗红"),
    PINK((byte)0x1f, new Color(255, 173, 156), "粉红"),
    YELLOW((byte)0xfb, new Color(255, 255, 0), "黄色"),
    ORANGE((byte)0x95, new Color(247, 222, 57), "橙色"),
    GRAY_YELLOW((byte)60, new Color(148, 107, 74), "灰黄"),
    LIME_GREEN((byte)0xfa,  new Color(0, 255, 0), "闪光绿"),
    DARK_GREEN((byte)0xd4, new Color(49, 115, 41), "墨绿"),
    QING_BI_SE((byte)0x06, new Color(0, 132, 132), "青碧色"),
    BLUE((byte)0xfc, new Color(0, 0, 255), "蓝色"),
    NAVY_BLUE((byte)0xeb, new Color(24, 0, 132), "深蓝"),
    BABY_BLUE((byte)0x93, new Color(140, 214, 239), "浅蓝"),
    MAGENTA((byte)0xfd, new Color(255, 0, 255), "品红"),
    CYAN((byte)0xfe, new Color(0, 255, 255), "青色"),
    WHITE((byte)0xff, new Color(255, 255, 255), "白色"),
    BLACK((byte)0x00, new Color(0, 0, 0), "黑色"),
    GRAY((byte)0x9f, new Color(186, 186, 186), "灰色");

    private byte rgb323;
    private Color javaColor;
    private String desc;

    ColorMappingEnum(byte rgb323, Color javaColor, String desc){
        this.rgb323 = rgb323;
        this.javaColor = javaColor;
        this.desc = desc;
    }

    public byte getRgb323() {
        return rgb323;
    }

    public void setRgb323(byte rgb323) {
        this.rgb323 = rgb323;
    }

    public Color getJavaColor() {
        return javaColor;
    }

    public void setJavaColor(Color javaColor) {
        this.javaColor = javaColor;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
