package cn.dezhisoft.cloud.mi.newugc.ugc.core.ws;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.ErrorMessage;

import android.util.Xml;

/**
 * Android 平台 XML抽象解析基类
 * 
 * @author Rosson Chen
 * 
 */
public abstract class CWBaseXmlParser {

	/** encoding */
	public static final String ENCODING = "UTF-8";

	/** input stream */
	private InputStream input;

	/** xml pull for android */
	protected XmlPullParser parser;

	/** xml tag name */
	private String tagName;
	
	/** error message*/
	protected ErrorMessage errorMessage ;

	/**
	 * 构造函数
	 * 
	 * @param input		: 输入流
	 */
	public CWBaseXmlParser(InputStream input) {
		try {
			this.input = input;
			parser = Xml.newPullParser();
			parser.setInput(this.input, ENCODING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开始解析soap, 解析完成后会自动关闭输入流
	 * 
	 * @return		: 如果是正常解析完成后,返回的是getParserObject(); 如果有错误消息,则返回的是ErrorMessage对象
	 * 
	 */
	public final Object parserSoapXml(){
		try {
			// 解析事件
			int event = parser.getEventType();
			// 没有错误消息
			boolean canRead = true ;
			//  开始解析
			while (canRead && event != XmlPullParser.END_DOCUMENT) {
				switch (event) {
				case XmlPullParser.START_TAG:
					// 当前标签名
					tagName = parser.getName();
					// 是否是错误消息
					if(tagName.equals("Errors")){
						errorMessage = new ErrorMessage();
					} else if (tagName.equals("Error")) {
						//errorMessage.setCode(parser.getAttributeValue(0));
					} else if(tagName.equals("Message")){
						errorMessage.setMessage(parser.nextText());
					} else if(tagName.equals("Details")){
						errorMessage.setMessage(parser.nextText());
						canRead = false ;
						break ;
					} else {
						// 解析
						starTag(tagName);
					}
					break;
				case XmlPullParser.END_TAG:
					// 当前标签名
					tagName = parser.getName();
					// 解析
					endTag(tagName);
					break;
				}
				// 下一个标签事件
				event = parser.next();
			}
			// 返回. 如果是正常解析完成,则返回解析的对象,否则返回ErrorMessage
			return canRead ? getParserObject() : errorMessage;
		} catch(Exception e){
			e.printStackTrace() ;
			return e ;
		} finally { // 释放资源
			try {
				// 关闭输入流
				if (input != null)
					input.close();
				input = null;
				parser = null;
			}catch(Exception e){
				e.printStackTrace() ;
			}
		}
	}

	protected void endTag(String tag) throws Exception {
		
	}
	
	protected abstract void starTag(String tag)throws Exception;
	
	protected abstract Object getParserObject() ;
}
