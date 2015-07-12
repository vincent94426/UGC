package cn.dezhisoft.cloud.mi.newugc.common.net.ftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.dezhisoft.cloud.mi.newugc.common.util.DebugUtil;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Metadata;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.Task;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.ws.CWUploadWebService;

import android.os.Environment;

public class FileUtil {
	private static final String XML_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?> <ScriptEntity> 	<ID>$Uid</ID> 	<Type>22</Type> 	<Title>$Title</Title> 	<SubTitle></SubTitle> 	<Keywords>$Keywords</Keywords> 	<ElementsOfNews> 		<Date>$Date</Date> 		<Location></Location> 	</ElementsOfNews> 	<Content>$Content</Content> 	<AssociateDates/> 	<RelMeterial> 		<MaterialGUID>$MaterialGUID</MaterialGUID> 		<Title>$Title</Title> 		<Length>0</Length> 		<InPoint>0</InPoint> 		<OutPoint>0</OutPoint> 		<KeyFrame>String</KeyFrame> 		<KeyFramePath></KeyFramePath> 		<Files> 			<File> 				<FileName>$FileName</FileName> 				<FileInPoint>0</FileInPoint> 				<FileOutPoint>200</FileOutPoint> 				<ClipInPoint>0</ClipInPoint> 				<ClipOutPoint>0</ClipOutPoint> 				<ClipClass>0</ClipClass> 				<QualityType>0</QualityType> 				<MediaChannel>0</MediaChannel> 			</File> 		</Files> 	</RelMeterial> 	<ExtendAttributes> 		<ExtendAttribute> 			<AttributeID>username</AttributeID> 			<AttributeName>username</AttributeName> 			<AttributeValue>$username</AttributeValue> 		</ExtendAttribute> 		<ExtendAttribute> 			<AttributeID>usercode</AttributeID> 			<AttributeName>usercode</AttributeName> 			<AttributeValue>$usercode</AttributeValue> 		</ExtendAttribute> 		<ExtendAttribute> 			<AttributeID>columncode</AttributeID> 			<AttributeName>columncode</AttributeName> 			<AttributeValue>$columncode</AttributeValue> 		</ExtendAttribute> 		<ExtendAttribute> 			<AttributeID>newspapertype</AttributeID> 			<AttributeName>报题类型</AttributeName> 			<AttributeValue>2</AttributeValue> 		</ExtendAttribute> 		<ExtendAttribute> 			<AttributeID>columnname</AttributeID> 			<AttributeName>columnname</AttributeName> 			<AttributeValue>$columnname</AttributeValue> 		</ExtendAttribute> 	</ExtendAttributes> 	<SerialNo>0</SerialNo> </ScriptEntity> ";
	private static final String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ugc";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	
	//private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	public static String writeDataXmlFile(String uuid, String title, String description, String videoFile){
		String dataXmlFilePath = ROOT_PATH + File.separator	+ uuid + ".xml";
		FileOutputStream fos;
		try {
			File dir = new File(ROOT_PATH);
			if(dir.exists() || dir.mkdirs()){
				fos = new FileOutputStream(new File(dataXmlFilePath));
			
				String xmlContent = XML_TEMPLATE.replaceAll("\\$title", title);
				xmlContent = xmlContent.replaceAll("\\$content", description);
				
				//String nowDate = sdf.format(new Date());
				//String remotePath = "/ugc/" + nowDate + "/" + uuid + "/" + new File(videoFile).getName();
				String remotePath = new File(videoFile).getName();
				
				xmlContent = xmlContent.replaceAll("\\$FileName", remotePath);
				
				fos.write(xmlContent.getBytes("UTF-8"));
				fos.close();
				
				return dataXmlFilePath;
			}
		} catch (FileNotFoundException e) {
			DebugUtil.traceThrowableLog(e);
		} catch (UnsupportedEncodingException e) {
			DebugUtil.traceThrowableLog(e);
		} catch (IOException e) {
			DebugUtil.traceThrowableLog(e);
		}
		return null;
	}
	
	public static String writeDataXmlFile(Task task){
		Metadata meta = task.getMetadata();
		String title = null == meta ? "" : meta.getTitle();
		String description = null == meta ? "" : meta.getDescription();
		
		title = null == title ? "" : title;
		description = null == description ? "" : description;
		
		String keywords = "";
		String uuid = task.getTransferUID();
		String videoFile = task.getTaskPath();
		String username = CWUploadWebService.UserProxy.getUser().getUsername() == null ? "" : CWUploadWebService.UserProxy.getUser().getUsername();
		String usercode = String.valueOf(CWUploadWebService.UserProxy.getUser().getId());
		String columnname = meta.getCatalogName() == null ? "" : meta.getCatalogName();
		String columncode = meta.getCatalogId() == null ? "" : meta.getCatalogId();
		String nowDate = sdf.format(new Date());
		
		String dataXmlFilePath = ROOT_PATH + File.separator	+ uuid + ".xml";
		FileOutputStream fos;
		try {
			String remotePath = new File(videoFile).getName();
			File dir = new File(ROOT_PATH);
			if(dir.exists() || dir.mkdirs()){
				fos = new FileOutputStream(new File(dataXmlFilePath));
			
				String xmlContent = XML_TEMPLATE.replaceAll("\\$Title", title);
				
				xmlContent = xmlContent.replaceAll("\\$Uid", uuid);
				
				xmlContent = xmlContent.replaceAll("\\$Content", description);
				
				xmlContent = xmlContent.replaceAll("\\$Keywords", keywords);
				
				xmlContent = xmlContent.replaceAll("\\$FileName", remotePath);

				xmlContent = xmlContent.replaceAll("\\$Date", nowDate);

				xmlContent = xmlContent.replaceAll("\\$MaterialGUID", uuid);

				xmlContent = xmlContent.replaceAll("\\$username", username);

				xmlContent = xmlContent.replaceAll("\\$usercode", usercode);
				
				xmlContent = xmlContent.replaceAll("\\$columncode", columncode);

				xmlContent = xmlContent.replaceAll("\\$columnname", columnname);
				
				fos.write(xmlContent.getBytes("UTF-8"));
				fos.close();
				
				return dataXmlFilePath;
			}
		} catch (FileNotFoundException e) {
			DebugUtil.traceThrowableLog(e);
		} catch (UnsupportedEncodingException e) {
			DebugUtil.traceThrowableLog(e);
		} catch (IOException e) {
			DebugUtil.traceThrowableLog(e);
		}
		return null;
	}
}
