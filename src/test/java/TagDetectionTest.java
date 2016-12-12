import mir2.robot.navigation.Coordinate;
import mir2.robot.scan.GameScreenScanner;
import mir2.robot.screen.Mir2Screen;
import mir2.robot.scan.TemplateMatch;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_3BYTE_BGR;

/**
 * Created by yang on 2016/11/11.
 */
public class TagDetectionTest {//48 * 32     21*17
    @Test
    public void testHairTagMatch() throws IOException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        BufferedImage gameScreen = mir2Screen.gameScreenImage();
        int widthPerGrid = 48;
        int heightPerGrid = 32;
        int numOfGridHorzital = 21;
        int numOfGridVertical = 24;
        System.out.println(gameScreen.getWidth() + ":" + gameScreen.getHeight());
        List<BufferedImage> grids = new ArrayList<>();
        TemplateMatch templateMatch = new TemplateMatch();
        BufferedImage hairTag = new BufferedImage(8, 6, TYPE_3BYTE_BGR);
        for (int i = 0; i < hairTag.getWidth(); i++) {
            for (int j = 0; j < hairTag.getHeight(); j++) {
                hairTag.setRGB(i, j, Color.BLUE.getRGB());
            }
        }
        ImageIO.write(hairTag, "png", new File("C:\\Users\\yang\\Pictures\\hairtag.png"));
        for (int i = 0; i < numOfGridHorzital; i++) {
            for (int j = 0; j < numOfGridVertical; j++) {
                System.out.println("i:" + i + " j:" + j);
                BufferedImage subimage = gameScreen.getSubimage(i * widthPerGrid, j*heightPerGrid, widthPerGrid, heightPerGrid);
                /*Mat srcMat = ImageUtil.bufferedImageToMat(subimage);
                Mat tagMat = ImageUtil.bufferedImageToMat(hairTag);
                Coordinate match = templateMatch.match(srcMat, tagMat);
                Imgcodecs.imwrite("C:\\Users\\yang\\Pictures\\aa\\s" + i + "_" + j + ".png", srcMat);
                //ImageIO.write(subimage, "png", new File("C:\\Users\\yang\\Pictures\\aa\\s" + i + "_" + j + ".png"));
                System.out.println("(i j):" + i + " " + j + "   " + match.getX() + ":" + match.getY());*/
                if(templateMatch.hairTagMatch(subimage)){
                    ImageIO.write(subimage, "png", new File("C:\\Users\\yang\\Pictures\\aa\\hair_found_" +i + "_" + j+ " .png"));
                }
            }
        }
    }
    @Test
    public void testSmallMonsterTag() throws IOException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        BufferedImage gameScreen = mir2Screen.gameScreenImage();
        int widthPerGrid = 48;
        int heightPerGrid = 32;
        int numOfGridHorzital = 21;
        int numOfGridVertical = 24;
        System.out.println(gameScreen.getWidth() + ":" + gameScreen.getHeight());
        TemplateMatch templateMatch = new TemplateMatch();
        BufferedImage hairTag = new BufferedImage(8, 6, TYPE_3BYTE_BGR);
        for (int i = 0; i < hairTag.getWidth(); i++) {
            for (int j = 0; j < hairTag.getHeight(); j++) {
                hairTag.setRGB(i, j, Color.YELLOW.getRGB());
            }
        }
        ImageIO.write(gameScreen, "png", new File("C:\\Users\\yang\\Pictures\\aa\\gameScreenForMonsterFound.png"));
        for (int i = 0; i < numOfGridHorzital; i++) {
            for (int j = 0; j < numOfGridVertical; j++) {
                System.out.println("i:" + i + " j:" + j);
                BufferedImage subimage = gameScreen.getSubimage(i * widthPerGrid, j*heightPerGrid, widthPerGrid, heightPerGrid);
                if(templateMatch.tagMatch(gameScreen, new Rectangle(i*widthPerGrid, j*heightPerGrid, widthPerGrid, heightPerGrid),
                        Color.YELLOW, 10, 10)){
                    ImageIO.write(subimage, "png", new File("C:\\Users\\yang\\Pictures\\aa\\monster_found_" +i + "_" + j+ " .png"));
                    System.out.println("i:" + i + " j:" + j);
                }
            }
        }
    }

    @Test
    public void testDetectHum() throws IOException {
        GameScreenScanner gameScreenScanner = new GameScreenScanner();
        List<Coordinate> mapCoordinates = gameScreenScanner.detectAllHumCoordinate();

    }
}
