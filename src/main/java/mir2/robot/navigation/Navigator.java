package mir2.robot.navigation;

import com.google.common.collect.Maps;
import mir2.clientinfo.map.MapTileInfo;
import mir2.clientinfo.map.MapUtil;
import mir2.robot.scan.CoordinateDetector;
import mir2.robot.scan.MapNameDetector;
import mir2.robot.scan.GameScreenScanner;
import mir2.robot.scan.SmallMapScanner;
import mir2.robot.enums.DirectionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Created by yang on 2016/11/7.
 */
public class Navigator {
    private Map<String, MapTileInfo[][]> mapIdToTiles = new HashMap<>();
    private Map<String, Map<String, List<Coordinate>>> mapIdToNPCInfo = new HashMap<>();

    private Map<String, String> mapNameToMapId = Maps.newHashMap();
    private String agentCurrentMap;
    //detectors
    private MapNameDetector mapNameDetector = new MapNameDetector();
    private CoordinateDetector coordinateDetector = new CoordinateDetector();
    private GameScreenScanner gameScreenScanner = new GameScreenScanner();
    private SmallMapScanner smallMapScanner = new SmallMapScanner();

    private Logger logger = LoggerFactory.getLogger(Navigator.class);
    private static Navigator instance = null;

    public static synchronized void getInstance(String initMap){
        if(instance == null) {
            instance = new Navigator(initMap);
        }
        else {
            instance.setAgentCurrentMap(initMap);
        }
    }
    public static synchronized Navigator getInstance(){
        if(instance == null){
            instance = new Navigator("比奇县");
        }
        return instance;
    }

    private Navigator(String agentCurrentMap){
        this.agentCurrentMap = agentCurrentMap;
        String mapIdNameMappingFilePath = this.getClass().getResource("/MapIdNameMapping.txt").getPath();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(mapIdNameMappingFilePath)));
            String line = null;
            while ((line = reader.readLine()) != null){
                String[] split = line.split(" ");
                mapNameToMapId.put(split[1], split[0]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Coordinate> findPath(Coordinate destination){
        Coordinate start = coordinateDetector.detectCoordination(agentCurrentMap);
        MapTileInfo[][] mapTileInfos = getMapTiles(agentCurrentMap);
        List<Coordinate> allNpcCoordinates = getAllNPCCoordinations(agentCurrentMap);
        byte[][] map = MapUtil.convertTilesToByteMap(mapTileInfos);
        List<Coordinate> obstacleCoordinates = smallMapScanner.getObstacles(currentCoordinate());
        MapUtil.addMovableObstaclesToMap(map, obstacleCoordinates);
        MapUtil.addMovableObstaclesToMap(map, allNpcCoordinates);
        AStar aStar = new AStar(map);
        AStarNode endNode = aStar.findPath(start, destination);
        return endNode == null ? Collections.<Coordinate>emptyList() : aStar.buildForwardPath(endNode);
    }

    public List<MapConnectionData> findConnectionPath(String destinationMap){
        List<MapConnectionData> mapConnectionDataList = MapUtil.loadMapConnectionData();
        List<MapConnectionData> path = new ArrayList<>();
        Map<String, List<MapConnectionData>> map2NextMapList = new HashMap<>();

        for (MapConnectionData mapConnectionData : mapConnectionDataList) {
            if(map2NextMapList.containsKey(mapConnectionData.getMapName())){
                map2NextMapList.get(mapConnectionData.getMapName()).add(mapConnectionData);
            }
            else {
                List<MapConnectionData> list = new ArrayList<>();
                list.add(mapConnectionData);
                map2NextMapList.put(mapConnectionData.getMapName(), list);
            }
        }
        String map = agentCurrentMap;
        Map<String, Boolean> visited = new HashMap<>();
        for (String key : map2NextMapList.keySet()) {
            visited.put(key, Boolean.FALSE);
        }
        if(dfs(map2NextMapList, map, destinationMap, path, visited))
            return path;
        return null;
    }

    private boolean dfs(Map<String, List<MapConnectionData>> map2NextMapList, String currentMap, String destinationMap,
                        List<MapConnectionData> path, Map<String, Boolean> visited){
        visited.put(currentMap, Boolean.TRUE);
        return dfs(map2NextMapList, currentMap, destinationMap, map2NextMapList.get(currentMap), path, visited);
    }
    private boolean dfs(Map<String, List<MapConnectionData>> map2NextMapList, String currentMap,
                        String destinationMap, List<MapConnectionData> children, List<MapConnectionData> path, Map<String, Boolean> visited){
        for (MapConnectionData mapConnectionData : children) {
            String nextMap = mapConnectionData.getNextMap();
            if(nextMap.equals(destinationMap)){
                path.add(mapConnectionData);
                return true;
            }
            else if(map2NextMapList.containsKey(nextMap) && visited.get(nextMap)==false){
                path.add(mapConnectionData);
                visited.put(nextMap, true);
                if(dfs(map2NextMapList, nextMap, destinationMap, map2NextMapList.get(nextMap), path, visited)){
                    return true;
                }
                path.remove(mapConnectionData);
            }
        }
        return false;
    }

    public List<Coordinate> findPathPivots(List<Coordinate> path){
        if(path == null || path.isEmpty()){
            logger.error("NavigationService findPathPivots error, path is empty");
            return null;
        }
        List<Coordinate> pivots = new ArrayList<>();
        Coordinate start = path.get(0);
        path.remove(0);
        DirectionEnum preDirection = Coordinate.directionBetween(start, path.get(0));
        int distance = 0;
        for (int i = 1; i < path.size(); i++) {
            ++distance;
            DirectionEnum direction = Coordinate.directionBetween(path.get(i-1), path.get(i));
            if(!direction.equals(preDirection) || distance==12){
                pivots.add(path.get(i-1));
                preDirection = direction;
                distance = 0;
            }
        }
        pivots.add(path.get(path.size() -1));
        return pivots;
    }

    public boolean isInMap(String mapName){
        return mapNameDetector.isInMap(mapName);
    }

    public String getAgentCurrentMap(){
        return agentCurrentMap;
    }

    public void setAgentCurrentMap(String agentCurrentMap){
        this.agentCurrentMap = agentCurrentMap;
    }

    public Coordinate currentCoordinate(){
        return coordinateDetector.detectCoordination(agentCurrentMap);
    }

    public boolean isCoordinationReachable(Coordinate coordinate) throws IOException, InterruptedException {
        int x = coordinate.getX();
        int y = coordinate.getY();
        return getMapTiles(agentCurrentMap)[x][y].isCanWalk()
                && gameScreenScanner.isCoordinateHasNoObstacle(coordinate, currentCoordinate())
                && !getAllNPCCoordinations(agentCurrentMap).contains(coordinate);
    }

    public MapTileInfo[][] getMapTiles(String mapName){
        String mapId = mapNameToMapId.get(mapName);
        if(mapIdToTiles.get(mapId) == null){
            mapIdToTiles.put(mapId, MapUtil.readMapInfo(mapId).getTiles());
        }
        return mapIdToTiles.get(mapId);
    }

    public List<Coordinate> getAllNPCCoordinations(String mapName){
        String mapId = mapNameToMapId.get(mapName);
        if(mapIdToNPCInfo.get(mapId) == null){
            mapIdToNPCInfo.put(mapId, MapUtil.loadNPCInfo(mapId));
        }
        Map<String, List<Coordinate>> npcNameToCoordinations = mapIdToNPCInfo.get(mapId);
        if(npcNameToCoordinations == null || npcNameToCoordinations.isEmpty()){
            return Collections.emptyList();
        }
        List<Coordinate> allNPCCoordinates = new ArrayList<>();
        for (Map.Entry<String, List<Coordinate>> entry : npcNameToCoordinations.entrySet()) {
            allNPCCoordinates.addAll(entry.getValue());
        }
        return allNPCCoordinates;
    }
}
