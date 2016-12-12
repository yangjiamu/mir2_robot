package mir2.robot.scan;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import mir2.robot.enums.TagTypeEnum;
import mir2.robot.navigation.CoordinateSmallMapPositionConverter;
import mir2.robot.navigation.Navigator;
import mir2.robot.navigation.Coordinate;
import mir2.robot.screen.Mir2Screen;
import mir2.util.ColorUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yang on 2016/11/14.
 */
public class SmallMapScanner {
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();
    private static Navigator navigator  = Navigator.getInstance();

    public List<Coordinate> getObstacles(Coordinate current){
        List<Coordinate> obstacles = new ArrayList<>();
        BufferedImage smallMap = mir2Screen.getSmallMap();
        obstacles.addAll(getMonsterAndArcherCoordinate(smallMap));
        obstacles.addAll(getNPCAndGuardCoordinate(smallMap));
        return obstacles;
    }
    public List<Coordinate> getMonsterAndArcherCoordinate(){
        BufferedImage smallMap = mir2Screen.getSmallMap();
        //ImageIO.write(smallMap, "png", new File("C:\\Users\\yang\\Pictures\\smallMap.png"));
        return getMonsterAndArcherCoordinate(smallMap);
    }

    public Coordinate nearestMonster(final Coordinate current){
        List<Coordinate> monsterAndArcherCoordinate = getMonsterAndArcherCoordinate();
        if(monsterAndArcherCoordinate == null || monsterAndArcherCoordinate.isEmpty())
            return null;
        Collections.sort(monsterAndArcherCoordinate, new Comparator<Coordinate>() {
            @Override
            public int compare(Coordinate o1, Coordinate o2) {
                return Coordinate.ChebyshevDistance(current, o1) - Coordinate.ChebyshevDistance(current, o2);
            }
        });
        return monsterAndArcherCoordinate.get(0);
    }
    public List<Coordinate> getMonsterAndArcherCoordinate(BufferedImage smallMap){
        List<Coordinate> all = new ArrayList<>();
        all.addAll(getCoordinateByTag(smallMap, TagTypeEnum.SYSTEM_MONSTER_TAG));
        return all;
    }

    public List<Coordinate> getNPCAndGuardCoordinate(BufferedImage smallMap){
        List<Coordinate> npcAndGuard = new ArrayList<>();
        npcAndGuard.addAll(getCoordinateByTag(smallMap, TagTypeEnum.SYSTEM_GUARD_AND_NPC_TAG));
        return npcAndGuard;
    }

    public List<Coordinate> getCoordinateByTag(BufferedImage smallMap, TagTypeEnum tag){
        final Coordinate current = navigator.currentCoordinate();
        List<Coordinate> coordinateList = new ArrayList<>();
        List<Point> matchPoints = new ArrayList<>();
        for (int i = 0; i < smallMap.getWidth() - tag.getTagWidth(); i++) {
            for (int j = 0; j < smallMap.getHeight() - tag.getTagHeight(); j++) {
                if(smallMap.getRGB(i, j) == tag.getColor().getJavaColor().getRGB()){
                    int w=0, h=0;
                    for (w=0; w < tag.getTagWidth(); ++w) {
                        for (h=0; h < tag.getTagHeight(); ++h) {
                            if(!(smallMap.getRGB(i+w, j+h) == tag.getColor().getJavaColor().getRGB()))
                                break;
                        }
                        if(h != tag.getTagHeight())
                            break;
                    }
                    if((w== tag.getTagWidth()) && (h== tag.getTagHeight())) {
                        if(matchPoints.isEmpty()){
                            matchPoints.add(new Point(i+1, j+1));
                        }
                        else {
                            boolean alreadyFound = false;
                            for (Point matchPoint : matchPoints) {
                                if((i+1) == matchPoint.getX() && (j+1) >= matchPoint.getY() && (j+1) <= (matchPoint.getY() + 2)
                                        || (j+1) == matchPoint.getY() && (i+1) >= matchPoint.getX() && (i+1) <= (matchPoint.getX() + 2)){
                                    alreadyFound = true;
                                    break;
                                }
                            }
                            if(!alreadyFound){
                                matchPoints.add(new Point(i+1, j+1));
                            }
                        }
                    }
                }
            }
        }
        coordinateList.addAll(Collections2.transform(matchPoints, new Function<Point, Coordinate>() {
            @Override
            public Coordinate apply(Point point) {
                return CoordinateSmallMapPositionConverter.convertToCoordination(current, (int)point.getX(), (int)point.getY());
            }
        }));
        return coordinateList;
    }

    public void test(){
        BufferedImage smallMap = mir2Screen.getSmallMap();
        System.out.println(
                ColorUtil.isRgbSimilar(smallMap.getRGB(56, 57), TagTypeEnum.GUARD_AND_ARCHER_TAG.getColor().getJavaColor().getRGB(), 50));
    }
}
