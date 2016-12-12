package mir2.robot.scan;

import mir2.robot.enums.TagTypeEnum;
import mir2.util.ColorUtil;
import mir2.util.ImageUtil;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by yangwenjie on 16/10/26.
 */

public class TemplateMatch {
    public TemplateMatch(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public java.awt.Point match(BufferedImage source, BufferedImage dst){
        Mat sourceMat = ImageUtil.bufferedImageToMat(source);
        Mat dstMat = ImageUtil.bufferedImageToMat(dst);
        return match(sourceMat, dstMat);
    }

    public java.awt.Point match(Mat source, Mat dst){
        Mat result = Mat.zeros(source.rows(), source.cols(), CvType.CV_32FC1);
        //调用模板匹配方法
        Imgproc.matchTemplate(source, dst, result, Imgproc.TM_SQDIFF);
        //规格化
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
        //获得最可能点，MinMaxLocResult是其数据格式，包括了最大、最小点的位置x、y
        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);
        org.opencv.core.Point matchLoc = mlr.minLoc;
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + dst.cols(),
                matchLoc.y + dst.rows()), new Scalar(0, 255, 0));
        return new java.awt.Point((int)matchLoc.x, (int)matchLoc.y);
    }

    public boolean hairTagMatch(BufferedImage source){
        for (int i = 0; i < source.getWidth(); i++) {
            for (int j = 0; j < source.getHeight(); j++) {
                if(ColorUtil.isRgbSimilar(source.getRGB(i, j), Color.BLUE.getRGB(), 50) &&
                        (i+8) < source.getWidth() && (j+6) < source.getHeight()){
                    int w = 0, h = 0;
                    for(; w < 8; ++w){
                        for(; h < 6; ++h){
                            if(!ColorUtil.isRgbSimilar(source.getRGB(i+w, j+h), Color.BLUE.getRGB(), 50))
                                break;
                        }
                        if(h != 6)
                            break;
                    }
                    if(w == 8 && h==6)
                        return true;
                }
            }
        }
        return false;
    }

    public boolean tagMatch(BufferedImage image, TagTypeEnum tag){
        return tagMatch(image, new Rectangle(0, 0, image.getWidth(), image.getHeight()), tag);
    }

    public boolean tagMatch(BufferedImage image, Rectangle rectangle, TagTypeEnum tag){
        return tagMatch(image, rectangle, tag.getColor().getJavaColor(), tag.getTagWidth(), tag.getTagHeight());
    }

    public boolean tagMatch(BufferedImage gameScreen, Rectangle rectangle, Color color, int tagWidth, int tagHeight){
        int startX = (int)rectangle.getX();
        int endX = (int)(startX + rectangle.getWidth());
        int startY = (int)rectangle.getY();
        int endY = (int)(startY + rectangle.getHeight());
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                if(gameScreen.getRGB(i, j) == color.getRGB() &&
                        (i+ tagWidth) < endX && (j+ tagHeight) < endY){
                    int m = 0, n = 0;
                    for(m=0; m < tagWidth; ++m){
                        for(n=0; n < tagHeight; ++n){
                            if(!(gameScreen.getRGB(i+ m, j+ n) == color.getRGB()))
                                break;
                        }
                        if(n != (tagHeight))
                            break;
                    }
                    if((m == tagWidth) && (n == tagHeight))
                        return true;
                }
            }
        }
        return false;
    }
}