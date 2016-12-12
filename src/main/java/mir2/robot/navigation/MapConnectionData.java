package mir2.robot.navigation;

import lombok.Data;

/**
 * Created by yang on 2016/11/21.
 */
@Data
public class MapConnectionData {
    public MapConnectionData(){

    }
    public MapConnectionData(String mapName, String nextMap){
        this.mapName = mapName;
        this.nextMap = nextMap;
    }
    private String mapName;
    private String nextMap;
    private Coordinate enterCoordinate;
    private Coordinate coordinateAfterEnter;
}
