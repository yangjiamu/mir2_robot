package mir2.map;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.joot.jmir2.display.Graphics;
import org.joot.jmir2.display.OpenGL;
import org.joot.jmir2.exception.JMir2Exception;
import org.joot.jmir2.texture.Texture;
import org.joot.jmir2.texture.loader.TextureLoader;
import org.joot.jmir2.util.AllCache;
import org.joot.jmir2.util.Files;
import org.joot.jmir2.util.Misc;
import org.joot.jmir2.util.Wils.WILImageInfo;

/**
 * 地图工具类<br>TODO 处理门 
 */
public class MapHolder {

	/** log */
	private static Logger log = Logger.getLogger(MapHolder.class);
	/** 图形相关 */
	private Graphics graphics = Graphics.get();
	/** 显卡绘制相关 */
	private OpenGL gl = OpenGL.get();
	/** 缓存 */
	private AllCache cache = AllCache.get();
	/** 纹理加载器，当用户第一次进入地图时加载 */
	private WeakReference<java.util.Map<String, TextureLoader>> initLoaders = null;
	/** 地图卷动或绘制时掉纹理的补充 */
	private TextureLoader texLoader = null;
	
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
	public static short mhRoleX() {
		return instance.x;
	}

	/** 角色身处纵坐标 */
	private short y;

	/** 获取角色身处y */
	public static short mhRoleY() {
		return instance.y;
	}

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
	private MapHolder() { }
	private static MapHolder instance = new MapHolder();

	/** 进入某张地图(同步方式) */
	public static void mhEnterAsyn(String mapNo, short x, short y) {
		instance.map = readMapFromFile(mapNo);
		/*for(int i = 0; i < instance.map.getWidth(); ++i) {
			for(int j = 0; j < instance.map.getHeight(); ++j) {
				if(instance.map.getTiles()[i][j].getObjImgIdx() == 1727 || instance.map.getTiles()[i][j].getObjImgIdx() == 1734) {
					System.out.println();
				}
			}
		}*/
		// 地图宽度(像素)
		instance.mw = instance.map.getWidth() * PIXEL_WIDTH_PER_TILE;
		// 地图高度(像素)
		instance.mh = instance.map.getHeight() * PIXEL_HEIGHT_PER_TILE;
		update(x, y);
		loadTexturesAsyn();
	}
	
	/** 异步进入地图 */
	public static void mhEnterSync(String mapNo, short x, short y/* 此处应该加入进度回调接口 */) {
		// TODO 异步加载资源时使用回调
	}

	/** 移动到地图某位置 */
	public static void mhMove(short x, short y) {
		update(x, y);
		//loadTextureSync();
	}

	/** 绘制地图 */
	public static void mhDraw() {
		if (instance.initLoaders.get() != null) {
			java.util.Map<String, TextureLoader> initLoaders = instance.initLoaders.get();
			float count = 0;
			float percents = 0;
			Iterator<String> keys = initLoaders.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				TextureLoader loader = initLoaders.get(key);
				if(loader.getProgress() == 1) {
					initLoaders.get(key).dispose();
					keys.remove();
				} else {
					count += 1;
					percents += loader.getProgress();
				}
			}
			if(instance.initLoaders.get().isEmpty()) instance.initLoaders.clear();
			if(count != percents) {
				// 绘制百分比
				//System.out.println(percents / count * 100 + "%");
				return;
			}
		}
		if(instance.texLoader == null) instance.texLoader = TextureLoader.newInstance();
		
		// 对于地图数据，如果绘制的第一列为奇数，则大地砖不会显示，此处将绘制区域向左移，保证大地砖和动态地图/光线等正确绘制
		int left = instance.tws - EXTEND_LEFT;
		if(left < 0)
			left = 0;
		for(int w = left; w < instance.tws; ++w) {
			for (int h = instance.ths; h < instance.the; ++h) {
				MapTileInfo mti = instance.map.getTiles()[w][h];
				// 绘制左上角x
				int cpx = (int) (instance.px + (w - instance.tws) * PIXEL_WIDTH_PER_TILE);
				// 绘制左上角y
				int cpy = (int) (instance.py + (h - instance.ths) * PIXEL_HEIGHT_PER_TILE);
				if (mti.isHasBng()) {
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_TILES, 0, mti.getBngImgIdx());
					Texture tex = instance.cache.getTexture(index);
					if(tex == null) {
						instance.texLoader.load(index);
					} else {
						instance.gl.drawTexture2DNormal(tex, cpx, cpy);
					}
				}
				if (mti.isHasMid()) {
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_SMTILES, 0, mti.getBngImgIdx());
					Texture tex = instance.cache.getTexture(index);
					if(tex == null) {
						instance.texLoader.load(index);
					} else {
						instance.gl.drawTexture2DNormal(tex, cpx, cpy);
					}
				}
			}
		}
		// 绘制完地砖后再绘制对象层
		// TODO 将动态地图绘制提出到最上层精灵
		for(int w = left; w < instance.tws; ++w) {
			for (int h = instance.ths; h < instance.the; ++h) {
				MapTileInfo mti = instance.map.getTiles()[w][h];
				// 绘制左上角x
				int cpx = (int) (instance.px + (w - instance.tws) * PIXEL_WIDTH_PER_TILE);
				// 绘制左上角y
				int cpy = (int) (instance.py + (h - instance.ths) * PIXEL_HEIGHT_PER_TILE);
				if (mti.isHasAni()) {
					int frame = mti.getAniFrame();
					int ati = (instance.graphics.getTick() - 1) / (instance.graphics.getMaxFps() / frame);
					if(ati < 0) ati = 0;
					if(ati >= frame) ati = frame - 1;
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), (short) (mti.getObjImgIdx() + ati));
					Texture t = instance.cache.getTexture(index);
					if(t == null) {
						instance.texLoader.load(index);
					} else {
						WILImageInfo wii = instance.cache.getImageInfo(Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), (short) (mti.getObjImgIdx() + ati)));
						instance.gl.drawTexture2DAdd(t, cpx + wii.getOffsetX(), cpy - t.getHeight() + wii.getOffsetY());
					}
				} else if (mti.isHasObj()) {
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), mti.getObjImgIdx());
					Texture t = instance.cache.getTexture(index);
					if (t != null) {
						instance.gl.drawTexture2DNormal(t, cpx, cpy - t.getHeight());
					} else {
						instance.texLoader.load(index);
					}
				}
			}
		}
		
		
		// 对于游戏显示区域(玩家可见)进行绘制(也包括向右和向下的延伸)
		for (int w = instance.tws; w < instance.twe; ++w) {
			for (int h = instance.ths; h < instance.the; ++h) {
				MapTileInfo mti = instance.map.getTiles()[w][h];
				// 绘制左上角x
				int cpx = (int) (instance.px + (w - instance.tws) * PIXEL_WIDTH_PER_TILE);
				// 绘制左上角y
				int cpy = (int) (instance.py + (h - instance.ths) * PIXEL_HEIGHT_PER_TILE);


				if (mti.isHasBng()) {
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_TILES, 0, mti.getBngImgIdx());
					Texture tex = instance.cache.getTexture(index);
					if(tex == null) {
						instance.texLoader.load(index);
					} else {
						instance.gl.drawTexture2DNormal(tex, cpx, cpy);
					}
				}
				if (mti.isHasMid()) {
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_SMTILES, 0, mti.getBngImgIdx());
					Texture tex = instance.cache.getTexture(index);
					if(tex == null) {
						instance.texLoader.load(index);
					} else {
						instance.gl.drawTexture2DNormal(tex, cpx, cpy);
					}
				}
			}
		}

		// 绘制完地砖后再绘制对象层
		// TODO 将动态地图绘制提出到最上层精灵
		for (int w = instance.tws; w < instance.twe; ++w) {
			for (int h = instance.ths; h < instance.the; ++h) {
				MapTileInfo mti = instance.map.getTiles()[w][h];
				// 绘制左上角x
				int cpx = (int) (instance.px + (w - instance.tws) * PIXEL_WIDTH_PER_TILE);
				// 绘制左上角y
				int cpy = (int) (instance.py + (h - instance.ths) * PIXEL_HEIGHT_PER_TILE);
				if (mti.isHasAni()) {
					int frame = mti.getAniFrame();
					int ati = (instance.graphics.getTick() - 1) / (instance.graphics.getMaxFps() / frame);
					if(ati < 0) ati = 0;
					if(ati >= frame) ati = frame - 1;
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), (short) (mti.getObjImgIdx() + ati));
					Texture t = instance.cache.getTexture(index);
					if(t == null) {
						instance.texLoader.load(index);
					} else {
						WILImageInfo wii = instance.cache.getImageInfo(Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), (short) (mti.getObjImgIdx() + ati)));
						instance.gl.drawTexture2DAdd(t, cpx + wii.getOffsetX(), cpy - t.getHeight() + wii.getOffsetY());
					}
				} else if (mti.isHasObj()) {
					Integer index = Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), mti.getObjImgIdx());
					Texture t = instance.cache.getTexture(index);
					if (t != null) {
						instance.gl.drawTexture2DNormal(t, cpx, cpy - t.getHeight());
					} else {
						instance.texLoader.load(index);
					}
				}
			}
		}
	}

	/**
	 * 计算参数
	 * 
	 * @param x
	 * 			角色身处x
	 * @param y
	 * 			角色身处y
	 */
	private static void update(short x, short y) {
		// 计算绘制区域左上角坐标
		// 绘制区域左上角x
		instance.px = (short) (instance.graphics.getWidth() > instance.mw ? (instance.graphics.getWidth() - instance.mw) / 2 : 0);
		// 绘制区域左上角y
		instance.py = (short) (instance.graphics.getHeight() > instance.mh ? (instance.graphics.getHeight() - instance.mh) / 2 : 0);
		// 计算绘制宽度和高度
		// 绘制宽度
		instance.gw = (short) (instance.graphics.getWidth() > instance.mw ? instance.mw : instance.graphics.getWidth());
		// 绘制高度
		instance.gh = (short) (instance.graphics.getHeight() > instance.mh ? instance.mh : instance.graphics.getHeight());

		// 绘图区域左上角为地图块第几列
		instance.tws = (short) (x - (instance.gw / PIXEL_WIDTH_PER_TILE - 1) / 2);
		if (instance.tws < 0)
			instance.tws = 0;
		// 绘图区域左上角为地图块第几行
		instance.ths = (short) (y - (instance.gh / PIXEL_HEIGHT_PER_TILE - 1) / 2);
		if (instance.ths < 0)
			instance.ths = 0;
		
		// 绘制区域右下角为地图块第几列
		// 将绘制区域向右移动，保证对象层不缺失
		instance.twe = (short) (instance.tws + instance.gw / PIXEL_WIDTH_PER_TILE + EXTEND_RIGHT);
		if(instance.the > instance.map.getWidth())
			instance.the = instance.map.getWidth();
		// 绘制区域右下角为地图块第几行
		// 将绘制区域向下延伸，保证对象层不缺失
		instance.the = (short) (instance.ths + instance.gh / PIXEL_HEIGHT_PER_TILE + EXTEND_BOTTOM);
		if(instance.the > instance.map.getHeight())
			instance.the = instance.map.getHeight();

		// 纹理准备参数
		instance.pws = (short) (x - (instance.gw / PIXEL_WIDTH_PER_TILE - 1));
		if (instance.pws < 0)
			instance.pws = 0;
		instance.phs = (short) (y - (instance.gh / PIXEL_HEIGHT_PER_TILE - 1));
		if (instance.phs < 0)
			instance.phs = 0;
		instance.pwe = (short) (instance.tws + instance.gw / PIXEL_WIDTH_PER_TILE * 2);
		if (instance.pwe > instance.map.getWidth())
			instance.pwe = instance.map.getWidth();
		instance.phe = (short) (instance.ths + instance.gh / PIXEL_HEIGHT_PER_TILE * 2);
		if (instance.phe > instance.map.getHeight())
			instance.phe = instance.map.getHeight();

		// 对于无法置于绘制区域“正中”的情况，在上面的起始位置中相应坐标向上移动了一格，绘制终止坐标也要相应的上移
		if ((instance.gw / PIXEL_WIDTH_PER_TILE - 1) % 2 != 0)
			instance.twe -= 1;
		if ((instance.gh / PIXEL_HEIGHT_PER_TILE - 1) % 2 != 0)
			instance.the -= 1;
	}
	
	/** 准备纹理贴图(同步) */
	private static void loadTexturesAsyn() {
		java.util.Map<String, TextureLoader> texLoaders = new Hashtable<String, TextureLoader>();
		TextureLoader tilesLoader = TextureLoader.newInstance();
		TextureLoader smTilesLoader = TextureLoader.newInstance();
		for (int w = instance.pws; w < instance.pwe; ++w) {
			for (int h = instance.phs; h < instance.phe; ++h) {
				MapTileInfo mti = instance.map.getTiles()[w][h];
				
				if (mti.isHasBng()) {
					tilesLoader.load(Misc.buildTextureIndex(Texture.TEXTURE_TYPE_TILES, 0, mti.getBngImgIdx()));
				}
				if (mti.isHasMid()) {
					smTilesLoader.load(Misc.buildTextureIndex(Texture.TEXTURE_TYPE_SMTILES, 0, mti.getMidImgIdx()));
				}
				if(mti.isHasAni()) {
					for(int i = 0; i < mti.getAniFrame(); ++i) {
						if(!texLoaders.containsKey("Objects" + mti.getObjFileIdx()))
							texLoaders.put("Objects" + mti.getObjFileIdx(), TextureLoader.newInstance());
						texLoaders.get("Objects" + mti.getObjFileIdx()).load(Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), (short) (mti.getObjImgIdx() + i)));
					}
				} else if (mti.isHasObj()) {
					if(!texLoaders.containsKey("Objects" + mti.getObjFileIdx()))
						texLoaders.put("Objects" + mti.getObjFileIdx(), TextureLoader.newInstance());
					texLoaders.get("Objects" + mti.getObjFileIdx()).load(Misc.buildTextureIndex(Texture.TEXTURE_TYPE_OBJECTS, mti.getObjFileIdx(), mti.getObjImgIdx()));
				}
			}
		}
		texLoaders.put("Tiles", tilesLoader);
		texLoaders.put("SmTiles", smTilesLoader);
		instance.initLoaders = new WeakReference<java.util.Map<String,TextureLoader>>(texLoaders);
	}
	
	/**
	 * 从本地文件读取地图信息
	 * XXX 感谢星缘提供的思路<br>使用字节数组而非流读取提升效率
	 * 
	 * @param mapNo
	 *            地图编号
	 * @return 地图实体
	 */
	public static Map readMapFromFile(String mapNo) {
		try (FileInputStream fis = new FileInputStream(Files.file("/Users/yangwenjie/Documents/0.map"))) {
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
			JMir2Exception exception = JMir2Exception.valueOf(ex, JMir2Exception.EXCEPTION_TYPE_FILESTREAM);
			log.error(exception.getMessage(), ex);
			throw exception;
		} catch (IOException ex) {
			JMir2Exception exception = JMir2Exception.valueOf(ex, JMir2Exception.EXCEPTION_TYPE_FILESTREAM);
			log.error(exception.getMessage(), ex);
			throw exception;
		}
	}
}