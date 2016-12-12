package mir2.robot.navigation;

/**
 * Created by yang on 2016/11/14.
 */
public class CoordinateSmallMapPositionConverter {
    private static final int ROLE_START_X_IN_SMALL_MAP = 59;    //index start from 1
    private static final int ROLE_END_X_IN_SMALL_MAP = 60;
    private static final int ROLE_START_Y_IN_SMALL_MAP = 59;    //index start from 1
    private static final int ROLE_END_Y_IN_SMALL_MAP = 62;

    private static int[] pixelDiffToCoordDiff= {0,1,3,4,6,7,9,10,12,13,15,16,18,19,21,22,24,25,27,
            28,30,31,33,34,36,37,39,40,42,43,45,46,48,49,51,52,54,55,57,58,60};
    public static Coordinate convertToCoordination(Coordinate current, int x, int y){
        int coordDiffInY = Math.abs(y - ROLE_START_Y_IN_SMALL_MAP);
        int pixelDiffInX = Math.abs(x - ROLE_START_X_IN_SMALL_MAP);
        int coordDiffInX = 0;
        for (int i = 0; i < pixelDiffToCoordDiff.length; i++) {
            if(pixelDiffToCoordDiff[i] == pixelDiffInX) {
                coordDiffInX = i;
                break;
            }
        }
        int coordX = (x > ROLE_START_X_IN_SMALL_MAP) ? (current.getX() + coordDiffInX) : (current.getX() - coordDiffInX);
        int coordY = (y > ROLE_START_Y_IN_SMALL_MAP) ? (current.getY() + coordDiffInY) : (current.getY() - coordDiffInY);
        return new Coordinate(coordX, coordY);
    }

    public static void main(String[] args) {
        int pixelDif = 0;
        int i= 0;
        StringBuilder sb = new StringBuilder();
        while (pixelDif < 60){
            if(i%2 == 0){
                pixelDif+=1;
            }
            else {
                pixelDif +=2;
            }
            System.out.println("coordination diff:" + i + " pixel diff:" + pixelDif);
            ++i;
            sb.append(pixelDif);
            sb.append(",");
        }
        System.out.println(sb.toString());
    }
}
