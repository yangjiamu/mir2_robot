package mir2;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Tesseract1;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;

/**
 * Created by yangwenjie on 16/10/27.
 */
public class TesseractTest {
    public static void main(String[] args) throws TesseractException {
        Tesseract1 instance = new Tesseract1();
        //Tesseract instance = new Tesseract();
        instance.setDatapath("/usr/local/share/tessdata/");
        //instance.setTessVariable("tessedit_char_whitelist", "0123456789");
        File file = new File("/Users/yangwenjie/Documents/digit1.png");
        long mark = System.currentTimeMillis();
        String result = instance.doOCR(file);
        System.out.println(System.currentTimeMillis()-mark);
        System.out.println(result);
    }
}
