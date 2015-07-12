package cn.dezhisoft.cloud.mi.newugc.ugc.ws.publish;

import java.io.InputStream;
import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;

import com.sobey.sdk.base.HttpVisitor;
import com.sobey.sdk.base.IResponse;
import com.sobey.sdk.base.Request;
import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.CommentType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Material;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.MaterialResouce;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.MaterialStatus;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.AbstractSoapImpl;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.AbstractXmlParser;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.IPublishProxy;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 发布管理实现
 * 
 * @author Rosson Chen
 *
 */
public final class PublishProxyImpl extends AbstractSoapImpl implements IPublishProxy {
	
	/** for debug*/
	static final String TAG	= "PublishProxyImpl" ;
	static final String FTP = "ftp://" ;
	
	protected PublishProxyImpl(HttpVisitor service) {
		super(service);
	}

	@Override
	public void queryPackage(String siteID,
			String session,
			String packageGuid,
			String catelogID,
			MaterialStatus status,
			int pageIndex,
			Category category,
			SoapResponse response) {
		
		Request resquest 	= getQueryPackageRequest(session, siteID, packageGuid, catelogID, status, pageIndex);
		IResponse res 		= getQueryPackageResponse(category);
		
		httpService.sendSoapRequest(resquest, response, res);
	}

	private Request getQueryPackageRequest(final String session,
			final String siteID,
			final String packageGuid,
			final String catalogId ,
			final MaterialStatus status,
			final int pageIndex){
		
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				SoapObject request = new SoapObject(NAMESPACE, "GetPackage");
				request.addProperty("SiteID", siteID);
				request.addProperty("SessionUID", session);
				
				// 分类id
				if(catalogId != null && !catalogId.equals(""))
					request.addProperty("CatalogID", catalogId);
				//
				request.addProperty("PackageGuid", packageGuid);
				request.addProperty("CountPerPage", PAGE_SIZE);
				request.addProperty("PageNumber", pageIndex);
				
				//
				SoapObject resultMode = new SoapObject("", "");
				resultMode.addProperty("WantBaseInfo", 1);
				resultMode.addProperty("WantMetadata", 1);
				request.addProperty("ResultMode", resultMode);
				
				return sendContentSoapRequest(NAMESPACE + "/" + "GetPackage",request);
			}
		};
	}
	
	private IResponse getQueryPackageResponse(final Category category){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				//
				if(category == null) return null ;
				// 节目列表
				final ArrayList<PackageType> list = category.getPackageTypes().getPackageTypeList() ;
				// xml 解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					
					/** 界面信息*/
					PackageType item = null ;
					
					@Override
					public void starTag(String tagName) throws Exception {
						if(tagName.equals("PageNumber")){
							category.getPackageTypes().setPageIndex(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("PageCount")){
							category.getPackageTypes().setPageCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("Package")){
							item = new PackageType() ;
						} else if(tagName.equals("PackageGuid")){
							item.setPackageGuid(parser.nextText());
						} else if(tagName.equals("CatalogId")){
							item.setCatalogId(parser.nextText());
						} else if(tagName.equals("Title")){
							item.setTitle(parser.nextText());
						} else if(tagName.equals("CreateTime")){
							item.setCreateTime(parser.nextText());
						} else if(tagName.equals("DeviceType")){
							item.setDeviceType(parser.nextText());
						} else if(tagName.equals("Status")){
							item.setStatus(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("CommentCount")){
							item.setCommentCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("CommentRating")){
							item.setCommentRating(Float.valueOf(parser.nextText()));
						} else if(tagName.equals("ClickCount")){
							item.setClickCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("DownloadCount")){
							item.setDownloadCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("IconUrl")){
							item.setIconUrl(parser.nextText());
						} else if(tagName.equals("description")){
							item.setDescribe(parser.nextText());
						}
					}
					
					@Override
					public Object getParserObject() {
						return category;
					}
					
					@Override
					public void endTag(String tagName) throws Exception {
						if(tagName.equals("Package") && item != null){
							list.add(item);
							item = null ;
						}
					}
				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	@Override
	public void queryMaterial(String siteID, 
			String session, 
			String orderBy,
			MediaType mediaType, 
			MaterialStatus status, 
			int pageIndex,
			PackageType packageType, 
			SoapResponse response) {
		
		final Request req 	= getQueryMaterialRequest(session, siteID, packageType.getPackageGuid(), status, pageIndex);
		final IResponse res = getQueryMaterialResponse(packageType);
		
		httpService.sendSoapRequest(req, response, res);
	}
	
	private Request getQueryMaterialRequest(final String session,
			final String siteID,
			final String packageGuid,
			final MaterialStatus status,
			final int pageIndex){
		
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				SoapObject request = new SoapObject(NAMESPACE, "GetMaterial");
				request.addProperty("SiteID", siteID);
				request.addProperty("SessionUID", session);
				
				if(packageGuid != null && !packageGuid.isEmpty()){
					request.addProperty("PackageGuid", packageGuid);
				}
				
				//
				request.addProperty("CountPerPage", PAGE_SIZE);
				request.addProperty("PageNumber", pageIndex);
				
				//
				SoapObject resultMode = new SoapObject("", "");
				resultMode.addProperty("WantBaseInfo", 1);
				resultMode.addProperty("WantAsset", "default");
				request.addProperty("ResultMode", resultMode);
				
				return sendContentSoapRequest(NAMESPACE + "/" + "GetMaterial",request);
			}
		};
	}
	
	private IResponse getQueryMaterialResponse(final PackageType packageType){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				//
				final ArrayList<Material> list = packageType.getMaterials() ;
				// xml 解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					
					Material item = null ;
					MaterialResouce resource = null ;
					
					@Override
					public void starTag(String tagName) throws Exception {
						if(tagName.equals("Material")){
							item = new Material() ;
						} else if(tagName.equals("PackageGuid")){
							item.setPackageGuid(parser.nextText());
						} else if(tagName.equals("MaterialGuid")){
							item.setMaterialGuid(parser.nextText());
						} else if(tagName.equals("Title")){
							item.setTitle(parser.nextText());
						} else if(tagName.equals("MediaType")){
							item.setMediaType(parser.nextText());
						} /*else if(tagName.equals("Duration")){
							item.setDuration(parser.nextText());
						} */else if(tagName.equals("CreateTime")){
							item.setCreateTime(parser.nextText());
						} else if(tagName.equals("Username")){
							item.setUserName(parser.nextText());
						} else if(tagName.equals("DeviceType")){
							item.setDeviceType(parser.nextText());
						} else if(tagName.equals("Status")){
							item.setStatus(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("ClickCount")){
							item.setClickCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("DownloadCount")){
							item.setDownloadCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("IconUrl")){
							item.setIconUrl(parser.nextText());
						} 
						// asset
						else if(tagName.equals("Asset")){
							resource = new MaterialResouce() ;
						} else if(tagName.equals("AssetType")){
							resource.setAssetType(parser.nextText());
						} else if(tagName.equals("FileUrl")){
							resource.setAssetFileUrl(parser.nextText());
						} else if(tagName.equals("FileSize")){
							resource.setAssetFileSize(parser.nextText());
						} else if(tagName.equals("StreamType")){
							resource.setAssetStreamType(parser.nextText());
						} else if(tagName.equals("FormatDesc")){
							resource.setAssetFormatDesc(parser.nextText());
						} else if(tagName.equals("Duration") && resource != null){
							resource.setAssetDuration(parser.nextText());
						} else if(tagName.equals("MediaTrack")){
							resource.setAssetMediaTrack(parser.nextText());
						}
					}
					
					@Override
					public Object getParserObject() {
						return packageType ;
					}
					
					@Override
					public void endTag(String tagName) throws Exception {
						if(tagName.equals("Material") && item != null){
							list.add(item);
							item = null ;
						} else if(tagName.equals("Asset") && item != null && resource != null){
							item.getResouces().add(resource);
							resource = null ;
						}
					}
				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}

	@Override
	public void queryAsset(String siteID, 
			String session, 
			String packageGuid,
			String materialGuid, 
			SoapResponse response) {
		
	}
	
	@Override
	public void queryComments(String session, PackageType packageType, int pageIndex,SoapResponse response) {
		
		Request req = getQueryCommentRequest(session, packageType.getPackageGuid(), pageIndex);
		IResponse res = getQueryCommentResponse(packageType);
		
		httpService.sendSoapRequest(req, response, res);
	}
	
	private Request getQueryCommentRequest(final String session,
			final String packageGuid,
			final int pageIndex){
		
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				SoapObject request = new SoapObject(NAMESPACE, "GetComment");
				request.addProperty("SessionUID", session);
				request.addProperty("PackageGuid", packageGuid);
				request.addProperty("CountPerPage", PAGE_SIZE);
				request.addProperty("PageNumber", pageIndex);
				//
				return sendContentSoapRequest(NAMESPACE + "/" + "GetComment",request);
			}
		};
	}

	private IResponse getQueryCommentResponse(final PackageType packageType){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				//
				final ArrayList<CommentType> list = packageType.getComment().getCommentTypes() ;
				// xml 解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					
					CommentType item = null ;
					
					@Override
					public void starTag(String tagName) throws Exception {
						if(tagName.equals("TotalCount")){
							
						} else if(tagName.equals("Rating")){
							packageType.getComment().setAverageRating(Float.valueOf(parser.next()));
						} else if(tagName.equals("PageNumber")){
							packageType.getComment().setPageNumber(Integer.valueOf(parser.next()));
						} else if(tagName.equals("PageCount")){
							packageType.getComment().setPageCount(Integer.valueOf(parser.next()));
						} else if(tagName.equals("Comment")){
							item = new CommentType() ;
						} else if(tagName.equals("User")){
							item.setCommentUser(parser.nextText());
						} else if(tagName.equals("Date")){
							item.setCommentDate(parser.nextText());
						} else if(tagName.equals("Rating")){
							item.setCommentRating(Integer.valueOf(parser.next()));
						} else if(tagName.equals("Title")){
							item.setCommentTitle(parser.nextText());
						} else if(tagName.equals("Content")){
							item.setCommentContent(parser.nextText());
						}
					}
					
					@Override
					public Object getParserObject() {
						return packageType ;
					}
					
					@Override
					public void endTag(String tagName) throws Exception {
						if(tagName.equals("Comment") && item != null){
							list.add(item);
							item = null ;
						}
					}
				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	@Override
	public void addComment(String session,String packageGuid, CommentType comment,SoapResponse response) {
		
		Request req = getAddCommentRequest(session,packageGuid,comment);
		IResponse res = getAddCommentResponse();
		
		httpService.sendSoapRequest(req, response, res);
	}
	
	private Request getAddCommentRequest(final String session,
			final String packageGuid,
			final CommentType comment){
		
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				SoapObject request = new SoapObject(NAMESPACE, "AddComment");
				request.addProperty("SessionUID", session);
				request.addProperty("PackageGuid", packageGuid);
				request.addProperty("Rating", comment.getCommentRating());
				request.addProperty("Title", comment.getCommentTitle());
				request.addProperty("Content", comment.getCommentContent());
				//
				return sendContentSoapRequest(NAMESPACE + "/" + "AddComment",request);
			}
		};
	}
	
	private IResponse getAddCommentResponse(){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				//
				// xml 解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					
					@Override
					public void starTag(String tagName) throws Exception {
						
					}
					
					@Override
					public Object getParserObject() {
						return true ;
					}
					
					@Override
					public void endTag(String tagName) throws Exception {
						
					}
				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	@Override
	public void addDownloadStatistics(String packageGuid, String materialGuid,
			SoapResponse response) {
		
		Request req = getStatisticsRequest(packageGuid,materialGuid,0,1);
		IResponse res = getStatisticsResponse();
		
		httpService.sendSoapRequest(req, response, res);
	}

	@Override
	public void addClickStatistics(String packageGuid, String materialGuid,
			SoapResponse response) {
		
		Request req = getStatisticsRequest(packageGuid,materialGuid,1,0);
		IResponse res = getStatisticsResponse();
		
		httpService.sendSoapRequest(req, response, res);
	}
	
	private Request getStatisticsRequest(final String packageGuid,
			final String materialGuid,
			final int click,
			final int download){
		
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				SoapObject request = new SoapObject(NAMESPACE, "AddHits");
				request.addProperty("PackageGuid", packageGuid);
				request.addProperty("MaterialGuid", materialGuid);
				request.addProperty("AddClick", click);
				request.addProperty("AddDownload", download);
				//
				return sendContentSoapRequest(NAMESPACE + "/" + "AddHits",request);
			}
		};
	}
	
	private IResponse getStatisticsResponse(){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				//
				// xml 解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					
					@Override
					public void starTag(String tagName) throws Exception {
						
					}
					
					@Override
					public Object getParserObject() {
						return true ;
					}
					
					@Override
					public void endTag(String tagName) throws Exception {
						
					}
				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	
	@Override
	public void searchPackage(String siteID, String session, String key,
			Category search, int pageIndex, SoapResponse response) {
		
		Request req = getSearchRequest(siteID,session,pageIndex,key) ;
		IResponse res = getQueryPackageResponse(search);
		
		httpService.sendSoapRequest(req, response, res);
	}

	private final Request getSearchRequest(final String siteId,
			final String session ,
			final int pageIndex,
			final String key){
		// 返回请求对象
		return new Request(Request.RESULT_TYPE_OBJECT) {
			@Override
			public InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "SearchPackage");
				request.addProperty("SiteID", siteId);
				request.addProperty("Keyword", key);
				request.addProperty("SessionUID", session);
				request.addProperty("CountPerPage", PAGE_SIZE);
				request.addProperty("PageNumber", pageIndex);
				
				SoapObject resultMode = new SoapObject("", "");
				resultMode.addProperty("WantBaseInfo", 1);
				resultMode.addProperty("WantMetadata", 1);
				request.addProperty("ResultMode", resultMode);
				
				return sendContentSoapRequest(NAMESPACE + "/" + "SearchPackage",request);
			}
		};
	}
	
	@Override
	public void queryRelatedPackageType(PackageType contentType,String session, int pageIndex,SoapResponse response) {
		
		Request req = getRelatedPackageRequest(session,contentType.getPackageGuid(),pageIndex);
		IResponse res = getRelatedPackageResponse(contentType);
		
		httpService.sendSoapRequest(req, response, res);
	}
	
	private final Request getRelatedPackageRequest(final String session,
			final String packageGuid, 
			final int pageIndex){
		// 返回请求对象
		return new Request(Request.RESULT_TYPE_OBJECT) {
			@Override
			public InputStream getResquestInputStream() throws Exception {
				
				SoapObject request = new SoapObject(NAMESPACE, "GetRelated");
				request.addProperty("SessionUID", session);
				request.addProperty("PackageGuid", packageGuid);
				request.addProperty("CountPerPage", PAGE_SIZE);
				request.addProperty("PageNumber", pageIndex);
				
				SoapObject resultMode = new SoapObject("", "");
				resultMode.addProperty("WantBaseInfo", 1);
				resultMode.addProperty("WantMetadata", 1);
				request.addProperty("ResultMode", resultMode);
				
				return sendContentSoapRequest(NAMESPACE + "/" + "GetRelated",request);
			}
		};
	}
	
	private final IResponse getRelatedPackageResponse(final PackageType contentType){
		return new IResponse() {
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// for debug
				debugResponse(input);
				//
				final ArrayList<PackageType> list = contentType.getRelatedPackage().getPackageTypeList() ; 
				// xml 解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					
					PackageType item = null ;
					
					@Override
					public void starTag(String tagName) throws Exception {
						if(tagName.equals("PageNumber")){
							contentType.getRelatedPackage().setPageIndex(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("PageCount")){
							contentType.getRelatedPackage().setPageCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("Package")){
							item = new PackageType() ;
						} else if(tagName.equals("PackageGuid")){
							item.setPackageGuid(parser.nextText());
						} else if(tagName.equals("CatalogId")){
							item.setCatalogId(parser.nextText());
						} else if(tagName.equals("Title")){
							item.setTitle(parser.nextText());
						} else if(tagName.equals("CreateTime")){
							item.setCreateTime(parser.nextText());
						} else if(tagName.equals("DeviceType")){
							item.setDeviceType(parser.nextText());
						} else if(tagName.equals("Status")){
							item.setStatus(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("CommentCount")){
							item.setCommentCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("CommentRating")){
							item.setCommentRating(Float.valueOf(parser.nextText()));
						} else if(tagName.equals("ClickCount")){
							item.setClickCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("DownloadCount")){
							item.setDownloadCount(Integer.valueOf(parser.nextText()));
						} else if(tagName.equals("IconUrl")){
							item.setIconUrl(parser.nextText());
						} else if(tagName.equals("description")){
							item.setDescribe(parser.nextText());
						}
					}
					
					@Override
					public Object getParserObject() {
						return contentType ;
					}
					
					@Override
					public void endTag(String tagName) throws Exception {
						if(tagName.equals("Package") && item != null){
							list.add(item);
							item = null ;
						}
					}
				};
				// 解析xml
				return xmlParser.parserSoapXml() ;
			}
		};
	}
}
