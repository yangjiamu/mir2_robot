package mir2.navigation;

import com.google.common.collect.Maps;
import mir2.DirectionEnum;
import mir2.map.MapHolder;
import mir2.map.MapTileInfo;
import mir2.robot.CoordinationDetection;
import mir2.robot.MapNameDetection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2016/11/7.
 */
public class NavigationService {
    private Map<Integer, MapTileInfo[][]> mapNumberToTiles = new HashMap<>();
    private MapNameDetection mapNameDetection = new MapNameDetection();
    private CoordinationDetection coordinationDetection = new CoordinationDetection();
    private Map<String, Integer> mapNameToMapNumber = Maps.newHashMap();
    private String currentMap;
    private AStar aStar = new AStar();
    private MapHolder mapHolder = new MapHolder();
    private Logger logger = LoggerFactory.getLogger(NavigationService.class);

    public NavigationService(String currentMap){
        this.currentMap = currentMap;
        String mapNoNameMappingFilePath = this.getClass().getResource("/MapNoNameMapping.txt").getPath();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(mapNoNameMappingFilePath)));
            String line = null;
            while ((line = reader.readLine()) != null){
                String[] split = line.split(" ");
                mapNameToMapNumber.put(split[1], Integer.parseInt(split[0]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<MapCoordination> findPath(MapCoordination start, MapCoordination destination){
        Integer mapNumber = mapNameToMapNumber.get(currentMap);
        if(mapNumberToTiles.get(mapNumber) == null){
            mapNumberToTiles.put(mapNumber, mapHolder.readMapFromFile(mapNumber).getTiles());
        }
        aStar.setTiles(mapNumberToTiles.get(mapNumber));
        AStarNode endNode = aStar.findPath(start, destination);
        return endNode == null ? null : aStar.buildPath(endNode);
    }

    public boolean findPath(String destinationMap){
        return true;
    }

    public List<MapCoordination> findPathPivots(MapCoordination start, List<MapCoordination> path){
        if(path == null || path.isEmpty()){
            logger.error("NavigationService findPathPivots error, path is empty");
            return null;
        }
        List<MapCoordination> pivots = new ArrayList<>();
        DirectionEnum preDirection = getDirection(start, path.get(0));
        for (int i = 1; i < path.size(); i++) {
            DirectionEnum direction = getDirection(path.get(i-1), path.get(i));
            if(!direction.equals(preDirection)){
                pivots.add(path.get(i-1));
                preDirection = direction;
            }
        }
        pivots.add(path.get(path.size() -1));
        return pivots;
    }

    public boolean checkIsInLine(MapCoordination coordination1, MapCoordination coordination2){
        if(coordination1.getX() == coordination2.getX() || coordination1.getY() == coordination2.getY())
            return true;
        return Math.abs(coordination1.getX() - coordination2.getX()) == Math.abs(coordination1.getY() - coordination2.getY());
    }

    public DirectionEnum getDirection(MapCoordination current, MapCoordination destination){
        if(current.getX() == destination.getX()){
            return current.getY() < destination.getY() ? DirectionEnum.SOUTH : DirectionEnum.NORTH;
        }
        if(current.getY() == destination.getY()){
            return current.getX() < destination.getX() ? DirectionEnum.EAST : DirectionEnum.WEST;
        }
        int xDiff = destination.getX() - current.getX();
        int yDiff = destination.getY() - current.getY();
        if(xDiff < 0){
            return  yDiff < 0 ? DirectionEnum.NORTH_WEST : DirectionEnum.SOUTH_WEST;
        }

        else if(xDiff > 0){
            return yDiff > 0 ? DirectionEnum.SOUTH_EAST : DirectionEnum.NORTH_EAST;
        }
        return DirectionEnum.STAND_POINT;
    }

    public int getLineDistance(MapCoordination coordination1, MapCoordination coordination2){
        if(coordination1.getX() == coordination2.getX()){
            return Math.abs(coordination1.getY() - coordination2.getY());
        }
        if(coordination1.getY() == coordination2.getY()){
            return Math.abs(coordination1.getX() - coordination2.getX());
        }
        int xDiff = Math.abs(coordination1.getX() - coordination2.getX());
        //int yDiff = Math.abs(coordination1.getY() - coordination2.getY());
        return xDiff;
    }

    public boolean isInMap(String mapName){
        return mapNameDetection.inInMap(mapName);
    }

    public String getCurrentMap(){
        return currentMap;
    }

    public boolean isInCoordination(MapCoordination coord){
        return coord.equals(coordinationDetection.detectCoordination(currentMap));
    }

    public MapCoordination getCurrentCoordination(){
        return coordinationDetection.detectCoordination(currentMap);
    }
}
