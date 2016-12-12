import mir2.robot.scan.GameScreenScanner;
import mir2.robot.enums.TagTypeEnum;
import mir2.robot.navigation.Navigator;
import mir2.robot.navigation.Coordinate;
import mir2.robot.screen.ScreenGrid;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2016/11/12.
 */
public class GameScreenScannerTest {
    @Test
    public void testDetectHumCoordination() throws IOException, InterruptedException {
        GameScreenScanner gameScreenScanner = new GameScreenScanner();
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        List<Coordinate> coordinates = gameScreenScanner.detectAllHumCoordinate();
        for (Coordinate coordinate : coordinates) {
            System.out.println(coordinate);
        }
    }

    @Test
    public void testScanDnItems(){
        GameScreenScanner gameScreenScanner = new GameScreenScanner();
        long mark = System.currentTimeMillis();
        Map<TagTypeEnum, List<Coordinate>> tag2CoordinateList = gameScreenScanner.scanDnItemsOnGround();
        System.out.println(System.currentTimeMillis() - mark);
        for (Map.Entry<TagTypeEnum, List<Coordinate>> entry : tag2CoordinateList.entrySet()) {
            System.out.println(entry.getKey() + "   " + entry.getValue());
        }
    }
    @Test
    public void testDetectSmallMonsterCoordination() throws IOException, InterruptedException {
        Navigator.getInstance("比奇县");
        Navigator navigator =  Navigator.getInstance();
        GameScreenScanner gameScreenScanner = new GameScreenScanner();
        List<ScreenGrid> screenGrids = gameScreenScanner.detectNormalSmallMonster();
        List<Coordinate> coordinates = gameScreenScanner.detectSmallMonsterCoordinate();
        for (Coordinate coordinate : coordinates) {
            System.out.println(coordinate);
        }
        /*for (ScreenGrid screenGrid : screenGrids) {
            robot2.clickMouseLeftButton(screenGrid.getAbsoluteScreenGridClickPoint());
            robot2.delay(1000 * 5);
        }*/
    }

    @Test
    public void testDetectAllObstacles() throws IOException, InterruptedException {
        GameScreenScanner gameScreenScanner = new GameScreenScanner();
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        List<Coordinate> coordinates = gameScreenScanner.detectObstacles(navigator.currentCoordinate());
        for (Coordinate coordinate : coordinates) {
            System.out.println(coordinate);
        }
    }
    @Test
    public void testIsCoordinationReachable() throws IOException, InterruptedException {
        GameScreenScanner gameScreenScanner = new GameScreenScanner();
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        Coordinate coordinate = new Coordinate(365, 306);
        System.out.println(gameScreenScanner.isCoordinateHasNoObstacle(coordinate, navigator.currentCoordinate()));
    }
}
