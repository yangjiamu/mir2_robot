package mir2.robot.scan;

import mir2.robot.Mir2Robot;
import mir2.robot.screen.Mir2Screen;
import mir2.util.ImageUtil;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by yang on 2016/11/6.
 */
public class CoordinationDetectorRunnable implements Runnable{
    private Mir2Robot mir2Robot;
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(CoordinationDetectorRunnable.class);

    public CoordinationDetectorRunnable(Mir2Robot mir2Robot){
        this.mir2Robot = mir2Robot;
    }
    @Override
    public void run() {
        Tesseract1 digitOCRInstance = new Tesseract1();
        digitOCRInstance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        digitOCRInstance.setLanguage("eng");
        digitOCRInstance.setTessVariable("tessedit_char_whitelist", "0123456789");
        TemplateMatch templateMatch = new TemplateMatch();
        String twoDotImagePath = this.getClass().getResource("/two_dot.png").getPath();
        Mat twoDot = Imgcodecs.imread(twoDotImagePath.substring(1, twoDotImagePath.length()));
        //Mat twoDot = Imgcodecs.imread("C:\\Users\\yang\\Pictures\\two_dot.png");
        while (true){
            BufferedImage coordinationArea = mir2Screen.getCoordinationArea(mir2Robot.getMap());
            Point matchPoint = templateMatch.match(ImageUtil.bufferedImageToMat(coordinationArea), twoDot);
            //erase : between x and y
            for (int i = matchPoint.x; i <matchPoint.x+twoDot.width(); i++) {
                for (int j = matchPoint.y; j <matchPoint.y+twoDot.height() ; j++) {
                    coordinationArea.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
            ImageUtil.changeBackgroundToBlack(coordinationArea);
            String coordination = null;
            //1.8 1.9 pretty ok bu can mistake 3 and 8
            //2.0 ok bu transfer : to 1 sometimes
            //2.1 very good but sometimes can not seperate x and y
            //2.2 can not seperate x and y
            //2.3 - 2.5 becomes bad again
            //2.6 mistake 3 and 8
            long mark = System.currentTimeMillis();
            try {
                coordination = digitOCRInstance.doOCR(ImageUtil.scaleImage(coordinationArea, 2.0f));
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            String[] cords = coordination.trim().split(" ");
            logger.info("CoordinationDetectorRunnable runInLineByDistance, coordination recognized by ocr{}", coordination.trim());
            //System.out.println("cost: " + (System.currentTimeMillis() - mark));
        }
    }
    public static void main(String[] args) {
        Mir2Robot mir2Robot = new Mir2Robot();
        new Thread(new CoordinationDetectorRunnable(mir2Robot)).start();
    }
}
