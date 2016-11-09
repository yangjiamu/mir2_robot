package mir2.navigation;

/**
 * Created by yangwenjie on 16/10/28.
 */
import lombok.Getter;
import lombok.Setter;
import mir2.map.MapTileInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
@Setter
public class AStar {
    public static final int STEP = 10;

    public MapTileInfo[][] getTiles() {
        return tiles;
    }

    public void setTiles(MapTileInfo[][] tiles) {
        this.tiles = tiles;
    }

    //private boolean[][] nodes;//nodes[i][j]=true if there is an obstacle
    private MapTileInfo[][] tiles;
    private ArrayList<AStarNode> openList = new ArrayList<>();
    private ArrayList<AStarNode> closeList = new ArrayList<>();

    public AStar(){

    }

    public AStar(MapTileInfo [][] tiles){

    }
    /*public AStar(boolean [][] nodes){
        this.nodes = nodes;
    }*/

    public AStarNode findMinFNodeInOpenList() {
        AStarNode tempNode = openList.get(0);
        for (AStarNode node : openList) {
            if (node.F < tempNode.F) {
                tempNode = node;
            }
        }
        return tempNode;
    }

    public ArrayList<AStarNode> findNeighborNodes(AStarNode currentNode) {
        ArrayList<AStarNode> arrayList = new ArrayList<>();
        int x, y;
        //top
        x = currentNode.x;
        y = currentNode.y - 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        //bottom
        x = currentNode.x;
        y = currentNode.y + 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        //left
        x = currentNode.x - 1;
        y = currentNode.y;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        //right
        x = currentNode.x + 1;
        y = currentNode.y;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        //top left
        x = currentNode.x - 1;
        y = currentNode.y - 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        //top right
        x = currentNode.x - 1;
        y = currentNode.y + 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        //bottom left
        x = currentNode.x + 1;
        y = currentNode.y - 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }

        x = currentNode.x + 1;
        y = currentNode.y + 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new AStarNode(x, y));
        }
        return arrayList;
    }

    public boolean canReach(int x, int y) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length) {
            return tiles[x][y].isCanWalk();
        }
        return false;
    }

    public AStarNode findPath(MapCoordination start, MapCoordination end) {
        AStarNode startNode = new AStarNode(start.getX(), start.getY());
        AStarNode endNode = new AStarNode(end.getX(), end.getY());
        // 把起点加入 open list
        openList.add(startNode);
        while (openList.size() > 0) {
            // 遍历 open list ，查找 F值最小的节点，把它作为当前要处理的节点
            AStarNode currentNode = findMinFNodeInOpenList();
            // 从open list中移除
            openList.remove(currentNode);
            // 把这个节点移到 close list
            closeList.add(currentNode);
            ArrayList<AStarNode> neighborNodes = findNeighborNodes(currentNode);
            for (AStarNode node : neighborNodes) {
                if (exists(openList, node)) {
                    foundPoint(currentNode, node);
                } else {
                    notFoundPoint(currentNode, endNode, node);
                }
            }
            if (find(openList, endNode) != null) {
                return find(openList, endNode);
            }
        }
        return find(openList, endNode);
    }

    public List<MapCoordination> buildPath(AStarNode node){
        Stack<AStarNode> stack = new Stack<>();
        List<MapCoordination> path = new ArrayList<>(stack.size());
        while (node != null){
            stack.push(new AStarNode(node.x, node.y));
            node = node.parent;
        }
        stack.pop();//drop the start point
        while (!stack.isEmpty()){
            AStarNode top = stack.pop();
            path.add(new MapCoordination(top.x, top.y));
        }
        return path;
    }

    private void foundPoint(AStarNode tempStart, AStarNode node) {
        int G = calcG(tempStart, node);
        if (G < node.G) {
            node.parent = tempStart;
            node.G = G;
            node.calcF();
        }
    }

    private void notFoundPoint(AStarNode tempStart, AStarNode end, AStarNode node) {
        node.parent = tempStart;
        node.G = calcG(tempStart, node);
        node.H = calcH(end, node);
        node.calcF();
        openList.add(node);
    }

    private int calcG(AStarNode start, AStarNode mapCoordination) {
        int G = STEP;
        int parentG = mapCoordination.parent != null ? mapCoordination.parent.G : 0;
        return G + parentG;
    }

    private int calcH(AStarNode end, AStarNode mapCoordination) {
        int step = Math.abs(mapCoordination.x - end.x) + Math.abs(mapCoordination.y - end.y);
        return step * STEP;
    }

    public static AStarNode find(List<AStarNode> mapCoordinations, AStarNode point) {
        for (AStarNode n : mapCoordinations)
            if ((n.x == point.x) && (n.y == point.y)) {
                return n;
            }
        return null;
    }

    public static boolean exists(List<AStarNode> nodes, AStarNode node) {
        for (AStarNode n : nodes) {
            if ((n.x == node.x) && (n.y == node.y)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exists(List<AStarNode> nodes, int x, int y) {
        for (AStarNode n : nodes) {
            if ((n.x == x) && (n.y == y)) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        /*int[][] NODES = {
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        };
        boolean[][] BOOLEAN_NODES = new boolean[NODES.length][NODES[0].length];
        for (int i = 0; i < NODES.length; i++) {
            for (int j = 0; j < NODES[0].length; j++) {
                if(NODES[i][j] == 1){
                    BOOLEAN_NODES[i][j] = true;
                }
                else {
                    BOOLEAN_NODES[i][j] = false;
                }
            }
        }

        MapCoordination startNode = new MapCoordination(5, 1);
        MapCoordination endNode = new MapCoordination(5, 5);
        long mark = System.currentTimeMillis();
        MapCoordination parent = new AStar(BOOLEAN_NODES).findPath(startNode, endNode);
        System.out.println(System.currentTimeMillis() - mark);

        for (int i = 0; i < NODES.length; i++) {
            for (int j = 0; j < NODES[0].length; j++) {
                System.out.print(NODES[i][j] + ", ");
            }
            System.out.println();
        }
        ArrayList<MapCoordination> arrayList = new ArrayList<MapCoordination>();

        while (parent != null) {
            // System.out.println(parent.x + ", " + parent.y);
            arrayList.add(new MapCoordination(parent.x, parent.y));
            parent = parent.parent;
        }
        System.out.println("\n");

        for (int i = 0; i < NODES.length; i++) {
            for (int j = 0; j < NODES[0].length; j++) {
                if (exists(arrayList, i, j)) {
                    System.out.print("@, ");
                } else {
                    System.out.print(NODES[i][j] + ", ");
                }

            }
            System.out.println();
        }*/

    }

}
