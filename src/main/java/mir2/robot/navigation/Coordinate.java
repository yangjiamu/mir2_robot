package mir2.robot.navigation;

import lombok.Getter;
import lombok.Setter;
import mir2.robot.enums.DirectionEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangwenjie on 16/10/28.
 */
@Getter
@Setter
public class Coordinate {
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int x;
    public int y;

    @Override
    public String toString() {
        return "Coordinate [x=" + x +
                ", y=" + y + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    public static boolean isInLine(Coordinate coordinate1, Coordinate coordinate2){
        if(coordinate1.getX() == coordinate2.getX() || coordinate1.getY() == coordinate2.getY())
            return true;
        return Math.abs(coordinate1.getX() - coordinate2.getX()) == Math.abs(coordinate1.getY() - coordinate2.getY());
    }

    public static int lineDistance(Coordinate coordinate1, Coordinate coordinate2){
        if(!isInLine(coordinate1, coordinate2)){
            return -1;
        }
        if(coordinate1.getX() == coordinate2.getX()){
            return Math.abs(coordinate1.getY() - coordinate2.getY());
        }
        if(coordinate1.getY() == coordinate2.getY()){
            return Math.abs(coordinate1.getX() - coordinate2.getX());
        }
        int xDiff = Math.abs(coordinate1.getX() - coordinate2.getX());
        //int yDiff = Math.abs(coordinate1.getY() - coordinate2.getY());
        return xDiff;
    }

    public static int ChebyshevDistance(Coordinate coordinate1, Coordinate coordinate2){
        return Math.max(Math.abs(coordinate1.x - coordinate2.x), Math.abs(coordinate1.y - coordinate2.y));
    }

    public static DirectionEnum ChebyshevDirection(Coordinate from, Coordinate to){
        if(isInLine(from, to)){
            return directionBetween(from, to);
        }
        int xDif = to.x - from.x;
        int yDif = to.y - from.y;
        if(Math.abs(xDif) <= Math.abs(yDif)){
            return from.x < to.x ?  DirectionEnum.EAST : DirectionEnum.WEST;
        }
        else {
            return from.y < to.y ? DirectionEnum.SOUTH : DirectionEnum.NORTH;
        }
    }


    public static DirectionEnum directionBetween(Coordinate from, Coordinate to){
        if(from.getX() == to.getX()){
            return from.getY() < to.getY() ? DirectionEnum.SOUTH : DirectionEnum.NORTH;
        }
        if(from.getY() == to.getY()){
            return from.getX() < to.getX() ? DirectionEnum.EAST : DirectionEnum.WEST;
        }
        int xDiff = to.getX() - from.getX();
        int yDiff = to.getY() - from.getY();
        if(xDiff < 0){
            return  yDiff < 0 ? DirectionEnum.NORTH_WEST : DirectionEnum.SOUTH_WEST;
        }
        else if(xDiff > 0){
            return yDiff > 0 ? DirectionEnum.SOUTH_EAST : DirectionEnum.NORTH_EAST;
        }
        return DirectionEnum.STAND_POINT;
    }

    public static Map<Integer, List<Coordinate>> divide(Coordinate current, List<Coordinate> coordinates){
        Map<Integer, List<Coordinate>> result = new HashMap<>();
        List<Coordinate> leftTop = new ArrayList<>();
        List<Coordinate> leftBottom = new ArrayList<>();
        List<Coordinate> rightTop = new ArrayList<>();
        List<Coordinate> rightBottom = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            if(coordinate.x <= current.x){
                if(coordinate.y <= current.y){
                    leftTop.add(coordinate);
                }
                else {
                    leftBottom.add(coordinate);
                }
            }
            else {
                if(coordinate.y <= current.y){
                    rightTop.add(coordinate);
                }
                else {
                    rightBottom.add(coordinate);
                }
            }
        }
        result.put(1, leftTop);
        result.put(2, rightTop);
        result.put(3, leftBottom);
        result.put(4, rightBottom);
        return result;
    }
    public static Coordinate neighbor(Coordinate center, DirectionEnum directionEnum, int n){
        return new Coordinate(center.x + n*directionEnum.getX(), center.y + n*directionEnum.getY());
    }
    public static Coordinate neighbor(Coordinate center, DirectionEnum direction){
        return neighbor(center, direction, 1);
    }
}
