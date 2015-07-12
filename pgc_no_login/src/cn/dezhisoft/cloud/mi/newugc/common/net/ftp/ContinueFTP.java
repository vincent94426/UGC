
package cn.dezhisoft.cloud.mi.newugc.common.net.ftp;

import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.common.util.StringUtil;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import android.os.Bundle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContinueFTP {
    private static final String ENCODE_UTF8 = "UTF-8";

    private static final String ENCODE_GBK = "GBK";

    private static final String ENCODE_ISO = "ISO-8859-1";

    private static final String FTP_USER_PASSWORD = "sobey";

    private static final String FTP_USER_NAME = "sobey";

    private static final int FTP_SERVER_PORT = 21;

    private static final String FTP_SERVER_IP = "172.16.174.111";

    public FTPClient ftpClient = new FTPClient();

    private FtpProgressListener progressListener = null;

    private String localEncode = ENCODE_GBK;

    private ContinueFTP() {
        // 设置将过程中使用到的命令输出到控制台
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(
                System.out)));
    }

    private ContinueFTP(FtpProgressListener progressListener) {
        // 设置将过程中使用到的命令输出到控制台
        this.ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(
                System.out)));
        this.progressListener = progressListener;
    }

    /**
     * 连接到FTP服务器
     * 
     * @param hostname 主机名
     * @param port 端口
     * @param username 用户名
     * @param password 密码
     * @return 是否连接成功
     * @throws IOException
     */
    private boolean connect(String hostname, int port, String username, String password)
            throws IOException {
        ftpClient.connect(hostname, port);
        if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            if (ftpClient.login(username, password)) {
                if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {// 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
                    localEncode = ENCODE_UTF8;
                }
                ftpClient.setControlEncoding(localEncode);
                return true;
            }
        }
        disconnect();
        return false;
    }

    /** */
    /**
     * 从FTP服务器上下载文件,支持断点续传，上传百分比汇报
     * 
     * @param remote 远程文件路径
     * @param local 本地文件路径
     * @return 上传的状态
     * @throws IOException
     */
    private DownloadStatus download(String remote, String local) throws IOException {
        // 设置被动模式
        ftpClient.enterLocalPassiveMode();
        // 设置以二进制方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        DownloadStatus result;

        // 检查远程文件是否存在
        FTPFile[] files = ftpClient.listFiles(encodeFtpString(remote));
        if (files.length != 1) {
            System.out.println("远程文件不存在");
            return DownloadStatus.Remote_File_Noexist;
        }

        long lRemoteSize = files[0].getSize();
        File f = new File(local);
        // 本地存在文件，进行断点下载
        if (f.exists()) {
            long localSize = f.length();
            // 判断本地文件大小是否大于远程文件大小
            if (localSize >= lRemoteSize) {
                System.out.println("本地文件大于远程文件，下载中止");
                return DownloadStatus.Local_Bigger_Remote;
            }

            // 进行断点续传，并记录状态
            FileOutputStream out = new FileOutputStream(f, true);
            ftpClient.setRestartOffset(localSize);
            InputStream in = ftpClient.retrieveFileStream(encodeFtpString(remote));
            byte[] bytes = new byte[1024];
            long step = lRemoteSize / 100;
            long process = localSize / step;
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
                localSize += c;
                long nowProcess = localSize / step;
                if (nowProcess > process) {
                    process = nowProcess;
                    if (process % 10 == 0)
                        System.out.println("下载进度：" + process);
                    // TODO 更新文件下载进度,值存放在process变量中
                }
            }
            in.close();
            out.close();
            boolean isDo = ftpClient.completePendingCommand();
            if (isDo) {
                result = DownloadStatus.Download_From_Break_Success;
            } else {
                result = DownloadStatus.Download_From_Break_Failed;
            }
        } else {
            OutputStream out = new FileOutputStream(f);
            InputStream in = ftpClient.retrieveFileStream(encodeFtpString(remote));
            byte[] bytes = new byte[1024];
            long step = lRemoteSize / 100;
            long process = 0;
            long localSize = 0L;
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
                localSize += c;
                long nowProcess = localSize / step;
                if (nowProcess > process) {
                    process = nowProcess;
                    if (process % 10 == 0)
                        System.out.println("下载进度：" + process);
                    // TODO 更新文件下载进度,值存放在process变量中
                }
            }
            in.close();
            out.close();
            boolean upNewStatus = ftpClient.completePendingCommand();
            if (upNewStatus) {
                result = DownloadStatus.Download_New_Success;
            } else {
                result = DownloadStatus.Download_New_Failed;
            }
        }
        return result;
    }

    /** */
    /**
     * 上传文件到FTP服务器，支持断点续传
     * 
     * @param local 本地文件名称，绝对路径
     * @param remote 远程文件路径，使用/home/directory1/subdirectory/file.ext或是
     *            http://www.guihua.org /subdirectory/file.ext
     *            按照Linux上的路径指定方式，支持多级目录嵌套，支持递归创建不存在的目录结构
     * @return 上传结果
     * @throws IOException
     */
    private UploadStatus upload(String local, String remote) throws IOException {
        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();
        // 设置以二进制流的方式传输
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        ftpClient.setControlEncoding(localEncode);
        UploadStatus result;
        // 对远程目录的处理
        String remoteFileName = remote;
        if (remote.contains("/")) {
            remoteFileName = remote.substring(remote.lastIndexOf("/") + 1);
            // 创建服务器远程目录结构，创建失败直接返回
            if (CreateDirecroty(remote, ftpClient) == UploadStatus.Create_Directory_Fail) {
                return UploadStatus.Create_Directory_Fail;
            }
        }

        // 检查远程是否存在文件
        // FTPFile[] files = ftpClient.listFiles(new
        // String(remoteFileName.getBytes(FTP_CONTROL_ENCODE),"iso-8859-1"));
        FTPFile[] files = ftpClient.listFiles(encodeFtpString(remoteFileName));
        if (files.length == 1) {
            long remoteSize = files[0].getSize();
            File f = new File(local);
            long localSize = f.length();
            if (remoteSize == localSize) {
                return UploadStatus.File_Exits;
            } else if (remoteSize > localSize) {
                return UploadStatus.Remote_Bigger_Local;
            }

            // 尝试移动文件内读取指针,实现断点续传
            result = uploadFile(remoteFileName, f, ftpClient, remoteSize);

            // 如果断点续传没有成功，则删除服务器上文件，重新上传
            if (result == UploadStatus.Upload_From_Break_Failed) {
                if (!ftpClient.deleteFile(remoteFileName)) {
                    return UploadStatus.Delete_Remote_Faild;
                }
                result = uploadFile(remoteFileName, f, ftpClient, 0);
            }
        } else {
            result = uploadFile(remoteFileName, new File(local), ftpClient, 0);
        }
        return result;
    }

    /** */
    /**
     * 断开与远程服务器的连接
     * 
     * @throws IOException
     */
    private void disconnect() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
    }

    /** */
    /**
     * 递归创建远程服务器目录
     * 
     * @param remote 远程服务器文件绝对路径
     * @param ftpClient FTPClient对象
     * @return 目录创建是否成功
     * @throws IOException
     */
    private UploadStatus CreateDirecroty(String remote, FTPClient ftpClient) throws IOException {
        UploadStatus status = UploadStatus.Create_Directory_Success;
        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
        if (!directory.equalsIgnoreCase("/")
                && !ftpClient.changeWorkingDirectory(encodeFtpString(directory))) {
            // 如果远程目录不存在，则递归创建远程服务器目录
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            while (true) {
                String subDirString = remote.substring(start, end);
                String subDirectory = encodeFtpString(subDirString);
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    if (ftpClient.makeDirectory(subDirectory)) {
                        ftpClient.changeWorkingDirectory(subDirectory);
                    } else {
                        DebugUtil.traceLog("创建目录失败:" + subDirectory);
                        return UploadStatus.Create_Directory_Fail;
                    }
                }

                start = end + 1;
                end = directory.indexOf("/", start);

                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return status;
    }

    /**
     * 设置FTP编码
     */
    private String encodeFtpString(String directory) throws UnsupportedEncodingException {
        return new String(directory.getBytes(localEncode), ENCODE_ISO);
        // return directory;
    }

    /** */
    /**
     * 上传文件到服务器,新上传和断点续传
     * 
     * @param remoteFile 远程文件名，在上传之前已经将服务器工作目录做了改变
     * @param localFile 本地文件File句柄，绝对路径
     * @param remoteSize 需要显示的处理进度步进值
     * @param ftpClient FTPClient引用
     * @return
     * @throws IOException
     */
    private UploadStatus uploadFile(String remoteFile, File localFile, FTPClient ftpClient,
            long remoteSize) throws IOException {
        UploadStatus status;
        // 显示进度的上传
        long step = localFile.length() / 100;
        long process = 0;
        long total = localFile.length();// TODO 可能有断点续传，因此用total计算进度可能有问题
        long localreadbytes = 0L;
        RandomAccessFile raf = new RandomAccessFile(localFile, "r");
        OutputStream out = ftpClient.appendFileStream(encodeFtpString(remoteFile));

        if (progressListener != null) {
            progressListener.onStart();
        }
        long startTime = new Date().getTime();
        // 断点续传
        if (remoteSize > 0) {
            ftpClient.setRestartOffset(remoteSize);
            process = remoteSize / step;
            raf.seek(remoteSize);
            localreadbytes = remoteSize;
        }
        byte[] bytes = new byte[1024];
        int c;
        while ((c = raf.read(bytes)) != -1) {
            out.write(bytes, 0, c);
            localreadbytes += c;
            if (localreadbytes / step != process) {
                process = localreadbytes / step;
                System.out.println("上传进度:" + process);
                //保持socket连接，发送无意义指令
                if (new Date().getTime() - startTime > 5000) {
                    ftpClient.sendNoOp();
                    startTime = new Date().getTime();
                }

                // TODO 汇报上传状态

                if (progressListener != null) {
                    progressListener.onProgress(process, localreadbytes, total, null);
                }
            }
        }
        out.flush();
        raf.close();
        out.close();
        boolean result = ftpClient.completePendingCommand();
        if (result) {
            changeRemoteFileName(ftpClient, remoteFile);
        }
        if (remoteSize > 0) {
            status = result ? UploadStatus.Upload_From_Break_Success
                    : UploadStatus.Upload_From_Break_Failed;

            if (progressListener != null) {
                progressListener.onFinish(result);
            }

        } else {
            status = result ? UploadStatus.Upload_New_File_Success
                    : UploadStatus.Upload_New_File_Failed;

            if (progressListener != null) {
                progressListener.onFinish(result);
            }
        }
        return status;
    }

    private synchronized void changeRemoteFileName(FTPClient ftpClient, String fileName) {
        // TODO 上传完成,将后缀_vtb去掉
        try {
            boolean result = ftpClient.rename(encodeFtpString(fileName),
                    encodeFtpString(fileName.substring(0, fileName.length() - 4)));
            System.out.println("重命名:" + fileName + ":" + result);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接FTP出错：" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ContinueFTP myFtp = new ContinueFTP();
        try {
            myFtp.connect(FTP_SERVER_IP, FTP_SERVER_PORT, FTP_USER_NAME, FTP_USER_PASSWORD);
            // myFtp.ftpClient.makeDirectory(myFtp.encodeFtpString("电"));
            // myFtp.ftpClient.changeWorkingDirectory(myFtp.encodeFtpString("电"));
            System.out.println(myFtp.upload("/Users/zhaoliangsakai/downloads/shengzhengOTT.rar",
                    "/test/test.zip_vtb"));
            myFtp.disconnect();
        } catch (IOException e) {
            System.out.println("连接FTP出错：" + e.getMessage());
        }
    }

    private String addr, username, password;

    private int port;

    public static ContinueFTP createNewInstance(String addr, int port, String username,
            String password, FtpProgressListener listener) {
        ContinueFTP myFtp = new ContinueFTP(listener);

        myFtp.addr = addr;
        myFtp.username = username;
        myFtp.password = password;
        myFtp.port = port;

        return myFtp;
    }

    private boolean bUploading = false;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public boolean upload(Task task) {
        // ContinueFTP myFtp = new ContinueFTP(listener);
        try {
            bUploading = true;

            String uuid = task.getTransferUID();
            String videoFile = task.getTaskPath();

            String dataFile = buildMetaDataFile(task, uuid);
            DebugUtil.traceLog("data xml:" + dataFile);

            Metadata meta = task.getMetadata();
            String column = StringUtil.isBlank(meta.getCatalogName()) ? "ugc" : meta
                    .getCatalogName();
            String taskuser = StringUtil.isBlank(task.getAuthor()) ? "ugc" : task.getAuthor();
            String nowDate = sdf.format(new Date());

            this.connect(addr, port, username, password);

            String remoteDir = "/" + column + "/" + taskuser + "/" + nowDate + "/" + uuid + "/";
            // 上传媒体文件
            // TODO 传输完成前使用 文件名+_vtb
            UploadStatus videoStatus = this.upload(videoFile,
                    remoteDir + new File(videoFile).getName() + "_vtb");

            this.disconnect();

            ContinueFTP dataFtp = createNewInstance(addr, port, username, password, null);
            dataFtp.connect(addr, port, username, password);

            // 上传xml信息
            // TODO 传输完成前使用 文件名+_vtb
            UploadStatus dataStatus = dataFtp.upload(dataFile,
                    remoteDir + new File(dataFile).getName() + "_vtb");

            dataFtp.disconnect();

            bUploading = false;

            DebugUtil.traceLog("remoteDir:" + remoteDir);
            DebugUtil.traceLog("videoStatus:" + videoStatus + ", dataStatus:" + dataStatus);

            boolean videoSuccess = false;
            if (videoStatus == UploadStatus.Upload_New_File_Success
                    || videoStatus == UploadStatus.Upload_From_Break_Success) {
                videoSuccess = true;
            }

            boolean dataSuccess = false;
            if (dataStatus == UploadStatus.Upload_New_File_Success
                    || dataStatus == UploadStatus.Upload_From_Break_Success) {
                dataSuccess = true;
            }

            return videoSuccess && dataSuccess;
        } catch (Exception e) {
            DebugUtil.traceThrowableLog(e);
        }
        return false;
    }

    private String buildMetaDataFile(Task task, String uuid) {
        Metadata meta = task.getMetadata();
        String title = null == meta ? "" : meta.getTitle();
        String description = null == meta ? "" : meta.getDescription();
        title = null == title ? "" : title;
        description = null == description ? "" : description;

        String dataFile = FileUtil.writeDataXmlFile(task);
        return dataFile;
    }

    public void stop() {
        try {
            if (bUploading) {
                this.ftpClient.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static interface FtpProgressListener {
        int RESULT_OK = 0;

        int RESULT_ERROR = -1;

        void onStart();

        void onProgress(long percent, long curProgress, long total, Bundle params);

        void onFinish(boolean result);
    }
}
