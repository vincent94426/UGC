package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.BitmapUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.BitmapUtil.ScalingLogic;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.BaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

public class PicturePreviewActivity extends BaseActivity {

	private ImageView img;

	private String imgPath;
	
	@Override
	protected View onCreateBody() {
		View body = getLayoutInflater().inflate(
				R.layout.picture_preview_layout, null);
		img = (ImageView) body.findViewById(R.id.img);
		Intent intent = getIntent();
		imgPath = intent.getStringExtra(UgV2Constants.KEY_REQ_PATH);
		if (imgPath != null && !"".equals(imgPath)) {
			new LoadImgAsyncTask().execute();
		}
		
		setUiTitle(R.string.picture_preview_page_title);
		
		setRightBtnVisibility(View.GONE);
		return body;
	}

	class LoadImgAsyncTask extends AsyncTask<Void, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(Void... arg0) {
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			return BitmapUtil.createThumbnailScaledBitmap(imgPath,
					ScalingLogic.FIT, dm.widthPixels, dm.heightPixels);
//			Bitmap imageBitmap = BitmapFactory.decodeFile(imgPath);
//			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			img.setImageBitmap(result);
			super.onPostExecute(result);
		}

	}
	
	@Override
	protected void onBackBtnClick() {
		
		startActivity(new Intent(PicturePreviewActivity.this, MainActivity.class));
		
		finish();
	}
	
	@Override
	protected void onLeftBtnClick(View v) {

		startActivity(new Intent(PicturePreviewActivity.this, MainActivity.class));
		
		finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			startActivity(new Intent(PicturePreviewActivity.this, MainActivity.class));
			
			finish();

			return true;

		}

		return false;

	}

}
