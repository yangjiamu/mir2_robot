package mir2.robot.scan;

import mir2.robot.screen.Mir2Screen;
import mir2.util.ImageUtil;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * Created by yang on 2016/11/8.
 */
public class MapNameDetector {
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private static final Logger logger = LoggerFactory.getLogger(MapNameDetector.class);
    Tesseract1 chiSimOCRInstance = new Tesseract1();

    public MapNameDetector(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        chiSimOCRInstance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        chiSimOCRInstance.setLanguage("chi_sim");
    }

    public boolean isInMap(String mapName){
        BufferedImage mapNameImage = mir2Screen.getMapNameArea(mapName);
        ImageUtil.changeBackgroundToBlack(mapNameImage);
        String recognizedMap = null;
        try {
            //mapName = chiSimOCRInstance.doOCR(mapNameImage);
            //recognizedMap = chiSimOCRInstance.doOCR(ImageUtil.scaleImage(mapNameImage, 1.4f));//比奇ok
            recognizedMap = chiSimOCRInstance.doOCR(ImageUtil.scaleImage(mapNameImage, 1.3f));//比奇 ok （兽）昌人古墓一层
            if(recognizedMap.contains("昌")){
                recognizedMap = recognizedMap.replace("昌", "兽");
            }
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        recognizedMap = recognizedMap.trim();
        recognizedMap = recognizedMap.substring(0, mapName.length());
        logger.debug("map name recognized by ocr:{}", recognizedMap.trim());
        return recognizedMap.trim().equals(mapName);
        //return isApproximateMatch(mapName, recognizedMap.trim());
    }

    private boolean isApproximateMatch(String exact, String recognized){
        float n = 0;
        for (int i = 0; i < exact.length(); i++) {
            if(exact.charAt(i) == recognized.charAt(i))
                ++n;
        }
        System.out.println(n);
        float p = (float)(n / exact.length());
        return Double.compare(p, 0.8) > 0;
    }

    public static void main(String[] args) {
        MapNameDetector mapNameDetector = new MapNameDetector();
        System.out.println(mapNameDetector.isInMap("兽人古墓一层"));
        System.out.println(mapNameDetector.isInMap("比奇"));
    }
}
