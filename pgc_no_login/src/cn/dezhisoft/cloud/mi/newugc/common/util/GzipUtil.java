package cn.dezhisoft.cloud.mi.newugc.common.util;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipUtil {

	public static byte[] ungzip( InputStream is ) {
		GZIPInputStream gis = null;
		try {
			gis = new GZIPInputStream( is );
			return IOUtil.readBytes( gis );
		} catch ( IOException e ) {
			throw new RuntimeException("gzip发生错误", e);
		} finally {
			IOUtil.close( gis );
		}
	}

    public static byte[] gzip(byte[] in){
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
        GZIPOutputStream gos = null;
        try {
        	gos = new GZIPOutputStream(baos);
            gos.write(in);
            gos.finish();
            gos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("gzip发生错误",e);
        }finally{
        	IOUtil.close( baos );
        	IOUtil.close( gos );
        }
    }

    public static byte[] ungzip( byte[] in ){
    	InputStream is = null;
    	try{
    		is = new ByteArrayInputStream( in );
            return ungzip( is );
    	}finally{
    		IOUtil.close( is );
    	}
    }
}
