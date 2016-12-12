import mir2.robot.navigation.MapConnectionData;
import mir2.robot.navigation.Coordinate;
import mir2.robot.navigation.Navigator;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by yang on 2016/11/8.
 */
public class NavigatorTest {
    @Test
    public void testFindPath() throws IOException, InterruptedException {
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        Coordinate current = new Coordinate(363, 285);
        Coordinate destination = new Coordinate(90, 165);
        List<Coordinate> path = navigator.findPath(destination);
        for (int i = 0; i < 5; i++) {
            Coordinate next = path.get(i);
            System.out.println(next.x + "    " + next.y);
        }
        System.out.println("---" + path.get(path.size()-1).x + "    " + path.get(path.size()-1).y);
    }

    @Test
    public void testFindConnectionPath(){
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        List<MapConnectionData> path = navigator.findConnectionPath("兽人古墓四层");
        if(path == null || path.isEmpty()){
            System.out.println("not found");
            return;
        }
        for (MapConnectionData mapConnectionData : path) {
            System.out.println(mapConnectionData);
        }
    }

    @Test
    public void testFindPathPivots() throws IOException, InterruptedException {
        Navigator.getInstance("比奇县");
        Navigator navigator = Navigator.getInstance();
        Coordinate start = new Coordinate(363, 285);
        Coordinate destination = new Coordinate(90, 165);
        List<Coordinate> path = navigator.findPath(destination);

        List<Coordinate> pathPivots = navigator.findPathPivots(path);
        for (int i = 1; i < pathPivots.size(); i++) {
            System.out.println(Coordinate.isInLine(pathPivots.get(i-1), pathPivots.get(i)));
        }
    }
}
