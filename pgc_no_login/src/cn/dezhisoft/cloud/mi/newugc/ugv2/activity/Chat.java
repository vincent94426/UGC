package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.common.QqMessage;
import cn.dezhisoft.cloud.mi.newugc.ugv2.common.MessageType;
import cn.dezhisoft.cloud.mi.newugc.ugv2.model.QqClientConServer;
//import com.jason.qqclient2.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.tools.ManageChat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Chat extends Activity implements Runnable{

	public Button chat_send;
	public TextView chat_show;
	public EditText chat_sendMessage;
	public String loginId;
	public String friendId;
	Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugv2_chat);
		Intent intent=getIntent();
		loginId=intent.getStringExtra("id");
		friendId=intent.getStringExtra("friendId");
		
		chat_send=(Button)findViewById(R.id.chat_send);
		chat_show=(TextView)findViewById(R.id.chat_show);
		chat_sendMessage=(EditText)findViewById(R.id.chat_sendMessage);
		chat_show.setText("");
		
		
		chat_send.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream(QqClientConServer.s.getOutputStream());
					String content1=loginId+":  ";
					String content=chat_sendMessage.getText().toString()+"\n";
					content1+=content;
					chat_show.append(content1);
					chat_sendMessage.setText("");
					
					QqMessage m=new QqMessage();
					m.setType(MessageType.message_comm_mes);
					m.setSender(loginId);
					m.setGetter(friendId);
					m.setContent(content);
					oos.writeObject(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
		});
		
		Thread t=new Thread(this);
		t.start();
		
		handler=new Handler(){
			public void handleMessage(Message msg){
				if(msg.what==3){
					Bundle b=(Bundle)msg.obj;
					String sender=b.getString("sender");
					String content=b.getString("content");
					chat_show.append(sender+":  "+content);
				}
				super.handleMessage(msg);
			}
		};
	}
	
	
	public void showMessage(QqMessage m){
		String info=m.getSender()+":  "+m.getContent();
		try{
			chat_show.append(info);
		}catch(Exception e){
			Log.i("Log","所谓异常");
			e.printStackTrace();
			
		}
		
		Log.i("关键", "我现在在聊天界面中"+info);
	}
	
	
	public void run(){
		while(true){
			try{
				//读取【如果读不到就等待】
				//ObjectInputStream ois=new ObjectInputStream(ManageClientConServerThread.getClientConServerThread(loginId).getS().getInputStream());
				Bundle bdl=new Bundle();
				Message msg=handler.obtainMessage();
				msg.what=3;
				ObjectInputStream ois=new ObjectInputStream(QqClientConServer.s.getInputStream());
				QqMessage m=(QqMessage)ois.readObject();
				String sender=m.getSender();
				String content=m.getContent();
				bdl.putString("sender", sender);
				bdl.putString("content", content);
				msg.obj=bdl;
				handler.sendMessage(msg);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
	
	
	

}
