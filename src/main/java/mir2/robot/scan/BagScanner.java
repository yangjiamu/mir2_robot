package mir2.robot.scan;

import mir2.robot.Robot2;
import mir2.robot.enums.TagTypeEnum;
import mir2.robot.screen.Mir2Screen;
import mir2.robot.screen.Mir2ScreenRelativeConstant;
import mir2.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yang on 2016/11/23.
 */
public class BagScanner {
    private Mir2Screen mir2Screen = Mir2Screen.getInstance();

    private List<TagTypeEnum> itemTags = TagTypeEnum.itemTags();
    private TemplateMatch templateMatch = new TemplateMatch();
    private Point bagStartPoint;
    private Robot2 robot2 = Robot2.getInstance();
    private BufferedImage imageForBagLocate;

    public BagScanner(){
        try {
            String path = BagScanner.class.getResource("/forSmallBagMatch.png").getPath();
            imageForBagLocate = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isBagOpen(){
        Point leftTop = mir2Screen.gameScreenLeftTop();
        Point matchPoint = templateMatch.match(mir2Screen.gameScreenImage(), imageForBagLocate);
        return matchPoint.x > leftTop.x && matchPoint.y > leftTop.y
                && matchPoint.y < 100 && matchPoint.y < 100;
    }

    public BufferedImage[] smallBagGridImages(){
        return mir2Screen.getSmallBagImages();
    }

    public BufferedImage[][] bagGridImages(){
        BufferedImage imageForBagLocate = null;
        if(bagStartPoint == null) {
            Point matchPoint = templateMatch.match(mir2Screen.gameScreenImage(), imageForBagLocate);
            bagStartPoint = new Point(matchPoint.x + 14, matchPoint.y + 13);
        }
        return mir2Screen.getBagImages(bagStartPoint);
    }

    public TagTypeEnum[] recognizeItemInSmallBag(){
        BufferedImage[] smallBagGridImages = smallBagGridImages();
        TagTypeEnum[] itemTagInSmallBagGrid = new TagTypeEnum[smallBagGridImages.length];
        List<TagTypeEnum> matchedTags = new ArrayList<>();
        for (int i = 0; i < smallBagGridImages.length; i++) {
            matchedTags.clear();
            matchBagGridAndItem(matchedTags, smallBagGridImages[i]);
            if(!matchedTags.isEmpty()){
                if(matchedTags.size() > 1){
                    sortByTagSize(matchedTags);
                }
                itemTagInSmallBagGrid[i] = matchedTags.get(0);
            }
        }
        return itemTagInSmallBagGrid;
    }

    public TagTypeEnum[][] recognizeItemInBag(){
        BufferedImage[][] bagGridImages = bagGridImages();
        List<TagTypeEnum> matchedTags = new ArrayList<>();
        TagTypeEnum[][] itemTagInBagGrid = new TagTypeEnum[bagGridImages.length][bagGridImages[0].length];

        for (int i = 0; i < bagGridImages.length; i++) {
            for (int j = 0; j < bagGridImages[0].length; j++) {
                try {
                    ImageUtil.saveImage(bagGridImages[i][j], "bag_grid_"+i+"_"+j+".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                matchedTags.clear();
                matchBagGridAndItem(matchedTags, bagGridImages[i][j]);
                if(!matchedTags.isEmpty()){
                    if(matchedTags.size() > 1){
                        sortByTagSize(matchedTags);
                    }
                    itemTagInBagGrid[i][j] = matchedTags.get(0);
                }
            }
        }
        return itemTagInBagGrid;
    }

    public void updateBag(TagTypeEnum[][] bag, int i, int j){
        BufferedImage[][] bagGridImage = bagGridImages();
        List<TagTypeEnum> matchedTags = new ArrayList<>();
        matchBagGridAndItem(matchedTags, bagGridImage[i][j]);
        if(!matchedTags.isEmpty()){
            sortByTagSize(matchedTags);
            bag[i][j] = matchedTags.get(0);
        }
        else {
            bag[i][j] = null;
        }
    }

    private void matchBagGridAndItem(List<TagTypeEnum> matchedTags, BufferedImage bagGridImage){
        for (TagTypeEnum itemTag : itemTags) {
            if(templateMatch.tagMatch(bagGridImage, itemTag)){
                matchedTags.add(itemTag);
            }
        }
    }
    private void sortByTagSize(List<TagTypeEnum> tagList){//sort by tag size descent
        Collections.sort(tagList, new Comparator<TagTypeEnum>() {
            @Override
            public int compare(TagTypeEnum o1, TagTypeEnum o2) {
                return -(Integer.compare(o1.getTagWidth(), o2.getTagWidth()));
            }
        });
    }

    public Point getBagGridClickPosition(int i, int j){
        return new Point((int)(mir2Screen.gameScreenLeftTop().getX() + bagStartPoint.getX() + Mir2ScreenRelativeConstant.BAG_GRID_WIDTH*j +  Mir2ScreenRelativeConstant.BAG_GRID_WIDTH/2),
                (int)(mir2Screen.gameScreenLeftTop().getY() +  bagStartPoint.getY() + Mir2ScreenRelativeConstant.BAG_GRID_HEIGHT*i + Mir2ScreenRelativeConstant.BAG_GRID_HEIGHT/2));
    }
}
