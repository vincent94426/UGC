package cn.dezhisoft.cloud.mi.newugc.ugc.model;

import java.util.ArrayList;

/**
 * 
 * @author Rosson Chen
 *
 */
public class Comment {

	/** 总条数*/
	private int commentCount ;
	
	/** 当前返回的页面索引(从0开始)*/
	private int pageNumber ;
	
	/** 所有评论的总页面数*/
	private int pageCount ;
	
	/** 平均分*/
	private float averageRating ;
	
	/** 评论*/
	private final ArrayList<CommentType> commentTypes = new ArrayList<CommentType>();
	
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public ArrayList<CommentType> getCommentTypes() {
		return commentTypes;
	}
	
	public void addCommentType(CommentType commentType){
		commentTypes.add(commentType);
	}

	public float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(float averageRating) {
		this.averageRating = averageRating;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
}
