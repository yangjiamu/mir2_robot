package mir2.map;

import mir2.util.Misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 地图工具类<br>TODO 处理门 
 */
public class MapHolder {

	/** log */
	/**
	 * 地图磁块宽
	 * <br>
	 * 逻辑坐标点的屏幕像素宽度
	 */
	private final static byte PIXEL_WIDTH_PER_TILE = 48;
	/**
	 * 地图磁块高
	 * <br>
	 * 逻辑坐标点的屏幕像素高度
	 */
	private final static byte PIXEL_HEIGHT_PER_TILE = 32;
	/** 绘制地图时向左延伸块儿数量 */
	private final static byte EXTEND_LEFT = 5;
	/** 绘制地图时向右延伸块儿数量 */
	private final static byte EXTEND_RIGHT = 5;
	/** 绘制地图时向下延伸块儿数量 */
	private final static byte EXTEND_BOTTOM = 5;

	/** 游戏进行中的地图 */
	private Map map;
	/** 地图宽度 */
	private int mw;
	/** 地图高度 */
	private int mh;
	/** 角色身处横坐标 */
	private short x;

	/** 获取角色身处x */

	/** 角色身处纵坐标 */
	private short y;

	/** 获取角色身处y */

	/** 地图绘制区域左上角(相对于游戏区域直角坐标系) */
	private short px;
	/** 地图绘制区域右上角(相对于游戏区域直角坐标系) */
	private short py;
	/** 地图绘制区域宽度 */
	private short gw;
	/** 地图绘制区域高度 */
	private short gh;
	/** 绘图区域左上角为地图块第几列 */
	private short tws;
	/** 绘图区域左上角为地图块第几行 */
	private short ths;
	/** 绘制区域右下角为地图块第几列 */
	private short twe;
	/** 绘制区域右下角为地图块第几行 */
	private short the;

	/**
	 * 纹理图片需要准备的坐标左上角列数
	 * <br>
	 * 对于地图绘制而要，需要先预测角色可能出现的坐标
	 * <br>
	 * 我们将绘制区域2倍大小作为预测的角色可能出现的位置
	 */
	private short pws;
	/**
	 * 纹理图片需要准备的左上角行数
	 */
	private short phs;
	/**
	 * 纹理图片需要准备的右下角列数
	 */
	private short pwe;
	/**
	 * 纹理图片需要准备的右下角行数
	 */
	private short phe;

	/* 单例 */
	public MapHolder() { }

	/**
	 * 从本地文件读取地图信息
	 * XXX 感谢星缘提供的思路<br>使用字节数组而非流读取提升效率
	 * 
	 * @param mapNo
	 *            地图编号
	 * @return 地图实体
	 */
	public Map readMapFromFile(Integer mapNo) {
		try{
			String mapInfoPath = this.getClass().getResource("/Map/" + String.valueOf(mapNo) + ".map").getPath();
			FileInputStream fis = new FileInputStream(new File(mapInfoPath));
			Map res = new Map();
			byte[] bytes = new byte[4];
			fis.read(bytes);
			res.setWidth(Misc.readShort(bytes, 0, true));
			res.setHeight(Misc.readShort(bytes, 2, true));
			fis.skip(48);
			MapTileInfo[][] mapTileInfos = new MapTileInfo[res.getWidth()][res.getHeight()];
			byte[] datas = new byte[12];
			for (int width = 0; width < res.getWidth(); ++width)
				for (int height = 0; height < res.getHeight(); ++height) {
					fis.read(datas);
					mapTileInfos[width][height] = Misc.readMapTileInfo(datas);
					if (width % 2 != 0 || height % 2 != 0)
						mapTileInfos[width][height].setHasBng(false);
				}
			res.setMapTiles(mapTileInfos);
			return res;
		} catch (FileNotFoundException ex) {
		} catch (IOException ex) {
		}
		return null;
	}
}