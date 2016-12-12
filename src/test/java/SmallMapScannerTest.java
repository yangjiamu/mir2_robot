import mir2.robot.scan.SmallMapScanner;
import mir2.robot.navigation.Navigator;
import mir2.robot.navigation.Coordinate;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by yang on 2016/11/14.
 */
public class SmallMapScannerTest {
    @Test
    public void testDetectGuard() throws InterruptedException, IOException {
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        SmallMapScanner smallMapScanner = new SmallMapScanner();
        List<Coordinate> coordinates = smallMapScanner.getObstacles(navigator.currentCoordinate());
        for (Coordinate coordinate : coordinates) {
            System.out.println(coordinate);
        }
    }


    @Test
    public void test() throws InterruptedException, IOException {
    }
}
