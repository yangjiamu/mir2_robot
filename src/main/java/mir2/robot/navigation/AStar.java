package mir2.robot.navigation;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

/**
 * Created by yang on 2016/11/18.
 */
@Getter
@Setter
public class AStar {
    public static final int STEP = 1;
    private byte[][] gridMap;
    AStarNode startNode = null;
    private Comparator<AStarNode> fComparaotr = new Comparator<AStarNode>() {
        @Override
        public int compare(AStarNode o1, AStarNode o2) {
            return (int)(o1.F - o2.F);
        }
    };
    private PriorityQueue<AStarNode> open = new PriorityQueue<>(fComparaotr);
    private Set<AStarNode> openSet = new HashSet<>();
    private Set<AStarNode> close = new HashSet<>();
    private int direction = 1;
    private int[][] directionMap = {{1,2,3},
            {4,0,5},
            {6,7,8}};
    public AStar(){

    }

    public AStar(byte[][] gridMap){
        this.gridMap = gridMap;
    }

    public ArrayList<AStarNode> neighborNodes(AStarNode currentNode) {
        ArrayList<AStarNode> arrayList = new ArrayList<>();
        int x, y;
        //top
        x = currentNode.x;
        y = currentNode.y - 1;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        //bottom
        x = currentNode.x;
        y = currentNode.y + 1;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        //left
        x = currentNode.x - 1;
        y = currentNode.y;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        //right
        x = currentNode.x + 1;
        y = currentNode.y;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        //top left
        x = currentNode.x - 1;
        y = currentNode.y - 1;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        //top right
        x = currentNode.x - 1;
        y = currentNode.y + 1;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        //bottom left
        x = currentNode.x + 1;
        y = currentNode.y - 1;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }

        x = currentNode.x + 1;
        y = currentNode.y + 1;
        if (canReach(x, y) && !close.contains(new AStarNode(x, y))) {
            arrayList.add(new AStarNode(x, y));
        }
        return arrayList;
    }

    public boolean canReach(int x, int y) {
        if (inBound(x, y)) {
            return gridMap[x][y] == 0;
        }
        return false;
    }

    private boolean inBound(int x, int y){
        return x>=0 && x< gridMap.length && y>0 && y< gridMap[0].length;
    }

    public AStarNode findPath(Coordinate start, Coordinate end) {
        startNode = new AStarNode(start.getX(), start.getY());
        AStarNode endNode = new AStarNode(end.getX(), end.getY());
        startNode.parent = new AStarNode(startNode.x, startNode.y-1);
        // add start node to open list
        open.add(startNode);
        openSet.add(startNode);
        while (!open.isEmpty()) {
            // 遍历 open list ，查找 F值最小的节点，把它作为当前要处理的节点
            //AStarNode currentNode = open.remove();
            AStarNode currentNode = minInOpenList(open);
            openSet.remove(currentNode);
            // 把这个节点移到 close list
            close.add(currentNode);
            ArrayList<AStarNode> neighborNodes = neighborNodes(currentNode);
            for (AStarNode node : neighborNodes) {
                if (openSet.contains(node)) {//already in open list
                    updateF(currentNode, node);
                } else {
                    addToOpenList(currentNode, endNode, node);
                }
            }
            if (openSet.contains(endNode)) {
                return find(open, endNode);
            }
        }
        if(open.contains(endNode))
            return find(open, endNode);
        return null;
    }

    public List<Coordinate> buildForwardPath(AStarNode node){
        Stack<AStarNode> stack = new Stack<>();
        List<Coordinate> path = new LinkedList<>();
        while (node != null){
            stack.push(new AStarNode(node.x, node.y));
            node = node.parent;
        }
        //stack.pop();//drop the start point
        while (!stack.isEmpty()){
            AStarNode top = stack.pop();
            path.add(new Coordinate(top.x, top.y));
        }
        return path;
    }

    private AStarNode minInOpenList(PriorityQueue<AStarNode> open){//对F值相同的点，选与起点欧式距离近的
        if(open.size() == 1) {
            AStarNode min = open.remove();
            return min;
        }
        AStarNode min = open.remove();
        List<AStarNode> sameFValueNodes = new ArrayList<>();
        sameFValueNodes.add(min);
        while (!open.isEmpty() && open.peek().F == min.F){
            sameFValueNodes.add(open.remove());
        }
        double minEuclideanDis = Double.MAX_VALUE;
        for (AStarNode node : sameFValueNodes) {
            if(euclideanDistance(startNode, node) < minEuclideanDis){
                minEuclideanDis = euclideanDistance(startNode, node);
                min = node;
            }
        }
        for (AStarNode node : sameFValueNodes) {
            if(!min.equals(node)){
                open.add(node);
            }
        }
        return min;
    }

    private AStarNode minInOpenList1(PriorityQueue<AStarNode> open){//尽量选方向不变的
        if(open.size() == 1) {
            AStarNode min = open.remove();
            if(min.parent != null){
                direction = computeDirection(min.parent, min);
            }
            return min;
        }
        AStarNode min = open.remove();
        List<AStarNode> sameFValueNodes = new ArrayList<>();
        sameFValueNodes.add(min);
        while (!open.isEmpty() && open.peek().F == min.F){
            sameFValueNodes.add(open.remove());
        }
        for (AStarNode node : sameFValueNodes) {
            if(direction == computeDirection(node.parent, node)) {
                min = node;
                break;
            }
        }
        direction = computeDirection(min.parent, min);
        for (AStarNode node : sameFValueNodes) {
            if(!node.equals(min)){
                open.add(node);
            }
        }
        return min;
    }

    private int computeDirection(AStarNode from, AStarNode to){
        /** 0   1   2
         *  3   4   5
         *  6   7   8
         */
        /*if(from.x == to.x){
            if(to.y > from.y)
                return  7;
            else return 1;
        }
        if(from.y == to.y){
            if(to.x > from.x)
                return  5;
            else
                return  3;
        }
        if(to.x < from.x){
            if(to.y < from.y)
                return 0;
            else return 6;
        }
        else {
            if(to.y < from.y)
                return 2;
            else return 8;
        }*/
        return directionMap[from.x - to.x + 1][from.y - to.y + 1];
    }

    private void updateF(AStarNode predecessor, AStarNode node) {
        int G = calcG(node);
        if (G < node.G) {
            node.parent = predecessor;
            node.G = G;
            node.calcF();
        }
    }

    private void addToOpenList(AStarNode predecessor, AStarNode end, AStarNode node) {
        node.parent = predecessor;
        node.G = calcG(node);
        node.H = calcH(end, node);
        node.calcF();
        //openList.add(node);
        open.add(node);
        openSet.add(node);
    }

    private int calcG(AStarNode node) {
        int G = STEP;
        int parentG = node.parent != null ? node.parent.G : 0;
        return G + parentG;
    }

    private int ChebyshevDistance(AStarNode node1, AStarNode node2){
        return STEP*Math.max(Math.abs(node1.x - node2.x), Math.abs(node1.y - node2.y));
    }

    private double euclideanDistance(AStarNode node1, AStarNode node2){
        int xDif = node1.x - node2.x;
        int yDif = node1.y - node2.y;
        return Math.sqrt(xDif*xDif + yDif*yDif);
    }
    private int calcH(AStarNode goal, AStarNode node) {
        //int hypothesisDistance = Math.abs(mapCoordination.x - end.x) + Math.abs(mapCoordination.y - end.y);
        int hypothesisDistance = ChebyshevDistance(goal, node);
        int startX = node.x - 2;
        int endX = node.x + 2;
        int startY = node.y - 2;
        int endY = node.y + 2;
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
                if(inBound(i, j)) {
                    if (gridMap[i][j] == 1) {//static obstacle
                        //System.out.println("i:"+i+" j:"+j);
                        //hypothesisDistance += 1;
                    } else if (gridMap[i][j] == 2) {//movable obstacle:monster
                        //System.out.println(i + "    " + j);
                        int d = ChebyshevDistance(new AStarNode(i, j), node);
                        if (d == 1) {
                            hypothesisDistance += 6;
                        } else if (d == 2) {
                            hypothesisDistance += 3;
                        }
                    }
                }
            }
        }
        return hypothesisDistance;
    }

    public  AStarNode find(PriorityQueue<AStarNode> openList, AStarNode node) {
        Iterator<AStarNode> iterator = openList.iterator();
        while (iterator.hasNext()){
            AStarNode t = iterator.next();
            if(t.equals(node)){
                return t;
            }
        }
        return null;
    }

    public static boolean exists1(List<Coordinate> coordinates, int x, int y) {
        for (Coordinate n : coordinates) {
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
        };*/
        byte[][] map = AStarTest.getMap();

        Coordinate startNode = new Coordinate(12, 18);
        Coordinate endNode = new Coordinate(0, 14);
        AStar aStar = new AStar(map);
        AStarNode parent = aStar.findPath(startNode, endNode);
        List<Coordinate> paths = aStar.buildForwardPath(parent);
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (exists1(paths, i, j)) {
                    System.out.print("@, ");
                } else {
                    System.out.print(map[i][j] + ", ");
                }

            }
            System.out.println();
        }

    }

}