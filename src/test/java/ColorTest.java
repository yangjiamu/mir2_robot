import mir2.util.ColorUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by yang on 2016/11/22.
 */
public class ColorTest {
    @Test
    public void testRGB() throws IOException {
        String dirPath = "C:\\Users\\yang\\Pictures\\color_found_test\\colors";
        String outPath = "C:\\Users\\yang\\Pictures\\color_found_test\\rgb_table.txt";
        PrintWriter printWriter = new PrintWriter(new File(outPath));
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (File file1 : files) {
            BufferedImage image = ImageIO.read(file1);
            int[] ints = ColorUtil.rgbDetach(image.getRGB(0, 0));
            printWriter.println(file1.getName().substring(0, 2)+ "  " + "red:" + ints[0] + "    green:" + ints[1] + "   blue:"+ ints[2]);
            System.out.println(file1.getName().substring(0, 2) + "  " + "red:" + ints[0] + "    green:" + ints[1] + "   blue:"+ ints[2]);
        }
        printWriter.flush();
    }
}
