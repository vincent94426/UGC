package cn.dezhisoft.cloud.mi.newugc.ugv2.view.metro.fragment;

import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.dezhisoft.cloud.mi.newugc.R;

public class SampleFragment extends BaseFragment {

	@Override
	protected int getTitleResId() {
		return R.string.module_text_contact;
	}

	@Override
	protected void onCreateViewBody(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
	}

	@Override
	protected void refreshCloudData(Map<String, String> statisticsData) {
	}

	@Override
	protected CharSequence getProgressDialogTitile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getNumberResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void startTask(String realm, String lpsust) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStartBackup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onStartRestore() {
		// TODO Auto-generated method stub
		
	}
}
