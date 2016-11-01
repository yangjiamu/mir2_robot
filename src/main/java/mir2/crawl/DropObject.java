package mir2.crawl;

import java.util.Arrays;

/**
 * Created by yangwenjie on 16/10/30.
 */
public class DropObject {
    private String url;
    private String name;
    private int needLevel;
    private int otherRestriction;
    private int weight;
    private int valueLevel;
    private byte[] pic;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNeedLevel() {
        return needLevel;
    }

    public void setNeedLevel(int needLevel) {
        this.needLevel = needLevel;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getValueLevel() {
        return valueLevel;
    }

    public void setValueLevel(int valueLevel) {
        this.valueLevel = valueLevel;
    }

    public byte[] getPic() {
        return pic;
    }

    public void setPic(byte[] pic) {
        this.pic = pic;
    }

    public int getOtherRestriction() {
        return otherRestriction;
    }

    public void setOtherRestriction(int otherRestriction) {
        this.otherRestriction = otherRestriction;
    }

    @Override
    public String toString() {
        return "DropObject{" +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", needLevel=" + needLevel +
                ", otherRestriction=" + otherRestriction +
                ", weight=" + weight +
                ", valueLevel=" + valueLevel +
                ", pic=" + Arrays.toString(pic) +
                '}';
    }
}
