package cn.dezhisoft.cloud.mi.newugc.ioffer.config;

import java.io.Serializable;

import com.sobey.sdk.db.Table;
import com.sobey.sdk.db.TableID;
import com.sobey.sdk.db.TableInt;
import com.sobey.sdk.db.TableID.INCREASE_TYPE;

@Table(TableName = "_size")
public final class VideoSize implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 视频大小类型 : 直播,录制*/
	public static final class Type {
		public static final int LIVE		= 1 ;
		public static final int RECORDER	= 2 ;
	}

	@TableID(increase = INCREASE_TYPE.AUTO)
	@TableInt
	private int id;

	@TableInt
	private int width;

	@TableInt
	private int height;

	/** 1: live; 2: recorder */
	@TableInt
	private int type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
