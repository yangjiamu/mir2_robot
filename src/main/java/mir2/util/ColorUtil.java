package mir2.util;

import java.awt.*;

/**
 * Created by yang on 2016/11/5.
 */
public class ColorUtil {
    public static boolean isRgbSimilar(int color1Rgb, int color2Rgb, int threshold){
        int[] ints = rgbDetach(color1Rgb);
        Color color1 = new Color(ints[0], ints[1], ints[2]);
        int[] ints1 = rgbDetach(color2Rgb);
        Color color2 = new Color(ints1[0], ints1[1], ints1[2]);
        return Math.abs(color1.getRed() - color2.getRed()) <= threshold &&
                Math.abs(color1.getGreen() - color2.getGreen()) <= threshold &&
                Math.abs(color1.getBlue() - color2.getBlue()) <= threshold;
    }

    public static boolean isColorSimilar(Color color1, Color color2, int threshold){
        return Math.abs(color1.getRed() - color2.getRed()) <= threshold &&
                Math.abs(color1.getGreen() - color2.getGreen()) <= threshold &&
                Math.abs(color1.getBlue() - color2.getBlue()) <= threshold;
    }

    public static double rgbCosineSimilarity(int color1Rgb, int color2Rgb){
        int[] ints = rgbDetach(color1Rgb);
        int[] ints1 = rgbDetach(color2Rgb);
        float dotProduct = 0;
        for (int i = 0; i < ints.length; i++) {
            dotProduct += ints[i]*ints1[i];
        }
        double cosine = dotProduct/(Math.sqrt(ints.length) * Math.sqrt(ints1.length));
        return cosine;
    }

    public static int[] rgbDetach(int rgbVale){
        int[] color = new int[3];
        color[0] = (rgbVale >> 16) & 0xFF;//red
        color[1] = (rgbVale >> 8) & 0xFF;//green
        color[2] = (rgbVale >> 0) & 0xFF;//blue
        return color;
    }

    public static boolean isColorEqual(Color color1, Color color2){
        return color1.getRed() == color2.getRed() &&
                color1.getGreen() == color2.getGreen() &&
                color1.getBlue() == color2.getBlue();
    }
    public static boolean isWhiteColor(int rgb){
        int[] ints = ColorUtil.rgbDetach(rgb);
        Color color = new Color(ints[0], ints[1], ints[2]);
        return color.getRed() == 255 && color.getGreen() == 255 && color.getBlue() == 255;
    }
}
