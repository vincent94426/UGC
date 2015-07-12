package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.ImageItem;
import cn.dezhisoft.cloud.mi.newugc.ugc.preview.BitmapPreview;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish.DownResourceCallback;

/**
 * 
 * @author Eric
 * 
 */
public class BitmapPreviewActivity extends BasePreviewActivity {

	static final String TAG = "BitmapPreviewActivity";
	
	static final String MEDIA_PATH	= Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" ;
	
	BitmapPreview previewView ;
	Bitmap bitmap;
	ImageItem imageItem;
	ProgressDialog progressDialog = null;
	TextView bnt_save ,loading_hint;
	String path ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_program_preview_image);
		
		//TextView title = (TextView)findViewById(R.id.ioffer_title_bar_content);
		loading_hint   = (TextView)findViewById(R.id.ioffer_program_preview_download_hint);
		//bnt_save  = (TextView)findViewById(R.id.bnt_ioffer_title_bar_right);
		//title.setText(R.string.label_preview_image_title);
		//bnt_save.setText(R.string.label_preview_image_save);
		//
		initView();
	}

	public void initView() {
		
		previewView = (BitmapPreview) findViewById(R.id.ioffer_program_preview_image_view);
		
		previewView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish() ;
			}
		}) ;

		int  preview = getPreviewMode();
		path 		 = getPreviewPath();
		//
		if(preview == PREVIEW_MODE_LOCAL){
			//bnt_save.setVisibility(View.INVISIBLE);
			bitmap = BitmapUtil.createBitmap(BitmapUtil.MODE_BIG_BITMAP, path);
			previewView.loadBitmap(path);
		} else {
			//bnt_save.setVisibility(View.VISIBLE);
			
			if (path != null && !path.equals("")) {
				
//				final IDownResource download = mIofferService.getDownloader() ;
//				Bitmap bitmap = download.getBitmap(path);
//				// 是否为空
//				if(bitmap != null){
//					previewView.loadBitmap(bitmap);
//				} else {
//				
//					loading_hint.setVisibility(View.VISIBLE);
//					imageItem = new ImageItem();
//					imageItem.setUrl(path);
//					
//					download.downloadBitmap(IDownResource.MODE_PREVIEW,new DownResourceCallback(imageItem,mHandler) {
//						
//						
//						@Override
//						public void onDownloadCallback(String url, int total,final int parent, byte[] data, int length) {
//							mHandler.post(new Runnable() {
//								
//								@Override
//								public void run() {
//									loading_hint.setText(parent + "%");
//								}
//							}) ;
//						}
//	
//						@Override
//						public void onFinish(String url, final Object value) {
//							mHandler.post(new Runnable() {
//								
//								@Override
//								public void run() {
//									if(value instanceof Bitmap){
//										loading_hint.setVisibility(View.GONE);
//										Bitmap bitmap = (Bitmap)value ;
//										previewView.loadBitmap(bitmap);
//									}
//								}
//							});
//						}
//					});
//					}
			} else {
				Toast.makeText(mContext, "Bitmap url address invailed", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	public final void release(){
		if(bitmap != null){
			bitmap.recycle() ;
		}
		bitmap 	  = null ;
		imageItem = null ;
		progressDialog = null ;
	}
	
	public final void topBarButtonOnclick(View view){
		switch (view.getId()) {
		case R.id.bnt_ioffer_title_bar_left:
			finish();
			break;
		case R.id.bnt_ioffer_title_bar_right:
			saveBitmap();
			break ;
		}
	}
	
	private final void saveBitmap(){
		FileOutputStream os = null ;
		FileInputStream input = null ;
		byte[] buf		  = null ;
		try{
			String out = MEDIA_PATH + getFileName(path);
			File outFile = new File(out);
			if(outFile != null && outFile.exists()){
				Toast.makeText(mContext, R.string.label_preview_image_exist, Toast.LENGTH_LONG).show();
				return ;
			}
			
//			os 		= new FileOutputStream(outFile);
//			input 	= new FileInputStream(mIofferService.getDownloader().getBitmapCachePath(imageItem));
//			if(input != null){
//				buf = new byte[1024];
//				int read = -1 ;
//				while((read = input.read(buf)) > 0){
//					os.write(buf, 0, read);
//					os.flush();
//				}
//				Toast.makeText(mContext, R.string.label_preview_image_save_ok, Toast.LENGTH_LONG).show();
//			}
		} catch (Exception e){
			e.printStackTrace() ;
		} finally{
			try {
				if (input != null)input.close();
				input = null;
				if (os != null)os.close();
				os  = null;
				buf = null ;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case IofferDefine.MSG_DOWNLOAD_BEGIN:
					progressDialog = ProgressDialog.show(BitmapPreviewActivity.this, getString(R.string.label_preview_image_plswait), getString(R.string.label_preview_image_loading), true);
					break;
				case DownResourceCallback.MSG_DOWNLOAD_BITMAP_SUCCESS:
					//if(previewView != null && imageItem != null)
					//	previewView.loadBitmap(mIofferService.getDownloader().getBitmap(imageItem));
					break;
				case IofferDefine.MSG_DOWNLOAD_FAILED:
					showMessageDialog(getString(R.string.label_preview_image_download_failed));
					break;
				case IofferDefine.MSG_DOWNLOAD_END:
					if(progressDialog != null && progressDialog.isShowing())
						progressDialog.dismiss();
					break;
				}
			}
		};
	}

}
