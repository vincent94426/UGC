
package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.common.util.CommonUtils;
import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.ThreadUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.ToastUtil;
import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Util;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.upload.IFileUpload;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWWebService;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.UGCWebService;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.activity.BaseActivity;
import cn.dezhisoft.cloud.mi.newugc.ugv2.base.app.Ugv2Application;
import cn.dezhisoft.cloud.mi.newugc.ugv2.constants.UgV2Constants;

public class MainActivity extends BaseActivity {
    // protected static final ArrayList<Category> categoryList = new
    // ArrayList<Category>();

    private EditText etTitle, etDetail;

    private TextView tvFileName, tvFileSize;

    private Spinner spCategory;

    private TextView btUpload;

    private String catalogId = null, catalogName = null;

    private int taskRequestCode;

    private String taskRequestPath;

    private static final String TAG = "MainActivity";

    @Override
    protected View onCreateBody() {
        View body = getLayoutInflater().inflate(R.layout.ugv2_main_layout, null);

        etTitle = (EditText) body.findViewById(R.id.ugv2_main_input_title);
        etDetail = (EditText) body.findViewById(R.id.ugv2_main_input_description);

        spCategory = (Spinner) body.findViewById(R.id.ugv2_main_spinner_catergory);

        tvFileName = (TextView) body.findViewById(R.id.ugv2_main_filename_text);
        tvFileSize = (TextView) body.findViewById(R.id.ugv2_main_filesize_text);
        btUpload = (TextView) body.findViewById(R.id.ugv2_main_btn_upload);

        btUpload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                compressPic(taskRequestCode);
            }
        });
//        Log.d("Main", taskRequestPath);

        initParams();

        setUiTitle(R.string.ugv2_txt_title_main);

        return body;
    }

    private void initParams() {
        Intent intent = getIntent();
        if (null == intent || null == intent.getExtras()) {
            ToastUtil
                    .showToastLong(MainActivity.this, getString(R.string.ugv2_txt_msg_error_param));
            DebugUtil.traceLog("mainactivity, init , params error");
            finish();
            return;
        }

        taskRequestCode = intent.getIntExtra(UgV2Constants.KEY_REQ_CODE, -1);
        taskRequestPath = intent.getStringExtra(UgV2Constants.KEY_REQ_PATH);

        DebugUtil.traceLog("mainactivity, init , taskRequestCode:" + taskRequestCode
                + ", taskRequestPath:" + taskRequestPath);

        if (-1 == taskRequestCode || TextUtils.isEmpty(taskRequestPath)) {
            ToastUtil
                    .showToastLong(MainActivity.this, getString(R.string.ugv2_txt_msg_error_param));
            finish();
            return;
        }
//        taskRequestPath = Uri.decode(taskRequestPath);
        taskRequestPath = taskRequestPath.replace("file://", "");
        File file = new File(taskRequestPath);
        String filename = "文件名称: " + file.getName();
        String filesize = "文件大小: " + CommonUtils.toLargeUnit(file.length(), 1);

        tvFileName.setText(filename);
        tvFileSize.setText(filesize);

        queryCategorySpinner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThreadUtil.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                try {
                    startPreviewActivity();
                } catch (Exception e) {
                    DebugUtil.traceThrowableLog(e);
                }
            }
        }, 200);
    }

    @Override
    protected void onResume() {
        super.onResume();
        topBar.setRightBtnVisibility(View.GONE);
    }

    private void startUpload() {
        Task task = buildTaskAndMetedata();
        if (null != task) {
            Ugv2Application.getAppDatabase().saveObject(task);
            Ugv2Application.getAppDatabase().saveObject(task.getMetadata());
            sendQueryBroadcast();
            Toast.makeText(this, R.string.ugc_label_upload_add_succeed, Toast.LENGTH_SHORT).show();

            DebugUtil.traceLog("mainactivity, finish ,taskRequestCode:" + taskRequestCode
                    + ", taskRequestPath:" + taskRequestPath);
            finish();
        }
    }

    private Task buildTaskAndMetedata() {

        String title = etTitle.getText().toString().trim();
        String descri = etDetail.getText().toString().trim();
        String filePath = taskRequestPath;
        // String catalogId = spCategory.getSelectedItemPosition() ;

        // title is null
        if (TextUtils.isEmpty(title)) {
            ToastUtil.showToastShort(MainActivity.this, getString(R.string.ugc_warring_title));
            return null;
        }

        String result = Util.checkInput(title);
        if (result != null) {
            ToastUtil.showToastShort(MainActivity.this,
                    getString(R.string.warnning_login_input_illegal));
            return null;
        }

        // 检验文件是否存在
        if (TextUtils.isEmpty(filePath) || !new File(filePath).exists()) {
            ToastUtil.showToastShort(MainActivity.this, getString(R.string.ugc_warring_path));
            return null;
        }

        // 判断分类id
        /*
         * if(catalogId == null){ ToastUtil.showToastShort(MainActivity.this,
         * getString(R.string.ugc_warring_category)); return null; }
         */

        String tuid = UUID.randomUUID().toString();
        Task task = createUploadTask(taskRequestCode, filePath);
        task.setTransferUID(tuid);
        Metadata metadata = createTaskMetaData(title, descri, tuid);
        task.setMetadata(metadata);

        return task;
    }

    private Metadata createTaskMetaData(String title, String descri, String tuid) {
        Metadata metadata = new Metadata();
        metadata.setSiteId(UGCWebService.getSiteId());
        metadata.setTransferUID(tuid);
        metadata.setTitle(title); // title
        metadata.setDescription(descri); // description
        // metadata.setWhat(keyword);// keyword
        metadata.setGps("0,0");
        metadata.setWho("");
        metadata.setPlace("");
        metadata.setCatalogId(catalogId);// 分类id
        metadata.setCatalogName(catalogName);
        metadata.setTransferUID(tuid);
        return metadata;
    }

    protected Task createUploadTask(int taskRequestCode, String filePath) {
        Task task = new Task();
        task.setTaskName(new File(filePath).getName());
        task.setTaskPath(filePath);
        task.setCategory(Task.CATEGORY.UPLOAD);
        task.setState(Task.STATE.NEW);
        task.setAuthor(CWUploadWebService.UserProxy.getUser().getUsername());

        if (taskRequestCode == UgV2Constants.REQ_CODE_CHOOSER_VIDEO
                || taskRequestCode == UgV2Constants.REQ_CODE_VIDEO_CAPTURE
                || taskRequestCode == UgV2Constants.REQ_CODE_VIDEO_CUT) {
            if (CommonUtils.isPictureFile(filePath)) {
                task.setType(Task.TYPE.IMAGE);
            } else {
                task.setType(Task.TYPE.VIDEO);
            }
        } else if (taskRequestCode == UgV2Constants.REQ_CODE_CHOOSER_IMAGE
                || taskRequestCode == UgV2Constants.REQ_CODE_IMAGE_CAPTURE) {
            if (CommonUtils.isPictureFile(filePath)) {
                task.setType(Task.TYPE.IMAGE);
            } else {
                task.setType(Task.TYPE.VIDEO);
            }
        } else {
            task.setType(Task.TYPE.AUDIO);
        }

        return task;
    }

    protected void compressPic(int taskRequestCode) {
        
        Options opts = getImgInfo(taskRequestPath);
        if(null!=opts){
            Log.d("Main", taskRequestPath + " " + opts.outWidth+"*"+opts.outHeight);
        }
        if (CommonUtils.isPictureFile(taskRequestPath) && (opts.outWidth>1920 || opts.outHeight>1080)) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("缩放");
            dialog.setMessage("您的图片大小为"+opts.outWidth+"*"+opts.outHeight+"缩放后上传？");
            dialog.setNegativeButton("取消", null);
            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bitmap bitmap = compressBySize(taskRequestPath, 1920, 1080);
                    saveBitmap(bitmap);
                    recycleBitmap(bitmap);
                    startUpload();
                }
            });
            dialog.show();
        }else{
            startUpload();
        }
    }

    private Options getImgInfo(String path) {
        Options opts = null;
        if (CommonUtils.isPictureFile(path)) {
            opts = new Options();
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = BitmapFactory.decodeFile(path, opts);
            recycleBitmap(bitmap);
        }
        return opts;
    }

//    protected double getFileSize(String path) {
//        File file = new File(path);
//        double size = file.length() / 1024;
//        size = ((int) (size * 10)) / 10.0;
//        return size;
//    }
    
    public static Bitmap compressBySize(String pathName, int targetWidth,  
            int targetHeight) {  
        Options opts = new Options();  
        opts.inJustDecodeBounds = true;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；  
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);  
        // 得到图片的宽度、高度；  
        int imgWidth = opts.outWidth;  
        int imgHeight = opts.outHeight;  
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；  
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);  
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);  
        if (widthRatio > 1 || heightRatio > 1) {  
            opts.inSampleSize = Math.max(widthRatio, heightRatio);
        }  
        // 设置好缩放比例后，加载图片进内容；  
        opts.inJustDecodeBounds = false;  
        bitmap = BitmapFactory.decodeFile(pathName, opts);  
        return bitmap;  
    }

//
//    public String getNewImgPath(String path) {
//        int index = path.lastIndexOf('.');
//        return path.substring(0, index) + ".jpg";
//    }

    public void saveBitmap(Bitmap bm) {
    	String tempImgPath = Environment.getExternalStorageDirectory().getAbsolutePath() + UgV2Constants.CONTENT_PATH_PHOTO_TEMP;
    	String fileName = new File(taskRequestPath).getName();
        File f = new File(tempImgPath, fileName);
        File tempPath = new File(tempImgPath);
        if (!tempPath.exists()) {
        	tempPath.mkdirs();
        }
        if (f.exists()) {
        	f.delete();
        }
        try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.d(TAG, "图片已经保存");
            taskRequestPath = f.toString();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            System.gc();
            bitmap = null;
        }
    }

    private void startPreviewActivity() {
        String dataType;
        Intent intent = new Intent();
        if (taskRequestCode == UgV2Constants.REQ_CODE_CHOOSER_AUDIO) {
            dataType = UgV2Constants.DATA_TYPE_AUDIO;
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(taskRequestPath)), dataType);
        } else if (CommonUtils.isPictureFile(taskRequestPath)) {
            // dataType = UgV2Constants.DATA_TYPE_IMAGE;
            // intent.setAction(Intent.ACTION_VIEW);
            // intent.setDataAndType(Uri.fromFile(new File(taskRequestPath)),
            // dataType);

            intent.putExtra(UgV2Constants.KEY_REQ_PATH, taskRequestPath);
            intent.setClass(MainActivity.this, PicturePreviewActivity.class);
        } else {
            intent.putExtra(UgV2Constants.KEY_REQ_PATH, taskRequestPath);
            intent.setClass(MainActivity.this, VideoPreviewActivity.class);
        }

        startActivity(intent);
    }

    private void sendQueryBroadcast() {
        Intent intent = new Intent();
        intent.setAction(IFileUpload.ACTION_QUERY_NEW_TASK);
        sendBroadcast(intent);
    }

    private String[] columnCodes, columnNames;

    private void queryCategorySpinner() {
        User user = CWWebService.UserProxy.getUser();
        DebugUtil.traceLog("user:" + user);
        if (null == user || TextUtils.isEmpty(user.getCustomFieldForm())) {
            showAlertDialog();
            return;
        }
        DebugUtil.traceLog("user:" + user + ",CustomFieldForm:" + user.getCustomFieldForm());

        try {
            String customField = user.getCustomFieldForm();
            JSONObject jsonCustom = new JSONObject(customField);
            String codes = jsonCustom.optString(UgV2Constants.KEY_COLUMN_CODE);
            String names = jsonCustom.optString(UgV2Constants.KEY_COLUMN_NAME);
            if (TextUtils.isEmpty(codes) || TextUtils.isEmpty(names)) {
                // 没有栏目直接提醒用户
                showAlertDialog();
                return;
            }

            columnCodes = codes.split(",");
            columnNames = names.split(",");

            if (columnCodes.length != columnNames.length) {
                return;
            }

            initColumnSpinner();
        } catch (Exception e) {
            DebugUtil.traceThrowableLog(e);
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(getResources().getString(R.string.ugc_warring_hint_no_type_info));
        builder.setTitle(getResources().getString(R.string.ugc_warring_hint_title));
        builder.setPositiveButton(getResources().getString(R.string.ugc_warring_hint_bnt_yes),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    protected void initColumnSpinner() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item, columnNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(spinnerAdapter);
        spCategory.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                catalogId = columnCodes[position];
                catalogName = columnNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

}
