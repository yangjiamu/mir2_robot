import mir2.robot.scan.BagScanner;
import mir2.robot.enums.ColorMappingEnum;
import mir2.robot.enums.TagTypeEnum;
import mir2.util.ColorUtil;
import mir2.util.ImageUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/23.
 */
public class SmallBagTest {
    @Test
    public void testGetSmallBag() throws IOException {
        BagScanner bagScanner = new BagScanner();
        BufferedImage[] smallBag = bagScanner.smallBagGridImages();
        System.out.println(ColorMappingEnum.GRAY_YELLOW.getJavaColor().getRGB());
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                int[] rgbDetach = ColorUtil.rgbDetach(smallBag[0].getRGB(j+2, i));
                System.out.print(" r:"+rgbDetach[0] + " g:"+rgbDetach[1] + " b:"+rgbDetach[2] + "    ");
            }
            System.out.println();
        }
    }

    @Test
    public void testGetBag() throws IOException {
        BagScanner bagScanner = new BagScanner();
        BufferedImage[][] bag = bagScanner.bagGridImages();
        for (int i = 0; i < bag.length; i++) {
            for (int j = 0; j < bag[0].length; j++) {
                ImageUtil.saveImage(bag[i][j], "bag_" + String.valueOf(i+1) + "_" + String.valueOf(j+1) + ".png");
            }
        }
    }

    @Test
    public void testRecognizeBag(){
        BagScanner bagScanner = new BagScanner();
        TagTypeEnum[][] tags = bagScanner.recognizeItemInBag();
        for (int i = 0; i < tags.length; i++) {
            for (int j = 0; j < tags[0].length; j++) {
                if(!(tags[i][j] == null)) {
                    System.out.print(tags[i][j].getDesc() + "   ");
                }
                else {
                    System.out.print("null    ");
                }
            }
            System.out.println();
        }
    }

    @Test
    public void testRecognizeSmallBag(){
        BagScanner bagScanner = new BagScanner();
        TagTypeEnum[] tagTypeEna = bagScanner.recognizeItemInSmallBag();
        for (TagTypeEnum tagTypeEnum : tagTypeEna) {
            System.out.println(tagTypeEnum.getDesc());
        }
    }


    @Test
    public void testRecognizeSunWater(){
        BagScanner bagScanner = new BagScanner();
        BufferedImage[][] bag = bagScanner.bagGridImages();
        BufferedImage image = bag[3][3];
        int[] ints1 = ColorUtil.rgbDetach(ColorMappingEnum.GRAY_YELLOW.getJavaColor().getRGB());
        System.out.println("-----r:"+ints1[0]+" g:"+ints1[1]+"b:"+ints1[2]+"  ");
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int[] ints = ColorUtil.rgbDetach(image.getRGB(i, j));
                System.out.print("r:"+ints[0]+" g:"+ints[1]+"b:"+ints[2]+"  ");
            }
            System.out.println();
        }
    }

    @Test
    public void testColorMappping() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\yang\\Pictures\\small_bag0.png"));
        int index = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                index = (byte)((i*16) + j);
                int[] rgbDetach = ColorUtil.rgbDetach(image.getRGB(j+2, i));
                String s = Integer.toHexString(index);
                s = s.length() > 2 ? s.substring(s.length()-2, s.length()) : s;
                System.out.print(s + " r:"+rgbDetach[0] + " g:"+rgbDetach[1] + " b:"+rgbDetach[2] + "    ");
                ++index;
            }
            System.out.println();
        }

    }
}
