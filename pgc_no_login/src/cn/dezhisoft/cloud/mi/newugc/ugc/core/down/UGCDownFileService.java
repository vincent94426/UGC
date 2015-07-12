package cn.dezhisoft.cloud.mi.newugc.ugc.core.down;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Pattern;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.config.BaseConfig;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.config.Text;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.down.IFileDownload.IDownloadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.AssetsType.DownloadState;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import com.sobey.sdk.db.DatabaseManager;
import com.sobey.sdk.db.IDBManager;
import com.sobey.sdk.db.SQLiteUpdateListener;
import com.sobey.sdk.ftp.FTPCallback;
import com.sobey.sdk.ftp.FTPClient;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class UGCDownFileService extends Service implements Runnable,FTPCallback {
	
	/** debug*/ 
	private static final String TAG = "UGCDownloadService" ;
	private static final int 	MAX_QUEUE 	= 64 ;
	private static final String FTP_PREFIX	= "ftp://" ;
	
	private final ArrayList<AssetsType> mHistoryList = new ArrayList<AssetsType>() ;
	private final ArrayBlockingQueue<AssetsType> downQueue = new ArrayBlockingQueue<AssetsType>(MAX_QUEUE);
	private IDBManager				mDatabaseAccess ;
	private IDownloadCallback		mCallback ;
	private BroadcastReceiver		mReceiver ;
	
	/** down binder */
	private static DownBinder mBinder ;
	private String 		mLocation ;
	private boolean 	mStart ;
	private Thread 		mThread ;
	private FTPClient	mFtpClient ;
	private String 		mDownloadUrl ;
	private AssetsType  mCurrentAssets ;
	private int 		mCurrentPercent ;
	private int 		mLastPercent ;
	private int 		mTotal ;
	private String 		mIp ;
	private final int	mPort = 21 ;
	private final String mAnonymous = "anonymous" ;
	private final String mPassword 	= "my@126.com" ;
	
	public UGCDownFileService(){
		mBinder = new DownBinder() ;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private void LOGD(String msg){
		Log.d(TAG, msg);
	}
	
	private void LOGE(String msg){
		Log.e(TAG, msg);
	}
	
	private void LOGW(String msg){
		Log.w(TAG, msg);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		LOGD("Download Service onCteate()") ;
		
		mLocation	= Environment.getExternalStorageDirectory().getPath() + "/" + getPackageName() + "/";
		mIp			= null ;
		downQueue.clear() ;
		reset();
		
		try{
			File file = new File(mLocation);
			if(!file.exists()) file.mkdirs() ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		// 初始化话数据库
		if (DatabaseManager.getDatabaseManager() == null) {
			DatabaseManager.initDBManager(this,BaseConfig.DB_VERSION,new SQLiteUpdateListener() {
				
				@Override
				public void updateDatabase(SQLiteDatabase db,IDBManager manager,int oldVersion, int newVersion) {
					
					/*
					String table = DatabaseUtil.getTableNameByClass(AssetsType.class);
					
					if(db != null && table != null){
						db.execSQL("DROP TABLE " + table);
					}*/
				}
			});
		}

		mDatabaseAccess = DatabaseManager.getDatabaseManager();
		
		IntentFilter filter = new IntentFilter() ;
		filter.addAction(IFileDownload.ACTION_QUERY_NEW_TASK_DOWN) ;
		mReceiver	= new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				LOGD("下载服务接收到新任务消息 ");
				queryNewAssetsType();
			}
		} ;
		registerReceiver(mReceiver, filter) ;
		
		loadHistoryAssetsType();
		
		mStart	= true ;
		mThread = new Thread(this) ;
		mThread.setDaemon(true);
		mThread.setName("#Download Thread");
		mThread.start() ;
	}
	
	/** 文件下载实现 */
	public static IFileDownload getUGCFileDownloadImpl(){
		return mBinder ;
	}
	
	/** 队列大小*/
	private int getDownQueueSize(){
		return downQueue.size() ;
	}
	
	private void reset(){
		mDownloadUrl	= "" ;
		mLastPercent	= 0 ;
		mCurrentPercent = 0 ;
		mTotal			= 0 ;
		mCurrentAssets	= null ;
		mFtpClient		= null ;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		
		LOGD("Download Service onStart()") ;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		unregisterReceiver(mReceiver) ;
		
		LOGD("Download Service onDestroy()") ;
		
		release();
	}
	
	private void loadHistoryAssetsType(){
		
		LOGD("从数据库中加载下载信息 ");
		
		final ArrayList<AssetsType> hlist = mHistoryList ;
		
		hlist.clear() ;
		
		ArrayList<Object> list = mDatabaseAccess.queryAllObject(AssetsType.class,"order by id DESC");
		
		for(Object obj : list){
			hlist.add((AssetsType)obj) ;
		}
		
		for(AssetsType asset : hlist){
			int state = asset.getState() ;
			if(state == DownloadState.NEW
					|| state == DownloadState.RUNNING
					|| state == DownloadState.WAIT){
				putToDownQueue(asset) ;
			}
		}
	}
	
	private void queryNewAssetsType(){
		
		final ArrayList<AssetsType> hlist = mHistoryList ;
		final ArrayBlockingQueue<AssetsType> queue = downQueue ;
		
		ArrayList<Object> list = mDatabaseAccess.queryAllObject(AssetsType.class,new String[]{"state"},new String[]{ String.valueOf(DownloadState.NEW)});
		
		for(Object obj : list){
			
			AssetsType asset = (AssetsType)obj ;
			
			// 历史队列中
			if(!hlist.contains(asset)){
				hlist.add(asset);
			}
			
			// 下载队列中
			if(!queue.contains(asset)){
				putToDownQueue(asset);
			}
		}
	}
	
	private boolean putToDownQueue(AssetsType assets){
		
		if(assets == null){
			LOGE("ERROR: AssetsType is null");
			return false ;
		}
		
		final ArrayBlockingQueue<AssetsType> queue = downQueue ;
		
		if(queue.size() < MAX_QUEUE){
			assets.setDetail("Wait");
			assets.setState(DownloadState.WAIT) ;
			queue.add(assets);
			return true ;
		}
		
		return false ;
	}
	
	private void updateAssetsType(boolean finish ,String msg){
		
		final AssetsType assets = pollDownQueue() ;
		
		// 如果正在上传
		if(mFtpClient != null){
			mFtpClient.stopDownload() ;
		}
		
		if(assets != null){
			// 更新任务状态
			assets.setProgress(mCurrentPercent);
			assets.setTotal(mTotal);
			assets.setDetail(msg);
			assets.setState(finish ? DownloadState.DONE : DownloadState.STOP) ;
			// 更新数据库
			mDatabaseAccess.updateObject(assets);
			// 回调
			notifyCallback(assets,msg);
		}
		
		mFtpClient	= null ;
		
		LOGW("DownloadService: " + msg);
	}
	
	// 从队列中取数据
	private AssetsType pollDownQueue(){
		return downQueue.poll() ;
	}
	
	private void release(){
		
		if(mFtpClient != null){
			mFtpClient.stopDownload() ;
		}

		mStart	= false ;
		try {
			if(mThread != null)
				mThread.join() ;
			mThread = null ;
		}catch(Exception e){
			e.printStackTrace() ;
		}
		
		mFtpClient = null ;
	}

	@Override
	public void uploadProgress(int total, int progress) {
		
	}

	@Override
	public void downloadProgress(int total, int progress) {
		// 计算进度
		float f 		= (float)progress / total ;
		int percent 	= (int)(f * 100) ;
		// 当前进度
		mCurrentPercent = percent ;
		mTotal			= (int)total ;
		// 回调
		if(percent != mLastPercent && mCallback != null){
			mLastPercent	= percent ;
			mCallback.progress(mCurrentAssets, 0, mLastPercent, total);
		}
		
		// 下载完成
		if(progress >= total){
			updateAssetsType(true,Text.DOWNLOAD_FINISH);
		}
	}
	
	private boolean checkAssetsTypeURL(AssetsType asset){
		return asset != null && asset.getDownloadUrl().equals("") ;
	}
	
	/** 回调 */
	private void notifyCallback(AssetsType asset,String msg){
		if(mCallback != null)  mCallback.stateCallback(asset, msg) ;
	}
	
	/** 得到文件名称 */
	private String getFileName(String url){
		
		if(url == null || url.equals("")) return "" ;
		
		int pos = url.lastIndexOf("/") ;
		
		if(pos < 0 ) return "" ;
		
		return url.substring(pos + 1);
	}
	
	/** FTP登录, 得到文件的实际下载路径 . 如果登录成功就返回实际的资源路径 */
	private String ftpLogin(String url,String uname,String upwd){
		
		String noftp 	= url.substring(FTP_PREFIX.length());
		int length 		= noftp.indexOf('/');
		String ip 		= noftp.substring(0, length);
		String filePath = noftp.substring(length);
		String name		= uname ;
		String pwd		= upwd ;
		
		if(uname.equals("")){
			name	= mAnonymous ;
			pwd		= mPassword ;
		}
		
		if(mIp == null) {
			// 是否ip 
			if(!matchIP(ip)){
				// 是否域名
				String host = CWUploadWebService.parserHostIpAddress(ip) ;
				if(host != null){
					mIp	= host ;
				} else {
					return null;
				}
			} else {
				mIp	= ip ;
			}
		}
		
		if(mFtpClient == null){
			mFtpClient	= new FTPClient() ;
		}
		
		mFtpClient.setFTPCallback(this);
		
		return mFtpClient.login(mIp, mPort, name, pwd) == 0 ? filePath : null;
	}
	
	private boolean matchIP(String ip) {
		String reg = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.\\d{1,3}";
		Pattern p = Pattern.compile(reg);
		return p.matcher(ip).matches();
	}

	@Override
	public void run() {
		
		AssetsType asset ;
		final ArrayList<AssetsType> list 			= mHistoryList ;
		final ArrayBlockingQueue<AssetsType> queue 	= downQueue ;
		final IDBManager		db					= mDatabaseAccess ;
		
		while(mStart){
			
			asset =  queue.peek() ;
			
			if(asset != null && !asset.getDownloadUrl().equals(mDownloadUrl)){
				// 清除临时变量
				mLastPercent	= 0 ;
				mCurrentPercent = 0 ;
				mTotal			= 0 ;
				// 是否有效地址
				if(checkAssetsTypeURL(asset)){
					pollDownQueue() ;
					db.deleteObject(asset) ;
					list.remove(asset) ;
					LOGE(Text.DOWNLOAD_URL_INVALID);
					continue ;
				}
				// 当前下载任务
				mDownloadUrl	= asset.getDownloadUrl() ;
				// 设置本地路径
				asset.setLocalPath(mLocation + getFileName(mDownloadUrl));// /mnt/sdcard/com.sobey.ioffer/
				//
				LOGE("Download, URL:" + mDownloadUrl);
				String url	= ftpLogin(mDownloadUrl,asset.getFtpUname(),asset.getFtpUpwd()) ;
				// 登录
				if(url != null && mFtpClient != null){
					mCurrentAssets	= asset ;
					// debug
					String name = url.substring(url.lastIndexOf("/") + 1);
					LOGW("Remote URL: " + url);
					LOGW("Location Dir: " + mLocation);
					// 开始下载
					mFtpClient.download(mLocation + name, url, true) ;
				} else {
					LOGD("FTP 登录失败");
					updateAssetsType(false,Text.FTP_LOGIN_ERROR);
				}
				//
				reset() ;
			}
			
			asset = null ;
			try{
				Thread.sleep(20);
			}catch(Exception e){
				e.printStackTrace() ;
			}
		}
	}

	/** FTP 下载Binder */
	private final class DownBinder extends Binder implements IFileDownload {

		protected DownBinder(){
			
		}
		
		@Override
		public void setCallback(IDownloadCallback callback) {
			mCallback	= callback ;
		}

		@Override
		public ArrayList<AssetsType> getHistoryList() {
			return mHistoryList;
		}

		@Override
		public int getDownloadSize() {
			return getDownQueueSize();
		}

		@Override
		public boolean checkAssetsType(AssetsType asset) {
			
			if(asset == null){
				LOGD("CheckAssets ERROR!");
				return false ;
			}
			
			return mHistoryList.contains(asset);
		}

		@Override
		public void removeAssetsType(AssetsType asset) {
			
			if(asset == null) return ;
			
			final ArrayList<AssetsType> list 			= mHistoryList ;
			final ArrayBlockingQueue<AssetsType> queue 	= downQueue ;
			final IDBManager		db					= mDatabaseAccess ;
			
			boolean run = asset.getDownloadUrl().equals(mDownloadUrl) ;
			if(run) updateAssetsType(false,"");
			
			// 删除物理文件
			try{
				File file = new File(asset.getLocalPath());
				if(file != null && file.exists())
					file.delete() ;
			}catch(Exception e){
				e.printStackTrace() ;
			}
			
			// 删除数据库
			db.deleteObject(asset);
			list.remove(asset);
			//
			if(!run)queue.remove(asset);
		}

		@Override
		public boolean resumeAssetsType(AssetsType asset) {
			return putToDownQueue(asset);
		}

		@Override
		public void stopDownload() {
			updateAssetsType(false,"");
		}
	}
}
