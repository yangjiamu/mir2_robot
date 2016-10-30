package mir2.map;

/**
 * Created by yangwenjie on 16/10/28.
 */
import java.util.ArrayList;
import java.util.List;

public class AStar {
    public static final int STEP = 10;

    private boolean[][] nodes;//nodes[i][j]=true if there is an obstacle
    private ArrayList<Node> openList = new ArrayList<Node>();
    private ArrayList<Node> closeList = new ArrayList<Node>();

    public AStar(boolean [][] nodes){
        this.nodes = nodes;
        System.out.println(nodes.length + ":" + nodes[0].length);
    }
    public Node findMinFNodeInOpenList() {
        Node tempNode = openList.get(0);
        for (Node node : openList) {
            if (node.F < tempNode.F) {
                tempNode = node;
            }
        }
        return tempNode;
    }

    public ArrayList<Node> findNeighborNodes(Node currentNode) {
        ArrayList<Node> arrayList = new ArrayList<Node>();
        int x, y;
        //top
        x = currentNode.x;
        y = currentNode.y - 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        //bottom
        x = currentNode.x;
        y = currentNode.y + 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        //left
        x = currentNode.x - 1;
        y = currentNode.y;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        //right
        x = currentNode.x + 1;
        y = currentNode.y;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        //top left
        x = currentNode.x - 1;
        y = currentNode.y - 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        //top right
        x = currentNode.x - 1;
        y = currentNode.y + 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        //bottom left
        x = currentNode.x + 1;
        y = currentNode.y - 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }

        x = currentNode.x + 1;
        y = currentNode.y + 1;
        if (canReach(x, y) && !exists(closeList, x, y)) {
            arrayList.add(new Node(x, y));
        }
        return arrayList;
    }

    public boolean canReach(int x, int y) {
        if (x >= 0 && x < nodes.length && y >= 0 && y < nodes[0].length) {
            return nodes[x][y] == false;
        }
        return false;
    }

    public Node findPath(Node startNode, Node endNode) {

        // 把起点加入 open list
        openList.add(startNode);

        while (openList.size() > 0) {
            // 遍历 open list ，查找 F值最小的节点，把它作为当前要处理的节点
            Node currentNode = findMinFNodeInOpenList();
            // 从open list中移除
            openList.remove(currentNode);
            // 把这个节点移到 close list
            closeList.add(currentNode);

            ArrayList<Node> neighborNodes = findNeighborNodes(currentNode);
            for (Node node : neighborNodes) {
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

    private void foundPoint(Node tempStart, Node node) {
        int G = calcG(tempStart, node);
        if (G < node.G) {
            node.parent = tempStart;
            node.G = G;
            node.calcF();
        }
    }

    private void notFoundPoint(Node tempStart, Node end, Node node) {
        node.parent = tempStart;
        node.G = calcG(tempStart, node);
        node.H = calcH(end, node);
        node.calcF();
        openList.add(node);
    }

    private int calcG(Node start, Node node) {
        int G = STEP;
        int parentG = node.parent != null ? node.parent.G : 0;
        return G + parentG;
    }

    private int calcH(Node end, Node node) {
        int step = Math.abs(node.x - end.x) + Math.abs(node.y - end.y);
        return step * STEP;
    }

    public static Node find(List<Node> nodes, Node point) {
        for (Node n : nodes)
            if ((n.x == point.x) && (n.y == point.y)) {
                return n;
            }
        return null;
    }

    public static boolean exists(List<Node> nodes, Node node) {
        for (Node n : nodes) {
            if ((n.x == node.x) && (n.y == node.y)) {
                return true;
            }
        }
        return false;
    }

    public static boolean exists(List<Node> nodes, int x, int y) {
        for (Node n : nodes) {
            if ((n.x == x) && (n.y == y)) {
                return true;
            }
        }
        return false;
    }

    public static class Node {
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

    public static void main(String[] args) {
        int[][] NODES = {
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

        Node startNode = new Node(5, 1);
        Node endNode = new Node(5, 5);
        long mark = System.currentTimeMillis();
        Node parent = new AStar(BOOLEAN_NODES).findPath(startNode, endNode);
        System.out.println(System.currentTimeMillis() - mark);

        for (int i = 0; i < NODES.length; i++) {
            for (int j = 0; j < NODES[0].length; j++) {
                System.out.print(NODES[i][j] + ", ");
            }
            System.out.println();
        }
        ArrayList<Node> arrayList = new ArrayList<Node>();

        while (parent != null) {
            // System.out.println(parent.x + ", " + parent.y);
            arrayList.add(new Node(parent.x, parent.y));
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
        }

    }

}
