package cn.dezhisoft.cloud.mi.newugc.ugc.model;


/**
 * 带图片的对象
 * 
 * @author Rosson Chen
 *
 */
public class ImageItem extends com.sobey.sdk.cache.ImageItem implements Cloneable {

	public ImageItem(){
		setHighUrl("");
		setLowUrl("");
		setDecodeWidth(0);
		setDecodeHeight(0);
	}

	@Override
	public ImageItem clone(){
		try{
			return (ImageItem)super.clone();
		}catch(CloneNotSupportedException e){
			return new ImageItem() ;
		}
	}
	
}
