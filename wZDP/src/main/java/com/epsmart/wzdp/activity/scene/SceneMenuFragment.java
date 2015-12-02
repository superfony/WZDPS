package com.epsmart.wzdp.activity.scene;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.scene.fragment.ItemProCheckFragment;
import com.epsmart.wzdp.activity.scene.fragment.ItemProInfoFragment;
import com.epsmart.wzdp.activity.scene.fragment.ItemProServiceFragment;
import com.epsmart.wzdp.activity.scene.fragment.ItemProWorkFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 
 * @author fony sdingmenu菜单 切换的实现
 * 
 */

public class SceneMenuFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public ImageView provision_imag;
	public ImageView construction_imag;
	public ImageView service_imag;
//	public ImageView work_imag;
	private Fragment newContent = null;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_scene, container,false);
		provision_imag = (ImageView) contextView.findViewById(R.id.provision_imag);
		construction_imag = (ImageView) contextView.findViewById(R.id.construction_imag);
		service_imag = (ImageView) contextView.findViewById(R.id.service_imag);
//		work_imag = (ImageView)contextView.findViewById(R.id.work_imag);

		if (newContent instanceof ItemProInfoFragment) {
			construction_imag.setBackgroundResource(R.drawable.construction_down);
		} else if (newContent instanceof ItemProCheckFragment) {
			provision_imag.setBackgroundResource(R.drawable.provision_down);
		} else if (newContent instanceof ItemProServiceFragment) {
			service_imag.setBackgroundResource(R.drawable.service_down);
		}
//		else if (newContent instanceof ItemProWorkFragment) {
//			work_imag.setBackgroundResource(R.drawable.work_down);
//		}

		provision_imag.setOnClickListener(click_image);
		construction_imag.setOnClickListener(click_image);
		service_imag.setOnClickListener(click_image);
//		work_imag.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {

			int id = v.getId();
			if (id == R.id.construction_imag) {
				newContent = new ItemProInfoFragment();
				construction_imag.setBackgroundResource(R.drawable.construction_down);
				service_imag.setBackgroundResource(R.drawable.service_up);
				provision_imag.setBackgroundResource(R.drawable.provision_up);
//				work_imag.setBackgroundResource(R.drawable.work_up);
			} else if (id == R.id.service_imag) {
				newContent = new ItemProServiceFragment();
				service_imag.setBackgroundResource(R.drawable.service_down);
				construction_imag.setBackgroundResource(R.drawable.construction_up);
				provision_imag.setBackgroundResource(R.drawable.provision_up);
//				work_imag.setBackgroundResource(R.drawable.work_up);
			} else if (id == R.id.provision_imag) {
				newContent = new ItemProCheckFragment();
				provision_imag.setBackgroundResource(R.drawable.provision_down);
				construction_imag.setBackgroundResource(R.drawable.construction_up);
				service_imag.setBackgroundResource(R.drawable.service_up);
//				work_imag.setBackgroundResource(R.drawable.work_up);
			}
//			else if (id == R.id.work_imag) {
//				newContent = new ItemProWorkFragment();
//				work_imag.setBackgroundResource(R.drawable.work_down);
//				provision_imag.setBackgroundResource(R.drawable.provision_up);
//				construction_imag.setBackgroundResource(R.drawable.construction_up);
//				service_imag.setBackgroundResource(R.drawable.service_up);
//			}

			if (newContent != null)
				switchFragment(newContent);

		}
	};

	public void setActivity(SceneActivity mActivity) {
		this.mActivity = mActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof SceneActivity) {
			SceneActivity fca = (SceneActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
