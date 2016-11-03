package mir2.screen;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yangwenjie on 16/11/3.
 */
public class Mir2ScreenInfoTest {
    @Test
    public void testMir2ScreenInfo() throws InterruptedException {
        Thread.sleep(1000 * 2);
        Mir2Screen mir2Screen  = Mir2Screen.getMir2Screen();
        System.out.println(mir2Screen.getMir2TitleBarLeftTop());
        System.out.println(mir2Screen.getMir2TitleBarRightBottom());
        System.out.println(mir2Screen.getMir2ScreenLeftTop());
        System.out.println(mir2Screen.getMir2ScreenRightBottom());

        Assert.assertEquals(1024.0f, mir2Screen.getMir2ScreenRightBottom().getX() - mir2Screen.getMir2ScreenLeftTop().getX());
        Assert.assertEquals(768.0f, mir2Screen.getMir2ScreenRightBottom().getY() - mir2Screen.getMir2ScreenLeftTop().getY());
    }
}
