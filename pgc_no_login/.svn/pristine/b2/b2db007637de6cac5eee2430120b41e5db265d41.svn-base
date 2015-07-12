package cn.dezhisoft.cloud.mi.newugc.ioffer.exception;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.dezhisoft.cloud.mi.newugc.R;

/**
 * 
 * @author Rosson Chen
 *
 */
public final class ExceptionReportActivity extends Activity {
	
	public static final String exceptionMsg = "exceptionMsg";

	private TextView reportContent;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.bug_report_layout);

		reportContent = (TextView) findViewById(R.id.error_content);
		String sw = getIntent().getStringExtra(exceptionMsg);
		reportContent.setText(sw);
	}
	
	public void buttonOnclick(View v){
		if(v.getId() == R.id.bnt_send_error){
			finish() ;
		}
	}

}
