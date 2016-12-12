package mir2.robot.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/11.
 */
public enum TagTypeEnum {
    HAIR_TAG(1, "人物角色头发标记", ColorMappingEnum.BLUE, 8, 6, 0),
    GUARD_AND_ARCHER_TAG(2, "大刀弓箭手标记", ColorMappingEnum.CYAN, 10, 10, 0),
    NORMAL_SMALL_MONSTER(3, "普通种类小怪标记", ColorMappingEnum.YELLOW, 10, 10, 0),
    SPECIAL_SMALL_MONSTER1(4, "特殊种类1小怪标记", ColorMappingEnum.YELLOW, 15, 15, 0),
    SPECIAL_SMALL_MONSTER2(5, "特殊种类2小怪标记", ColorMappingEnum.RED, 10, 10, 0),
    BOSS_TIGER_TAG(6, "老虎标记", ColorMappingEnum.MAGENTA, 20, 20, 0),
    BOSS_SHIWANG_TAG(7, "尸王标记", ColorMappingEnum.MAGENTA, 20, 20, 0),
    BOSS_GUIWANG_TAG(8, "鬼王标记", ColorMappingEnum.MAGENTA, 20, 20, 0),
    BOSS_SPIDER_TAG(9, "紫晶蜘蛛标记", ColorMappingEnum.RED, 20, 20, 0),

    DN_ITEM_DRUG(20, "地上药品", ColorMappingEnum.PINK, 4, 5, 1),
    DN_ITEM_GOLD_COIN(21, "金币", ColorMappingEnum.GRAY_YELLOW, 4, 5, 1),
    DN_ITEM_HIGH_VALUE(22, "高价值", ColorMappingEnum.DARK_RED, 4, 5, 1),
    DN_ITEM_VALUABLE(23, "值钱商店物品", ColorMappingEnum.NAVY_BLUE, 4, 4, 1),//
    DN_ITEM_WORTHLESS(24, "不值钱商店物品", ColorMappingEnum.ORANGE, 4, 5, 1),
    DN_ITEM_DILAOHUICHENGSUIJI(25, "地牢回城随机", ColorMappingEnum.QING_BI_SE, 4, 5, 1),
    DN_ITEM_METERIAL(26, "材料", ColorMappingEnum.GRAY, 4, 5, 1),
    DN_ITEM_CHESTNUT(27, "栗子", ColorMappingEnum.WHITE, 4, 4, 1),//

    ITEM_HP_S_S(30, "HP小单瓶", ColorMappingEnum.PINK, 4, 4, 2),
    ITEM_HP_S_G(31, "HP小捆", ColorMappingEnum.PINK, 16,16, 2),
    ITEM_HP_M_S(32, "HP中单瓶", ColorMappingEnum.PINK, 7 ,7, 2),
    ITEM_HP_M_G(33, "HP中捆", ColorMappingEnum.PINK, 19, 19, 2),
    ITEM_HP_L_S(34, "HP大瓶", ColorMappingEnum.PINK, 10, 10, 2),
    ITEM_HP_L_G(35, "HP大捆", ColorMappingEnum.PINK, 21,21, 2),
    ITEM_HP_XL_S(36, "HP超大瓶", ColorMappingEnum.PINK, 13, 13, 2),
    ITEM_HP_XL_G(35, "HP超大捆", ColorMappingEnum.PINK, 24,24, 2),

    ITEM_MP_S_S(30, "MP小单瓶", ColorMappingEnum.BABY_BLUE, 4, 4, 2),
    ITEM_MP_S_G(31, "MP小捆", ColorMappingEnum.BABY_BLUE, 16,16, 2),
    ITEM_MP_M_S(32, "MP中单瓶", ColorMappingEnum.BABY_BLUE, 7 ,7, 2),
    ITEM_MP_M_G(33, "MP中捆", ColorMappingEnum.BABY_BLUE, 19, 19, 2),
    ITEM_MP_L_S(34, "MP大瓶", ColorMappingEnum.BABY_BLUE, 10, 10, 2),
    ITEM_MP_L_G(35, "MP大捆", ColorMappingEnum.BABY_BLUE, 21,21, 2),
    ITEM_MP_XL_S(36, "MP超大瓶", ColorMappingEnum.BABY_BLUE, 13, 13, 2),
    ITEM_MP_XL_G(35, "MP超大捆", ColorMappingEnum.BABY_BLUE, 24,24, 2),

    ITEM_TAI_YANG_SHUI_S(36, "太阳水小", ColorMappingEnum.GRAY_YELLOW, 8, 8, 2),
    ITEM_TAI_YANG_SHUI_L(37, "太阳水大", ColorMappingEnum.GRAY_YELLOW, 10, 10, 2),

    ITEM_VALUABLE_RING(38, "值钱戒指", ColorMappingEnum.NAVY_BLUE, 4, 4, 2),
    ITEM_VALUABLE_BRACELET(39, "值钱手镯", ColorMappingEnum.NAVY_BLUE, 7, 7, 2),
    ITEM_VALUABLE_NECKLACE(40, "值钱项链", ColorMappingEnum.NAVY_BLUE, 10, 10, 2),
    ITEM_VALUABLE_HELMET(41, "值钱头盔", ColorMappingEnum.NAVY_BLUE, 13, 13, 2),
    ITEM_VALUABLE_CLOTH(42, "值钱盔甲", ColorMappingEnum.NAVY_BLUE, 16, 16, 2),
    ITEM_VALUABLE_WEAPON(43, "值钱武器", ColorMappingEnum.NAVY_BLUE, 19, 19, 2),

    ITEM_WORTHLESS_RING(44, "不值钱戒指", ColorMappingEnum.ORANGE, 4, 4, 2),
    ITEM_WORTHLESS_BRACELET(39, "不值钱手镯", ColorMappingEnum.ORANGE, 7, 7, 2),
    ITEM_WORTHLESS_NECKLACE(40, "不值钱项链", ColorMappingEnum.ORANGE, 10, 10, 2),
    ITEM_WORTHLESS_HELMET(41, "不值钱头盔", ColorMappingEnum.ORANGE, 13, 13, 2),
    ITEM_WORTHLESS_CLOTH(42, "不值钱盔甲", ColorMappingEnum.ORANGE, 16, 16, 2),
    ITEM_WORTHLESS_WEAPON(43, "不值钱武器", ColorMappingEnum.ORANGE, 19, 19, 2),

    ITEM_VALUABLE_MINERAL(44, "值钱矿石", ColorMappingEnum.BLUE, 8, 8, 2),
    ITEM_WORTHLESS_MINERAL(45, "不值钱矿石", ColorMappingEnum.YELLOW, 8, 8, 2),

    ITEM_HUI_CHENG_S(46, "回城卷单个", ColorMappingEnum.LIME_GREEN, 8, 8, 2),
    ITEM_HUI_CHENG_G(47, "回城卷捆", ColorMappingEnum.LIME_GREEN, 12, 12, 2),
    ITEM_SUI_JI_S(46, "随机卷单个", ColorMappingEnum.DARK_GREEN, 8, 8, 2),
    ITEM_SUI_JI_G(47, "随机卷捆", ColorMappingEnum.DARK_GREEN, 12, 12, 2),
    ITEM_DI_LAO_S(46, "地牢卷单个", ColorMappingEnum.QING_BI_SE, 8, 8, 2),
    ITEM_DI_LAO_G(47, "地牢卷捆", ColorMappingEnum.QING_BI_SE, 12, 12, 2),
    ITEM_HANG_HUI_S(46, "行会卷单个", ColorMappingEnum.CYAN, 8, 8, 2),
    ITEM_HANG_HUI_G(47, "行会卷捆", ColorMappingEnum.CYAN, 12, 12, 2),

    ITEM_HIGH_VALUE(48, "高价值物品", ColorMappingEnum.DARK_RED, 8, 8, 2),
    ITEM_SKILL_BOOK(49, "技能书", ColorMappingEnum.MAGENTA, 8, 8, 2),
    ITEM_CHESTNUT(50, "栗子", ColorMappingEnum.WHITE, 8, 8, 2),
    ITEM_MATERIAL(51, "材料", ColorMappingEnum.GRAY, 8, 8, 2),
    ITEM_OTHER(52, "其它", ColorMappingEnum.WHITE, 12, 12, 2),

    SYSTEM_MONSTER_TAG(30, "系统小地图怪物标记", ColorMappingEnum.RED, 3, 3, 2),
    SYSTEM_GUARD_AND_NPC_TAG(31, "系统小地图NPC、大刀守卫标记", ColorMappingEnum.LIME_GREEN, 3, 3, 2);
    private int code;
    private String desc;
    private ColorMappingEnum color;
    private int tagWidth;
    private int tagHeight;
    private int type;//1:dnitem 2:item  0:monster or npc or human
    TagTypeEnum(int code, String desc, ColorMappingEnum color, int w, int h, int type){
        this.code = code;
        this.desc = desc;
        this.color = color;
        this.tagWidth = w;
        this.tagHeight = h;
        this.type = type;
    }

    public TagTypeEnum fromCode(int code){
        for(TagTypeEnum v: values()){
            if(code == v.code)
                return v;
        }
        return null;
    }

    public static List<TagTypeEnum> dnItemTags(){
        List<TagTypeEnum> dnItemTags = new ArrayList<>();
        for (TagTypeEnum v : values()) {
            if(v.getType() == 1)
                dnItemTags.add(v);
        }
        return dnItemTags;
    }

    public static List<TagTypeEnum> itemTags(){
        List<TagTypeEnum> itemTags = new ArrayList<>();
        for (TagTypeEnum v : values()) {
            if(v.getType() == 2) {
                itemTags.add(v);
            }
        }
        return itemTags;
    }

    public static List<TagTypeEnum> obstacleTags(){
        List<TagTypeEnum> obstacleTags = new ArrayList<>();
        for (TagTypeEnum v : values()) {
            if(v.getType() == 0){
                obstacleTags.add(v);
            }
        }
        return obstacleTags;
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

    public ColorMappingEnum getColor() {
        return color;
    }

    public void setColor(ColorMappingEnum color) {
        this.color = color;
    }

    public int getTagWidth() {
        return tagWidth;
    }

    public void setTagWidth(int tagWidth) {
        this.tagWidth = tagWidth;
    }

    public int getTagHeight() {
        return tagHeight;
    }

    public void setTagHeight(int tagHeight) {
        this.tagHeight = tagHeight;
    }

    public int getType() {
        return type;
    }
}
