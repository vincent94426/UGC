package cn.dezhisoft.cloud.mi.newugc.ugc.model;

/**
 * 排序类型
 * 
 * @author Rosson Chen
 *
 */
public enum OrderBy {

	/** 按照热度排序*/
	HOTSPOT("hotspot"), 
	
	/** 按照点击量排序*/
	CLICKRATE("clickrate"), 
	
	/** 按照发布时间排序*/
	NEWEST("newest"),
	
	/** 按照发布时间排序*/
	ORDERBY("newest");

	/** 类型 */
	private String oderBy;

	OrderBy(String order) {
		oderBy = order;
	}

	public final String getOrderBy() {
		return oderBy;
	}
	
	private void setOrderBy(String orderBy){
		this.oderBy = orderBy ;
	}
	
	/**
	 * 创建新的排序
	 * 
	 * @param orderBy		: 排序关键字
	 * @return
	 */
	public static OrderBy createOrderBy(String orderBy){
		ORDERBY.setOrderBy(orderBy);
		return ORDERBY;
	}
}
