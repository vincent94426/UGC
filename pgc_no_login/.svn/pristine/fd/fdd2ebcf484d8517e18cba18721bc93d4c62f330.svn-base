package cn.dezhisoft.cloud.mi.newugc.ugc.ws;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Category;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.CommentType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.MaterialStatus;

/**
 * UGC 平台(IOFFER 5.0) 内容汇聚接口
 * 1. 查询素材列表
 * 2. 查询素材详细资源信息
 * 3. 查询素材评论
 * 4. 添加评论
 * 5. 查询素材相关素材
 * 6. 搜索素材
 * 
 * @author Rosson Chen
 *
 */
public interface IPublishProxy {

	/** 每页显示的条数 */ 
	public static final int 	PAGE_SIZE			= 10 ;
	
	/** 查询分类组 */
	public void queryPackage(String siteID,
			String session,
			String packageGuid,
			String catelogID,
			MaterialStatus status,
			int pageIndex,
			Category category,
			SoapResponse response) ;
	
	/** 查询素材列表 */
	public void queryMaterial(String siteID,
			String session,
			String orderBy,
			MediaType mediaType,
			MaterialStatus status,
			int pageIndex,
			PackageType packageType,
			SoapResponse response);
	
	/** 查询素材资源信息 */
	public void queryAsset(String siteID,
			String session,
			String packageGuid,
			String materialGuid,
			SoapResponse response);
	
	/** 查询评论 */
	public void queryComments(String session,PackageType packageType,int pageIndex,SoapResponse response);
	
	/** 添加评论 */
	public void addComment(String session,String packageGuid,CommentType comment,SoapResponse response);
	
	/** 下载统计 */
	public void addDownloadStatistics(String packageGuid,String materialGuid, SoapResponse response);
	
	/** 点击统计 */
	public void addClickStatistics(String packageGuid,String materialGuid, SoapResponse response);
	
	/** 查询相关素材 */
	public void queryRelatedPackageType(PackageType contentType,String session,int pageIndex,SoapResponse response);
	
	/** 搜索素材 */
	public void searchPackage(String siteID,String session,String key,Category search,int pageIndex,SoapResponse response) ;
}
