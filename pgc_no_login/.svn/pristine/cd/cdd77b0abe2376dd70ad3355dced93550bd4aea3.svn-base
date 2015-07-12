package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.common.Config;
import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ioffer.ui.view.CommentListView;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.Comment;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;

/**
 * 评论列表
 * 
 * @author Eric
 * 
 */
public class ContentTypeCommentListActivity extends BaseActivity implements OnScrollListener {
	
	TextView programName;
	TextView commentAmout;
	RatingBar programRating;
	TextView commentDate;
	CommentListView commentList;
	Comment comment;

	Button nextPage;

	PackageType packageType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_program_comment_layout);
		packageType = mIofferService.getCurrentPackageType();
		// 查询评论
		reLoad();
		//
		initView();
	}
	
	private void reLoad(){
		packageType.getComment().getCommentTypes().clear() ;
		mIofferService.queryComments(packageType, Config.FIRST_PAGE,new SoapResponse(mHandler));
	}

	private void initView() {
		
		programName = (TextView) findViewById(R.id.ioffer_program_comment_title);
		commentAmout = (TextView) findViewById(R.id.program_comment_amount);
		commentList = (CommentListView) findViewById(R.id.program_comment_list);
		programRating = (RatingBar) findViewById(R.id.program_comment_rating);
		nextPage = (Button) findViewById(R.id.program_comment_nextpage);

		comment = packageType.getComment();

		programName.setText(packageType.getTitle() + "(" + packageType.getCommentCount() + getString(R.string.label_content_cmamount) + ")");
		commentAmout.setText(packageType.getCommentCount() + getString(R.string.label_comment_amount_add));
		programRating.setRating(comment.getAverageRating());

		commentList.loadCommentList(packageType.getComment().getCommentTypes());

		commentList.setOnScrollListener(this);

		nextPage.setVisibility(View.GONE);
	}

	public void topBarOnClick(View view) {
		switch (view.getId()) {
		case R.id.ioffer_program_comment_about:
			getActivityManager().backPrevious();
			break;

		case R.id.program_comment_reload:
			commentList.clearComments();
			
			reLoad();
			
			break;
		}
	}

	public void buttonOnClick(View view) {
		switch (view.getId()) {
		case R.id.program_comment_edit:
			getActivityManager().pushActivity(ContentTypeCommentAddActivity.class);
			break;
		case R.id.program_comment_nextpage:
			
			if (comment.getPageNumber() < comment.getPageCount()){
				mIofferService.queryComments(packageType, comment.getPageNumber() + 1,new SoapResponse(mHandler));
			}
			
			nextPage.setVisibility(View.GONE);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false ;
	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case IofferDefine.MSG_QUERY_CONTENT_COMMENT_SUCCESS:
					// 加载评论
					commentList.loadCommentList(packageType.getComment().getCommentTypes());
					// 加载平均分
					programRating.setRating(packageType.getComment().getAverageRating());
					break;
				case IofferDefine.MSG_QUERY_CONTENT_COMMENT_FAILED:
					showMessageDialog(getString(R.string.label_comment_query_failed));
					break;
				}
			}
		};
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		if (visibleItemCount == totalItemCount) {
			return;
		}
		if ((firstVisibleItem + visibleItemCount) == totalItemCount && comment.getPageNumber() + 1 <= comment.getPageCount()) {
			nextPage.setVisibility(View.VISIBLE);
		} else {
			nextPage.setVisibility(View.GONE);
		}
	}

	int scrollState;

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
	}
}
