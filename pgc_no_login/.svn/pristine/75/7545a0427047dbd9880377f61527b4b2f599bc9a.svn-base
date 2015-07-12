package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import cn.dezhisoft.cloud.mi.newugc.R;

import cn.dezhisoft.cloud.mi.newugc.ioffer.service.IofferDefine;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.CommentType;
import cn.dezhisoft.cloud.mi.newugc.ugc.model.PackageType;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse;
import cn.dezhisoft.cloud.mi.newugc.ugc.ws.SoapResponse.SoapRequestMessage;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class ContentTypeCommentAddActivity extends BaseActivity {
	
	RatingBar rating;
	EditText editTitle, editComment;
	PackageType packageType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ioffer_program_comment_edit_layout);

		packageType = mIofferService.getCurrentPackageType();

		rating = (RatingBar) findViewById(R.id.ioffer_comment_edit_rating);
		editTitle = (EditText) findViewById(R.id.ioffer_comment_edit_edit_title);
		editComment = (EditText) findViewById(R.id.ioffer_comment_edit_edit_comment);
	}

	public void topBarOnClick(View view) {
		switch (view.getId()) {
		case R.id.ioffer_comment_edit_cancel:
			getActivityManager().backPrevious();
			break;
		case R.id.ioffer_comment_edit_send:
			float f = rating.getRating();
			final int ratingFloat = (int) f;
			final String editTitleStr = editTitle.getText().toString();
			final String editCommentStr = editComment.getText().toString();
			final String userName = mIofferService.getUser().getUsername();
			sendComment(ratingFloat, editTitleStr, editCommentStr, userName);
			break;
		}
	}

	private void sendComment(int rating, String title, String commentMsg, String user) {
		
		packageType = mIofferService.getCurrentPackageType();
		
		CommentType comment = new CommentType();
		comment.setCommentContent(commentMsg);
		comment.setCommentRating(rating);
		comment.setCommentTitle(title);
		comment.setCommentUser(user);

		if (title.equals("")) {
			showMessageDialog(getString(R.string.label_comment_edit_warring_title));
		} else if (commentMsg.equals("")) {
			showMessageDialog(getString(R.string.label_comment_edit_warring_comment));
		} else {
			mIofferService.addCommentType(packageType.getPackageGuid(), comment,new SoapResponse(mHandler));
		}

	}

	@Override
	protected Handler getMessageHandler() {
		return new Handler() {

			@Override
			public void handleMessage(Message msg) {

				switch (msg.what) {
				case SoapRequestMessage.MSG_START_SOAP:
					showDialog(DIALOG_ID_REQUEST);
					break;
				case SoapRequestMessage.MSG_END_SOAP:
					dismissDialog(DIALOG_ID_REQUEST);
					break;
				case IofferDefine.MSG_ADD_CONTENT_COMMENT_SUCCESS:
					Toast.makeText(ContentTypeCommentAddActivity.this, R.string.label_comment_edit_success, Toast.LENGTH_SHORT).show();
					getActivityManager().backPrevious();
					break;
				case IofferDefine.MSG_ADD_CONTENT_COMMENT_FAILED:
					showMessageDialog(getString(R.string.label_comment_edit_failed));
					break;
				}
			}

		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false ;
	}
}
