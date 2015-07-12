package cn.dezhisoft.cloud.mi.newugc.ugv2.activity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.dezhisoft.cloud.mi.newugc.R;
import cn.dezhisoft.cloud.mi.newugc.ugv2.common.QqMessage;
import cn.dezhisoft.cloud.mi.newugc.ugv2.common.MessageType;
import cn.dezhisoft.cloud.mi.newugc.ugv2.model.QqClientConServer;
import cn.dezhisoft.cloud.mi.newugc.ugv2.tools.ManageChat;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MyFriendList extends Activity{

	String loginId; //用于接受来自Login界面的用户Id
	TextView friend1;
	TextView friend2;
	TextView friend3;
	TextView friend4;
	TextView friend5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ugv2_friendlist);
		Intent intent=getIntent();
		loginId=intent.getStringExtra("id");
		
		friend1=(TextView)findViewById(R.id.list_friend1);
		friend2=(TextView)findViewById(R.id.list_friend2);
		friend3=(TextView)findViewById(R.id.list_friend3);
		friend4=(TextView)findViewById(R.id.list_friend4);
		friend5=(TextView)findViewById(R.id.list_friend5);

		
		friend1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent2=new Intent();
				String friendIdTemp=friend1.getText().toString();
				String friendId=friendIdTemp.substring(friendIdTemp.lastIndexOf(':')+1);
				Log.i("friendId", friendId);
				
				Chat qqChat=new Chat();
				Thread t=new Thread(qqChat);
				t.start();
				//ManageChat.addChat(loginId+" "+friendId, qqChat);
				intent2.putExtra("id", loginId);
				intent2.putExtra("friendId", friendId);
				intent2.setClass(MyFriendList.this, Chat.class);
				startActivity(intent2);
			}
			
		});
		
		friend2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent2=new Intent();
				String friendIdTemp=friend2.getText().toString();
				String friendId=friendIdTemp.substring(friendIdTemp.lastIndexOf(':')+1);
				Log.i("friendId", friendId);
				
				Chat qqChat=new Chat();
				ManageChat.addChat(loginId+" "+friendId, qqChat);
				intent2.putExtra("id", loginId);
				intent2.putExtra("friendId", friendId);
				intent2.setClass(MyFriendList.this, Chat.class);
				startActivity(intent2);
			}
			
		});
		
		
	}

}
