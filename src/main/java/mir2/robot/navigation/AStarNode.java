package mir2.robot.navigation;

/**
 * Created by yang on 2016/11/8.
 */
public class AStarNode {
    public AStarNode(int x, int y) {
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

    public AStarNode parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AStarNode aStarNode = (AStarNode) o;

        if (x != aStarNode.x) return false;
        return y == aStarNode.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "AStarNode{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
