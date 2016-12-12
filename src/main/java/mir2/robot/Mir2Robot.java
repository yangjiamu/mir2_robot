package mir2.robot;

import lombok.Getter;
import lombok.Setter;
import mir2.robot.navigation.Coordinate;
import mir2.robot.scan.*;
import mir2.robot.enums.DirectionEnum;
import mir2.robot.enums.RoleCurrentStatusEnum;
import mir2.robot.enums.TagTypeEnum;
import mir2.robot.navigation.CoordinateScreenGridConverter;
import mir2.robot.navigation.MapConnectionData;
import mir2.robot.navigation.Navigator;
import mir2.robot.screen.Mir2Screen;
import mir2.robot.screen.ScreenGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2016/11/5.
 */
@Getter
@Setter
public class Mir2Robot {
    private static final Robot2 robot = Robot2.getInstance();
    private static final Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private GameScreenScanner gameScreenScanner = new GameScreenScanner();
    private ScreenDetector screenDetector = new ScreenDetector();
    private Navigator navigator = Navigator.getInstance();
    private SmallMapScanner smallMapScanner = new SmallMapScanner();
    private TemplateMatch templateMatch = new TemplateMatch();
    private BagScanner bagScanner = new BagScanner();
    private String nickName;
    private String career;
    private Integer level;

    private Integer hp;
    private Integer mp;
    private volatile Integer currentHp;
    private volatile Integer currentMp;
    private Integer carryWeight;
    private Integer currentCarryWeight;
    private TagTypeEnum[][] bag;
    private TagTypeEnum[] smallBag;
    private volatile RoleCurrentStatusEnum currentStatus;
    private int safeDistance = 6;
    private static final Logger logger = LoggerFactory.getLogger(Mir2Robot.class);

    public Mir2Robot(){
        this("欧阳", 24, "Warrior");
    }

    public Mir2Robot(String nickName, int level, String career) {
        this.nickName = nickName;
        this.level = level;
        this.career = career;
        this.navigator = Navigator.getInstance();
        initRoleInfo();
    }

    private void initRoleInfo(){
        String careerInfoPath = this.getClass().getResource("/careerinfo/" + career + ".txt").getPath();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(careerInfoPath)));
            String line = null;
            while ((line = reader.readLine()) != null){
                if(line.startsWith("#"))
                    continue;
                String[] split = line.split(" ");
                if(Integer.parseInt(split[0]) == level){
                    this.hp = Integer.parseInt(split[1]);
                    this.currentHp = this.hp;
                    this.mp = Integer.parseInt(split[2]);
                    this.currentMp = this.mp;
                    this.carryWeight = Integer.parseInt(split[3]);
                    this.currentCarryWeight = this.carryWeight;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        if(openBag()){
            bag = bagScanner.recognizeItemInBag();
            smallBag = bagScanner.recognizeItemInSmallBag();
        }
        closeBag();
    }

    public boolean openBag(){
        clickBagOpenCloseButton();
        return bagScanner.isBagOpen();
    }

    public void pickItemsOnGround(){
        Map<TagTypeEnum, List<Coordinate>> dnItems = gameScreenScanner.scanDnItemsOnGround();
        /*拾取策略，DN_ITEM_HIGH_VALUE -> DN_ITEM_VALUABLE -> DN_ITEM_GOLD_COIN -> DN_ITEM_WORTHLESS -> DN_ITEM_DRUG -> DN_ITEM_CHESTNUT
        * -> DN_ITEM_DILAOHUICHENGSUIJI -> DN_ITEM_METERIAL
        * */
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_HIGH_VALUE)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_VALUABLE)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_GOLD_COIN)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_WORTHLESS)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_DRUG)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_CHESTNUT)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_DILAOHUICHENGSUIJI)) {
            pickAnItem(coordinate);
        }
        for (Coordinate coordinate : dnItems.get(TagTypeEnum.DN_ITEM_METERIAL)) {
            pickAnItem(coordinate);
        }
    }

    public void pickAnItem(Coordinate coordinate){
        if(coordinate == null){
            return;
        }
        walkTo(coordinate, 3);
        robot.delay(100);
        robot.pressKey(CharacterToKeycode.characterToKeyCode('\t'));
        robot.delay(100);
    }

    public boolean closeBag(){
        clickBagOpenCloseButton();
        return !bagScanner.isBagOpen();
    }

    private void clickStateInfoPlateOpenCloseButton(){
        clickOpenCloseButton(mir2Screen.stateInfoPlateOpenButtonPosition());
    }

    private void clickBagOpenCloseButton(){
        clickOpenCloseButton(mir2Screen.bagOpenButtonPosition());
    }

    public void clickSkillPlateOpenCloseButton(){
        clickOpenCloseButton(mir2Screen.skillInfoPlateOpenButtonPosition());
    }

    public void clickVoiceOpenCloseButton(){
        clickOpenCloseButton(mir2Screen.voiceOpenCloseButtonPosition());
    }

    private void clickOpenCloseButton(Point buttonPosition){
        robot.mouseMove(buttonPosition);
        robot.delay(200);
        robot.clickMouseLeftButton();
        robot.delay(200);
    }

    public void dropUnknowItem(){
        bag = bagScanner.recognizeItemInBag();
        for (int i = 0; i < bag.length; i++) {
            for (int j = 0; j < bag[0].length; j++) {
                if(bag[i][j] != null && bag[i][j].equals(TagTypeEnum.ITEM_OTHER)){
                    System.out.println("i:"+i+"j:"+j);
                    dropItemInGagGrid(bag, i, j);
                }
            }
        }
    }

    public void dropItemInGagGrid(TagTypeEnum[][] bag, int i, int j){
        if(bag[i][j] != null){
            dropSingleItemInBagGrid(i,j);
            bagScanner.updateBag(bag, i, j);
            if(bag[i][j] != null){
                dropMultiItemInBagGrid(i, j);
                bagScanner.updateBag(bag, i, j);
            }
        }
    }

    public void dropSingleItemInBagGrid(int i, int j){
        robot.mouseMove(bagScanner.getBagGridClickPosition(i,j));
        robot.delay(50);
        robot.clickMouseLeftButton();
        robot.mouseMove(mir2Screen.getOkButtonPosition());
        robot.delay(500);
        robot.clickMouseLeftButton();
        robot.delay(50);
        robot.clickMouseLeftButton();
        robot.delay(50);
    }

    public void dropMultiItemInBagGrid(int i, int j){
        robot.mouseMove(bagScanner.getBagGridClickPosition(i,j));
        robot.delay(50);
        robot.clickMouseLeftButton();
        robot.mouseMove(mir2Screen.getMaxButtonPosition());
        robot.delay(500);
        robot.clickMouseLeftButton();
        robot.delay(50);
        robot.clickMouseLeftButton();
        robot.delay(200);
        robot.mouseMove(mir2Screen.getOkButtonPosition());
        robot.delay(200);
        robot.clickMouseLeftButton();
        robot.delay(50);
    }

    public void faceTo(DirectionEnum direction){
        robot.clickMouseLeftButton(direction.getTurnAroundMousePoint());
    }

    public void walkByStep(DirectionEnum direction, int nStep){
        for (int i = 0; i < nStep; i++) {
            //logger.info("walkByStep direction{}", direction.getDesc());
            robot.mouseMove(direction.getTurnAroundMousePoint());
            robot.delay(100);
            robot.clickMouseLeftButton();
            robot.delay(900);
        }
        robot.delay(100);
    }

    public void walkByStepSafely(DirectionEnum direction, int nStep, TagTypeEnum tagToCheck){
        for (int i = 0; i < nStep; i++) {
            //safety check
            Coordinate current = navigator.currentCoordinate();
            Coordinate neighborCoordinate = Coordinate.neighbor(current, direction);
            ScreenGrid screenGrid = CoordinateScreenGridConverter.convertToScreenGrid(neighborCoordinate, current);
            BufferedImage screenGridImage = mir2Screen.getScreenGridImage(screenGrid);
            if(gameScreenScanner.screenGridHasTag(screenGridImage, tagToCheck)){
                return;
            }
            walkByStep(direction, 1);
        }
    }

    public void runInLineByTime(DirectionEnum direction, int millseconds){
        robot.mouseMove(direction.getRunMousePoint());
        robot.pressMouseRightButton();
        robot.delay(millseconds);
        robot.releaseMouseRightButton();
    }

    public void runInLineByDistance(DirectionEnum direction, int distance){
        if(distance < 2)
            return;
        int extraTime = 50;
        if(distance %2 == 1){
            walkByStep(direction, 1);
            --distance;
        }
        int mouseRightPressTime = (distance == 2 ? 100 : (600 + ((distance - 4)/2)*700));
        mouseRightPressTime += extraTime;
        System.out.println("distance:"+distance + " time:"+mouseRightPressTime);
        runInLineByTime(direction, mouseRightPressTime);
        robot.delay(200);
    }

    public boolean walkInLine(Coordinate destination){
        Coordinate currentPosition = navigator.currentCoordinate();
        //logger.info("walkInLine, current:{} destination:{}", currentPosition, destination);
        if (!Coordinate.isInLine(currentPosition, destination)) {
            logger.error("walkInLine, not in line, current:{}, destination:{}",
                    currentPosition, destination);
            return false;
        }
        DirectionEnum direction = Coordinate.directionBetween(currentPosition, destination);
        int distance = Coordinate.lineDistance(currentPosition, destination);
        if(distance < 0) {
            logger.error("distance negative");
            return false;
        }
        //logger.debug("walkInLine, distance1:{}", distance);
        if (distance == 1) {//walk mode
            walkByStepSafely(direction, distance, TagTypeEnum.GUARD_AND_ARCHER_TAG);
            currentPosition = navigator.currentCoordinate();
            if(currentPosition.equals(destination)){
                return true;
            }
            else {
                logger.error("walkInLine, can not reach destination, current:{} destination:{}",
                        currentPosition, destination);
                return false;
            }
        }
        else {//runInLineByDistance mode
            runInLineByDistance(direction, distance);
            currentPosition = navigator.currentCoordinate();
            if(currentPosition.equals(destination)){
                return true;
            }
            else {
                if(!direction.equals(Coordinate.directionBetween(currentPosition, destination))){
                    logger.error("walkInLine, direction changed during reaching destination, current:{} " +
                            "destination:{}", currentPosition, destination);
                    return false;
                }
                else {
                    distance = Coordinate.lineDistance(currentPosition, destination);
                    //logger.debug("walkInLine, distance2:{}", distance);
                    walkByStep(direction, distance);
                    currentPosition = navigator.currentCoordinate();
                    if(currentPosition.equals(destination)){
                        return true;
                    }
                    else {
                        logger.error("walkInLine, can not reach destination, current:{} destination:{}",
                                currentPosition, destination);
                        return false;
                    }
                }
            }
        }
    }

    public boolean walkInLineAndEnter(Coordinate destination, String nextMap){
        Coordinate currentPosition = navigator.currentCoordinate();
        //logger.info("walkInLine, current:{} destination:{}", currentPosition, destination);
        if (!Coordinate.isInLine(currentPosition, destination)) {
            logger.error("walkInLine, not in line, current:{}, destination:{}",
                    currentPosition, destination);
            return false;
        }
        DirectionEnum direction = Coordinate.directionBetween(currentPosition, destination);
        int distance = Coordinate.lineDistance(currentPosition, destination);
        if(distance < 0) {
            logger.error("distance negative");
            return false;
        }
        //logger.debug("walkInLine, distance1:{}", distance);
        if (distance == 1) {//walk mode
            walkByStepSafely(direction, distance, TagTypeEnum.GUARD_AND_ARCHER_TAG);
            if(navigator.isInMap(nextMap)){
                return true;
            }
            else {
                logger.error("walkInLine, can not reach destination, current:{} destination:{}",
                        navigator.currentCoordinate(), destination);
                return false;
            }
        }
        else {//runInLineByDistance mode
            runInLineByDistance(direction, distance);
            robot.delay(500);
            if(navigator.isInMap(nextMap)){
                return true;
            }
            else {
                currentPosition = navigator.currentCoordinate();
                if(!direction.equals(Coordinate.directionBetween(currentPosition, destination))){
                    logger.error("walkInLine, direction changed during reaching destination, current:{} " +
                            "destination:{}", currentPosition, destination);
                    return false;
                }
                else {
                    distance = Coordinate.lineDistance(currentPosition, destination);
                    //logger.debug("walkInLine, distance2:{}", distance);
                    walkByStep(direction, distance);

                    if(navigator.isInMap(nextMap)){
                        return true;
                    }
                    else {
                        logger.error("walkInLine, can not reach destination, current:{} destination:{}",
                                navigator.currentCoordinate(), destination);
                        return false;
                    }
                }
            }
        }
    }

    public boolean walkTo(Coordinate destination, int timesToTry){
        boolean reached = false;
        int i = 1;
        Coordinate current = navigator.currentCoordinate();
        while (!reached){
            logger.debug("walkTo, current:{}, destination:{}, try:{}", current, destination, i++);
            List<Coordinate> path = navigator.findPath(destination);
            if (path == null || path.isEmpty()){
                logger.error("walkTo, could not found path, current:{} destination:{}", current, destination);
                return false;
            }
            List<Coordinate> pathPivots = navigator.findPathPivots(path);
            for (int j = 0; j <pathPivots.size(); ++j) {
                if(!walkInLine(pathPivots.get(j))){
                    logger.info("walkTo, during walkInLine failed, current:{} destination:{}",
                            current, pathPivots.get(j));
                    break;
                }
            }
            current = navigator.currentCoordinate();
            reached = current.equals(destination);
            if((--timesToTry) == 0){
                break;
            }
        }
        return reached;
    }

    public boolean walkToAndEnter(MapConnectionData connectionData){
        while (!navigator.isInMap(connectionData.getNextMap())){
            List<Coordinate> path = navigator.findPath(connectionData.getEnterCoordinate());
            if(path == null || path.isEmpty()){
                logger.error("walkToAndEnter, could not found path, current:{} destination{}",
                        navigator.currentCoordinate(), connectionData.getEnterCoordinate());
                break;
            }
            List<Coordinate> pathPivots = navigator.findPathPivots(path);
            for (int j = 0; j <pathPivots.size()-1; ++j) {
                if(!walkInLine(pathPivots.get(j))){
                    logger.info("walkTo, during walkInLine failed, current:{} destination:{}",
                            navigator.currentCoordinate(), pathPivots.get(j));
                    break;
                }
            }
            if(walkInLineAndEnter(pathPivots.get(pathPivots.size()-1), connectionData.getNextMap())){
                return true;
            }
        }
        return navigator.isInMap(connectionData.getNextMap());
    }

    public boolean walkToMap(String map){
        while (!navigator.isInMap(map)){
            List<MapConnectionData> path = navigator.findConnectionPath(map);
            for (MapConnectionData mapConnectionData : path) {
                if(walkToAndEnter(mapConnectionData)){
                    navigator.setAgentCurrentMap(mapConnectionData.getNextMap());
                    logger.info("walkToMap enter{}", navigator.getAgentCurrentMap());
                }
                else {
                    break;
                }
            }
        }
        return navigator.isInMap(map);
    }

    public void killAroundMonstersInScreen(){//杀掉周围的怪物，dfs
        Coordinate current = navigator.currentCoordinate();
        Coordinate nearestMonsterInScreen = screenDetector.nearestMonsterInScreen(current);
        while (nearestMonsterInScreen != null){
            System.out.println(nearestMonsterInScreen);
            System.out.println(Coordinate.ChebyshevDirection(current, nearestMonsterInScreen));
            if(Coordinate.ChebyshevDistance(current, nearestMonsterInScreen) >= safeDistance){
                ScreenGrid screenGrid = CoordinateScreenGridConverter.convertToScreenGrid(nearestMonsterInScreen, current);
                robot.clickMouseLeftButton(screenGrid.getAbsoluteScreenGridClickPoint());
                robot.delay(1000*3);
            }
            else {
                ScreenGrid screenGrid = CoordinateScreenGridConverter.convertToScreenGrid(nearestMonsterInScreen, current);
                robot.clickMouseLeftButton(screenGrid.getAbsoluteScreenGridClickPoint());
                robot.delay(1000*3);
            }
            current = navigator.currentCoordinate();
            nearestMonsterInScreen = screenDetector.nearestMonsterInScreen(current);
        }
    }

    public void killAroundMonster(int maxDistance){
        killAroundMonstersInScreen();
        Coordinate current = navigator.currentCoordinate();
        Coordinate nearestMonsterInSmallMap = screenDetector.nearestMonsterInSmallMap(current);
        while (nearestMonsterInSmallMap != null){
            int distance = Coordinate.ChebyshevDistance(current, nearestMonsterInSmallMap);
            runInLineByDistance(Coordinate.ChebyshevDirection(current, nearestMonsterInSmallMap), distance/2);
            killAroundMonstersInScreen();
            current = navigator.currentCoordinate();
            nearestMonsterInSmallMap = screenDetector.nearestMonsterInSmallMap(current);
        }
    }

    public void stopMoving(){
        robot.mouseMove((int)mir2Screen.getAbsoluteStandPoint().getX(), (int)mir2Screen.getAbsoluteStandPoint().getY());
        robot.clickMouseLeftButton();
    }

    public String getMap(){
        return navigator.getAgentCurrentMap();
    }

    public Coordinate currentCoordinate() throws InterruptedException {
        return navigator.currentCoordinate();
    }
}
