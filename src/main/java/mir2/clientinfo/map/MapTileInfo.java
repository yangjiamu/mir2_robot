package mir2.clientinfo.map;

/**
 * MapTile方便程序逻辑的另类解读方式
 * 
 * @author ShawRyan
 *
 */
public class MapTileInfo {
	/** 背景图索引 */
	private short bngImgIdx;
	/** 是否有背景图(在热血传奇2地图中，背景图大小为4个地图块，具体到绘制地图时则表现在只有横纵坐标都为双数时才绘制) */
	private boolean hasBng;
	/** 是否可行走(站立) */
	private boolean canWalk;
	/** 补充背景图索引 */
	private short midImgIdx;
	/** 是否有补充图 */
	private boolean hasMid;
	/** 对象图索引 */
	private short objImgIdx;
	/** 是否有对象图 */
	private boolean hasObj;
	/** 是否可以飞越 */
	private boolean canFly;
	/** 门索引 */
	private byte doorIdx;
	/** 是否有门 */
	private boolean hasDoor;
	/** 门偏移 */
	private byte doorOffset;
	/** 门是否开启 */
	private boolean doorOpen;
	/** 动画帧数 */
	private byte aniFrame;
	/** 是否有动画 */
	private boolean hasAni;
	/** 动画跳帧数 */
	private byte aniTick;
	/** 资源文件索引 */
	private byte objFileIdx;
	/** 光线 */
	private byte light;
	
	/** 无参构造函数 */
	public MapTileInfo() { }
	/** 带全部参数的构造函数 */
	public MapTileInfo(short bngImgIdx, boolean hasBng, boolean canWalk, short midImgIdx, boolean hasMid, short objImgIdx, boolean hasObj, boolean canFly, byte doorIdx, boolean hasDoor, byte doorOffset, boolean doorOpen, byte aniFrame, boolean hasAni, byte aniTick, byte objFileIdx, byte light) {
		this.bngImgIdx = bngImgIdx;
		this.hasBng = hasBng;
		this.canWalk = canWalk;
		this.midImgIdx = midImgIdx;
		this.hasMid = hasMid;
		this.objImgIdx = objImgIdx;
		this.hasObj = hasObj;
		this.canFly = canFly;
		this.doorIdx = doorIdx;
		this.hasDoor = hasDoor;
		this.doorOffset = doorOffset;
		this.doorOpen = doorOpen;
		this.aniFrame = aniFrame;
		this.hasAni = hasAni;
		this.aniTick = aniTick;
		this.objFileIdx = objFileIdx;
		this.light = light;
	}
	/** 基于已有实体构造对象 */
	public MapTileInfo(MapTileInfo mapTileInfo) {
		this.bngImgIdx = mapTileInfo.bngImgIdx;
		this.hasBng = mapTileInfo.hasBng;
		this.canWalk = mapTileInfo.canWalk;
		this.midImgIdx = mapTileInfo.midImgIdx;
		this.hasMid = mapTileInfo.hasMid;
		this.objImgIdx = mapTileInfo.objImgIdx;
		this.hasObj = mapTileInfo.hasObj;
		this.canFly = mapTileInfo.canFly;
		this.doorIdx = mapTileInfo.doorIdx;
		this.hasDoor = mapTileInfo.hasDoor;
		this.doorOffset = mapTileInfo.doorOffset;
		this.doorOpen = mapTileInfo.doorOpen;
		this.aniFrame = mapTileInfo.aniFrame;
		this.hasAni = mapTileInfo.hasAni;
		this.aniTick = mapTileInfo.aniTick;
		this.objFileIdx = mapTileInfo.objFileIdx;
		this.light = mapTileInfo.light;
	}

	/** 获取背景图索引 */
	public short getBngImgIdx() {
		return bngImgIdx;
	}
	/** 设置背景图索引 */
	public void setBngImgIdx(short bngImgIdx) {
		this.bngImgIdx = bngImgIdx;
	}
	/** 获取该地图块是否有背景图 */
	public boolean isHasBng() {
		return hasBng;
	}
	/** 设置该地图块是否有背景图 */
	public void setHasBng(boolean hasBng) {
		this.hasBng = hasBng;
	}
	/** 获取该地图块是否可以站立或走过 */
	public boolean isCanWalk() {
		return canWalk;
	}
	/** 设置该地图块是否可以站立或走过 */
	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}
	/** 获取补充图索引 */
	public short getMidImgIdx() {
		return midImgIdx;
	}
	/** 设置补充图索引 */
	public void setMidImgIdx(short midImgIdx) {
		this.midImgIdx = midImgIdx;
	}
	/** 获取该地图块是否有补充图 */
	public boolean isHasMid() {
		return hasMid;
	}
	/** 设置该地图块是否有补充图 */
	public void setHasMid(boolean hasMid) {
		this.hasMid = hasMid;
	}
	/** 获取对象图索引 */
	public short getObjImgIdx() {
		return objImgIdx;
	}
	/** 设置对象图索引 */
	public void setObjImgIdx(short objImgIdx) {
		this.objImgIdx = objImgIdx;
	}
	/** 获取该地图块是否有对象图 */
	public boolean isHasObj() {
		return hasObj;
	}
	/** 设置该地图块是否有对象图 */
	public void setHasObj(boolean hasObj) {
		this.hasObj = hasObj;
	}
	/** 获取该地图块是否可以飞越 */
	public boolean isCanFly() {
		return canFly;
	}
	/** 设置该地图块是否可以飞越 */
	public void setCanFly(boolean canFly) {
		this.canFly = canFly;
	}
	/** 获取门索引 */
	public byte getDoorIdx() {
		return doorIdx;
	}
	/** 设置门索引 */
	public void setDoorIdx(byte doorIdx) {
		this.doorIdx = doorIdx;
	}
	/** 获取该地图块是否有门 */
	public boolean isHasDoor() {
		return hasDoor;
	}
	/** 设置该地图块是否有门 */
	public void setHasDoor(boolean hasDoor) {
		this.hasDoor = hasDoor;
	}
	/** 获取门偏移 */
	public byte getDoorOffset() {
		return doorOffset;
	}
	/** 设置门偏移 */
	public void setDoorOffset(byte doorOffset) {
		this.doorOffset = doorOffset;
	}
	/** 获取该地图块门是否打开 */
	public boolean isDoorOpen() {
		return doorOpen;
	}
	/** 设置该地图块门是否打开 */
	public void setDoorOpen(boolean doorOpen) {
		this.doorOpen = doorOpen;
	}
	/** 获取动画帧数 */
	public byte getAniFrame() {
		return aniFrame;
	}
	/** 设置动画帧数 */
	public void setAniFrame(byte aniFrame) {
		this.aniFrame = aniFrame;
	}
	/** 获取该地图块是否有动画 */
	public boolean isHasAni() {
		return hasAni;
	}
	/** 设置该地图块是否有动画 */
	public void setHasAni(boolean hasAni) {
		this.hasAni = hasAni;
	}
	/** 获取动画跳帧数 */
	public byte getAniTick() {
		return aniTick;
	}
	/** 设置动画跳帧数 */
	public void setAniTick(byte aniTick) {
		this.aniTick = aniTick;
	}
	/** 获取资源文件索引 */
	public byte getObjFileIdx() {
		return objFileIdx;
	}
	/** 设置资源文件索引 */
	public void setObjFileIdx(byte objFileIdx) {
		this.objFileIdx = objFileIdx;
	}
	/** 获取亮度 */
	public byte getLight() {
		return light;
	}
	/** 设置亮度 */
	public void setLight(byte light) {
		this.light = light;
	}
}