package cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;

import cn.dezhisoft.cloud.mi.newugc.ugc.model.CommentType;

/**
 * 
 * @author Eric
 *
 */
public class CommentListView extends BaseListView {

	CommentAdapter adapter ;
	
	public CommentListView(Context context) {
		this(context, null);
	}

	public CommentListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		adapter = new CommentAdapter(context);
		setAdapter(adapter);
	}
	
	public void loadCommentList(ArrayList<CommentType> list){
		adapter.loadCommentsList(list);
	}
	
	public void clearComments(){
		adapter.clearCommentList();
	}
	
}
