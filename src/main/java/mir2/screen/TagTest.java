package mir2.screen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangwenjie on 16/11/1.
 */
public class TagTest {
    public static void main(String[] args) throws IOException {
        BufferedImage src = ImageIO.read(new File("/Users/yangwenjie/Documents/000000.bmp"));
        System.out.println("width: " + src.getWidth() + ",height: " + src.getHeight());
        int centerX = src.getWidth()/2;
        int centerY = src.getHeight()/2;

        drawCircle(src, centerX, centerY, 10, 0);

        ImageIO.write(src, "bmp", new File("/Users/yangwenjie/Documents/000000_cicle.bmp"));
    }

    public static void drawCircle(BufferedImage image, int x, int y, int r, int rgbColor){
        for(int i=x-r; i<x+r; ++i){
            for (int j = y-r; j < y + r; j++) {
                if(dis(i, j, x, y) <= r){
                    image.setRGB(i, j, Color.red.getRGB());
                }
            }
        }
    }

    public static float dis(int x1, int y1, int x2, int y2){
        return (float) Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
}
