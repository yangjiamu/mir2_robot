package mir2.util;

import org.imgscalr.Scalr;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/7.
 */
public class ImageUtil {
    private static final String rootDir = "C:\\Users\\yang\\Pictures\\";
    public static void changeBackgroundToBlack(BufferedImage image){
        for (int i = 0; i < image.getWidth(); i++) {
            for (int i1 = 0; i1 < image.getHeight(); i1++) {
                if (!ColorUtil.isWhiteColor(image.getRGB(i, i1))){
                    image.setRGB(i, i1, new Color(0, 0, 0).getRGB());
                }
            }
        }
    }
    public static BufferedImage scaleImage(BufferedImage image, float scale){
        return Scalr.resize(image, Scalr.Method.BALANCED, (int)(image.getWidth() * scale), (int)(image.getHeight() * scale));
    }

    public static BufferedImage scaleImage(BufferedImage image, float hScale, float vScale){
        return Scalr.resize(image, Scalr.Method.BALANCED, (int)(image.getWidth() * hScale), (int)(image.getHeight() * vScale));
    }

    public static Mat bufferedImageToMat(BufferedImage bi) {
        BufferedImage image = null;
        if(bi.getType() != BufferedImage.TYPE_3BYTE_BGR){
            image = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            image.setData(bi.getData());
        }
        else {
            image = bi;
        }
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
        /*String tempPath = "C:\\Users\\yang\\Pictures\\temp.png";
        try {
            ImageIO.write(bi, "png", new File(tempPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Imgcodecs.imread(tempPath);*/
    }

    public static void saveImage(BufferedImage image, String fileName) throws IOException {
        ImageIO.write(image, "png", new File(rootDir + fileName + ".png"));
    }

    public BufferedImage readImage(String fileName) throws IOException {
        return ImageIO.read(new File(rootDir + fileName + ".png"));
    }
}
