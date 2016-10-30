package mir2.map;

/**
 * 热血传奇2地图
 * <br>
 * 即地图文件(*.map)到Java中数据结构的描述
 * <br>
 * 即一个MapHeader和一个MapTile二维数组
 * <br>
 * 但实际上不使用MapHeader和MapTile，因为MapHeader和MapTile数据太散，不便于使用
 * <br>
 * 而是将MapHeader中关键地图信息提取出来放到Map里，将MapTile重新解析为MapTileInfo以方便程序逻辑
 * 
 * @author ShawRyan
 *
 */
public class Map {
	
	/** 地图编号 */
	private String no;
	/** 地图宽度 */
	private short width;
	/** 地图高度 */
	private short height;
	/** 地图块数据 */
	private MapTileInfo[][] tiles;
	
	/** 无参构造函数 */
	public Map() { }
	/** 全参构造函数 */
	public Map(String no, short width, short height, MapTileInfo[][] mapTiles) {
		this.no = no;
		this.width = width;
		this.height = height;
		this.tiles = mapTiles;
	}
	/** 基于已有实例构造对象 */
	public Map(Map map) {
		this.no = map.no;
		this.width = map.width;
		this.height = map.height;
		this.tiles = map.tiles;
	}
	
	/** 获取地图编号 */
	public String getNo() {
		return no;
	}
	/** 设置地图编号 */
	public void setNo(String no) {
		this.no = no;
	}
	/** 获取地图宽度 */
	public short getWidth() {
		return width;
	}
	/** 设置地图宽度 */
	public void setWidth(short width) {
		this.width = width;
	}
	/** 获取地图高度 */
	public short getHeight() {
		return height;
	}
	/** 设置地图高度 */
	public void setHeight(short height) {
		this.height = height;
	}
	/** 获取地图块信息 */
	public MapTileInfo[][] getTiles() {
		return tiles;
	}
	/** 设置地图块信息 */
	public void setMapTiles(MapTileInfo[][] mapTiles) {
		this.tiles = mapTiles;
	}
}