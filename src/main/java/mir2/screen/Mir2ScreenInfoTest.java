package mir2.screen;

import org.junit.Assert;
import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2ScreenInfoTest {
    @Test
    public void testMir2ScreenInfo() throws InterruptedException {
        Thread.sleep(1000 * 2);
        Mir2Screen mir2Screen  = Mir2Screen.getInstance();
        System.out.println(mir2Screen.getMir2TitleBarLeftTop());
        System.out.println(mir2Screen.getMir2TitleBarRightBottom());
        System.out.println(mir2Screen.getMir2ScreenLeftTop());
        System.out.println(mir2Screen.getMir2ScreenRightBottom());

        Assert.assertEquals(1024.0f, mir2Screen.getMir2ScreenRightBottom().getX() - mir2Screen.getMir2ScreenLeftTop().getX());
        Assert.assertEquals(768.0f, mir2Screen.getMir2ScreenRightBottom().getY() - mir2Screen.getMir2ScreenLeftTop().getY());
    }

    @Test
    public void testCaptureGameWindow() throws InterruptedException, IOException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        BufferedImage image = mir2Screen.getGameScreen();
        ImageIO.write(image, "png", new File("C:\\Users\\yang\\Pictures\\game_window_for_stand_point_locate.png"));
        Thread.sleep(1000 * 2);
    }

    @Test
    public void testHPMP(){
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
    }

    @Test
    public void testLocateHPMPCircle() throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        BufferedImage gameScreen = mir2Screen.getGameScreen();
        String path = "C:\\Users\\yang\\Pictures\\game_window_for_hp_cicle.png";
        String targetPath = "C:\\Users\\yang\\Pictures\\hp_mp_cicle.png";
        ImageIO.write(gameScreen, "png", new File(path));
        Mat source = Imgcodecs.imread(path);
        Mat target = Imgcodecs.imread(targetPath);
        Mat result = Mat.zeros(source.rows(), source.cols(), CvType.CV_32FC1);
        Imgproc.matchTemplate(source, target, result, Imgproc.TM_SQDIFF);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);
        org.opencv.core.Point matchLoc = mlr.minLoc;
        System.out.println("x:" + matchLoc.x);
        System.out.println("y:" + matchLoc.y);

    }


    @Test
    public void splitHPMPPic() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\hp_mp_cicle_full_24.png"));
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        BufferedImage hpImage = image.getSubimage(0, 0, image.getWidth() / 2, image.getHeight());
        BufferedImage mpImage = image.getSubimage(image.getWidth() / 2, 0, image.getWidth() / 2, image.getHeight());
        ImageIO.write(hpImage, "png", new File("C:\\Users\\yang\\Pictures\\hp_half_cicle_full_24.png"));
        ImageIO.write(mpImage, "png", new File("C:\\Users\\yang\\Pictures\\mp_half_cicle_full_24.png"));
    }

    @Test
    public void testHPMPDetect() throws IOException {
        BufferedImage hpCicle1 = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\hp_half_cicle_1.png"));
        BufferedImage mpCicle1 = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\mp_half_cicle_1.png"));
        BufferedImage hpFullCicle = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\hp_half_cicle_full_24.png"));
        BufferedImage mpFullCicle = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\mp_half_cicle_full_24.png"));
        System.out.println(hpCicle1.getColorModel().getPixelSize());
        System.out.println(mpCicle1.getColorModel().getPixelSize());
        System.out.println(hpFullCicle.getColorModel().getPixelSize());
        System.out.println(mpFullCicle.getColorModel().getPixelSize());
        int y = 0;
        boolean lineEqual = true;
        for (y = hpFullCicle.getHeight()-1; y >0; y--) {
            for (int x = 0; x < hpFullCicle.getWidth(); x++) {
                System.out.println("x:" + x + " y:" + y);
                System.out.println(hpCicle1.getRGB(x, y) + "    " + hpFullCicle.getRGB(x, y));
                if (!rgbSimilar(hpCicle1.getRGB(x, y), hpFullCicle.getRGB(x, y))){
                    lineEqual = false;
                    break;
                }
            }
            if(!lineEqual){
                break;
            }
        }
        System.out.println("hp percentage: " + (float)(hpFullCicle.getHeight()-y)/hpFullCicle.getHeight());

        //mp
        y = 0;
        lineEqual = true;
        for (y = mpFullCicle.getHeight()-1; y >0; y--) {
            for (int x = 0; x < mpFullCicle.getWidth(); x++) {
                if (!rgbSimilar(mpCicle1.getRGB(x, y), mpFullCicle.getRGB(x, y))){
                    lineEqual = false;
                    break;
                }
            }
            if(!lineEqual){
                break;
            }
        }
        System.out.println("mp percentage: " + (float)(hpFullCicle.getHeight() - y)/hpFullCicle.getHeight());
    }
    @Test
    public void testHPMPCicleCapture() throws IOException, InterruptedException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        /*for (int i = 0; i < 50; i++) {
            Thread.sleep(1000 * 2);
            BufferedImage hpmpCicle = mir2Screen.getHPMPCicle();
            ImageIO.write(hpmpCicle, "png", new File("C:\\Users\\yang\\Pictures\\hp\\cicle_" +i  + ".png"));
        }*/
        BufferedImage fullHPMPCicle = mir2Screen.getHPMPCicle();
        ImageIO.write(fullHPMPCicle, "png", new File("C:\\Users\\yang\\Pictures\\hp_full_cicle_full_24.png"));
    }
    @Test
    public void testSmallBag() throws IOException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        BufferedImage[] smallBagImages = mir2Screen.getSmallBagImages();
        int i = 1;
        for (BufferedImage smallBagImage : smallBagImages) {
            ImageIO.write(smallBagImage, "png", new File("C:\\Users\\yang\\Pictures\\" + "bag_" +i + ".png"));
            i++;
        }
    }
    private static boolean rgbSimilar(int color1Rgb, int color2Rgb){
        int[] ints = colorFromRgb(color1Rgb);
        Color color1 = new Color(ints[0], ints[1], ints[2]);
        int[] ints1 = colorFromRgb(color2Rgb);
        Color color2 = new Color(ints1[0], ints1[1], ints1[2]);
        return Math.abs(color1.getRed() - color2.getRed()) <= 120 &&
                Math.abs(color1.getGreen() - color2.getGreen()) <= 120 &&
                Math.abs(color1.getBlue() - color2.getBlue()) <= 120;
    }

    private static int[] colorFromRgb(int rgbVale){
        int[] color = new int[3];
        color[0] = (rgbVale & 0x00ff0000) >> 16;
        color[1] = (rgbVale & 0x0000ff00) >> 8;
        color[2] = rgbVale & 0x000000ff;
        return color;
    }

    @Test
    public void testColorSim(){
        Color colorUp = new Color(227, 215, 47);
        Color colorDown = new Color(254, 240, 53);
        System.out.println(rgbSimilar(colorUp.getRGB(), colorDown.getRGB()));
    }

    @Test
    public void getStandPointCapture() throws IOException {

    }
}
