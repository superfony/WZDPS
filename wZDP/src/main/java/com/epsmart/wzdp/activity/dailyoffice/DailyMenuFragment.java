package com.epsmart.wzdp.activity.dailyoffice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.dailyoffice.fragment.ItemNoticeFragment;
import com.epsmart.wzdp.activity.dailyoffice.fragment.ItemOnlineFragment;
import com.epsmart.wzdp.activity.dailyoffice.fragment.ItemWorkFragment;
import com.epsmart.wzdp.common.PermissHelp;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 日常办公模块   sdingmenu菜单 切换的实现
 * 
 */

public class DailyMenuFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public ImageView notice_imag;// 通知公告
	public ImageView work_imag;// 工作日志
	public ImageView online_imag;// 在线交流
	private Fragment newContent = null;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_daily, container,
				false);
		notice_imag = (ImageView) contextView.findViewById(R.id.notice_imag);
		work_imag = (ImageView) contextView.findViewById(R.id.work_imag);
		online_imag = (ImageView) contextView.findViewById(R.id.online_imag);

		if (newContent instanceof ItemNoticeFragment) {
			notice_imag.setBackgroundResource(R.drawable.notice_down);
		} else
			if (newContent instanceof ItemWorkFragment) {
			work_imag.setBackgroundResource(R.drawable.dwork_down);
		} else if (newContent instanceof ItemOnlineFragment) {
			online_imag.setBackgroundResource(R.drawable.online_down);
		}

		notice_imag.setOnClickListener(click_image);
		work_imag.setOnClickListener(click_image);
		online_imag.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {

			int id = v.getId();
			if (id == R.id.notice_imag) {
				newContent = new ItemNoticeFragment();// 通知公告
				notice_imag.setBackgroundResource(R.drawable.notice_down);
				work_imag.setBackgroundResource(R.drawable.dwork_up);
				online_imag.setBackgroundResource(R.drawable.online_up);
			} else
				if (id == R.id.work_imag) {
					if (!PermissHelp.isPermiss("028")) {
						work_imag.setBackgroundResource(R.drawable.dwork_down);
						 PermissHelp.showToast(getActivity());
						return;
					}
				newContent = new ItemWorkFragment();// 工作日志
				work_imag.setBackgroundResource(R.drawable.dwork_down);
				notice_imag.setBackgroundResource(R.drawable.notice_up);
				online_imag.setBackgroundResource(R.drawable.online_up);
			} else if (id == R.id.online_imag) {
				newContent = new ItemOnlineFragment();// 在线交流
				online_imag.setBackgroundResource(R.drawable.online_down);
				notice_imag.setBackgroundResource(R.drawable.notice_up);
				work_imag.setBackgroundResource(R.drawable.dwork_up);
			}

			if (newContent != null)
				switchFragment(newContent);

		}
	};

	public void setActivity(DailyActivity mActivity) {
		this.mActivity = mActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof DailyActivity) {
			DailyActivity fca = (DailyActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
