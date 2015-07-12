package cn.dezhisoft.cloud.mi.newugc.ugv2.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.util.Log;

import cn.dezhisoft.cloud.mi.newugc.ugv2.common.QqMessage;
import cn.dezhisoft.cloud.mi.newugc.ugv2.common.User;


public class QqClientConServer {
	//public Socket s;     
	public static Socket s;     
	
	@SuppressWarnings("resource")
	public boolean sendLoginInfoToServer(Object o){
		boolean b=false;
		
		try {
			s=new Socket();
			try{
				s.connect(new InetSocketAddress("192.168.81.111",10086), 5000);
				Log.i("Success", "连接成功");
			}catch(SocketTimeoutException e){
				Log.i("Error", "连接超时");
				return false;
			}
			
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			QqMessage ms=(QqMessage)ois.readObject(); 
			//Log.i("receive", ms.getType());
			if(ms.getType().equals("1")){
				b=true;
			}
			//oos.close();
			//ois.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
		}
		return b;
	}
	
}
