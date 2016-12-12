import mir2.clientinfo.map.MapTileInfo;
import mir2.clientinfo.map.MapUtil;
import mir2.robot.navigation.AStar;
import mir2.robot.navigation.AStarNode;
import mir2.robot.navigation.Coordinate;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by yang on 2016/11/17.
 */
public class AStarAndDStarTest {

    @Test
    public void aStarTest() throws IOException, InterruptedException {
        AStar aStar = new AStar();
        //AStar aStar = new AStar();
        MapTileInfo[][] tiles = MapUtil.readMapInfo("0").getTiles();
        byte[][] map = MapUtil.convertTilesToByteMap(tiles);
        aStar.setGridMap(map);
        long mark = System.currentTimeMillis();
        AStarNode path = aStar.findPath(new Coordinate(579, 585), new Coordinate(382, 317));
        System.out.println("---" + path);
        System.out.println("---" + path.parent);
        System.out.println(System.currentTimeMillis() - mark);
        List<Coordinate> coordinates = aStar.buildForwardPath(path);
        System.out.println("path length:"+ coordinates.size());
        BufferedImage image = new BufferedImage(700, 700, BufferedImage.TYPE_3BYTE_BGR);
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                boolean found = false;
                for (Coordinate coordinate : coordinates) {
                    if(i== coordinate.x && j== coordinate.y){
                        found = true;
                        break;
                    }
                }
                if(found){
                    image.setRGB(i, j, Color.GREEN.getRGB());
                }
                else {
                    image.setRGB(i, j, Color.RED.getRGB());
                }
            }
        }
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\astar_navigation.png"));
    }
    @Test
    public void timeCostCompare(){

    }
}
