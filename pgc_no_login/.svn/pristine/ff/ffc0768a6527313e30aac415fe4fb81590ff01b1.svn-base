package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import java.io.InputStream;
import java.util.ArrayList;

import org.ksoap2.serialization.SoapObject;

import android.util.Log;

import com.sobey.sdk.base.HttpVisitor;
import com.sobey.sdk.base.IResponse;
import com.sobey.sdk.base.Request;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Advert;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Site;

/**
 * 站点管理实现
 * 
 * @author Rosson Chen
 *
 */
public final class SiteProxyImpl extends AbstractSoapImpl implements ISiteProxy {
	
	static final String TAG = "SiteProxyImpl" ;
	
	protected SiteProxyImpl(HttpVisitor service) {
		super(service);
	}

	@Override
	public void querySiteInfo(SoapResponse response,ArrayList<Site> list) {
		
		if(response == null ){
			Log.e(TAG, "ERROR: querySiteInfo() response is null");
			return ; 
		}
		
		if(list == null ){
			Log.e(TAG, "ERROR: querySiteInfo() fill Site list is null");
			return ; 
		}
		
		httpService.sendSoapRequest(requestSiteInfo(),response, responseSiteInfo(list));
	}

	private final Request requestSiteInfo(){
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "GetSiteInfo");
				//request.addProperty("SiteID", "");
				return sendUpSoapRequest(NAMESPACE + "/" + "GetSiteInfo",request);
			}
		};
	}
	
	private final IResponse responseSiteInfo(final ArrayList<Site> list){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input) ;
				// 解析
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {

					Site site ;
					@Override
					protected void starTag(String tag) throws Exception {
						if(tag.equals("Site")){
							site = new Site() ;
						} else if(tag.equals("SiteID")){
							site.setSiteId(parser.nextText());
						} else if(tag.equals("Name")){
							site.setSiteName(parser.nextText());
						} else if(tag.equals("IconUrl")){
							//site.setUrl(parser.nextText());
						} else if(tag.equals("Description")){
							site.setDescription(parser.nextText());
						}
					}

					@Override
					protected Object getParserObject() {
						return list ;
					}

					@Override
					protected void endTag(String tag) throws Exception {
						if(tag.equals("Site") && site != null){
							list.add(site) ;
							site = null ;
						}
					}
					
				} ;
				//
				return xmlParser.parserSoapXml() ;
			}
		};
	}
	
	@Override
	public void querySiteCategory(String session,String siteId,SoapResponse response,ArrayList<Category> list) {
		
		if(siteId == null){
			Log.e(TAG, "ERROR: site id is invailed ");
			return ; 
		}
		
		if(response == null ){
			Log.e(TAG, "ERROR: querySiteCategory() SoapResponse is null");
			return ; 
		}
		
		if(list == null ){
			Log.e(TAG, "ERROR: querySiteCategory() fill Category list is null");
			return ;
		}
		
		httpService.sendSoapRequest(requestCategory(siteId,session), response, responseCategory(list));
	}
	
	private final Request requestCategory(final String siteid,final String session){
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "GetCatalogs");
				request.addProperty("SiteID", siteid);
				//request.addProperty("SessionUID", session);// for 新媒体
				return sendUpSoapRequest(NAMESPACE + "/" + "GetCatalogs",request);
			}
		};
	}
	
	private final IResponse responseCategory(final ArrayList<Category> list){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// debug
				debugResponse(input);
				// xml解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					/** 临时列表*/
					ArrayList<Category> temp = new ArrayList<Category>() ;
					/** 临时对象 */
					Category catalog = null;

					@Override
					public void starTag(String tag) throws Exception {
						if (tag.equals("Catalog")) {
							catalog = new Category() ;
						} else if (tag.equals("CatalogID")) {
							catalog.setCatalogId(parser.nextText());
						} else if (tag.equals("ParentID")) {
							catalog.setParentId(parser.nextText());
						} else if (tag.equals("Name")) {
							catalog.setCatalogName(parser.nextText());
						} else if (tag.equals("Description")) {
							catalog.setDescription(parser.nextText());
						} 
					}

					@Override
					public void endTag(String tag) throws Exception {
						if (tag.equals("Catalog") && catalog != null) {
							temp.add(catalog);
							catalog = null;
						}
					}

					@Override
					public Object getParserObject() {
						// 分类排序
						if(temp.size() > 0)
							list.clear();
						// 根分类
						for(Category catalog : temp){
							if(catalog.getParentId().equals("0")){
								list.add(catalog);
							}
						}
						// 查询每一个父分类的子分类
						for(Category catalog : list){
							for(Category sub : temp){
								if(sub.getParentId().equals(catalog.getCatalogId())){
									catalog.addSubCatalog(sub);
								}
							}
						}
						//
						temp.clear();
						//
						return list;
					}
					
				};
				// 解析xml文件
				return xmlParser.parserSoapXml();
			}
		};
	}
	
	@Override
	public void querySiteAdverts(String siteId, int number,SoapResponse response,ArrayList<Advert> list) {
		
		if(siteId == null){
			Log.e(TAG, "ERROR: site id is invailed ");
			return ; 
		}
		
		if(number <= 0) {
			Log.e(TAG, "ERROR: querySiteAdverts() number <= 0 ");
			return ; 
		}
		
		if(list == null) {
			Log.e(TAG, "ERROR: querySiteAdverts() fill Advert list null");
			return ; 
		}
		
		if(response == null){ 
			Log.e(TAG, "ERROR: querySiteAdverts() SoapResponse is null");
			return ; 
		}
		
		httpService.sendSoapRequest(requestAdverts(siteId,number), response, responseAdverts(list));
	}

	private final Request requestAdverts(final String siteid,final int number){
		return new Request(Request.RESULT_TYPE_OBJECT) {
			
			@Override
			public InputStream getResquestInputStream() throws Exception {
				SoapObject request = new SoapObject(NAMESPACE, "GetAdverts");
				request.addProperty("SiteID", siteid);
				request.addProperty("NeedCount", number);
				return sendUpSoapRequest(NAMESPACE + "/" + "GetAdverts",request);
			}
		};
	}
	
	private final IResponse responseAdverts(final ArrayList<Advert>  list){
		return new IResponse() {
			
			@Override
			public Object wrapperObject(InputStream input) throws Exception {
				// xml解析器
				final AbstractXmlParser xmlParser = new AbstractXmlParser(input) {
					/** 临时对象 */
					Advert advert = null;
					@Override
					public void starTag(String tag) throws Exception {
						if (tag.equals("Advert")) {
							advert = new Advert();
						} else if (tag.equals("Tips")) {
							advert.setTips(parser.nextText());
						} else if (tag.equals("IconUrl")) {
							//advert.setUrl(parser.nextText());
						} else if (tag.equals("LinkToUrl")) {
							advert.setLinkUrl(parser.nextText());
						}
					}

					@Override
					public void endTag(String tag) throws Exception {
						if (tag.equals("Advert") && advert != null) {
							list.add(advert);
							advert = null;
						}
					}

					@Override
					public Object getParserObject() {
						return list ;
					}
					
				};
				// 解析xml文件
				return xmlParser.parserSoapXml();
			}
		};
	} 
}
