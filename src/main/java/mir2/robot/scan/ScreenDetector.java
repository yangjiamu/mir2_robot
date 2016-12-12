package mir2.robot.scan;

import mir2.robot.navigation.Navigator;
import mir2.robot.navigation.Coordinate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yang on 2016/11/19.
 */
public class ScreenDetector {
    private SmallMapScanner smallMapScanner = new SmallMapScanner();
    private GameScreenScanner gameScreenScanner = new GameScreenScanner();

    public Coordinate nearestMonsterInScreen(final Coordinate current){
        Navigator.getInstance("比奇县");
        List<Coordinate> coordinates = gameScreenScanner.detectSmallMonsterCoordinate();
        sortByDistance(coordinates, current);
        if(!coordinates.isEmpty()){
            return coordinates.get(0);
        }
        return null;
    }

    public Coordinate nearestMonsterInSmallMap(Coordinate current){
        List<Coordinate> coordinates = smallMapScanner.getMonsterAndArcherCoordinate();
        sortByDistance(coordinates, current);
        if(!coordinates.isEmpty()){
            return coordinates.get(0);
        }
        return null;
    }

    private void sortByDistance(List<Coordinate> coordinates, final Coordinate current){
        Collections.sort(coordinates, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate o1, Coordinate o2) {
                return Coordinate.ChebyshevDistance(current, o1) - Coordinate.ChebyshevDistance(current, o2);
            }
        });
    }
}
