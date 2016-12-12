import mir2.robot.screen.Mir2Screen;
import mir2.robot.screen.ScreenGrid;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/13.
 */
public class ScreenTest {
    @Test
    public void testGetScreenGridImage() throws IOException {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        ScreenGrid screenGrid = new ScreenGrid(11, 11);
        BufferedImage screenGridImage = mir2Screen.getScreenGridImage(screenGrid);
        ImageIO.write(screenGridImage, "png", new File("C:\\Users\\yang\\Pictures\\aa\\getScreenGridImage11_11.png"));
        screenGrid = new ScreenGrid(11, 9);
        screenGridImage = mir2Screen.getScreenGridImage(screenGrid);
        ImageIO.write(screenGridImage, "png", new File("C:\\Users\\yang\\Pictures\\aa\\getScreenGridImage11_9.png"));
    }
}
