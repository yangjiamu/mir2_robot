package mir2.clientinfo.map;

import mir2.robot.navigation.MapConnectionData;
import mir2.robot.navigation.Coordinate;
import mir2.util.Misc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yang on 2016/11/14.
 */
public class MapUtil {
    public static void main(String[] args) {
        String []mapIds = {"0", "1", "D001", "D002", "D003", "D011", "D012"};
        for (String mapId : mapIds) {
            modifyMapObjectLayerInfo(mapId);
        }
    }
    public static Map readMapInfo(String mapId) {
        Map res = new Map();
        try{
            String mapInfoPath = MapUtil.class.getResource("/Map/" + mapId + ".map").getPath();
            FileInputStream fis = new FileInputStream(new File(mapInfoPath));
            byte[] bytes = new byte[4];
            fis.read(bytes);
            res.setWidth(Misc.readShort(bytes, 0, true));
            res.setHeight(Misc.readShort(bytes, 2, true));
            fis.skip(48);
            MapTileInfo[][] mapTileInfos = new MapTileInfo[res.getWidth()][res.getHeight()];
            byte[] datas = new byte[12];
            for (int width = 0; width < res.getWidth(); ++width)
                for (int height = 0; height < res.getHeight(); ++height) {
                    fis.read(datas);
                    mapTileInfos[width][height] = Misc.readMapTileInfo(datas);
                    if (width % 2 != 0 || height % 2 != 0)
                        mapTileInfos[width][height].setHasBng(false);
                }
            res.setMapTiles(mapTileInfos);

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        return res;
    }

    public static List<MapConnectionData> loadMapConnectionData(){
        List<MapConnectionData> mapConnectionDataList = new ArrayList<>();
        String path = MapUtil.class.getResource("/MapConnect.txt").getPath();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line = null;
            while ((line = reader.readLine()) != null){
                if(line.equals("") || line.startsWith("#")){
                    continue;
                }
                String[] split = line.split(" ");
                MapConnectionData mapConnectionData = new MapConnectionData(split[0], split[1]);
                String[] coordinate = split[2].split(":");
                mapConnectionData.setEnterCoordinate(new Coordinate(Integer.parseInt(coordinate[0].trim()), Integer.parseInt(coordinate[1].trim())));
                coordinate = split[3].split(":");
                mapConnectionData.setCoordinateAfterEnter(new Coordinate(Integer.parseInt(coordinate[0].trim()), Integer.parseInt(coordinate[1].trim())));
                mapConnectionDataList.add(mapConnectionData);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapConnectionDataList;
    }

    public static Map modifyMapObjectLayerInfo(String mapId){
        try{
            //String mapInfoPath = this.getClass().getResource("/Map/" + String.valueOf(mapNo) + ".gridMap").getPath();
            String mapInfoPath = "C:\\Users\\yang\\Downloads\\Mir2online\\Mir2online\\Map\\" + mapId +".map";
            FileInputStream fis = new FileInputStream(new File(mapInfoPath));
            RandomAccessFile raf = new RandomAccessFile(new File(mapInfoPath), "rw");
            Map res = new Map();
            byte[] bytes = new byte[4];
            fis.read(bytes);
            res.setWidth(Misc.readShort(bytes, 0, true));
            res.setHeight(Misc.readShort(bytes, 2, true));
            fis.skip(48);
            raf.seek(4 + 48);
            int index = 4 + 48;
            MapTileInfo[][] mapTileInfos = new MapTileInfo[res.getWidth()][res.getHeight()];
            byte[] datas = new byte[12];
            for (int width = 0; width < res.getWidth(); ++width) {
                for (int height = 0; height < res.getHeight(); ++height) {
                    //fis.read(datas);
                    raf.seek(index);
                    raf.read(datas);
                    MapTileInfo mapTileInfo = new MapTileInfo();
                    mapTileInfo = Misc.readMapTileInfo(datas);
                    mapTileInfos[width][height] = mapTileInfo;
                    if (width % 2 != 0 || height % 2 != 0)
                        mapTileInfos[width][height].setHasBng(false);
                    if (mapTileInfo.isHasObj()) {
                        Misc.setShortZero(datas, 4);
                        raf.seek(index);
                        raf.write(datas);
                    }
                    index += 12;
                }
            }
            raf.close();
            res.setMapTiles(mapTileInfos);
            return res;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void exportMap(String mapId, String path){
        Map map = readMapInfo(mapId);
        MapTileInfo[][] tiles = map.getTiles();
        BufferedImage image = new BufferedImage(tiles.length, tiles[0].length, BufferedImage.TYPE_BYTE_INDEXED);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isCanWalk()){
                    image.setRGB(i, j, Color.GREEN.getRGB());
                }
                else{
                    image.setRGB(i, j, Color.RED.getRGB());
                }
            }
        }
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static java.util.Map<String, List<Coordinate>> loadNPCInfo(String mapId){
        java.util.Map<String, List<Coordinate>> npcNameToCoordinations = new HashMap<>();
        String mapInfoRoot = MapUtil.class.getResource("/Map").getPath();
        File file = new File(mapInfoRoot);
        File[] files = file.listFiles();
        boolean exist = false;
        for (File file1 : files) {
            if(file1.getName().equals(mapId + ".txt"))
                exist = true;
        }
        if(!exist){
            return null;
        }
        String mapInfoPath = MapUtil.class.getResource("/Map/" + mapId + ".txt").getPath();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(mapInfoPath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        try {
            while ((line = reader.readLine()) != null){
                if(line.startsWith("NPC")){
                    String[] split = line.split(" ");
                    if(split.length == 3){
                        Coordinate coordinate = new Coordinate(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                        if(npcNameToCoordinations.containsKey(split[0])){
                            npcNameToCoordinations.get(split[0]).add(coordinate);
                        }
                        else {
                            ArrayList<Coordinate> coordinates = new ArrayList<>();
                            coordinates.add(coordinate);
                            npcNameToCoordinations.put(split[0], coordinates);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return npcNameToCoordinations;
    }

    public static byte[][] convertTilesToByteMap(MapTileInfo[][] tiles){
        byte[][] grids = new byte[tiles.length][tiles[0].length];
        for (int i = 0; i <tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isCanWalk()){
                    grids[i][j] = 0;
                }
                else {
                    grids[i][j] = 1;
                }
            }
        }
        return grids;
    }

    public static void addMovableObstaclesToMap(byte[][] grids, List<Coordinate> obstacleCoordinates){
        for (Coordinate coordinate : obstacleCoordinates) {
            grids[coordinate.getX()][coordinate.getY()] = 2;
        }
    }
}
