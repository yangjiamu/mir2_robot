import mir2.clientinfo.map.Map;
import mir2.clientinfo.map.MapHolder;
import mir2.clientinfo.map.MapTileInfo;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/13.
 */
public class MapHolderTest {
    @Test
    public void testWalkRead() throws IOException {
        MapHolder mapHolder = new MapHolder();
        Map map = mapHolder.readMapFromFile("0");
        MapTileInfo[][] tiles = map.getTiles();
        BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if(!tiles[i][j].isCanWalk()){
                    image.setRGB(i, j, Color.RED.getRGB());
                }
                else {
                    image.setRGB(i, j, Color.GREEN.getRGB());
                }
            }
        }
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\test\\比奇.png"));
    }

    @Test
    public void testHasObjRead() throws IOException {
        MapHolder mapHolder = new MapHolder();
        Map map = mapHolder.readMapFromFile("0");
        MapTileInfo[][] tiles = map.getTiles();
        BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isHasObj()){
                    image.setRGB(i, j, Color.RED.getRGB());
                }
                else {
                    image.setRGB(i, j, Color.GREEN.getRGB());
                }
            }
        }
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\test\\比奇_object_after_modify.png"));
    }

    @Test
    public void testHasBgrRead() throws IOException {
        MapHolder mapHolder = new MapHolder();
        Map map = mapHolder.readMapFromFile("0");
        MapTileInfo[][] tiles = map.getTiles();
        BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if(tiles[i][j].isHasBng()){
                    image.setRGB(i, j, Color.RED.getRGB());
                }
                else {
                    image.setRGB(i, j, Color.GREEN.getRGB());
                }
            }
        }
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\test\\比奇_background.png"));
    }

    @Test
    public void modifyMapObjectLayer(){
        MapHolder mapHolder = new MapHolder();
        mapHolder.changeMapObjectLayerInfo(0);
    }
}
