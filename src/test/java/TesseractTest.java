import mir2.robot.screen.Mir2Screen;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yangwenjie on 16/10/27.
 */
public class TesseractTest {
    public static void main(String[] args) throws TesseractException, IOException {
        Tesseract1 instance = new Tesseract1();
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        //Tesseract instance = new Tesseract();
        instance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        instance.setLanguage("chi_sim");
        //instance.setTessVariable("tessedit_char_whitelist", "0123456789");
        //File file = new File("/Users/yangwenjie/Documents/digit1.png");
        BufferedImage image = mir2Screen.getNavigationArea();
        float[] scales = {1.2f, 1.3f, 1.4f, 1.5f, 1.6f, 1.7f, 1.8f, 1.9f, 2.0f, 2.2f, 2.3f, 2.4f, 2.5f, 2.6f, 2.7f, 2.8f, 2.9f, 3.0f, 3.5f};
        for (float scale : scales) {
            BufferedImage scaledImage = Scalr.resize(image, Scalr.Method.BALANCED, image.getWidth() * 2, image.getHeight() * 2);
            String result = instance.doOCR(scaledImage);
            System.out.println(result);
        }

    }
}
