package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

/**
 * 
 * @author Rosson Chen
 * 
 */
public abstract class BasePreviewActivity extends BaseActivity {

	public static final int PREVIEW_MODE_LOCAL			= 1 ;
	public static final int PREVIEW_MODE_NETWORK		= PREVIEW_MODE_LOCAL + 1 ;
	public static final String PREVIEW_KEY				= "_preview" ;
	public static final String PREVIEW_PATH				= "_path" ;
	public static final String PREVIEW_NAME				= "_name" ;
	
	protected final int getPreviewMode(){
		return getIntent().getIntExtra(PREVIEW_KEY, PREVIEW_MODE_LOCAL) ;
	}
	
	protected final String getPreviewPath(){
		return getIntent().getStringExtra(PREVIEW_PATH);
	}
	
	protected final String getPreviewName(){
		return getIntent().getStringExtra(PREVIEW_NAME);
	}
	
	protected final String getFileName(String path){
		return path != null && !path.equals("") ? path.substring((path.lastIndexOf("/") + 1)) : "";
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		release();
	}
	
	public abstract void release();
}
