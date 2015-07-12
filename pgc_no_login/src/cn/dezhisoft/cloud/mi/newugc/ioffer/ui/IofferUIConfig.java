package cn.dezhisoft.cloud.mi.newugc.ioffer.ui;

import cn.dezhisoft.cloud.mi.newugc.R;

import com.sobey.android.ui.Tab;
import com.sobey.android.ui.UIConfig;
import cn.dezhisoft.cloud.mi.newugc.ioffer.upload.ui.UGCAddUploadActivity;

public final class IofferUIConfig extends UIConfig {

	@Override
	public int getTabMenuHeight() {
		return 0;
	}

	@Override
	public int getContentLayout() {
		return R.id.ice_root_content_scrollview;
	}

	@Override
	public int getNavigationLayout() {
		return R.layout.sobey_ui_ice_tab_menu;
	}

	@Override
	public Tab[] getNavigationTabs() {
		Tab[] tabs = null ;
		int text_size = 18;
		tabs = new Tab[3];
		
		//
		tabs[0] = new Tab();
		tabs[0].tab_id = Tab.TAB_ID_1;
		tabs[0].tab_text_rid = R.string.tab_menu_1;
		tabs[0].tab_text_size = text_size;
		tabs[0].draw_flag = Tab.DRAW_MODE_BITMAP_SWITCH | Tab.DRAW_MODE_TEXT_BOTTOM;
		tabs[0].tab_select_rid = R.drawable.ic_ioffer_menu_tab_content_selected;
		tabs[0].tab_unselect_rid = R.drawable.ic_ioffer_menu_tab_content_default;
		tabs[0].tab_view_id = R.id.ioffer_menu_tab1;
		
		//
		tabs[1] = new Tab();
		tabs[1].tab_id = Tab.TAB_ID_2;
		tabs[1].tab_text_rid = R.string.tab_menu_2;
		tabs[1].tab_text_size = text_size;
		tabs[1].draw_flag = Tab.DRAW_MODE_BITMAP_SWITCH | Tab.DRAW_MODE_TEXT_BOTTOM;
		tabs[1].tab_select_rid = R.drawable.ic_ioffer_menu_tab_add_selected;
		tabs[1].tab_unselect_rid = R.drawable.ic_ioffer_menu_tab_add_default;
		tabs[1].tab_view_id = R.id.ioffer_menu_tab2;
		
		//
		tabs[2] = new Tab();
		tabs[2].tab_id = Tab.TAB_ID_3;
		tabs[2].tab_text_rid = R.string.tab_menu_3;
		tabs[2].tab_text_size = text_size;
		tabs[2].draw_flag = Tab.DRAW_MODE_BITMAP_SWITCH | Tab.DRAW_MODE_TEXT_BOTTOM;
		tabs[2].tab_select_rid = R.drawable.ic_ioffer_menu_tab_user_selected;
		tabs[2].tab_unselect_rid = R.drawable.ic_ioffer_menu_tab_user_default;
		tabs[2].tab_view_id = R.id.ioffer_menu_tab3;
		
		return tabs ;
	}

	@Override
	public int getRootLayout() {
		return R.layout.sobey_ui_ice_root_layout;
	}

	@Override
	public Class getTab1Activity() {
		return TabContentActivity.class;
	}

	@Override
	public Class getTab2Activity() {
		return UGCAddUploadActivity.class;
	}

	@Override
	public Class getTab3Activity() {
		return TabUserActivity.class ;
	}

	@Override
	public Class getTab4Activity() {
		return null;
	}

	@Override
	public Class getTab5Activity() {
		return null;
	}

	/*
	@Override
	public int getFromLeftIn() {
		return R.anim.from_left_in;
	}

	@Override
	public int getFromLeftOut() {
		return R.anim.from_left_out;
	}

	@Override
	public int getFromRightIn() {
		return R.anim.from_right_in;
	}

	@Override
	public int getFromRightOut() {
		return R.anim.from_right_out;
	}*/
}
