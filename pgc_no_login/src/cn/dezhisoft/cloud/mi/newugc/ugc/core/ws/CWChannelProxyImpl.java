package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import java.io.InputStream;

import org.ksoap2.serialization.SoapObject;

import com.sobey.sdk.base.HttpVisitor;
import com.sobey.sdk.base.IResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ChannelInfo;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.User;
import cn.dezhisoft.cloud.mi.newugc.ugc.upload.ProtocolDefine;

/**
 * 通道管理接口实现
 * 
 * @author Rosson Chen
 *
 */
public final class CWChannelProxyImpl extends CWBaseSoapImpl implements CWIChannelProxy {

	static final String TAG				= "ChannelProxyImpl" ;
	static final String FTP_HEADER		= "ftp://" ;
	private static final int HEART_TIMER	= 4 * 1000 ;
	
	/** 心跳线程*/
	private boolean mStartHeart ;
	/** 心跳线程 */
	private Thread mHeartThread ;
	
	// 申请通道
	private final RequestChannelRequest requestChannel = new RequestChannelRequest() ;
	private final ResponseChannelRequest responseChannel = new ResponseChannelRequest() ;
	// 释放通道
	private final RequestReleaseChannel requestRelease = new RequestReleaseChannel() ;
	// 通道心跳
	private final RequestHearbeat requestHearbeat = new RequestHearbeat() ;
	
	public CWChannelProxyImpl(HttpVisitor service){
		super(service);
		
		mStartHeart 	= false ;
		mHeartThread	= null ;
	}
	
	@Override
	public void requestTransferChannel(final String stream,final String tuid,final CWSoapResponse response) {
		// 用户session
		final String session = CWWebService.UserProxy.getUser().getSessionUID() ;
		// 是否登录
		if(session.equals("")){
			//
			final String uname	 = CWWebService.getUserName() ;
			final String upwd	 = CWWebService.getUserPassword() ;
			//
			CWWebService.UserProxy.login(uname,upwd,new CWSoapResponse(){
				@Override
				public final Object dispatchResponse(Object value) {
					// 登录成功
					if(value instanceof User){
						sendRequestChannel(CWWebService.UserProxy.getUser().getSessionUID(),stream,tuid,response);
					} else {
						response.dispatchResponse(value);
					}
					return null;
				}
			});
		} else {
			// 请求通道
			sendRequestChannel(session,stream,tuid,response);
		}
	}
	
	private final void sendRequestChannel(String session ,String stream, String tuid,CWSoapResponse response){
		
		final RequestChannelRequest req  = requestChannel.cloneRequest() ;
		final ResponseChannelRequest res = responseChannel.cloneResponse() ;
		
		if(req == null || res == null || session == null) return ;
		
		final HttpVisitor service = httpService ;
		
		// 设置请求参数
		req.setParameter(session, tuid, ProtocolDefine.TransportMode.FTP, stream);
		// 请求
		service.sendSoapRequest(req,response,res) ;
	}

	@Override
	public final void startTransferHeartbeat(final String tuid) {
		// 运行
		mStartHeart 	= true ;
		// 执行心跳
		mHeartThread 	= new Thread(new Runnable() {
			
			@Override
			public void run() {
				long time = System.currentTimeMillis() ;
				final HttpVisitor service 	= httpService ;
				final RequestHearbeat req 	= requestHearbeat.cloneRequest() ;
				final CWSoapResponse response = new CWSoapResponse() ;
				while(mStartHeart){
					// send request
					if(System.currentTimeMillis() - time > HEART_TIMER){
						req.setParameter(tuid) ;
						service.sendSoapRequest(req, response, null);
						time	= System.currentTimeMillis() ;
					}
					// sleep
					try{
						Thread.sleep(20);
					}catch(Exception e){
						e.printStackTrace() ;
					}
				}
			}
		});
		// 心跳线程
		mHeartThread.setName("#Heart Thread");
		mHeartThread.setDaemon(true);
		mHeartThread.start() ;
	}

	@Override
	public void releaseTransferChannel(String tuid) {
		
		// 是否有效通道
		if(tuid == null || tuid.equals("")) return ;
		
		// 心跳线程结束
		if(!mStartHeart) return ;
		
		// 退出心跳
		mStartHeart = false ;
		try{
			if(mHeartThread != null)
				mHeartThread.join() ;
			mHeartThread = null ;
		} catch(Exception e){
			e.printStackTrace() ;
		}
		
		final RequestReleaseChannel req = requestRelease.cloneRequest() ;
		
		if(req == null) return ;
		
		// 设置请求参数
		req.setParameter(tuid);
		
		// 释放文件传输通道
		httpService.sendSoapRequest(req, new CWSoapResponse(), responseReleaseChannel);
	}

	// 心跳请求
	private final class RequestHearbeat extends CWBaseSoapRequet {

		String tuid;

		protected void setParameter(String tuid) {
			this.tuid = tuid;
		}

		@Override
		public final InputStream getResquestInputStream() throws Exception {

			SoapObject request = new SoapObject(NAMESPACE, "ChannelHeartbeat");
			request.addProperty("TransferUID", tuid);
			//
			return sendUploadSoapRequest(NAMESPACE + "/" + "ChannelHeartbeat",request);
		}

		@Override
		public RequestHearbeat cloneRequest() {
			try {
				return (RequestHearbeat) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
	};

	private final IResponse responseReleaseChannel = new IResponse() {
		
		@Override
		public Object wrapperObject(InputStream arg0) throws Exception {
			return null;
		}
	};
	
	// 释放通道
	private final class RequestReleaseChannel extends CWBaseSoapRequet {

		String tuid;

		protected void setParameter(String tuid) {
			this.tuid = tuid;
		}

		@Override
		public final InputStream getResquestInputStream() throws Exception {
			
			SoapObject request = new SoapObject(NAMESPACE, "ReleaseChannel");
			request.addProperty("TransferUID", tuid);
			
			return sendUploadSoapRequest(NAMESPACE + "/" + "ReleaseChannel",request);
		}

		@Override
		public RequestReleaseChannel cloneRequest() {
			try {
				return (RequestReleaseChannel) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
	};
	
	// 通道请求
	private final class RequestChannelRequest extends CWBaseSoapRequet {

		String sessionId;
		String tuid;
		String mode;
		String streamType;

		protected void setParameter(String sid, String tuid, String mode,String stream) {
			this.sessionId = sid;
			this.tuid = tuid;
			this.mode = mode;
			this.streamType = stream;
		}

		@Override
		public final InputStream getResquestInputStream() throws Exception {

			SoapObject request = new SoapObject(NAMESPACE, "RequestChannel");

			request.addProperty("SessionUID", sessionId);
			request.addProperty("TransferUID", tuid);
			request.addProperty("TransferMode", mode);
			request.addProperty("StreamType", streamType);
			return sendUploadSoapRequest(NAMESPACE + "/" + "RequestChannel",request);
		}

		@Override
		public RequestChannelRequest cloneRequest() {
			try {
				return (RequestChannelRequest) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
	};
	
	// 通道响应
	private final class ResponseChannelRequest extends CWBaseSoapResponse {

		@Override
		public final ResponseChannelRequest cloneResponse() {
			try {
				return (ResponseChannelRequest) super.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}

		@Override
		public final Object wrapperObject(InputStream input) throws Exception {
			// debug
			debugResponse(input);
			// xml 解析器
			final CWBaseXmlParser xmlParser = new CWBaseXmlParser(input) {
				
				/** 通道信息 */
				ChannelInfo channel = new ChannelInfo();

				@Override
				public void starTag(String tag) throws Exception {
					if (tag.equals("Status")) {
						channel.setStatus(parser.nextText());
					} else if (tag.equals("TransferUID")) {
						channel.setTransferUID(parser.nextText());
					} else if (tag.equals("ConnectionString")) {
						String conn = parser.nextText();
						// ftp://test:test@172.16.130.6:21
						int index = conn.indexOf(FTP_HEADER);
						if (index >= 0) {
							channel.setMode(ChannelInfo.Mode.FTP);
							String s = conn.substring(FTP_HEADER.length());
							String[] str = s.split("@");
							String[] users = str[0].split(":");
							String[] server = str[1].split(":");
							channel.getFtp().setFtpUname(users[0]);
							channel.getFtp().setFtpUpwd(users[1]);
							channel.getFtp().setFtpServer(server[0]);
							channel.getFtp().setFtpPort(Integer.parseInt(server[1]));
						} else {
							channel.setMode(ChannelInfo.Mode.TCP);
							String[] str = conn.split(":");
							channel.getTcp().setHost(str[0]);
							channel.getTcp().setPort(Integer.parseInt(str[1]));
						}
					} else if (tag.equals("RejectReason")) {
						channel.setRejectMessage(parser.nextText());
					}
				}

				@Override
				public Object getParserObject() {
					return channel;
				}
			};
			// 解析xml文件
			return xmlParser.parserSoapXml();
		}
	};
}
