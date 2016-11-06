package mir2.robot;

import mir2.role.GameRole;
import mir2.screen.Mir2Screen;
import mir2.util.ColorUtil;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/6.
 */
public class CoordinationDetectionRunnable implements Runnable{
    public static void main(String[] args) {
        new Thread(new CoordinationDetectionRunnable()).start();
    }
    @Override
    public void run() {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        GameRole gameRole = GameRole.getInstance();
        Tesseract1 chiSimOCRInstance = new Tesseract1();
        Tesseract1 digitOCRInstance = new Tesseract1();
        chiSimOCRInstance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        chiSimOCRInstance.setLanguage("chi_sim");
        digitOCRInstance.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
        digitOCRInstance.setLanguage("eng");
        digitOCRInstance.setTessVariable("tessedit_char_whitelist", "0123456789");
        while (true){
            BufferedImage mapNameImage = mir2Screen.getMapNameArea("白日门");
            BufferedImage coordinationArea = mir2Screen.getCoordinationArea("白日门");
            ColorUtil.changeBackgroundToBlack(mapNameImage);
            ColorUtil.changeBackgroundToBlack(coordinationArea);
            try {
                String s = chiSimOCRInstance.doOCR(mapNameImage);
                String mapName = chiSimOCRInstance.doOCR(scaleImage(mapNameImage, 1.8f));
                System.out.println(s);
                System.out.println(mapName);
                String s1 = digitOCRInstance.doOCR(coordinationArea);
                //1.8 1.9 pretty ok bu can mistake 3 and 8
                //2.0 ok bu transfer : to 1 sometimes
                //2.1 very good but sometimes can not seperate x and y
                //2.2 can not seperate x and y
                //2.3 - 2.5 becomes bad again
                //2.6 mistake 3 and 8
                String coordination = digitOCRInstance.doOCR(scaleImage(coordinationArea, 3.6f));
                System.out.println(s1);
                System.out.println(coordination);
            } catch (TesseractException e) {
                e.printStackTrace();
            }
            System.out.println(coordinationArea);
            try {
                Thread.sleep(1000*5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedImage scaleImage(BufferedImage image, float scale){
        return Scalr.resize(image, Scalr.Method.BALANCED, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));
    }

    public void changeBackground(BufferedImage image){
        for (int i = 0; i < image.getWidth(); i++) {
            for (int i1 = 0; i1 < image.getHeight(); i1++) {
                if (!isWhiteColor(image.getRGB(i, i1))){
                    image.setRGB(i, i1, new Color(0, 0, 0).getRGB());
                }
            }
        }
    }

    @org.junit.Test
    public void testChangeBackground() throws IOException {
        BufferedImage origin = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\coordination.png"));
        changeBackground(origin);
        ImageIO.write(origin, "png", new File("C:\\Users\\yang\\Pictures\\coordination_black_back.png"));
    }

    public boolean isWhiteColor(int rgb){
        int[] ints = ColorUtil.colorFromRgb(rgb);
        Color color = new Color(ints[0], ints[1], ints[2]);
        return color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255;
    }
}
