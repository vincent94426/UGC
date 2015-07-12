package cn.dezhisoft.cloud.mi.newugc.ioffer.common;

/**
 * 视频分辨率
 * 
 * @author Rosson Chen
 * 
 */
public final class Resolution {

	/** 宽度 */
	private int width;
	
	/** 高度 */
	private int height;
	
	protected Resolution(){
		setWidth(176);
		setHeight(144);
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
	
	/** 根据显示区域来计算视频的实际分辨率 */
	public static Resolution calculateDisplayResolution(int disWidth,int disHeight,int videoWidth,int videoHeight){
		Resolution res = new Resolution() ;
		// 视频的宽:高
		final float f 	= (float)videoWidth / videoHeight ; // 视频的比例
		// 视频的实际宽度小于显示区域宽度
		if(disWidth > (disHeight * f)){
			res.setWidth((int)(disHeight * f));
			res.setHeight(disHeight);
		} else {
			res.setWidth(disWidth);
			res.setHeight((int)(disWidth / f));
		}
		return res ;
	}
}
