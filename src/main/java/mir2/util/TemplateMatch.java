package mir2.util;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

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

    public java.awt.Point match(Mat source, Mat dst){
        Mat result = Mat.zeros(source.rows(), source.cols(), CvType.CV_32FC1);
        //调用模板匹配方法
        Imgproc.matchTemplate(source, dst, result, Imgproc.TM_SQDIFF);
        //规格化
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
        //获得最可能点，MinMaxLocResult是其数据格式，包括了最大、最小点的位置x、y
        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);
        org.opencv.core.Point matchLoc = mlr.minLoc;
        return new java.awt.Point((int)matchLoc.x, (int)matchLoc.y);
    }
}