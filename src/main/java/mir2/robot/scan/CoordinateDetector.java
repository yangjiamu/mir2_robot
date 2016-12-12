package mir2.robot.scan;

import mir2.robot.Robot2;
import mir2.robot.navigation.Coordinate;
import mir2.robot.screen.Mir2Screen;
import mir2.util.ImageUtil;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yang on 2016/11/8.
 */
public class CoordinateDetector {
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(CoordinateDetector.class);
    private Robot2 robot2 = Robot2.getInstance();
    Tesseract1 digitOCRInstance = new Tesseract1();
    TemplateMatch templateMatch = new TemplateMatch();
    Mat twoDot = null;
    public CoordinateDetector(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        digitOCRInstance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        digitOCRInstance.setLanguage("eng");
        digitOCRInstance.setTessVariable("tessedit_char_whitelist", "0123456789");
        String twoDotImagePath = this.getClass().getResource("/two_dot.png").getPath();
        twoDot = Imgcodecs.imread(twoDotImagePath.substring(1, twoDotImagePath.length()));
    }

    public Coordinate detectCoordination(String currentMap) {
        BufferedImage coordinationArea = mir2Screen.getCoordinationArea(currentMap);
        Point matchPoint = templateMatch.match(ImageUtil.bufferedImageToMat(coordinationArea), twoDot);
        //erase : between x and y
        for (int i = matchPoint.x; i <matchPoint.x+twoDot.width(); i++) {
            for (int j = matchPoint.y; j <matchPoint.y+twoDot.height() ; j++) {
                coordinationArea.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        ImageUtil.changeBackgroundToBlack(coordinationArea);
        String coordination = null;
        try {
            coordination = digitOCRInstance.doOCR(ImageUtil.scaleImage(coordinationArea, 2.0f));
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        String[] cords;
        if(coordination.trim().contains(" ")){
            cords = coordination.split(" ");
        }
        else if(coordination.trim().contains("\t")){
            cords = coordination.split("\t");
        }
        else {
            logger.error("coordination split error: " + coordination.trim());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return detectCoordination(currentMap);
        }
        //logger.debug("coordination recognized by ocr{}", coordination.trim());
        return new Coordinate(Integer.parseInt(cords[0].trim()), Integer.parseInt(cords[1].trim()));
    }
}
