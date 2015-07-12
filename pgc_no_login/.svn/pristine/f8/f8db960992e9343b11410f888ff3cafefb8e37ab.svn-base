package cn.dezhisoft.cloud.mi.newugc.common.net.httpclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.entity.mime.content.FileBody;

import cn.dezhisoft.cloud.mi.newugc.common.exception.UserCancelException;
import cn.dezhisoft.cloud.mi.newugc.common.net.HttpCancelListenner;

import android.util.Log;

public class FileBodyOffset extends FileBody {
	public static final int BUFFER_SIZE = 8 * 1024;
	private TransferProgressListener mListener;
	private HttpCancelListenner mCancel;
	private int progress = 0;
	private long offset;
	private long size;
	
	public FileBodyOffset(File file, String contentType, String charset, long offset, long size) {
		super(file, file.getName(), contentType, charset);
		this.size = size;
		this.offset = offset;
		
		long length = file.length();
		this.size = (int) Math.min(size, length - (offset + 1));
	}
	
	public FileBodyOffset(File file, String charset, long offset, long size) {
		super(file, file.getName(), "application/octet-stream", charset);
		this.size = size;
		this.offset = offset;
		
		long length = file.length();
		this.size = (int) Math.min(size, length - (offset + 1));
	}
	
	public void setListener(TransferProgressListener listener, HttpCancelListenner cancel) {
		this.mListener = listener;
		this.mCancel = cancel;
	}
	
	@Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.getFile());
    }
    
	@Override
	public void writeTo(OutputStream out) throws IOException {
		if (out == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		InputStream in = new FileInputStream(this.getFile());
		try {
			in.skip(offset);
			int len;
			long remaing = size;
			byte[] tmp = new byte[BUFFER_SIZE];
			while ((len = in.read(tmp)) != -1) {
				
				//Upload cancel
				if(mCancel != null && mCancel.isCanceled()) {
					throw new UserCancelException();
				}
				
				if(remaing > len) {
					remaing = remaing - len;
					out.write(tmp, 0, len);
					updateProgress(size - remaing);
				} else {
					Log.d("File", len + "/" + (remaing + 1));
					out.write(tmp, 0, len);
					remaing = remaing - len;
					updateProgress(size - remaing);
					break;
				}
			}
			out.flush();
		} finally {
			in.close();
		}
	}

	protected void updateProgress(long transportSize) {
		if(mListener != null) {
			//如果当前的�?和进度条保存的�?相同，则不�?知�?
			int p = (int) ((float)transportSize / size * 100);
			if(p != progress) {
				mListener.onProgress((int)Math.min(transportSize, size), (int)size, null);
				progress = p;
			}
		}
	}

	@Override
	public long getContentLength() {
		return this.size;
	}
}

