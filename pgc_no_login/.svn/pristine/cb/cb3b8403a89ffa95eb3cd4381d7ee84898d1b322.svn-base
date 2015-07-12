package cn.dezhisoft.cloud.mi.newugc.common.util;

import java.io.UnsupportedEncodingException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;

public class ChecksumUtil {
    private final static int BYTE_MASK = 0xff;
    
    public static String buildChecksum(String key){
    	byte[] keyBtyes;
    	try {
			keyBtyes = key.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			keyBtyes = key.getBytes();
		}
    	return crc32(keyBtyes) + "." + adler32(keyBtyes);
    }
   
    public static String crc32(byte[] input){
        CRC32 crc = new CRC32();
        crc.update(input);
        return base16(int2ByteArray((int)crc.getValue()));
    }
    
    public static String adler32(byte[] input){
        Adler32 adler = new Adler32();
        adler.update(input);
        return base16(int2ByteArray((int)adler.getValue()));
    }
    
    
    private static String base16(byte[] data) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            String temp = Integer.toHexString(data[i] & BYTE_MASK);
            if (temp.length() == 1){
            	temp = "0" + temp;
            }
            sb.append(temp);
        }
        return sb.toString().toUpperCase();
    }
    
    private static byte[] int2ByteArray(int num) {
        byte[] b = new byte[4];
        b[0] = (byte) (num >>> 24);
        b[1] = (byte) (num >>> 16);
        b[2] = (byte) (num >>> 8);
        b[3] = (byte) num;
        return b;
    }
    
}
