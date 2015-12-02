package com.epsmart.wzdp.activity.movedapprove;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.scene.fragment.ItemProServiceFragment;
import com.epsmart.wzdp.activity.supply.fragment.ItemFragment;
import com.epsmart.wzdp.common.PermissHelp;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 菜单 切换的实现
 * 
 */

public class MovedMenuFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public ImageView request;// 物资需求计划审批
	public ImageView service;// 项目现场服务审批
	public ImageView quality;// 质量监督问题审批
	private Fragment newContent = null;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_moved, container,
				false);
//		request = (ImageView) contextView.findViewById(R.id.request_imag);// 物资需求计划审批
		service = (ImageView) contextView.findViewById(R.id.service_imag);// 项目现场服务审批
		quality = (ImageView) contextView.findViewById(R.id.quality_imag);// 质量监督问题审批

		if (newContent instanceof ItemProServiceFragment) {
			service.setBackgroundResource(R.drawable.moved_service_d);
		} else if (newContent instanceof ItemFragment) {
			quality.setBackgroundResource(R.drawable.moved_quality_d);
		} 
//		else if (newContent instanceof Fragment) {
//			request.setBackgroundResource(R.drawable.moved_request_d);
//		}

//		request.setOnClickListener(click_image);
		service.setOnClickListener(click_image);
		quality.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {

			int id = v.getId();
			if (id == R.id.service_imag) {
				if (!PermissHelp.isPermiss("025")) {
					PermissHelp.showToast(getActivity());
					return;
				}
				newContent = new ItemProServiceFragment();
				service.setBackgroundResource(R.drawable.moved_service_d);
				quality.setBackgroundResource(R.drawable.moved_quality_u);
//				request.setBackgroundResource(R.drawable.moved_request_u);
			} else if (id == R.id.quality_imag) {
				if (!PermissHelp.isPermiss("013")) {
					PermissHelp.showToast(getActivity());
					return;
				}
				newContent = new ItemFragment();
				quality.setBackgroundResource(R.drawable.moved_quality_d);
//				request.setBackgroundResource(R.drawable.moved_request_u);
				service.setBackgroundResource(R.drawable.moved_service_u);
			} 
//			else if (id == R.id.request_imag) {
//				// newContent = new Fragment();//物资需求计划审批---未开发
//				request.setBackgroundResource(R.drawable.moved_request_d);
//				service.setBackgroundResource(R.drawable.moved_service_u);
//				quality.setBackgroundResource(R.drawable.moved_quality_u);
//			}

			if (newContent != null)
				switchFragment(newContent);

		}
	};

	public void setActivity(MovedActivity mActivity) {
		this.mActivity = mActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MovedActivity) {
			MovedActivity fca = (MovedActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
