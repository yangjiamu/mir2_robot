package mir2.crawl;

import org.htmlparser.util.ParserException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class CrawlMonsterInfo {
    private static Crawler crawler = new Crawler();
    private static final String root_dir="";
    public static void main(String[] args) throws IOException, URISyntaxException, ParserException {
        String content = crawler.getContent(Crawler.HOME_URL);

        Map<String, String> mapMonsterUrl = getMapMonsterUrl(content);
        Set<Map.Entry<String, String>> entries = mapMonsterUrl.entrySet();

        Map<String, String> monsterName2Url = new HashMap<>();
        /*for(Map.Entry<String, String> entry : entries) {
            String url = Crawler.HOME_URL + entries.iterator().next().getValue();
            String content1 = crawler.getContent(url);
            Map<String, String> monsterNameToUrl = getMonsterUrl(content1);

            //monsterName2Url.putAll(monsterNameToUrl);
        }*/


        /*for (Map.Entry<String, String> stringStringEntry : monsterName2Url.entrySet()) {
            System.out.println(stringStringEntry.getKey() + ":" + stringStringEntry.getValue());
        }*/

        /*String testUrl = "http://www.mir2wiki.cn/index.php?s=Home/index/map/tag/5/id/544/";
        String content1 = crawler.getContent(testUrl);
        Map<String, String> monsterUrl = getMonsterUrl(content1);
        for (Map.Entry<String, String> stringStringEntry : monsterUrl.entrySet()) {
            System.out.println(stringStringEntry.getKey() + ":" + stringStringEntry.getValue());
        }*/

        String monsterUrl = "http://www.mir2wiki.cn/index.php?s=Home/Index/guaiwu2/id/842";
        List<DropObject> dropObjects = getMonsterInfo(monsterUrl);
        for (DropObject dropObject : dropObjects) {
            System.out.println(dropObject);
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
            System.out.println(nextPageUrl.trim());
            nextPageUrl = nextPageUrl.replaceAll(" ", "");
            String nextPageContent = crawler.getContent(nextPageUrl.trim());
            map.putAll(getMonsterUrl(nextPageContent));
        }
        return map;
    }

    public static List<DropObject> getMonsterInfo(String monsterUrl) throws IOException, URISyntaxException, ParserException {
        List<DropObject> dropObjects = new ArrayList<>();
        String htmlContent = crawler.getContent(monsterUrl);
        String tbodyStr = htmlContent.substring(htmlContent.indexOf("<tbody>"), htmlContent.indexOf("</tbody>") + "</tbody>".length());
        //System.out.println(tbodyStr);
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
            System.out.println("-------");
            DropObject dropObject = new DropObject();
            for (int i = 0; i < 7; i++) {
                indexOfTdBegin = trStr.indexOf(tdBeginStr, start);
                indexOfTdEnd = trStr.indexOf(tdEndStr, start);
                String tdStr = trStr.substring(indexOfTdBegin, indexOfTdEnd + tdEndStr.length());
                //System.out.println(tdStr);
                //System.out.println("***********");
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
                    String weight = tdStr.substring(i1+1, i2);
                    dropObject.setWeight(Integer.parseInt(weight));
                    //System.out.println(weight);
                }
                if(i==5){
                    Matcher m5 = Pattern.compile("\\d+").matcher(tdStr);
                    m5.find();
                    String needRestriction = m5.group();
                    if(tdStr.contains("等级")){
                        dropObject.setNeedLevel(Integer.parseInt(needRestriction));
                    }
                    else {
                        dropObject.setOtherRestriction(Integer.parseInt(needRestriction));
                    }
                    //System.out.println(needRestriction);
                }
                start = indexOfTdEnd + tdEndStr.length();
            }
            dropObjects.add(dropObject);
        }
        return dropObjects;
    }
}
