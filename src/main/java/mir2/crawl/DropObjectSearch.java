package mir2.crawl;

import java.io.*;
import java.util.*;

/**
 * Created by yangwenjie on 16/11/1.
 */
public class DropObjectSearch {
    public static final String DROPOBJECT_DIR_MAC = CrawlMonsterInfo.ROOT_DIR_MAC + "/dropobject";
    public static final String DROPOBJECT_DIR_WIN = CrawlMonsterInfo.ROOT_DIR_WIN + "/dropobject";

    public static Map<String, Set<String>> dropObject2MonsterNames = new HashMap<>();
    public static Map<String, Set<String>> dropObject2MapNames = new HashMap<>();
    public static Map<String, Set<Monster>> dropObject2Monsterinfos = new HashMap<>();
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        generateDropObjectRelation();
        System.out.println(dropObject2MapNames.size());
        System.out.println(dropObject2MonsterNames.size());

        while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            while ((line = br.readLine()) != null){
                System.out.println(dropObject2MapNames.get(line));
                System.out.println(dropObject2MonsterNames.get(line));
                Set<Monster> monsterInfos = dropObject2Monsterinfos.get(line);
                if (monsterInfos != null) {
                    for (Monster monster : monsterInfos) {
                        System.out.println(monster);
                    }
                }
                else {
                    System.out.println("null");
                }
            }
        }
    }

    public static void generateDropObjectRelation() throws IOException, ClassNotFoundException {
        File file = new File(CrawlMonsterInfo.MONSTERINFO_MAC);
        File[] files = file.listFiles();
        for (File file1 : files) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file1));
            Monster monster = (Monster) ois.readObject();
            System.out.println(monster);
            String monsterName = monster.getMonsterName();
            List<DropObject> dropObjects = monster.getDropObjects();
            List<String> occuredMapNames = monster.getOccuredMapNames();
            for (DropObject dropObject : dropObjects) {
                if (dropObject2MonsterNames.containsKey(dropObject.getName())){
                    dropObject2MonsterNames.get(dropObject.getName()).add(monsterName);
                }
                else {
                    HashSet<String> monsterNameSet = new HashSet<String>();
                    monsterNameSet.add(monsterName);
                    dropObject2MonsterNames.put(dropObject.getName(), monsterNameSet);
                }
                if (dropObject2MapNames.containsKey(dropObject.getName())){
                    dropObject2MapNames.get(dropObject.getName()).addAll(occuredMapNames);
                }
                else {
                    HashSet<String> mapNameSet = new HashSet<>();
                    mapNameSet.addAll(occuredMapNames);
                    dropObject2MapNames.put(dropObject.getName(), mapNameSet);
                }
                if (dropObject2Monsterinfos.containsKey(dropObject.getName())){
                    dropObject2Monsterinfos.get(dropObject.getName()).add(monster);
                }
                else {
                    HashSet<Monster> monsterSet = new HashSet<>();
                    monsterSet.add(monster);
                    dropObject2Monsterinfos.put(dropObject.getName(), monsterSet);
                }
            }
        }
    }
}
