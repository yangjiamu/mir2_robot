package mir2;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * Created by yangwenjie on 16/10/26.
 */

class TemplateMatch {
    private String sourcePath, dstPath;
    private Mat source, dst;

    //原图片
    public void setSource(String picPath) {
        this.sourcePath = picPath;
    }

    //需要匹配的部分
    public void setDst(String picPath) {
        this.dstPath = picPath;
    }

    //处理，生成结果图
    public void process() {
        //将文件读入为OpenCV的Mat格式
        source = Imgcodecs.imread(sourcePath);
        dst = Imgcodecs.imread(dstPath);
        //创建于原图相同的大小，储存匹配度
        Mat result = Mat.zeros(source.rows(), source.cols(), CvType.CV_32FC1);
        //调用模板匹配方法
        Imgproc.matchTemplate(source, dst, result, Imgproc.TM_SQDIFF);
        //规格化
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1);
        //获得最可能点，MinMaxLocResult是其数据格式，包括了最大、最小点的位置x、y
        Core.MinMaxLocResult mlr = Core.minMaxLoc(result);
        Point matchLoc = mlr.minLoc;
        //在原图上的对应模板可能位置画一个绿色矩形
        Imgproc.rectangle(source, matchLoc, new Point(matchLoc.x + dst.width(), matchLoc.y + dst.height()), new Scalar(0, 255, 0));
        //Core.rectangle(source, matchLoc, new Point(matchLoc.x + dst.width(), matchLoc.y + dst.height()), new Scalar(0, 255, 0));
        //将结果输出到对应位置
        Imgcodecs.imwrite("/Users/yangwenjie/Documents/TMOutPut.png", source);
    }

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        //System.loadLibrary("opencv_java310");
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        TemplateMatch macher = new TemplateMatch();
        //设置原图
        macher.setSource("/Users/yangwenjie/Documents/screen.png");
        //设置要匹配的图
        macher.setDst("/Users/yangwenjie/Documents/human.png");

        long mark = System.currentTimeMillis();
        macher.process();
        System.out.println(System.currentTimeMillis() - mark);
    }
}