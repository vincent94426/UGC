package cn.dezhisoft.cloud.mi.newugc.ugc.model;

import java.io.Serializable;
import java.util.ArrayList;

import cn.dezhisoft.cloud.mi.newugc.ugc.core.model.MediaType;

/**
 * 站点分类信息.如: 新闻,体育,科技,热门,娱乐等
 * 
 * @author Rosson Chen
 *
 */
public class Category implements Serializable {

	/** 分类ID*/
	private String catalogId ;
	
	/** 父分类ID*/
	private String parentId ;
	
	/** 分类名: 比如热门,排行,搜索 */
	private String catalogName ;
	
	/** 分类描述信息*/
	private String description ;
	
	/** 排序类型*/
	private OrderBy	orderBy ;
	
	/** 数据类型: 视频|音频|图片*/
	private MediaType mediaType ;
	
	/** 类型关键字 */
	private String keyWord ;
	
	/** 子分类*/
	private final ArrayList<Category>  subCategortList	= new ArrayList<Category>();
	
	private final PackagePage packageTypes = new PackagePage() ;
	
	public Category(){
		this("");
	}
	
	public Category(String name){
		this(name,OrderBy.NEWEST);
	}
	
	public Category(String name,OrderBy order){
		this(name,order,MediaType.ALL);
	}
	
	public Category(String name,OrderBy order,MediaType type){
		subCategortList.clear();
		setCatalogName(name);
		setOrderBy(order);
		setMediaType(type);
		setKeyWord("");
	}
	
	public OrderBy getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(OrderBy orderBy) {
		this.orderBy = orderBy;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public ArrayList<Category> getSubCategoryList() {
		return subCategortList;
	}
	
	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getCatalogId() {
		return catalogId != null ? catalogId : "";
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCatalogName() {
		return catalogName != null ? catalogName : "unkown";
	}

	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public PackagePage getPackageTypes() {
		return packageTypes;
	}
	
	public void addSubCatalog(Category Catalog){
		subCategortList.add(Catalog);
	}
	
	public final void clearCatalogList(){
		subCategortList.clear();
	}
	
	/** 清空数据 */
	public void clear(){
		subCategortList.clear() ;
		keyWord = "" ;
		packageTypes.clear() ;
	}
}
