package mir2.role;

import lombok.Getter;
import lombok.Setter;
import mir2.DirectionEnum;
import mir2.navigation.MapCoordination;
import mir2.navigation.NavigationService;
import mir2.robot.Robot2;
import mir2.robot.RoleCurrentStatusEnum;
import mir2.screen.Mir2Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by yang on 2016/11/5.
 */
@Getter
@Setter
public class GameRole {
    private static final Robot2 robot = Robot2.getInstance();
    private static final Mir2Screen mir2Screen = Mir2Screen.getInstance();

    private NavigationService navigationService;

    private String nickName;
    private String career;
    private Integer level;
    private volatile Integer currentHp;
    private Integer hp;
    private volatile Integer currentMp;
    private Integer mp;
    private Integer currentCarryWeight;
    private Integer carryWeight;
    private volatile RoleCurrentStatusEnum currentStatus;
    private static final Logger logger = LoggerFactory.getLogger(GameRole.class);

    public GameRole(){
        this("欧阳", 24, "Warrior", new NavigationService("比奇县"));
    }

    public GameRole(String nickName, int level, String career, NavigationService navigationService) {
        this.nickName = nickName;
        this.level = level;
        this.career = career;
        this.navigationService = navigationService;
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
    }

    public boolean walkByLine(MapCoordination destination) throws InterruptedException {
        MapCoordination currentPosition = navigationService.getCurrentCoordination();
        logger.debug("walkByLine, current:{} destination:{}", currentPosition, destination);
        if (!navigationService.checkIsInLine(currentPosition, destination)) {
            logger.error("walkByLine, not in line, current:{}, destination:{}",
                    currentPosition, destination);
            return false;
        }
        DirectionEnum direction = navigationService.getDirection(currentPosition, destination);
        int distance = navigationService.getLineDistance(currentPosition, destination);
        logger.debug("walkByLine, distance1:{}", distance);
        if (distance < 3) {//walk mode
            walkByStep(direction, distance);
            currentPosition = navigationService.getCurrentCoordination();
            if(currentPosition.equals(destination)){
                return true;
            }
            else {
                //collision check
                logger.error("walkByLine, can not reach destination, current:{} destination:{}",
                        currentPosition, destination);
                return false;
            }
        }
        else {//runInLineByDistance mode
            runInLineByDistance(direction, distance);
            currentPosition = navigationService.getCurrentCoordination();
            if(currentPosition.equals(destination)){
                return true;
            }
            else {
                if(!direction.equals(navigationService.getDirection(currentPosition, destination))){
                    logger.error("walkByLine, direction changed during reaching destination, current:{} " +
                            "destination:{}", currentPosition, destination);
                    return false;
                }
                else {
                    distance = navigationService.getLineDistance(currentPosition, destination);
                    logger.debug("walkByLine, distance2:{}", distance);
                    walkByStep(direction, distance);
                    currentPosition = navigationService.getCurrentCoordination();
                    if(currentPosition.equals(destination)){
                        return true;
                    }
                    else {
                        logger.error("walkByLine, can not reach destination, current:{} destination:{}",
                                currentPosition, destination);
                        return false;
                    }
                }
            }
        }
    }
    public boolean walkTo(MapCoordination destination) throws InterruptedException {
        boolean reached = false;
        for(int i=0; i<1 && !reached; ++i){
            MapCoordination currentPosition = navigationService.getCurrentCoordination();
            logger.debug("walkTo, current:{}, destination:{}, try:{}", currentPosition, destination);
            List<MapCoordination> path = navigationService.findPath(currentPosition, destination);
            if (path == null || path.isEmpty()){
                logger.error("walkTo, could not found path, current:{} destination:{}", currentPosition, destination);
                return false;
            }
            for (int j = 0; j < path.size(); ++j){
                logger.debug("path" + j + " " + path.get(j));
            }
            List<MapCoordination> pathPivots = navigationService.findPathPivots(currentPosition, path);
            for (int j = 0; j < pathPivots.size(); ++j) {
                logger.debug("pivot" + j + " " + pathPivots.get(j));
            }
            int j;
            for (j = 0; j <pathPivots.size(); ++j) {
                if(!walkByLine(pathPivots.get(j))){
                    logger.error("walkTo, walkByLine failed, current:{} destination:{}",
                            navigationService.getCurrentCoordination(), pathPivots.get(j));
                    break;
                }
            }
            if(navigationService.isInCoordination(destination))
                reached = true;
        }
        return reached;
    }

    public boolean walkToMap(String mapName){
        //walk to destination
        //...
        //reset current map
        return true;
    }

    public void walkByStep(DirectionEnum direction, int nStep){
        //safety check
        for (int i = 0; i < nStep; i++) {
            logger.info("walkByStep direction{}", direction.getDesc());
            robot.mouseMove(direction.getTurnAroundMousePoint());
            robot.delay(100);
            robot.clickMouseLeftButton();
            robot.delay(900);
        }
        robot.delay(100);
    }

    public void faceTo(DirectionEnum direction){
        robot.clickMouseLeftButton(direction.getTurnAroundMousePoint());
    }

    public void stopMoving(){
        robot.mouseMove((int)mir2Screen.getAbsoluteStandPoint().getX(), (int)mir2Screen.getAbsoluteStandPoint().getY());
        robot.clickMouseLeftButton();
    }

    public void runInLineByDistance(DirectionEnum direction, int distance){
        int extraTime = 50;
        if(distance %2 == 1){
            walkByStep(direction, 1);
            --distance;
        }
        int mouseRightPressTime = (distance == 2 ? 100 : (600 + ((distance - 4)/2)*700));
        mouseRightPressTime += extraTime;
        runInLineByTime(direction, mouseRightPressTime);
        robot.delay(200);
        /*robot.mouseMove(direction.getRunMousePoint());
        robot.delay(100);
        robot.pressMouseRightButton();
        robot.delay(distance * 200);//300 mill seconds per step
        robot.releaseMouseRightButton();
        robot.delay(200);*/
    }

    public void runInLineByTime(DirectionEnum direction, int millseconds){
        robot.mouseMove(direction.getRunMousePoint());
        robot.pressMouseRightButton();
        robot.delay(millseconds);
        robot.releaseMouseRightButton();
    }

    public String getMap(){
        return navigationService.getCurrentMap();
    }

    public MapCoordination getCoordination(){
        return navigationService.getCurrentCoordination();
    }
}
