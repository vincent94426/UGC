package cn.dezhisoft.cloud.mi.newugc.ugv2.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

/**
 * 客户端
 * 
 * @author way
 * 
 */
public class Client {

	private Socket socket;
	private ClientThread clientThread;
	private String ip;
	private int port;

	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	public boolean start() {
		try {
			socket = new Socket();
			Log.i("GOD",socket.toString());
			socket.connect(new InetSocketAddress(ip, port), 3000);
			Log.i("GOD",socket.toString());
			if (socket.isConnected()) {
				clientThread = new ClientThread(socket);
				clientThread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 直接通过client得到读线程
	public ClientInputThread getClientInputThread() {
		return clientThread.getIn();
	}

	// 直接通过client得到写线程
	public ClientOutputThread getClientOutputThread() {
		return clientThread.getOut();
	}

	// 直接通过client停止读写消息
	public void setIsStart(boolean isStart) {
		clientThread.getIn().setStart(isStart);
		clientThread.getOut().setStart(isStart);
	}
	
	public class ClientThread extends Thread {

		private ClientInputThread in;
		private ClientOutputThread out;

		public ClientThread(Socket socket) {
			in = new ClientInputThread(socket);
			out = new ClientOutputThread(socket);
		}

		public void run() {
			in.setStart(true);
			out.setStart(true);
			in.start();
			out.start();
		}

		// 得到读消息线程
		public ClientInputThread getIn() {
			return in;
		}

		// 得到写消息线程
		public ClientOutputThread getOut() {
			return out;
		}
	}
}
