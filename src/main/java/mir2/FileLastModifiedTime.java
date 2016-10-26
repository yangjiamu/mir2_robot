package mir2;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yangwenjie on 16/10/26.
 */
public class FileLastModifiedTime {
    public static void main(String[] args) {
        File file = null;
        try {
            file = new File("C:\\Users\\yang\\Downloads\\Mir2online\\米尔在线\\Data\\Hum.wil");
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            System.out.println("Original last modified time: " + df.format(file.lastModified()));
            Date date = df.parse("2006/07/24 11:06");
            file.setLastModified(date.getTime());
            System.out.println(df.format(file.lastModified()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
