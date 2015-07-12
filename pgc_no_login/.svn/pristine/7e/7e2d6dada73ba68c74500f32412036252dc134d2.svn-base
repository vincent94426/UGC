package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.UGCDownFileService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Material;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.MaterialResouce;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.MaterialResouceType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.share.ShareApp;
import cn.dezhisoft.cloud.mi.newugc.ugc.share.ShareUtil;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IDownResource;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse.SoapRequestMessage;

/**
 * 素材详细信息
 * 1. 素材基本信息
 * 2. 素材列表信息
 * 
 * @author Rosson Chen
 * 
 */
public final class ContentTypeDetailActivity extends BaseActivity {

	static final String TAG 		= "IofferContentTypeDetailActivity" ;
	
	ImageView item_image;
	TextView program_title, item_event, item_name, item_date, item_author, item_cmamount;
	TextView program_describe;
	PackageType mPackageType;
	
	LinearLayout assertsList;
	RelativeLayout commentLayout , relateLayout;
	ProgressBar waitProgress ;
	IFileDownload ftpDownload ;
	
	Dialog 		sharedDialog ;
	Material	sharedMaterial ;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_program_detail_layout);
		
		waitProgress 	= (ProgressBar)findViewById(R.id.wait_progress_bar);
		mPackageType 	= mIofferService.getCurrentPackageType();
		
		// 加载UI组件
		loadViews(mPackageType);
		
		// 查询详细信息
		if(mPackageType.getMaterials().isEmpty()){
			mIofferService.queryMaterial(mPackageType,new SoapResponse(mHandler));
		} else {
			loadAssetsTypesList(mPackageType) ;
		}
		
		// 素材基本信息
		loadContentBaseInfo(mPackageType);
		
		// 加载分析
		loadSharedDialog();
		
		// 绑定下载服务
		Intent service = new Intent();
		service.setClass(this, UGCDownFileService.class);
		getApplicationContext().bindService(service, connection, Context.BIND_AUTO_CREATE);
	}
	
	/** 查找元素*/
	private ShareApp findShareApp(ArrayList<ShareApp> appList,int index){
		return index < appList.size() ? appList.get(index) : null ;
	}
	
	/** 动态构造分析选择栏 */
	private void loadSharedDialog(){
		
		sharedDialog = new Dialog(mContext,R.style.NobackDialog) ;
		sharedDialog.setContentView(R.layout.share_layout);
		LinearLayout list 			= (LinearLayout)sharedDialog.findViewById(R.id.group_content);
		ArrayList<ShareApp> appList = ShareUtil.queryShareResolveInfo(this);
		
		ShareApp down	= new ShareApp() ;
		down.setName("下载");
		appList.add(down);
		
		int total 	= appList.size() ;
		int row 	= total % 3 > 0 ? ((total / 3) + 1) : (total / 3) ;
		
		ResolveInfo info ;
		
		for(int r = 0 ,index = 0 ; r < row ; r++){
			
			ShareApp app 		= findShareApp(appList, index);
			if(app == null) {
				break ;
			}
			
			info				= app.getResolveInfo() ;
			View item 			= getLayoutInflater().inflate(R.layout.share_list_item, null);
			
			View one 			= item.findViewById(R.id.bnt_shared_one);
			TextView oneIcon 	= (TextView)one.findViewById(R.id.share_one_icon);
			TextView oneTitle 	= (TextView)one.findViewById(R.id.share_one_title);
			
			one.setTag(app) ;
			one.setOnClickListener(clickListener);
			oneTitle.setText(app.getName());
			if(info != null){
				oneIcon.setBackgroundDrawable(app.getResolveInfo().loadIcon(getPackageManager()));
			} else {
				oneIcon.setBackgroundResource(R.drawable.ic_download_def);
			}
			index++ ;
			
			// item 添加到List中
			list.addView(item);
			
			app 		= findShareApp(appList, index);
			if(app == null) {
				break ;
			}
			
			info				= app.getResolveInfo() ;
			View two 			= item.findViewById(R.id.bnt_shared_two);
			TextView twoIcon 	= (TextView)two.findViewById(R.id.share_two_icon);
			TextView twoTitle 	= (TextView)two.findViewById(R.id.share_two_title);
			
			two.setTag(app) ;
			two.setOnClickListener(clickListener);
			twoTitle.setText(app.getName());
			if(info != null){
				twoIcon.setBackgroundDrawable(app.getResolveInfo().loadIcon(getPackageManager()));
			} else {
				twoIcon.setBackgroundResource(R.drawable.ic_download_def);
			}
			index++ ;
			
			app 		= findShareApp(appList, index);
			if(app == null) {
				break ;
			}
			
			info				= app.getResolveInfo() ;
			View three 			= item.findViewById(R.id.bnt_shared_three);
			TextView threeIcon 	= (TextView)three.findViewById(R.id.share_three_icon);
			TextView threeTitle = (TextView)three.findViewById(R.id.share_three_title);
			
			three.setTag(app) ;
			three.setOnClickListener(clickListener);
			threeTitle.setText(app.getName());
			if(info != null){
				threeIcon.setBackgroundDrawable(app.getResolveInfo().loadIcon(getPackageManager()));
			} else {
				threeIcon.setBackgroundResource(R.drawable.ic_download_def);
			}
			index++ ;
		}
		
	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case SoapRequestMessage.MSG_START_SOAP:
					if(waitProgress != null) waitProgress.setVisibility(View.VISIBLE);
					break;
				case SoapRequestMessage.MSG_END_SOAP:
					if(waitProgress != null) waitProgress.setVisibility(View.GONE);
					break;
				case IofferDefine.MSG_QUERY_CONTENT_DETAIL_SUCCESS :
					loadContentBaseInfo(mPackageType);
					loadAssetsTypesList(mPackageType);
					break ;
				case IofferDefine.MSG_QUERY_CONTENT_ASSET_SUCCESS:
					loadAssetsTypesList(mPackageType) ;
					break ;
				case IofferDefine.MSG_QUERY_CONTENT_DETAIL_FAILED :
					Toast.makeText(mContext, "素材详细查询失败", Toast.LENGTH_SHORT).show();
					break ;
				case IofferDefine.MSG_QUERY_CONTENT_ASSET_FAILED :
					showMessageDialog(getString(R.string.label_content_detail_query_failed));
					break ;
				}
			}

		};
	}

	private void loadViews(PackageType content) {
		
		item_image = (ImageView) findViewById(R.id.program_detail_image);
		program_title = (TextView) findViewById(R.id.ioffer_program_detail_title);
		item_event = (TextView) findViewById(R.id.program_detail_event);
		item_name = (TextView) findViewById(R.id.program_detail_name);
		item_date = (TextView) findViewById(R.id.program_detail_date);
		item_author = (TextView) findViewById(R.id.program_detail_username);
		program_describe = (TextView) findViewById(R.id.program_detail_describe_text);
		assertsList	= (LinearLayout)findViewById(R.id.assets_content_list);
		commentLayout = (RelativeLayout) findViewById(R.id.assets_comment_layout);
		relateLayout  = (RelativeLayout) findViewById(R.id.assets_related_layout);
		item_cmamount = (TextView) commentLayout.findViewById(R.id.program_detail_cmamount);
	}
	
	/** 基本信息 */
	private void loadContentBaseInfo(PackageType content){
		
		program_title.setText(content.getTitle());
		item_name.setText(content.getTitle());
		item_date.setText(getString(R.string.label_content_pubulishdate) + formatDate(content.getCreateTime()));
		
		if (content.getUserName().equals("")) {
			item_author.setText(getText(R.string.label_content_unkown));
		} else {
			item_author.setText(content.getUserName());
		}
		
		// 设置评论和描述
		item_cmamount.setText(content.getCommentCount() + getString(R.string.label_content_cmamount));
		program_describe.setText(content.getDescribe());
	}
	
	/** 加载素材列表 */
	private void loadAssetsTypesList(PackageType content){
		
		ArrayList<Material> list = content.getMaterials() ;
		
		int size = list.size() ;
		final IDownResource downloader = mIofferService.getDownloader() ;
		String url ;
		
		for(int i = 0 ; i < size ; i++){
			
			final Material assets	= list.get(i) ;
			MediaType type 			= assets.getMediaType() ;
			url 					= assets.getIconUrl() ;
			
			View v = getLayoutInflater().inflate(R.layout.ioffer_program_detail_assert_item, null);
			v.setId(i);
			
			View preview	= v.findViewById(R.id.ioffer_program_preview) ;
			ImageView thumb  = (ImageView)v.findViewById(R.id.program_assets_big_icon) ;
			TextView icon	= (TextView)v.findViewById(R.id.program_assets_icon) ;
			TextView title	= (TextView)v.findViewById(R.id.program_assets_title) ;
			TextView duration = (TextView)v.findViewById(R.id.program_assets_content);
			View bntShared		= v.findViewById(R.id.program_assets_share_layout) ;
			
			preview.setTag(assets) ;
			bntShared.setTag(assets) ;
			title.setText(assets.getTitle()) ;
			
			if(type == MediaType.VIDEO){
				duration.setText(assets.getDuration());
			} else {
				duration.setText("");
			}
			
			if(type == MediaType.VIDEO){
				icon.setBackgroundResource(R.drawable.ic_ioffer_content_s_video);
			} else if(type == MediaType.AUDIO){
				icon.setBackgroundResource(R.drawable.ic_ioffer_content_s_audio);
			} else {
				icon.setBackgroundResource(R.drawable.ic_ioffer_content_s_image);
			}
			
			downloader.downBitmap(url, thumb, R.drawable.ic_ioffer_program_detail_watch_icon);
			
			// 预览
			preview.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					Material asset = (Material)v.getTag() ;
					
					MaterialResouce preview = findTargetResource(asset,MaterialResouceType.PREVIEW);
					
					if(preview == null){
						Toast.makeText(mContext, "没有可以预览的素材", Toast.LENGTH_LONG).show();
						return ;
					}
					
					mIofferService.addClickStatistics(mPackageType.getPackageGuid(), asset.getMaterialGuid(), new SoapResponse(mHandler));
					
					MediaType type 		= preview.getAssetMediaTrack() ;
					Intent intent = new Intent();
					//intent.putExtra(BasePreviewActivity.PREVIEW_NAME, preview.get);
					intent.putExtra(BasePreviewActivity.PREVIEW_KEY, BasePreviewActivity.PREVIEW_MODE_NETWORK);
					intent.putExtra(BasePreviewActivity.PREVIEW_PATH, preview.getAssetFileUrl());
					if(type == MediaType.VIDEO){
						intent.setClass(mContext, PlayVideoActivity.class);
						startActivity(intent);
					} else if(type == MediaType.AUDIO){
						intent.setClass(mContext, PlayAudioActivity.class);
						startActivity(intent);
					} else if(type == MediaType.IMAGE){
						intent.setClass(mContext, BitmapPreviewActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(mContext, "Media Type Unkown!", Toast.LENGTH_LONG).show();
					}
				}
			}) ;
			
			// 分享
			bntShared.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					sharedMaterial = (Material)v.getTag() ;
					
					if(sharedDialog != null){
						sharedDialog.show() ;
					}
				}
			}) ;
			
			assertsList.addView(v);
		}
		
		assertsList.invalidate() ;
	}
	
	private MaterialResouce findTargetResource(Material material,MaterialResouceType target){
		
		for(MaterialResouce res : material.getResouces()){
			if(res.getAssetType() == target){
				return res ;
			}
		}
		
		return null ;
	}
	
	private AssetsType createTask(Material material,MaterialResouce item){
		
		AssetsType task = new AssetsType() ;
		task.setDownloadUrl(item.getAssetFileUrl());
		task.setState(AssetsType.DownloadState.NEW);
		task.setIndentifier(material.getTitle());
		
		if(item.getAssetMediaTrack() == MediaType.VIDEO){
			task.setMediaType(cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType.VIDEO);
		} else if(item.getAssetMediaTrack() == MediaType.AUDIO){
			task.setMediaType(cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType.AUDIO);
		} else {
			task.setMediaType(cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType.IMAGE);
		}
		
		return task ;
	}
	
	/** 下载*/
	private void downloadAssetsType(){
		
		if(sharedMaterial == null){
			Toast.makeText(getApplication(), "素材错误!", Toast.LENGTH_SHORT).show() ;
			return ;
		}
		
		if(ftpDownload != null){
			
			MaterialResouce preview = findTargetResource(sharedMaterial,MaterialResouceType.RAW);
			
			if(preview == null){
				Toast.makeText(mContext, "没有可以下载的素材", Toast.LENGTH_LONG).show();
				return ;
			}
			
			AssetsType task = createTask(sharedMaterial,preview);
			
			if(!ftpDownload.checkAssetsType(task)){
				
				mIofferService.addDownloadStatistics(mPackageType.getPackageGuid(), sharedMaterial.getMaterialGuid(), new SoapResponse(mHandler));
				
				mAccessDatabase.saveObject(task) ;
				
				Intent intent = new Intent() ;
				intent.setAction(IFileDownload.ACTION_QUERY_NEW_TASK_DOWN);
				sendBroadcast(intent);
				Toast.makeText(getApplication(), R.string.ugc_label_down_hint, Toast.LENGTH_SHORT).show() ;
			} else {
				Toast.makeText(getApplication(), R.string.ugc_label_down_exist, Toast.LENGTH_SHORT).show() ;
			}
		} else {
			Toast.makeText(getApplication(), "Bind service error", Toast.LENGTH_SHORT).show() ;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		getApplicationContext().unbindService(connection);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	public String getContentEvent() {
		String contentEvent = "";
//		String event = mContentType.getItemCatalog();
//		if (!event.equals("")) {
//			contentEvent = ":" + event;
//		}

		return contentEvent;
	}

	public String formatDate(String time) {
		String date = "";

		char d[] = time.toCharArray();

		String year = String.valueOf(d, 0, 4);
		String month = String.valueOf(d, 5, 2);
		String day = String.valueOf(d, 8, 2);

		date = year + getString(R.string.label_content_year) + month + getString(R.string.label_content_month) + day + getString(R.string.label_content_day);

		return date;
	}

	public void topBarOnClick(View view) {
		switch (view.getId()) {
		case R.id.ioffer_program_detail_back:
			getActivityManager().backPrevious();
			break;
		}
	}

	public void buttonOnClick(View view) {
		switch (view.getId()) {
		case R.id.assets_comment_layout:
			getActivityManager().pushActivity(ContentTypeCommentListActivity.class);
			break;
		case R.id.assets_related_layout :
			mPackageType.getRelatedPackage().clear() ;
			mIofferService.setContentTypeRelated(mPackageType);
			getActivityManager().pushActivity(ContentTypeRelatedActivity.class);
			break;
		}
	}
	
	private void showSystemShare(String titile,String description,String preview){
		
		StringBuilder content = new StringBuilder() ;
		content.append("[");
		content.append(titile);
		content.append("]");
		content.append(description);
		content.append(",");
		content.append("\n");
		content.append(preview);
		content.append(",");
		content.append("\n");
		content.append("查看更多有趣视频请访问:" + "http://ugcm.sob-newstips.cn:9418/iUGC/");
		content.append(",");
		content.append("\n");
		content.append("下载客户端应用:");
		content.append("http://ugcm.sob-newstips.cn/client/iUGC.apk");
		
		Intent shareInt = new Intent(Intent.ACTION_SEND);
		shareInt.setType("text/plain");
		shareInt.putExtra(Intent.EXTRA_SUBJECT, "选择分享方式");
		shareInt.putExtra(Intent.EXTRA_TEXT, content.toString());
		shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(shareInt);
	}
	
	protected void sendMail(String emailBody) {
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		email.setType("plain/text");
		String emailSubject = "iUGC";
		
		StringBuilder conent = new StringBuilder() ;
		conent.append("[ ");
		conent.append("\n");
		conent.append("来源: iUGC");
		conent.append("\n");
		conent.append("iUGC下载地址:");
		conent.append("http://ugcm.sob-newstips.cn/client/iUGC.apk");
		conent.append("\n");
		conent.append("[Sobey]");
		// 设置邮件默认地址
		// email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
		// 设置邮件默认标题
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
		// 设置要默认发送的内容
		email.putExtra(android.content.Intent.EXTRA_TEXT, conent.toString());
		// 调用系统的邮件系统
		startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
	}
	 
	protected void sendMsg(String msg){
		
		StringBuilder conent = new StringBuilder() ;
		conent.append("全新内容汇聚 ");
		conent.append("\n");
		conent.append("来源: iUGC");
		conent.append("\n");
		conent.append("iUGC下载地址:");
		conent.append("http://ugcm.sob-newstips.cn/client/iUGC.apk");
		conent.append("\n");
		conent.append("[Sobey]");
		
		Uri smsToUri = Uri.parse("smsto:");  
	    Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);  
	    sendIntent.putExtra("sms_body", conent.toString());  
	    sendIntent.setType("vnd.android-dir/mms-sms");  
	    startActivity(sendIntent);  
	}

	private final ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			if(service instanceof IFileDownload){
				ftpDownload	= (IFileDownload)service ;
			}
		}
	} ;
	
	private final OnClickListener clickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			Object tag = v.getTag() ;
			
			if(tag instanceof ShareApp){
				ShareApp app 		= (ShareApp)tag ;
				ResolveInfo info 	= app.getResolveInfo() ;
				// 分享
				if(info != null){
					//IDownResource download = mIofferService.getDownloader() ;
					//String url			= sharedMaterial.getIconUrl() ;
					String title 		= mPackageType.getTitle() ;
					String description 	= mPackageType.getDescribe() ;
					//String preview 		= sharedAssetsType.getPreviewUrl() ;
					ShareUtil.sendShare(mContext, app, title, description, "",null/*download.getBitmapCachePath(url)*/);
				} 
				// 下载
				else {
					downloadAssetsType();
				}
			} 
			
			sharedDialog.dismiss() ;
		}
	};
}
