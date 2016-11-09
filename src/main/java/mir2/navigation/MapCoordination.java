package mir2.navigation;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by yangwenjie on 16/10/28.
 */
@Getter
@Setter
public class MapCoordination {
    public MapCoordination(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int x;
    public int y;

    @Override
    public String toString() {
        return "x=" + x +
                ", y=" + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapCoordination that = (MapCoordination) o;

        if (x != that.x) return false;
        return y == that.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
