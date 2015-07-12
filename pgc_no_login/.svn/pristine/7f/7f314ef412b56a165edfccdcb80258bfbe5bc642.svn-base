package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.io.File;

import cn.dezhisoft.cloud.mi.newugc.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 选择和裁剪图片的Activity
 * 
 * @author Rosson Chen
 *
 */
public class BaseCropBitmapActivity extends BaseActivity {
	
	static final int 	PHOTO_SUCCESS 		= 1000;
	static final int 	CAMERA_SUCCESS 		= 1001;
	static final int 	IMAGE_CROP_SUCCESS 	= 1003;
	static final int 	ICON_CLIP_WIDTH		= 128 ;
	static final int 	ICON_CLIP_HEIGHT	= 128 ;
	static final int 	ICON_WIDTH			= 64 ;
	static final int 	ICON_HEIGHT			= 64 ;
	
	/** 临时文件路径*/
	protected static final String TEMP_PATH = Environment.getExternalStorageDirectory() + File.separator + "IofferIcon" + File.separator;

	/** 头像文件*/
	protected File icon_file; 
	
	/** 临时图片*/
	protected Bitmap icon_bitmap ;
	
	/** 对话框*/
	private AlertDialog icon_choice_dialog ;
	
	/** 是否选择*/
	protected int choice_flag ;
	
	/**
	 * 选择对话框
	 */
	protected final void showChoiceBitmapDialog(){
		if(icon_choice_dialog == null) {
			String[] items = getResources().getStringArray(R.array.register_icon_choice);
			icon_choice_dialog = new AlertDialog.Builder(this).setTitle(R.string.label_register_title_choice)
					.setItems(items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (which == 0) {
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setType("image/*");//匹配所有照片
								startActivityForResult(intent, PHOTO_SUCCESS);
							} else {
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								File IconPath = new File(TEMP_PATH);
								if (!IconPath.exists()) {
									IconPath.mkdirs();
								}
								icon_file = new File(IconPath, "icon.jpg");
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(icon_file));
								startActivityForResult(intent, CAMERA_SUCCESS);
							}
						}
					}).create();
		}
		// 显示
		if(icon_choice_dialog != null && !icon_choice_dialog.isShowing())
			icon_choice_dialog.show();
	}
	
	/**
	 * 处理返回事件
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case PHOTO_SUCCESS :
			startPhotoZoom(data.getData());
			break;
		case CAMERA_SUCCESS:
			startPhotoZoom(Uri.fromFile(icon_file));
			break;
		case IMAGE_CROP_SUCCESS:
			choice_flag = 1 ;
			updateUserIcon(data);
			break;
		}
	}
	
	/**
	 * 获取裁减图片
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		//下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", ICON_CLIP_WIDTH);
		intent.putExtra("outputY", ICON_CLIP_HEIGHT);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, IMAGE_CROP_SUCCESS);
	}
	
	/**
	 * 
	 * 保存裁减之后的图片
	 * @param data
	 */
	protected void updateUserIcon(Intent data) {
		
	}
	
	/**
	 * 图片缩放
	 * 
	 * @param originalBitmap
	 *            原始的Bitmap
	 * @param newWidth
	 *            自定义宽度
	 * @param newHeight自定义高度
	 * @return 缩放后的Bitmap
	 */
	protected final Bitmap resizeImage(Bitmap originalBitmap, int newWidth,int newHeight) {
		int width = originalBitmap.getWidth();
		int height = originalBitmap.getHeight();
		// 计算宽、高缩放率
		float scanleWidth = (float) newWidth / width;
		float scanleHeight = (float) newHeight / height;
		// 创建操作图片用的matrix对象 Matrix
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(scanleWidth, scanleHeight);
		// 创建新的图片Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, width,height, matrix, true);
		return resizedBitmap;
	}
}
