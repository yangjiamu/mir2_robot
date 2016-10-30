package mir2;

/**
 * Created by yangwenjie on 16/10/25.
 */
public class Coordinate {
    private int x;
    private int y;
    public Coordinate(){
        this(0, 0);
    }
    public Coordinate(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
