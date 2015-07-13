package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.util.LinkedList;

import cn.dezhisoft.cloud.mi.newugc.ugv2.chat.common.util.Constants;
import cn.dezhisoft.cloud.mi.newugc.ugv2.client.Client;
import cn.dezhisoft.cloud.mi.newugc.ugv2.utils.SharePreferenceUtil;


import android.app.Application;
import android.app.NotificationManager;
import android.os.StrictMode;
import android.util.Log;

public class MyApplication extends Application {
	private Client client;// 客户端
	private static boolean isClientStart;// 客户端连接是否启动
	private NotificationManager mNotificationManager;
	private int newMsgNum = 0;// 后台运行的消息
	private LinkedList<RecentChatEntity> mRecentList;
	private RecentChatAdapter mRecentAdapter;
	private int recentNum = 0;

	@Override
	public void onCreate() {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		Log.i("GOD","MyApplication");
		SharePreferenceUtil util = new SharePreferenceUtil(this,
				Constants.SAVE_USER);
		System.out.println(util.getIp() + " " + util.getPort());
		client = new Client(util.getIp(), util.getPort());// 从配置文件中读ip和地址
		System.out.println(client.toString());
		//开启客户端线程,设置客户端状态为已经启动
		client.start();
		Log.i("GOD","client start");
		isClientStart=true;
		
		mRecentList = new LinkedList<RecentChatEntity>();
		mRecentAdapter = new RecentChatAdapter(getApplicationContext(),
				mRecentList);
		super.onCreate();
		
		System.out.println("MyApplication  onCreate ......isClientStart:"+isClientStart);
	}

	public Client getClient() {
		return client;
	}

	public boolean isClientStart() {
		System.out.println("MyApplication  isClientStart:"+isClientStart);
		return isClientStart;
	}

	public void setClientStart(boolean isClientStart) {
		this.isClientStart = isClientStart;
	}

	public NotificationManager getmNotificationManager() {
		return mNotificationManager;
	}

	public void setmNotificationManager(NotificationManager mNotificationManager) {
		this.mNotificationManager = mNotificationManager;
	}

	public int getNewMsgNum() {
		return newMsgNum;
	}

	public void setNewMsgNum(int newMsgNum) {
		this.newMsgNum = newMsgNum;
	}

	public LinkedList<RecentChatEntity> getmRecentList() {
		return mRecentList;
	}

	public void setmRecentList(LinkedList<RecentChatEntity> mRecentList) {
		this.mRecentList = mRecentList;
	}

	public RecentChatAdapter getmRecentAdapter() {
		return mRecentAdapter;
	}

	public void setmRecentAdapter(RecentChatAdapter mRecentAdapter) {
		this.mRecentAdapter = mRecentAdapter;
	}

	public int getRecentNum() {
		return recentNum;
	}

	public void setRecentNum(int recentNum) {
		this.recentNum = recentNum;
	}
}
