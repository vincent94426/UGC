
package cn.dezhisoft.cloud.mi.newugc.ugc.core.upload;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import cn.dezhisoft.cloud.mi.newugc.common.net.ftp.ContinueFTP;
import cn.dezhisoft.cloud.mi.newugc.common.net.ftp.ContinueFTP.FtpProgressListener;
import cn.dezhisoft.cloud.mi.newugc.common.net.ftp.FileUtil;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.config.BaseConfig;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.config.Text;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ChannelInfo;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task.STATE;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.IFileUploadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload.UploadMessage;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload.CodeError;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload.CodeFTP;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload.CodeVerify;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload.Command;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IUGCUpload.IUGCUploadCallback;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWIChannelProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWIUserProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWSoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.upload.ProtocolDefine;

import com.sobey.sdk.db.DatabaseManager;
import com.sobey.sdk.db.IDBManager;
import com.sobey.sdk.db.SQLiteUpdateListener;

/**
 * 鏂囦欢涓婁紶鏈嶅姟 1. 瀹炵幇鏂囦欢闃熷垪绠＄悊
 * 
 * @author Rosson Chen
 */
public final class UGCUploadFileService extends Service implements Runnable {

    private static final String TAG = "UGCFileUploadImpl";

    private static final int SIZE = 20;

    // private final UGCUploadImpl mUploadImpl ;
    private IFileUploadCallback mCallback;

    private IDBManager mDatabaseAccess;

    private final ArrayList<Task> mHistoryList = new ArrayList<Task>();

    private final ArrayList<Metadata> mMetadataList = new ArrayList<Metadata>();

    private BroadcastReceiver mReceiver;

    private final ArrayBlockingQueue<Task> UploadQueue = new ArrayBlockingQueue<Task>(SIZE + 1);

    private final ReentrantLock mLock = new ReentrantLock();

    private String mCurrentTuid;

    private long mTotalSize;

    private long mSendSize;

    private int mPercent;

    private int mLastPercent;

    private int mTaskId;// 澶氫换鍔＄殑id

    private Task mTask;

    private boolean mStart;

    private Thread mThread;

    private ChannelInfo mChannel;

    private static UGCUploadBinder mBinder;

    private ContinueFTP currentFtpInstance;

    public UGCUploadFileService() {
        mBinder = new UGCUploadBinder();
        // TODO 鍒濆鍖朏TP涓婁紶搴曞眰鏈嶅姟 婕旂ず淇敼
        // mUploadImpl =
        // UGCUploadImpl.createUGCUpload(UGCUploadImpl.UPLOAD_TYPE_FILE);
        // mUploadImpl.setCallback(callback);

        reset();
    }

    private void reset() {
        resetUpload();
        mStart = false;
        mThread = null;
        UploadQueue.clear();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** 鏂囦欢涓婁紶瀹炵幇 */
    public static IFileUpload getUGCFileUpload() {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "Uplaod Service onCreate()");

        // 娉ㄥ唽骞挎挱娑堟伅鎺ユ敹
        IntentFilter filter = new IntentFilter();
        filter.addAction(IFileUpload.ACTION_QUERY_NEW_TASK);
        mReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "UploadService receive new task query");
                queryNewTask();
            }
        };
        registerReceiver(mReceiver, filter);

        // 鍒濆鍖栬瘽鏁版嵁搴�
        if (DatabaseManager.getDatabaseManager() == null) {
            DatabaseManager.initDBManager(this, BaseConfig.DB_VERSION, new SQLiteUpdateListener() {

                @Override
                public void updateDatabase(SQLiteDatabase db, IDBManager manager, int oldVersion,
                        int newVersion) {

                    /*
                     * if(db != null){ String table =
                     * DatabaseUtil.getTableNameByClass(Task.class); if(table !=
                     * null){ db.execSQL("DROP TABLE " + table); } table =
                     * DatabaseUtil.getTableNameByClass(Metadata.class);
                     * if(table != null){ db.execSQL("DROP TABLE " + table); } }
                     */
                }
            });
        }

        mDatabaseAccess = DatabaseManager.getDatabaseManager();

        // 鍔犺浇鏁版嵁搴�
        loadTaskQueue();

        // 鍚姩涓婁紶绾跨▼
        startThread();
    }

    private void LOGD(String msg) {
        Log.d(TAG, msg);
    }

    private void LOGW(String msg) {
        Log.w(TAG, msg);
    }

    private int getUploadQueueSize() {
        return UploadQueue.size();
    }

    /** 鏌ヨ鏂颁换鍔� */
    private void queryNewTask() {

        LOGD("UploadService Query new task from database");

        final IDBManager db = mDatabaseAccess;
        final ArrayList<Task> history = mHistoryList;
        final ArrayList<Metadata> metas = mMetadataList;
        final ArrayBlockingQueue<Task> queue = UploadQueue;

        // 浠诲姟
        ArrayList<Object> list = db.queryAllObject(Task.class, new String[] {
            "state"
        }, new String[] {
            "" + Task.STATE.NEW
        });
        Task task;
        Metadata meta;
        for (int index = 0, size = list.size(); index < size; index++) {
            task = (Task) list.get(index);
            // 鏄惁瀛樺湪
            if (!history.contains(task)) {
                // meta data
                Object m = db.queryObject(Metadata.class, new String[] {
                    "transferUID"
                }, new String[] {
                    task.getTransferUID()
                });
                if (m != null) {
                    meta = (Metadata) m;
                    task.setMetadata(meta);
                    if (!metas.contains(meta)) {
                        metas.add(meta);
                    }
                    // 娣诲姞鍒板巻鍙茶褰曚腑
                    history.add(task);
                    // 鍚屼簨娣诲姞鍒颁笂浼犻槦鍒椾腑
                    if (task.getState() == Task.STATE.NEW && !queue.contains(task)) {
                        put(task);
                    }
                } else {
                    Log.w(TAG, "The Task not has Metadata tuid=" + task.getTransferUID());
                }
            }

        }
    }

    /** load task from database */
    private void loadTaskQueue() {

        Log.d(TAG, "Load task from database...");

        final IDBManager db = mDatabaseAccess;
        mHistoryList.clear();
        mMetadataList.clear();

        ArrayList<Task> history = mHistoryList;
        ArrayList<Metadata> metas = mMetadataList;

        // 鍘嗗彶浠诲姟
        ArrayList<Object> list = db.queryAllObject(Task.class, "order by id DESC");
        for (Object obj : list) {
            history.add((Task) obj);
        }

        // 鎵�鏈夊厓鏁版嵁
        list = db.queryAllObject(Metadata.class);
        for (Object obj : list) {
            metas.add((Metadata) obj);
        }

        // 鍏宠仈鍏冩暟鎹�
        for (Task task : history) {
            for (Metadata meta : metas) {
                if (task.getTransferUID().equals(meta.getTransferUID())) {
                    task.setMetadata(meta);
                    break;
                }
            }
        }

        // 鎵�鏈夋湭瀹屾垚鐨勪换鍔℃坊鍔犲埌涓婁紶闃熷垪涓�
        for (Task task : history) {
            int state = task.getState();
            if (state == Task.STATE.NEW || state == Task.STATE.RUNNING || state == Task.STATE.WAIT)
                put(task);
        }
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "Uplaod Service onStart()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Uplaod Service onDestroy()");

        unregisterReceiver(mReceiver);

        release();

        mBinder = null;
    }

    /** 閲嶇疆涓婁紶鍙傛暟 */
    private void resetUpload() {
        mCurrentTuid = "";
        mTotalSize = 0;
        mSendSize = 0;
        mPercent = 0;
        mLastPercent = 0;
        mTask = null;
        mTaskId = -1;
    }

    /** 鍚姩绾跨▼ */
    private void startThread() {

        Log.d(TAG, "Uplaod Service thread start()");

        if (mStart)
            return;

        if (mThread == null) {

            mStart = true;
            mThread = new Thread(this);
            mThread.setName("#Upload Thread");
            mThread.setDaemon(true);
            mThread.start();
        }
    }

    /** 閲婃斁 */
    private void release() {

        mStart = false;

        try {
            if (mThread != null)
                mThread.join();
            mThread = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        // common network
        // mUploadImpl.release() ;
        //
        reset();
    }

    /** 娣诲姞浠诲姟 */
    private boolean put(Task task) {

        if (task == null)
            return false;

        if (UploadQueue.size() < SIZE) {
            task.setState(Task.STATE.WAIT);// 绛夊緟涓婁紶
            task.setDetail(Text.WAIT);
            UploadQueue.offer(task);
            return true;
        }

        return false;
    }

    /**
     * 鏇存柊浠诲姟鐘舵��
     * 
     * @param finish : 鏄惁瀹屾垚
     */
    private void updataTaskState(boolean finish, String message) {

        String tuid = mCurrentTuid;

        // 鏈夋晥id
        if (tuid == null || tuid.equals(""))
            return;

        final ReentrantLock lock = mLock;
        // final UGCUploadImpl upload = mUploadImpl ;
        final CWIChannelProxy channelProxy = CWUploadWebService.ChannelProxy;
        final ChannelInfo channel = mChannel;
        final IDBManager db = mDatabaseAccess;
        final ArrayBlockingQueue<Task> queue = UploadQueue;

        // 閲婃斁 JNI 璧勬簮
        lock.lock();
        try {
            // network stop
            // upload.stop();
            if (null != currentFtpInstance) {
                currentFtpInstance.stop();
            }
            // 閲婃斁浼犺緭閫氶亾
            if (channel != null)
                channelProxy.releaseTransferChannel(channel.getTransferUID());

        } finally {
            lock.unlock();
        }
        // 绉婚櫎褰撳墠浠诲姟
        final Task task = queue.poll();
        // 鏇存柊鏁版嵁搴�
        if (task != null) {
            // 璁剧疆鐘舵��
            task.setState(finish ? Task.STATE.DONE : Task.STATE.STOP);
            db.updateObject(task);
            // 閫氱煡鐘舵��
            notifyChange((finish ? Task.STATE.DONE : Task.STATE.STOP), message);
        }
        // 閲嶇疆
        resetUpload();

    }

    /** 鐘舵�佸彉鍖� */
    private void notifyChange(int state, String message) {
        if (mCallback != null) {
            UploadMessage msg = new UploadMessage();
            msg.mTuid = mCurrentTuid;
            msg.mState = state;
            msg.mMessage = message;
            msg.mTaskId = mTaskId;
            mCallback.notifyChange(msg);
        }
    }

    private final void updateAllTask() {
        ArrayBlockingQueue<Task> queue = UploadQueue;
        IDBManager db = mDatabaseAccess;
        for (Task task : queue) {
            db.updateObject(task);
        }
    }

    /*
     * public boolean isUploadFile() { return mUploadImpl.isUploadFile(); }
     */

    @Override
    public void run() {

        Task task;
        final CWIChannelProxy channelProxy = CWUploadWebService.ChannelProxy;
        final ArrayBlockingQueue<Task> queue = UploadQueue;

        while (mStart) {
            // 涓婁紶闃熷垪
            task = queue.peek();

            // upload
            if (task != null) {

                if (!task.getTransferUID().equals(mCurrentTuid)) {

                    // 閲嶇疆浠诲姟
                    resetUpload();
                    // 璇锋眰涓婁紶閫氶亾
                    mCurrentTuid = task.getTransferUID();
                    mTask = task;

                    // 閫氱煡UI
                    notifyChange(Task.STATE.WAIT, Text.WAIT_CONNT);
                    // 鏂囦欢璺緞鏄惁鏈夋晥
                    if (mTask.getTaskPath().equals("")) {
                        notifyChange(Task.STATE.STOP, Text.INVALID_PATH);
                        mCurrentTuid = "";
                    } else {
                        // 璇锋眰閫氶亾
//                        channelProxy.requestTransferChannel(ProtocolDefine.StreamType.FILE,
//                                mCurrentTuid, response);
                    	Metadata meta = mTask.getMetadata();
                    	if (meta == null) {
                            updataTaskState(false, "No metadata");
                            continue;
                        }
                    	// 璁剧疆鍏ㄥ眬
                        mTaskId = 0;// 榛樿id=0
                        // 閫氶亾蹇冭烦
//                        service.startTransferHeartbeat(channel.getTransferUID());
//                        //
                        meta.setUsername("jiating");

                        // ContinueFTP.upload(task.getTransferUID(),
                        // task.getTaskPath(), dataFile, listener);
                        Log.d("Main", "ftp:" + task.getTaskName());
                        // 寮�濮嬩笂浼�,绾跨▼闃诲鐘舵��
                        currentFtpInstance = ContinueFTP.createNewInstance("192.168.81.111", 21, "jason", "1342",
                                ftpProgressListener);
                        if (currentFtpInstance.upload(task)) {// 寮�濮婩TP浼犺緭
                            LOGW("FTP transfer finish");
                            // updataTaskState(true, "FTP transfer end");
                        } else {
                            LOGW("FTP start error");
                            updataTaskState(false, Text.FTP_START_ERROR);
                        }
                        Log.d("Main", "getChannelEnd:" + task.getTaskName());
                    }

                }
            }
            task = null;
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LOGD("File upload thread exit");
        // 鍋滄
        updateAllTask();
    }

    private String pathForToday() {
        Time time = new Time();
        time.setToNow();
        return time.format("%Y/%m/%d/");
    }

    /** error code */
    private void disposeErrorCode(int code) {

        Log.d(TAG, "Upload Error code=" + code);

        switch (code) {
            case CodeError.CODE_ERROR_SEND_ERROR:
                break;
            case CodeError.CODE_ERROR_RECV_ERROR:
                break;
            case CodeError.CODE_ERROR_OPEN_FILE:
                break;
            case CodeError.CODE_ERROR_TIMEOUT:
                break;
            case CodeFTP.CODE_ERROR_LOGIN:
                break;
            case CodeFTP.CODE_ERROR_LOGOUT:
                break;
            case CodeFTP.CODE_ERROR_REMOTE_MKDIR:
                break;
            case CodeFTP.CODE_ERROR_REMOTE_CHANGE:
                break;
            case CodeFTP.CODE_ERROR_LOCAL_RFILE:
                break;
            case CodeFTP.CODE_ERROR_LOCAL_CFILE:
                break;
            case CodeFTP.CODE_ERROR_LOCAL_MKDIR:
                break;
            case CodeFTP.CODE_ERROR_SEND_DATA:
                break;
            case CodeFTP.CODE_ERROR_READ_DATA:
                break;
            case CodeFTP.CODE_ERROR_SOKET:
                break;
            case CodeFTP.CODE_REMOTE_MKFILE:
                break;
            case CodeFTP.CODE_DATAPORT_ERROR:
                break;
        }
    }

    FtpProgressListener ftpProgressListener = new FtpProgressListener() {

        @Override
        public void onStart() {

        }

        @Override
        public void onProgress(long percent, long curProgress, long total, Bundle params) {
            final IFileUploadCallback callback = mCallback;
            final ChannelInfo channel = mChannel;

            mTotalSize = total;
            mSendSize = curProgress;
            mPercent = (int) (((float) mSendSize / mTotalSize) * 100);

            // 杩涘害鍥炶皟
            if (mLastPercent != mPercent && callback != null) {
                mLastPercent = mPercent;
                callback.progress(mCurrentTuid, total, mPercent, STATE.RUNNING);
            }
        }

        @Override
        public void onFinish(boolean result) {
            updataTaskState(result, result ? Text.FTP_UPLOAD_FINISH : Text.NETWORK_ERROR);
        }
    };

    /** soap response */
    private final CWSoapResponse response = new CWSoapResponse() {

        @Override
        public Object dispatchResponse(Object value) {

            if (mTask == null)
                return null;

            final Task task = mTask;
            final Metadata meta = mTask.getMetadata();
            final CWIChannelProxy service = CWUploadWebService.ChannelProxy;
            final CWIUserProxy userProxy = CWUploadWebService.UserProxy;
            final ReentrantLock lock = mLock;

            if (value instanceof ChannelInfo) {

                boolean connect = false;
                ChannelInfo channel = (ChannelInfo) value;
                String ip = null;
                int port = 0;
                if (meta == null) {
                    updataTaskState(false, "No metadata");
                    return null;
                }
                // 閫氶亾鏄惁閫氳繃
                if (!channel.getStatus().equals(ProtocolDefine.CHANNEL_STATE_PASSED)) {
                    updataTaskState(false, "The channel refuse: " + channel.getRejectMessage());
                    return null;
                }

                // channel transfer mode
                int mode = channel.getMode();

                // tcp transfer
                if (mode == ChannelInfo.Mode.FTP) {

                    LOGW("FTP transfer");

                    ip = CWUploadWebService.parserHostIpAddress(channel.getFtp().getFtpServer());

                    if (ip != null) {

                        // 璁剧疆鍏ㄥ眬
                        mChannel = channel;
                        port = channel.getFtp().getFtpPort();
                        String uname = channel.getFtp().getFtpUname();
                        String upwd = channel.getFtp().getFtpUpwd();
                        mTaskId = 0;// 榛樿id=0
                        // 閫氶亾蹇冭烦
                        service.startTransferHeartbeat(channel.getTransferUID());
                        //
                        meta.setUsername(userProxy.getUser().getUsername());

                        // ContinueFTP.upload(task.getTransferUID(),
                        // task.getTaskPath(), dataFile, listener);
                        Log.d("Main", "ftp:" + task.getTaskName());
                        // 寮�濮嬩笂浼�,绾跨▼闃诲鐘舵��
                        currentFtpInstance = ContinueFTP.createNewInstance(ip, port, uname, upwd,
                                ftpProgressListener);
                        if (currentFtpInstance.upload(task)) {// 寮�濮婩TP浼犺緭
                            LOGW("FTP transfer finish");
                            // updataTaskState(true, "FTP transfer end");
                        } else {
                            LOGW("FTP start error");
                            updataTaskState(false, Text.FTP_START_ERROR);
                        }

                    } else {
                        updataTaskState(false, Text.FTP_HOST_ERROR);
                    }
                }
            } else if (value instanceof ErrorMessage) {
                // 鏇存柊鐘舵��
                updataTaskState(false, ((ErrorMessage) value).getMessage());
            } else {
                // 鏇存柊鐘舵��
                updataTaskState(false, Text.APPLY_CHANNEL_FAILED);
            }

            /*
             * final UGCUploadImpl upload = mUploadImpl ; final Task task =
             * mTask ; final Metadata meta = mTask.getMetadata() ; final
             * CWIChannelProxy service = CWUploadWebService.ChannelProxy ; final
             * CWIUserProxy userProxy = CWUploadWebService.UserProxy ; final
             * ReentrantLock lock = mLock ; if(value instanceof ChannelInfo){
             * boolean connect = false ; ChannelInfo channel =
             * (ChannelInfo)value ; String ip = null ; int port = 0 ; if(meta ==
             * null) { updataTaskState(false,"No metadata"); return null ; } //
             * 閫氶亾鏄惁閫氳繃
             * if(!channel.getStatus().equals(ProtocolDefine.CHANNEL_STATE_PASSED
             * )){ updataTaskState(false,"The channel refuse: " +
             * channel.getRejectMessage()); return null ; } // channel transfer
             * mode int mode = channel.getMode() ; // tcp transfer if(mode ==
             * ChannelInfo.Mode.TCP) { LOGW("Private TCP transfer") ; ip =
             * CWUploadWebService
             * .parserHostIpAddress(channel.getTcp().getHost()); port =
             * channel.getTcp().getPort() ; if(ip != null){ // 璁剧疆鍏ㄥ眬 mChannel =
             * channel ; // lock lock.lock() ; try{ // 涓婁紶璁剧疆
             * upload.setFileInfo(task.getTaskName(),
             * task.getTaskExtensionName());
             * meta.setUsername(userProxy.getUser().getUsername());
             * upload.setMetadata(meta, mCurrentTuid); // 杩炴帴 connect =
             * upload.connect(ip, port) ; // debug LOGW("Open connect :" +
             * (connect ? "success" : "failed!") + " ip=" + ip + " port=" +
             * port); // 鏄惁鎴愬姛 if(!connect){
             * updataTaskState(false,Text.OPEN_CONNT_FAILED); } else { // 閫氱煡UI
             * notifyChange(Task.STATE.RUNNING,Text.CONN_SUCCESS); // 閫氶亾蹇冭烦
             * service.startTransferHeartbeat(channel.getTransferUID()); // 寮�濮嬩笂浼�
             * LOGW("File Path =" + task.getTaskPath()) ;
             * upload.setFilePath(task.getTaskPath());
             * upload.start(task.getTaskProgress() > 0) ; } } finally{
             * lock.unlock() ; } } else {
             * updataTaskState(false,channel.getRejectMessage()); } } else
             * if(mode == ChannelInfo.Mode.FTP){ LOGW("FTP transfer") ; ip =
             * CWUploadWebService
             * .parserHostIpAddress(channel.getFtp().getFtpServer()); if(ip !=
             * null){ // 璁剧疆鍏ㄥ眬 mChannel = channel ; port =
             * channel.getFtp().getFtpPort() ; String uname =
             * channel.getFtp().getFtpUname() ; String upwd =
             * channel.getFtp().getFtpUpwd() ; mTaskId = 0 ;// 榛樿id=0 // 閫氶亾蹇冭烦
             * service.startTransferHeartbeat(channel.getTransferUID()); //
             * meta.setUsername(userProxy.getUser().getUsername()); //
             * 璁剧疆鍏冩暟鎹拰浼犺緭id upload.setMetadata(meta, mCurrentTuid); // FTP 浼犺緭璁剧疆
             * upload.setFTPServerInfo(ip, port, uname, upwd); // ftp 鐩綍
             * upload.setFTPDirectory(pathForToday()); // 鍒涘缓浼犺緭浠诲姟
             * ArrayList<String> files = new ArrayList<String>() ;
             * files.add(task.getTaskPath()); // 鏄惁澶氭枃浠� ArrayList<String> list =
             * task.getRelateFileList() ; for(String file : list){
             * files.add(file); } // 寮�濮嬩笂浼�,绾跨▼闃诲鐘舵�� if(upload.startFTPUpload(files,
             * true)){//寮�濮婩TP浼犺緭 LOGW("FTP transfer finish") ;
             * updataTaskState(true,"FTP transfer end"); } else {
             * LOGW("FTP start error") ;
             * updataTaskState(false,Text.FTP_START_ERROR); } } else {
             * updataTaskState(false,Text.FTP_HOST_ERROR); } } } else if(value
             * instanceof ErrorMessage){ // 鏇存柊鐘舵��
             * updataTaskState(false,((ErrorMessage)value).getMessage()); } else
             * { // 鏇存柊鐘舵�� updataTaskState(false,Text.APPLY_CHANNEL_FAILED); }
             */

            return value;
        }
    };

    private final IUGCUploadCallback callback = new IUGCUploadCallback() {

        @Override
        public void progress(long total, int sendsize, int id) {

            final IFileUploadCallback callback = mCallback;
            final ChannelInfo channel = mChannel;

            mTotalSize = total;
            mSendSize = sendsize;
            mPercent = (int) (((float) mSendSize / mTotalSize) * 100);

            // 杩涘害鍥炶皟
            if (mLastPercent != mPercent && callback != null) {
                mLastPercent = mPercent;
                callback.progress(mCurrentTuid, total, mPercent, id);
            }

            // FTP mode
            if (channel.getMode() == ChannelInfo.Mode.FTP) {
                // FTP 涓婁紶澶氫换鍔�
                mTaskId = id;
                // 鏄惁涓婁紶瀹屾垚
                if (total == sendsize) {
                    updataTaskState(true, Text.FTP_UPLOAD_FINISH);
                }
            }
        }

        @Override
        public void callback(int command, int code, String t1, String t2) {

            // callback to UI activity
            if (mCallback != null)
                mCallback.command(mCurrentTuid, command, code, t1);
            // update state
            if (command == Command.COMMAND_TYPE_VERFIY) {
                switch (code) {
                    case CodeVerify.CODE_VERIFY_FAILED:
                    case CodeVerify.CODE_VERIFY_TIMEOUT:
                        updataTaskState(false, t1);
                        break;
                    case CodeVerify.CODE_VERIFY_SUCCESS:
                        updataTaskState(true, Text.VERIFY_SUCCESS);
                        break;
                }
            } else if (command == Command.COMMAND_TYPE_ERROR) {

                updataTaskState(false, Text.NETWORK_ERROR);

                disposeErrorCode(code);
            }
        }

        @Override
        public void liveStatistics(long send, long lost, int bitRate) {

        }
    };

    private final class UGCUploadBinder extends Binder implements IFileUpload {

        @Override
        public void setCallback(IFileUploadCallback callback) {
            mCallback = callback;
        }

        @Override
        public void removeTask(Task task) {

            if (task == null)
                return;

            IDBManager db = mDatabaseAccess;
            ArrayList<Task> list = mHistoryList;
            ArrayList<Metadata> meta = mMetadataList;
            ArrayBlockingQueue<Task> queue = UploadQueue;

            // 鏄惁鏄鍦ㄤ笂浼犱换鍔�
            boolean run = task.getTransferUID().equals(mCurrentTuid);
            if (run)
                updataTaskState(false, "");// 浠庝笂浼犻槦鍒椾腑鍒犻櫎

            // 鍒犻櫎鏁版嵁搴�
            db.deleteObject(task);
            db.deleteObject(task.getMetadata());
            //
            meta.remove(task.getMetadata());
            list.remove(task);
            // 鏄惁鍦ㄤ笂浼犻槦鍒椾腑
            if (!run)
                queue.remove(task);
        }

        @Override
        public boolean resumeTask(Task task) {
            return put(task);
        }

        @Override
        public int getUploadSize() {
            return getUploadQueueSize();
        }

        @Override
        public void stopCurrentTask() {
            updataTaskState(false, "");
        }

        @Override
        public ArrayList<Task> getHistoryList() {
            return mHistoryList;
        }
    }
}
