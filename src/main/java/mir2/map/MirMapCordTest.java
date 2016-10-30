package mir2.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Created by yangwenjie on 16/10/27.
 */
public class MirMapCordTest {
    public static void main(String[] args) throws IOException {
        Map map = MapHolder.readMapFromFile("0");
        System.out.println("height: " + map.getHeight());
        System.out.println("width: " + map.getWidth());

        int curX=329,curY=266;
        System.out.println(map.getTiles()[curX][curY].isCanWalk());

        System.out.println(map.getTiles()[curX-1][curY].isCanWalk());
        System.out.println(map.getTiles()[curX+1][curY].isCanWalk());
        System.out.println(map.getTiles()[curX][curY-1].isCanWalk());
        System.out.println(map.getTiles()[curX][curY+1].isCanWalk());

        BufferedImage bufferedImage = new BufferedImage(map.getWidth(), map.getHeight(), TYPE_INT_RGB);
        MapTileInfo[][] tiles = map.getTiles();
        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                if (tiles[i][j].isCanWalk()) {
                    bufferedImage.setRGB(i, j, Color.green.getRGB());
                }
                else{
                    bufferedImage.setRGB(i, j, Color.red.getRGB());
                }
            }
        }
        ImageIO.write(bufferedImage, "png", new File("/Users/yangwenjie/Documents/0.png"));
    }
}
