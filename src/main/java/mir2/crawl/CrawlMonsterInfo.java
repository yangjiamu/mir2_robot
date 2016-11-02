package mir2.crawl;

import org.htmlparser.util.ParserException;
import org.junit.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class CrawlMonsterInfo {
    private static Crawler crawler = new Crawler();
    public static final String ROOT_DIR_WIN = "C:/mir2info/";
    public static final String ROOT_DIR_MAC = "/Users/yangwenjie/Documents/mir2info";
    public static final String MAPNAME2MONSTERINFO_WIN = ROOT_DIR_WIN + "/map2monster";
    public static final String MAPNAME2MONSTERINFO_MAC = ROOT_DIR_MAC + "/map2monster";
    public static final String MONSTERINFO_WIN = ROOT_DIR_WIN + "/monsterinfo";
    public static final String MONSTERINFO_MAC = ROOT_DIR_MAC + "/monsterinfo";

    public static void main(String[] args) throws IOException, URISyntaxException, ParserException, InterruptedException {
        String homePageContent = crawler.getContent(Crawler.HOME_URL);

        Map<String, String> mapMonsterUrl = getMapMonsterUrl(homePageContent);//mapname->url the url page contain the monster occured in map
        Set<Map.Entry<String, String>> entries = mapMonsterUrl.entrySet();

        Map<String, String> monsterName2UrlAll = new HashMap<>();
        for(Map.Entry<String, String> entry : entries) {
            String url = Crawler.HOME_URL + entry.getValue();
            String content1 = crawler.getContent(url);
            Map<String, String> monsterNameToUrl = getMonsterUrl(content1);

            //do some save??
            File rootFile = new File(MAPNAME2MONSTERINFO_MAC);
            if(!rootFile.exists()){
                rootFile.mkdir();
            }
            File file = new File(MAPNAME2MONSTERINFO_MAC + "/" + entry.getKey() + ".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            for (Map.Entry<String, String> entry1 : monsterNameToUrl.entrySet()) {
                fw.write(entry1.getValue() + "\n");
                System.out.println(entry1.getValue());
                fw.flush();
            }
            monsterName2UrlAll.putAll(monsterNameToUrl);
        }

        int i = 0;
        for (Map.Entry<String, String> entry : monsterName2UrlAll.entrySet()) {
            System.out.println(i++);
            File rootFile = new File(MONSTERINFO_MAC);
            if(!rootFile.exists()){
                rootFile.mkdir();
            }
            File file = new File(MONSTERINFO_MAC + "/" + entry.getKey() + ".txt");
            if(!file.exists()){
                file.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            String monsterUrl = Crawler.HOME_URL + entry.getValue();
            Monster monster = getMonsterInfo(monsterUrl);
            monster.setMonsterName(entry.getKey());
            oos.writeObject(monster);
            Thread.sleep(100);
        }
    }

    public static Map<String, String> getMapMonsterUrl(String homePageContent){
        Map<String, String> map = new HashMap<>();
        String mark = "<li><a href=\"#\" title=\"\">怪物";
        int i = homePageContent.indexOf(mark);
        int j = homePageContent.indexOf("</li>", i);
        String subStr = homePageContent.substring(i, j + "</li>".length());

        String regExp = "<a href=.*?<";
        Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(subStr);
        while (matcher.find()){
            String s = matcher.group();
            int i1 = s.indexOf("\"");
            int j1 = s.lastIndexOf("\"");
            String url = s.substring(i1+1, j1);
            String mapName = s.substring(j1+2, s.length() - 1);
            if(mapName != null && !mapName.equals("") && !mapName.equals("怪物"))
                map.put(mapName, url);
        }
        return map;
    }

    @Test
    public void testGetMonsterUrl(){

    }


    public static Map<String, String> getMonsterUrl(String content) throws IOException, URISyntaxException {
        Map<String, String> map = new HashMap<>();
        int i = content.indexOf("<table");
        int j = content.indexOf("</table>", i);
        String subContent = content.substring(i, j + "</table>".length());
        String regExp = "<a href=.*?<";
        Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(subContent);
        while (matcher.find()){
            String s = matcher.group();
            int i1 = s.indexOf("\"");
            int j1 = s.lastIndexOf("\"");
            String url = s.substring(i1 + 1, j1);
            String monsterName = s.substring(j1 + 2, s.length() - 1);
            if(monsterName != null && !monsterName.equals(""))
                map.put(monsterName, url);
        }

        //处理分页
        if(content.contains("下一页")){
            int mark = content.indexOf("下一页");
            int end = content.substring(0, mark).lastIndexOf('\'');
            int begin = content.substring(0, end).lastIndexOf('\'');
            String nextPageUrl = Crawler.HOME_URL + content.substring(begin+1, end);
            //System.out.println(nextPageUrl.trim());
            nextPageUrl = nextPageUrl.replaceAll(" ", "");
            String nextPageContent = crawler.getContent(nextPageUrl.trim());
            map.putAll(getMonsterUrl(nextPageContent));
        }
        return map;
    }

    public static Monster getMonsterInfo(String monsterUrl) throws IOException, URISyntaxException, ParserException {
        List<DropObject> dropObjects = new ArrayList<>();
        String htmlContent = crawler.getContent(monsterUrl);

        Monster monster = new Monster();
        int occurBegin = htmlContent.indexOf("<p>该怪物出现在");
        int occurEnd = htmlContent.indexOf("</p>", occurBegin);
        String occurStr = htmlContent.substring(occurBegin, occurEnd);
        Matcher occurMaatch = Pattern.compile("<a href=\".*?</a>", Pattern.DOTALL).matcher(occurStr);
        List<String> mapNames = new ArrayList<>();
        while (occurMaatch.find()){
            String containMapNameStr = occurMaatch.group();
            String mapName = containMapNameStr.substring(containMapNameStr.lastIndexOf('\"') + 2, containMapNameStr.lastIndexOf('<'));
            mapNames.add(mapName);
        }
        monster.setOccuredMapNames(mapNames);

        //parse drop object info
        String tbodyStr = htmlContent.substring(htmlContent.indexOf("<tbody>"), htmlContent.indexOf("</tbody>") + "</tbody>".length());
        String regExp = "<tr>.*?</tr>";
        Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(tbodyStr);
        String tdBeginStr = "<td";
        String tdEndStr = "</td>";
        while (matcher.find()){
            String trStr = matcher.group();
            int start = 0;
            int indexOfTdBegin = 0;
            int indexOfTdEnd = 0;
            DropObject dropObject = new DropObject();
            for (int i = 0; i < 7; i++) {
                indexOfTdBegin = trStr.indexOf(tdBeginStr, start);
                indexOfTdEnd = trStr.indexOf(tdEndStr, start);
                String tdStr = trStr.substring(indexOfTdBegin, indexOfTdEnd + tdEndStr.length());
                if(i==1){
                    Pattern p1 = Pattern.compile("<a href=\".*?</a>");
                    Matcher m1 = p1.matcher(tdStr);
                    m1.find();
                    String g1 = m1.group();
                    int nameStart = g1.indexOf('>');
                    int nameEnd = g1.lastIndexOf('<');
                    String name = g1.substring(nameStart + 1, nameEnd);
                    //System.out.println(name);
                    dropObject.setName(name);
                }
                if(i==3){
                    int i1 = tdStr.indexOf('>');
                    int i2 = tdStr.lastIndexOf('<');
                    String weightStr = tdStr.substring(i1+1, i2);
                    int weight = -1;
                    if (weightStr != null && !weightStr.equals(""))
                        weight = Integer.parseInt(weightStr);
                    dropObject.setWeight(weight);
                }
                if(i==5){
                    Matcher m5 = Pattern.compile("\\d+").matcher(tdStr);
                    if(m5.find()) {
                        String needRestriction = m5.group();
                        if (tdStr.contains("等级")) {
                            dropObject.setNeedLevel(Integer.parseInt(needRestriction));
                        } else {
                            dropObject.setOtherRestriction(Integer.parseInt(needRestriction));
                        }
                    }
                    else {
                        dropObject.setNeedLevel(-1);
                        dropObject.setOtherRestriction(-1);
                    }
                }
                start = indexOfTdEnd + tdEndStr.length();
            }
            dropObjects.add(dropObject);
        }

        //处理分页
        if(htmlContent.contains("下一页")){
            int mark = htmlContent.indexOf("下一页");
            int end = htmlContent.substring(0, mark).lastIndexOf('\'');
            int begin = htmlContent.substring(0, end).lastIndexOf('\'');
            String nextPageUrl = Crawler.HOME_URL + htmlContent.substring(begin+1, end);
            //System.out.println(nextPageUrl.trim());
            nextPageUrl = nextPageUrl.replaceAll(" ", "");
            dropObjects.addAll(getMonsterInfo(nextPageUrl.trim()).getDropObjects());
        }
        monster.setDropObjects(dropObjects);
        return monster;
    }
}
