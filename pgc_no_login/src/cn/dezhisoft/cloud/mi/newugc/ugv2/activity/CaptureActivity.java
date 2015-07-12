
package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.CommonUtils;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.SettingUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Resolution;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

public class CaptureActivity extends Activity implements SurfaceHolder.Callback {
    private static final String TAG = "CaptureActivity";

    public static final int REQUEST_VIDEOPATH_CODE = 0x0f000000;

    public static final String VIDEO_PATH = "_videopath";

    public static final String VIDEO_NAME = "_videoname";

    private static final String ParentPath = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + UgV2Constants.CONTENT_PATH_VIDEO;

    private String fileName = "";

    private static final int UPDATE_TIME = 1;

    private TextView recordBtn;

    private TextView exchangeBtn;

    private TextView okBtn;

    private TextView deleteBtn;

    private TextView recordTimeCounter;

    private MediaRecorder mediarecorder;

    private RelativeLayout mSurfaceLayout;

    private int mSurfaceLayoutWidth;

    private int mSurfaceLayoutHeight;

    private SurfaceView cameraSurfaceview;

    private SurfaceHolder cameraSurfaceHolder;

    private int mWidth;

    private int mHeight;

    private int mBitrate;

    private boolean bRecording = false;

    private boolean mRecorderFile = false;

    private boolean mPreview = false;

    private Camera mCamera;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.ugv2_capture_video_layout);

        loadRecorderParameter();
        init();

        fileName = "VEDIO_" + System.currentTimeMillis() + ".mp4";
    }

    private final boolean loadRecorderParameter() {
        mWidth = SettingUtil.readInt(getApplicationContext(),
                UgV2Constants.KEY_VIDEO_SELECTED_WIDTH, UgV2Constants.VIDEO_DEFAULT_WIDTH);
        mHeight = SettingUtil.readInt(getApplicationContext(),
                UgV2Constants.KEY_VIDEO_SELECTED_HEIGHT, UgV2Constants.VIDEO_DEFAULT_HEIGHT);

        int quality = SettingUtil.readInt(getApplicationContext(),
                UgV2Constants.KEY_VIDEO_SELECTED_QUALITY, UgV2Constants.VIDEO_QUALITY_LOW);
        mBitrate = CommonUtils.calculateBitrate(mWidth, mHeight, quality);
        DebugUtil.traceLog("mBitrate=  " + mBitrate);
        return true;
    }

    private void init() {
        mSurfaceLayout = (RelativeLayout) this.findViewById(R.id.ugv2_video_capture_surface_layout);

        recordBtn = (TextView) this.findViewById(R.id.ugv2_video_record_btn);
        VideoRecordListener videoRecordListener = new VideoRecordListener();
        recordBtn.setOnClickListener(videoRecordListener);
        cameraSurfaceview = (SurfaceView) this.findViewById(R.id.ugv2_camera_surfaceview);
        SurfaceHolder holder = cameraSurfaceview.getHolder();
        holder.addCallback(this);
        cameraSurfaceview.setKeepScreenOn(true);

        recordTimeCounter = (TextView) this.findViewById(R.id.ugv2_camera_time_counter);
        exchangeBtn = (TextView) this.findViewById(R.id.ugv2_camera_exchange_btn);
        okBtn = (TextView) this.findViewById(R.id.ugv2_video_ok_btn);
        deleteBtn = (TextView) this.findViewById(R.id.ugv2_video_delete_btn);
        okBtn.setOnClickListener(videoRecordListener);
        deleteBtn.setOnClickListener(videoRecordListener);
    }

    class VideoRecordListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v == recordBtn) {
                if (bRecording) {
                    DebugUtil.traceLog("stopMediaRecorder");
                    stopMediaRecorder();
                    stopCamera(false);
                    recordBtn.setBackgroundResource(R.drawable.ugv2_btn_video_record);
                    deleteBtn.setVisibility(View.VISIBLE);
                    okBtn.setVisibility(View.VISIBLE);
                } else {
                    DebugUtil.traceLog("startMediaRecorder");
                    fixedSurfaceLayout(cameraSurfaceHolder);
                    startMediaRecorder();
                    recordBtn.setBackgroundResource(R.drawable.ugv2_btn_video_recording);
                    deleteBtn.setVisibility(View.GONE);
                    okBtn.setVisibility(View.GONE);
                    createUpdateTimer();
                }
            } else if (v == okBtn) {
                setRecorderResult();
            } else if (v == deleteBtn) {
                mRecorderFile = false;
                finish();
            }
        }
    }

    private void stopMediaRecorder() {
        if (mediarecorder != null) {
            bRecording = false;
            // 设置后不会崩
            mediarecorder.setOnErrorListener(null);
            mediarecorder.stop();
            mediarecorder.release();
            mediarecorder = null;
        }
    }


    private void startMediaRecorder() {
        try {
            stopCamera(mPreview);

            File parent = new File(ParentPath);
            if (!parent.isDirectory()) {
                parent.mkdirs();
            }

            File file = new File(ParentPath + fileName);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            if (mCamera == null) {
                mCamera = Camera.open();
            }
            Parameters params = mCamera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            if (params.isVideoStabilizationSupported()) {
                params.setVideoStabilization(true);
            }
            mCamera.setParameters(params);
            // mCamera.cancelAutoFocus();
            mCamera.unlock();
            mediarecorder = new MediaRecorder();
            mediarecorder.setOnInfoListener(null);
            mediarecorder.setOnErrorListener(null);
            mediarecorder.setCamera(mCamera);
            mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mediarecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
            // mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            // mediarecorder.setVideoEncodingBitRate(mBitrate);
            CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            camcorderProfile.videoBitRate = mBitrate;
            camcorderProfile.videoFrameHeight = mHeight;
            camcorderProfile.videoFrameWidth = mWidth;
            mediarecorder.setProfile(camcorderProfile);
            // mediarecorder.setVideoFrameRate(30);
            // mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            // mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            // mediarecorder.setVideoSize(mWidth, mHeight);

            mediarecorder.setPreviewDisplay(cameraSurfaceHolder.getSurface());
            mediarecorder.setOutputFile(file.getAbsolutePath());
            mediarecorder.prepare();
            mediarecorder.start();

            bRecording = true;
            mRecorderFile = true;
        } catch (Exception e) {
            e.printStackTrace();
            DebugUtil.traceThrowableLog(e);
            mRecorderFile = false;
            stopMediaRecorder();
            stopCamera(false);
        }
    }

    private final void createUpdateTimer() {

        Thread mThread = new Thread(new Runnable() {

            @Override
            public void run() {
                Message msg = null;
                long enter_timer = System.currentTimeMillis();
                long timer;
                int hour;
                int minute;
                int second;
                String time;
                while (bRecording) {
                    try {
                        Thread.sleep(500);
                        timer = (System.currentTimeMillis() - enter_timer) / 1000;
                        hour = (int) (timer / 3600);
                        minute = (int) ((timer % 3600) / 60);
                        second = (int) (timer % 60);
                        time = (hour > 9 ? hour : "0" + hour) + ":"
                                + (minute > 9 ? minute : "0" + minute) + ":"
                                + (second > 9 ? second : "0" + second);
                        msg = mHandler.obtainMessage();
                        msg.what = UPDATE_TIME;
                        msg.obj = time;
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                mHandler.obtainMessage(UPDATE_TIME, "00:00:00").sendToTarget();
            }
        });
        mThread.setName("#Timer Thread");
        mThread.start();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TIME:
                    recordTimeCounter.setText(msg.obj.toString());
                    break;
            }
        }
    };

    private void setRecorderResult() {
        Intent intent = getIntent();
        intent.putExtra(UgV2Constants.KEY_RESULT_PATH, mRecorderFile ? (ParentPath + fileName) : "");
        intent.putExtra(UgV2Constants.KEY_RESULT_NAME, mRecorderFile ? fileName : "");
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initStartSurfaceLayout(SurfaceHolder holder, Camera camera) {
        Parameters params = camera.getParameters();
        Size size = params.getPreviewSize();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        LayoutParams lp = cameraSurfaceview.getLayoutParams();
        double ratio = (size.width + 0.0) / size.height;
        lp.height = dm.heightPixels;
        lp.width = (int) (dm.heightPixels * ratio);
        cameraSurfaceview.setLayoutParams(lp);
    }

    private void fixedSurfaceLayout(SurfaceHolder holder) {

        // Surface View 的比例计算
        Resolution res = Resolution.calculateDisplayResolution(mSurfaceLayoutWidth,
                mSurfaceLayoutHeight, mWidth, mHeight);

        holder.setFixedSize(res.getWidth(), res.getHeight());

        // 更新View宽度和高度
        LayoutParams lp = cameraSurfaceview.getLayoutParams();
        lp.width = res.getWidth();
        lp.height = res.getHeight();
        cameraSurfaceview.setLayoutParams(lp);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged:" + holder);
        cameraSurfaceHolder = holder;

        mCamera.autoFocus(new AutoFocusCallback() {

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {

                    mCamera.cancelAutoFocus();
                }
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated:" + holder);
        cameraSurfaceHolder = holder;

        mSurfaceLayoutWidth = mSurfaceLayout.getWidth();
        mSurfaceLayoutHeight = mSurfaceLayout.getHeight();
        // fixedSurfaceLayout(cameraSurfaceHolder);

        if (mCamera == null) {
            mCamera = Camera.open();
            initStartSurfaceLayout(cameraSurfaceHolder, mCamera);
            try {
                mCamera.setPreviewDisplay(cameraSurfaceHolder);
                startCameraPreview();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            startCameraPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed:" + holder);
        stopMediaRecorder();
        stopCamera(false);

        // cameraSurfaceview = null;
        // cameraSurfaceHolder = null;
    }

    private void startCameraPreview() {
        try {
            Parameters params = mCamera.getParameters();
            // params.setPreviewSize(176, 144);
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            mCamera.setParameters(params);
            mCamera.startPreview();
            mCamera.cancelAutoFocus();
            mPreview = true;
        } catch (Exception e) {
            e.printStackTrace();
            stopCamera(false);
        }
    }

    private void stopCamera(boolean preview) {
        if (mCamera != null) {
            if (preview)
                mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
}
