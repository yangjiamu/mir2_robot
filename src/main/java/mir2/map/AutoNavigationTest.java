package mir2.map;

import mir2.navigation.AStar;
import mir2.navigation.AStarNode;
import mir2.navigation.MapCoordination;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by yangwenjie on 16/10/28.
 */
public class AutoNavigationTest {
    public static void main(String[] args) throws IOException {
        Map map = new MapHolder().readMapFromFile(0);
        System.out.println("height: " + map.getHeight());
        System.out.println("width: " + map.getWidth());

        MapTileInfo[][] tiles = map.getTiles();
        boolean[][] point = getMapCordination(map.getTiles());
        MapCoordination start = new MapCoordination(363, 285);
        MapCoordination end = new MapCoordination(90, 165);
        long mark = System.currentTimeMillis();
        AStarNode parent = new AStar(tiles).findPath(start, end);
        ArrayList<AStarNode> path = new ArrayList<>();
        while (parent != null) {
            // System.out.println(parent.x + ", " + parent.y);
            path.add(new AStarNode(parent.x, parent.y));
            parent = parent.parent;
        }
        System.out.println(System.currentTimeMillis() - mark);
        BufferedImage bufferedImage = new BufferedImage(map.getWidth(), map.getHeight(), TYPE_INT_RGB);
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (AStar.exists(path, i, j)) {
                    bufferedImage.setRGB(i, j, Color.green.getRGB());
                }
                else{
                    bufferedImage.setRGB(i, j, Color.red.getRGB());
                }
                if (AStar.exists(path, i, j)) {
                    bufferedImage.setRGB(i, j, Color.green.getRGB());
                }
            }
        }
        ImageIO.write(bufferedImage, "png", new File("/Users/yangwenjie/Documents/navigation_0.png"));
    }

    public static boolean[][] getMapCordination(MapTileInfo [][]mapTileInfos){
        int width = mapTileInfos.length;
        int height = mapTileInfos[0].length;
        boolean[][] coordination = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(mapTileInfos[i][j].isCanWalk()){
                    coordination[i][j] = false;
                }
                else {
                    coordination[i][j] = true;
                }
            }
        }
        return coordination;
    }
}
