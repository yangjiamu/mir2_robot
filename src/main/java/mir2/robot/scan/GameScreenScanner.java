package mir2.robot.scan;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import mir2.robot.Robot2;
import mir2.robot.enums.TagTypeEnum;
import mir2.robot.navigation.CoordinateScreenGridConverter;
import mir2.robot.navigation.Navigator;
import mir2.robot.navigation.Coordinate;
import mir2.robot.screen.Mir2Screen;
import mir2.robot.screen.ScreenGrid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2016/11/11.
 */
public class GameScreenScanner {
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private Robot2 robot2 = Robot2.getInstance();
    private static Navigator navigator = Navigator.getInstance();
    private TemplateMatch templateMatch = new TemplateMatch();
    private static final int widthPerGrid = ScreenGrid.getWidthPerGrid();
    private static final int heightPerGrid = ScreenGrid.getHeightPerGrid();
    private static final int numOfGridHorizontal = ScreenGrid.getNumOfGridHorizontal();
    private static final int numOfGridVertical = ScreenGrid.getNumOfGridVertical();
    private List<TagTypeEnum> dnItemTags = TagTypeEnum.dnItemTags();
    private static final int DN_ITEM_SCAN_AREA_START_GRIDX = 4;
    private static final int DN_ITEM_SCAN_AREA_END_GRIDX = 17;
    private static final int DN_ITEM_SCAN_AREA_START_GRIDY = 4;
    private static final int DN_ITEM_SCAN_AREA_END_GRIDY = 17;
    public GameScreenScanner(){

    }

    public Map<TagTypeEnum, List<Coordinate>> scanDnItemsOnGround(){
        BufferedImage gameScreenImage1 = mir2Screen.gameScreenImage();
        robot2.delay(1000);
        BufferedImage gameScreenImage2 = mir2Screen.gameScreenImage();
        Map<TagTypeEnum, List<Coordinate>> tag2CoordinateList = new HashMap<>();
        for (TagTypeEnum tag : dnItemTags) {
            List<ScreenGrid> screenGridList = detectTag(gameScreenImage1, tag,
                    DN_ITEM_SCAN_AREA_START_GRIDX, DN_ITEM_SCAN_AREA_END_GRIDX,
                    DN_ITEM_SCAN_AREA_START_GRIDY, DN_ITEM_SCAN_AREA_END_GRIDY);
            tag2CoordinateList.put(tag, CoordinateScreenGridConverter.convertToCoordinate(screenGridList, navigator.currentCoordinate()));
        }
        for (TagTypeEnum tag : dnItemTags) {
            List<ScreenGrid> screenGridList = detectTag(gameScreenImage2, tag,
                    DN_ITEM_SCAN_AREA_START_GRIDX, DN_ITEM_SCAN_AREA_END_GRIDX,
                    DN_ITEM_SCAN_AREA_START_GRIDY, DN_ITEM_SCAN_AREA_END_GRIDY);
            List<Coordinate> list = CoordinateScreenGridConverter.convertToCoordinate(screenGridList, navigator.currentCoordinate());
            if (tag2CoordinateList.containsKey(tag)) {
                List<Coordinate> scannedBefore = tag2CoordinateList.get(tag);
                for (Coordinate coordinate : list) {
                    if(!scannedBefore.contains(coordinate)){
                        scannedBefore.add(coordinate);
                    }
                }
            } else {
                tag2CoordinateList.put(tag, list);
            }
        }
        return tag2CoordinateList;
    }

    public List<Coordinate> detectAllHumCoordinate(){
        return detectAllHumCoordinate(mir2Screen.gameScreenImage());
    }

    public List<Coordinate> detectAllHumCoordinate(BufferedImage gameScreen){
        List<ScreenGrid> hairTagMatchPointGrids = detectTag(gameScreen, TagTypeEnum.HAIR_TAG);
        List<ScreenGrid> humanStandPointGrids = new ArrayList<>();
        humanStandPointGrids.addAll(Collections2.transform(hairTagMatchPointGrids, new Function<ScreenGrid, ScreenGrid>() {
            @Override
            public ScreenGrid apply(ScreenGrid grid) {
                return new ScreenGrid(grid.getX(), grid.getY() + 2);
            }
        }));
        return CoordinateScreenGridConverter.convertToCoordinate(humanStandPointGrids, navigator.currentCoordinate());
    }

    public List<Coordinate> detectSmallMonsterCoordinate(){
        return smallMonsterCoordination(mir2Screen.gameScreenImage());
    }

    public List<Coordinate> smallMonsterCoordination(BufferedImage gameScreen){
        List<ScreenGrid> monsterStandScreenGrids = detectNormalSmallMonster(gameScreen);
        return CoordinateScreenGridConverter.convertToCoordinate(monsterStandScreenGrids, navigator.currentCoordinate());
    }

    public List<ScreenGrid> detectNormalSmallMonster(){
        return detectNormalSmallMonster(mir2Screen.gameScreenImage());
    }

    public List<ScreenGrid> detectNormalSmallMonster(BufferedImage gameScreen){
        return detectTag(gameScreen, TagTypeEnum.NORMAL_SMALL_MONSTER);
    }

    public List<Coordinate> detectDaDaoAndGongJianShou(){
        BufferedImage gameScreen = mir2Screen.gameScreenImage();
        return detectDaDaoAndGongJianShou(gameScreen);
    }

    public List<Coordinate> detectDaDaoAndGongJianShou(BufferedImage gameScreen){
        List<ScreenGrid> matchScreenGrids = detectTag(gameScreen, TagTypeEnum.GUARD_AND_ARCHER_TAG);
        return CoordinateScreenGridConverter.convertToCoordinate(matchScreenGrids, navigator.currentCoordinate());
    }

    public List<Coordinate> detectTiger(BufferedImage gameScreen){
        List<ScreenGrid> matchScreenGrids = detectTag(gameScreen, TagTypeEnum.BOSS_TIGER_TAG);
        return CoordinateScreenGridConverter.convertToCoordinate(matchScreenGrids, navigator.currentCoordinate());
    }

    public List<ScreenGrid> detectTag(BufferedImage gameScreen, TagTypeEnum tag){
        return detectTag(gameScreen, tag, 0, numOfGridHorizontal, 0, numOfGridVertical-2);
    }

    public List<ScreenGrid> detectTag(BufferedImage gameScreen, TagTypeEnum tag,
                                      int startGridX, int endGridX, int startGridY, int endGridY){
        List<ScreenGrid> matchPointGrids = new ArrayList<>();
        for (int i = startGridX; i < endGridX; i++) {
            for (int j = startGridY; j < endGridY; j++) {
                try {
                    ImageIO.write(gameScreen.getSubimage(i*widthPerGrid, j*heightPerGrid, widthPerGrid, heightPerGrid),
                                "png", new File("C:\\Users\\yang\\Pictures\\aa\\tagDetect" + i + "_" + j + ".png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(templateMatch.tagMatch(gameScreen,
                        new Rectangle(i*widthPerGrid, j*heightPerGrid, widthPerGrid, heightPerGrid),
                        tag.getColor().getJavaColor(),
                        tag.getTagWidth(),
                        tag.getTagHeight())) {
                    matchPointGrids.add(new ScreenGrid(i+1, j+1));
                }
            }
        }
        return matchPointGrids;
    }

    public boolean screenGridHasTag(BufferedImage screenGridImage, TagTypeEnum tag){
        return templateMatch.tagMatch(screenGridImage,
                new Rectangle(0, 0, screenGridImage.getWidth(), screenGridImage.getHeight()),
                tag.getColor().getJavaColor(), tag.getTagWidth()-1, tag.getTagHeight()-1);
    }

    public List<Coordinate> detectObstacles(Coordinate currentCoordinate){
        List<Coordinate> all = new ArrayList<>();
        BufferedImage gameScreen = mir2Screen.gameScreenImage();
        all.addAll(detectAllHumCoordinate(gameScreen));
        all.addAll(smallMonsterCoordination(gameScreen));
        all.addAll(detectDaDaoAndGongJianShou(gameScreen));
        return all;
    }

    public boolean isCoordinateHasNoObstacle(Coordinate coordinate, Coordinate current){
        ScreenGrid screenGrid = CoordinateScreenGridConverter.convertToScreenGrid(coordinate, current);
        System.out.println(screenGrid);
        if(!ScreenGrid.isValidScreenGrid(screenGrid)){//假设屏幕外面都没有活动障碍物
            return true;
        }
        ScreenGrid maybeHairTagScreenGrid = new ScreenGrid(screenGrid.getX(), screenGrid.getY() - 2);
        System.out.println(maybeHairTagScreenGrid);
        if(!ScreenGrid.isValidScreenGrid(maybeHairTagScreenGrid)){//无法检测屏幕顶部的半个角色
            return isScreenGridClear(screenGrid);
        }
        else {
            return isScreenGridClear(screenGrid) &&
                    isScreenGridClear(maybeHairTagScreenGrid);
        }
    }

    public boolean isScreenGridClear(ScreenGrid screenGrid){
        BufferedImage screenGridImage = mir2Screen.getScreenGridImage(screenGrid);
        /*ImageIO.write(screenGridImage, "png", new File("C:\\Users\\yang\\Pictures\\aa\\isReach" +
                screenGrid.getX() + "_" + screenGrid.getY() + ".png"));*/
        for (TagTypeEnum v : TagTypeEnum.obstacleTags()) {
            if(templateMatch.tagMatch(screenGridImage, v)){
                return false;
            }
        }
        return true;
    }
}
