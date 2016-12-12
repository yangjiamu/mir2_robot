package mir2.robot.navigation;

import mir2.robot.screen.ScreenGrid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/11/12.
 */
public class CoordinateScreenGridConverter {
    private static ScreenGrid standPointGrid = new ScreenGrid(11, 11);
    public static Coordinate convertToCoordinate(ScreenGrid grid, Coordinate currentCoordinate){
        return new Coordinate(currentCoordinate.getX() + (grid.getX() - standPointGrid.getX()),
                currentCoordinate.getY() + (grid.getY() - standPointGrid.getY()));
    }

    public static List<Coordinate> convertToCoordinate(List<ScreenGrid> grids, Coordinate current){
        List<Coordinate> coordinates = new ArrayList<>();
        for (ScreenGrid grid : grids) {
            coordinates.add(convertToCoordinate(grid, current));
        }
        return coordinates;
    }

    public static ScreenGrid convertToScreenGrid(Coordinate coordinate, Coordinate current){
        return new ScreenGrid(standPointGrid.getX() + (coordinate.getX() - current.getX()),
                standPointGrid.getY() + (coordinate.getY() - current.getY()));
    }

    public static List<ScreenGrid> convertToScreenGrid(List<Coordinate> coordinates, Coordinate current){
        List<ScreenGrid> screenGrids = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            screenGrids.add(convertToScreenGrid(coordinate, current));
        }
        return screenGrids;
    }
    public static void main(String[] args) {
        Coordinate current = new Coordinate(327, 326);
        ScreenGrid screenGrid = new ScreenGrid(9, 9);
        Coordinate coordinate = convertToCoordinate(screenGrid, current);
        System.out.println(coordinate);
        ScreenGrid screenGrid1 = convertToScreenGrid(coordinate, current);
        System.out.println(screenGrid.equals(screenGrid1));
    }
}
