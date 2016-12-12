import mir2.robot.CharacterToKeycode;
import mir2.robot.Robot2;
import mir2.robot.scan.CoordinationDetectorRunnable;
import mir2.robot.enums.DirectionEnum;
import mir2.robot.navigation.Navigator;
import mir2.robot.navigation.Coordinate;
import mir2.robot.Mir2Robot;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by yang on 2016/11/8.
 */
public class Mir2RobotTest {
    @Test
    public void testRunSpeed() throws InterruptedException {
        Mir2Robot mir2Robot = new Mir2Robot();
        Coordinate start = mir2Robot.currentCoordinate();
        long mark = System.currentTimeMillis();
        mir2Robot.runInLineByTime(DirectionEnum.NORTH, 6100);
        System.out.println("cost:" + (System.currentTimeMillis() - mark));
        Thread.sleep(1000);
        System.out.println("distance: " + Coordinate.isInLine(start, mir2Robot.currentCoordinate()));
    }

    @Test
    public void testWalkTo() throws InterruptedException, IOException {
        Robot2 robot2 = Robot2.getInstance();
        Coordinate destination = new Coordinate(382, 317);
        Mir2Robot mir2Robot = new Mir2Robot();
        Thread.sleep(1000);
        for (int i = 0; i < 1; i++) {
            robot2.pressKey(CharacterToKeycode.characterToKeyCode('1'));
            Thread.sleep(1000);
            if(mir2Robot.walkTo(destination, Integer.MAX_VALUE)){
                System.out.println("*****************************success");
            }
            else {
                System.out.println("******************************failed");
            }
            System.out.println(mir2Robot.currentCoordinate());
        }
    }

    @Test
    public void testPickAnItem(){
        Coordinate coordinate = new Coordinate(396, 345);
        Mir2Robot mir2Robot = new Mir2Robot();
        mir2Robot.pickAnItem(coordinate);
    }

    @Test
    public void testPressKey(){
        Mir2Robot mir2Robot = new Mir2Robot();
        Robot2 robot2 =  Robot2.getInstance();
        robot2.pressKey(CharacterToKeycode.characterToKeyCode('\t'));
    }

    @Test
    public void testPickDnItems(){
        Mir2Robot mir2Robot = new Mir2Robot();
        mir2Robot.pickItemsOnGround();
    }

    @Test
    public void testWalkToMap(){
        Navigator.getInstance("比奇县");
        Mir2Robot mir2Robot = new Mir2Robot("欧阳", 24, "Warrior");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mir2Robot.walkToMap("兽人古墓一层");
    }

    @Test
    public void testDropUnknowItem(){

    }

    @Test
    public void testKillAroundMonsterInScreen() throws InterruptedException {
        Mir2Robot mir2Robot = new Mir2Robot();
        Thread.sleep(1000);
        mir2Robot.killAroundMonstersInScreen();
    }

    @Test
    public void testKillAroundMonster() throws InterruptedException {
        Navigator.getInstance("兽人古墓一层");
        Mir2Robot mir2Robot = new Mir2Robot("欧阳", 24, "Warrior");

        Thread.sleep(1000);
        mir2Robot.killAroundMonster(20);

    }

    @Test
    public void testWalkByStep() throws InterruptedException {
        Mir2Robot mir2Robot = new Mir2Robot();
        //new Thread(new CoordinationDetectorRunnable(roleAgent)).start();
        Thread.sleep(4000);
        mir2Robot.walkByStep(DirectionEnum.NORTH, 1);
        mir2Robot.walkByStep(DirectionEnum.NORTH, 2);
        mir2Robot.walkByStep(DirectionEnum.NORTH, 3);
    }

    @Test
    public void testWalkByLine() throws InterruptedException {
        Mir2Robot mir2Robot = new Mir2Robot();
        new Thread(new CoordinationDetectorRunnable(mir2Robot)).start();
        Thread.sleep(4000);
        mir2Robot.walkInLine(new Coordinate(350, 285));
        mir2Robot.walkInLine(new Coordinate(350, 288));
        mir2Robot.walkInLine(new Coordinate(350, 293));

    }

    @Test
    public void testRobot2() throws InterruptedException {
        Thread.sleep(3000);
        Robot2 robot2 = Robot2.getInstance();
        for (int i = 0; i < 1000; i++) {
            robot2.mouseMove(i, 0);
            robot2.clickMouseLeftButton();
            Thread.sleep(100);
        }

    }
}
