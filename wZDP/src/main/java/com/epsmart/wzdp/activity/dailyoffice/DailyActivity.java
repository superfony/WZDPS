package com.epsmart.wzdp.activity.dailyoffice;

import android.content.Intent;
import android.os.Bundle;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.dailyoffice.fragment.ItemNoticeFragment;
import com.epsmart.wzdp.activity.dailyoffice.fragment.ItemOnlineFragment;
import com.epsmart.wzdp.activity.dailyoffice.fragment.ItemWorkFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 日常办公 菜单
 */
public class DailyActivity extends CommonActivity {
	private int modelFlag = 0;
	private DailyMenuFragment smf;
	private String TAG = DailyActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		modelFlag = Integer.parseInt(getIntent().getStringExtra("tag"));
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public SlidingMenuFragment addSlidingMenuFragment() {
		smf = new DailyMenuFragment();
		smf.setActivity(DailyActivity.this);
		smf.setNewContent(mContent);
		return smf;
	}

	/**
	 * 装配第一个需要显示的Fragmet
	 */
	protected void initSdingBar(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}
		if (mContent == null) {
			switch (modelFlag) {
			case 0:
				mContent = new ItemNoticeFragment();// 通知公告
				break;
			case 1:
				mContent = new ItemWorkFragment();// 工作日志
				break;
			case 2:
				mContent = new ItemOnlineFragment();// 在线交流
				break;
			}
		}
		super.initSdingBar(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mContent instanceof ItemOnlineFragment) {
			((ItemOnlineFragment) mContent).oo();
		}
		if (mContent instanceof ItemNoticeFragment) {
			((ItemNoticeFragment) mContent).oo();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
