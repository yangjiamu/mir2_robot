package mir2.map;

/**
 * Created by yangwenjie on 16/10/28.
 */
public class Node {
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x;
    public int y;

    public int F;
    public int G;
    public int H;

    public void calcF() {
        this.F = this.G + this.H;
    }

    public Node parent;
}
