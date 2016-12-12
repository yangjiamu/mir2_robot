package mir2.robot.scan;

import mir2.robot.Mir2Robot;
import mir2.robot.screen.Mir2Screen;
import mir2.util.ColorUtil;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by yang on 2016/11/5.
 */
public class HpMpDetectorRunnable implements Runnable{
    private  final String hpHalfPath = this.getClass().getResource("/hp_half_cicle_full_24.png").getPath();
    private  final String mpHalfPath = this.getClass().getResource("/hp_half_cicle_full_24.png").getPath();
    private  final String hpFullPath = this.getClass().getResource("/hp_full_cicle_full_24.png").getPath();
    @Override
    public void run() {
        Mir2Screen mir2Screen = Mir2Screen.getInstance();
        Mir2Robot mir2Robot = new Mir2Robot();
        BufferedImage hpHalfImage = null;
        BufferedImage mpHalfImage = null;
        BufferedImage hpFullImage = null;
        try {
            hpHalfImage = ImageIO.read(new File(hpHalfPath));
            mpHalfImage = ImageIO.read(new File(mpHalfPath));
            hpFullImage = ImageIO.read(new File(hpFullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true){
            if(mir2Robot.getLevel() < 7 || (mir2Robot.getLevel() < 26 && mir2Robot.getCareer().equals("战士"))){//only have hp
                BufferedImage curHpImage = mir2Screen.getHPMPCicle();
                mir2Robot.setCurrentHp((int) (mir2Robot.getHp() * matchPercentage(curHpImage, hpFullImage)));
            }
            else {
                BufferedImage curHpMpImage = mir2Screen.getHPMPCicle();
                BufferedImage curHpImage = curHpMpImage.getSubimage(0, 0, curHpMpImage.getWidth() / 2, curHpMpImage.getHeight());
                BufferedImage curMpImage = curHpMpImage.getSubimage(0, curHpMpImage.getWidth() / 2, curHpMpImage.getWidth() / 2, curHpMpImage.getHeight());
                mir2Robot.setCurrentHp((int) (mir2Robot.getHp() * matchPercentage(curHpImage, hpHalfImage)));
                mir2Robot.setCurrentMp((int) (mir2Robot.getMp() * matchPercentage(curMpImage, mpHalfImage)));
            }
            //detect current level and reset role level
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private float matchPercentage(BufferedImage cur, BufferedImage full){
        int y = 0;
        boolean lineEqual = true;
        for (y = full.getHeight()-1; y >0; y--) {
            for (int x = 0; x < full.getWidth(); x++) {
                if (!ColorUtil.isRgbSimilar(cur.getRGB(x, y), full.getRGB(x, y), 120)){
                    lineEqual = false;
                    break;
                }
            }
            if(!lineEqual){
                break;
            }
        }
        return (float)(full.getHeight() - y)/full.getHeight();
    }

    @Test
    public void testReadResource(){
    }
}
