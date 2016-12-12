package mir2.clientinfo;

import org.junit.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangwenjie on 16/10/26.
 */
public class FileLastModifiedTime {
    private static final String dataPath = "C:\\Users\\yang\\Downloads\\Mir2online\\Mir2online\\Data\\";
    private static final String mapPath = "C:\\Users\\yang\\Downloads\\Mir2online\\Mir2online\\Map\\";
    public static void main(String[] args) throws IOException, ParseException {
        //exportCorrectTime();

        /*for (int i = 1; i < 16; i++) {
            reSetTime(dataPath, "Mon" + i + ".wil");
        }*/
        //reSetTime(dataPath, "Hair.wil");
        /*String []mapIds = {"0", "1", "D001", "D002", "D003", "D011", "D012"};
        for (String mapId : mapIds) {
            reSetTime(mapPath, mapId + ".map");
        }*/
        reSetTime(dataPath, "Dnitems.wil");
        reSetTime(dataPath, "Items.wil");
    }

    @Test
    public void reSetTime() throws ParseException {
        File file = new File("C:\\Users\\yang\\Downloads\\Mir2online\\Mir2online\\Map\\0.map");
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        System.out.println(df.format(file.lastModified()));
        Date date = df.parse("2003/10/20 21:58:44");
        file.setLastModified(date.getTime());
    }

    public static void exportCorrectTime() throws FileNotFoundException {
        String correctFilePath1 = "C:\\Users\\yang\\Downloads\\Mir2online_not_changed\\米尔在线\\Data";
        String correctFilePath2 = "C:\\Users\\yang\\Downloads\\Mir2online_not_changed\\米尔在线\\Map";
        String outputPath = "C:\\Users\\yang\\Downloads\\Mir2online\\correctTimeReserved.txt";
        PrintWriter printWriter = new PrintWriter(new File(outputPath));
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        File file = new File(correctFilePath1);
        File[] files = file.listFiles();
        for (File file1 : files) {
            String dateTimeStr = df.format(new Date(file1.lastModified()));
            printWriter.println(file1.getName() + "\t" + dateTimeStr);
        }
        file = new File(correctFilePath2);
        files = file.listFiles();
        for (File file1 : files) {
            String dateTimeStr = df.format(new Date(file1.lastModified()));
            printWriter.println(file1.getName() + "\t" + dateTimeStr);
        }
        printWriter.flush();
        printWriter.close();
    }
    public static void reSetTime(String rootPath, String fileName) throws IOException, ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Users\\yang\\Downloads\\Mir2online\\correctTimeReserved.txt")));
        String line = null;
        Map<String, String> fileName2DateStr = new HashMap<>();

        while ((line = reader.readLine()) != null){
            String[] split = line.split("\t");
            if(split.length != 2){
                System.out.println("eror");
            }
            fileName2DateStr.put(split[0], split[1]);
        }
        File file = new File(rootPath + fileName);
        Date date = df.parse(fileName2DateStr.get(fileName));
        file.setLastModified(date.getTime());
    }
}
