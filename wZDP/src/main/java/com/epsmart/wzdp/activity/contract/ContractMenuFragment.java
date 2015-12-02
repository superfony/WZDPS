package com.epsmart.wzdp.activity.contract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.contract.fragment.TowerProgressFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

public class ContractMenuFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public ImageView tower_progress, porcelain_progress, reunite_progress,
			wireway_progress, ground_progress, cable_progress,fittings_progress;
	private Fragment newContent = null;
	private String types;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
		types = newContent.getArguments().getString("types");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceSta) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_contract, container,false);
		tower_progress = (ImageView) contextView.findViewById(R.id.tower_progress);
		porcelain_progress = (ImageView) contextView.findViewById(R.id.porcelain_progress);
		reunite_progress = (ImageView) contextView.findViewById(R.id.reunite_progress);
		wireway_progress = (ImageView) contextView.findViewById(R.id.wireway_progress);
		ground_progress = (ImageView) contextView.findViewById(R.id.ground_progress);
		cable_progress = (ImageView) contextView.findViewById(R.id.cable_progress);
		fittings_progress = (ImageView) contextView.findViewById(R.id.fittings_progress);

		if ("tower".equals(types)) {
			tower_progress.setBackgroundResource(R.drawable.progress_down);// 铁塔
		} else if ("porcelain".equals(types)) {
			porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_down);// 瓷
		}else if ("reunite".equals(types)) {
			reunite_progress.setBackgroundResource(R.drawable.pro_reunite_down);// 复合
		}else if ("wireway".equals(types)) {
			wireway_progress.setBackgroundResource(R.drawable.pro_wireway_down);// 导线
		}else if ("ground".equals(types)) {
			ground_progress.setBackgroundResource(R.drawable.pro_ground_down);// 地线
		}else if ("cable".equals(types)) {
			cable_progress.setBackgroundResource(R.drawable.pro_cable_down);// 光缆
		}else if ("fittings".equals(types)) {
			fittings_progress.setBackgroundResource(R.drawable.pro_fittings_down);// 金具
		}

		tower_progress.setOnClickListener(click_image);
		porcelain_progress.setOnClickListener(click_image);
		reunite_progress.setOnClickListener(click_image);
		wireway_progress.setOnClickListener(click_image);
		ground_progress.setOnClickListener(click_image);
		cable_progress.setOnClickListener(click_image);
		fittings_progress.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			int id = v.getId();
			if (id == R.id.tower_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				tower_progress.setBackgroundResource(R.drawable.progress_down);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_up);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_up);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_up);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_up);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_up);
			} else if (id == R.id.porcelain_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_down);
				tower_progress.setBackgroundResource(R.drawable.progress_up);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_up);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_up);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_up);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_up);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_up);
			}else if (id == R.id.reunite_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_down);
				tower_progress.setBackgroundResource(R.drawable.progress_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_up);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_up);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_up);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_up);
			}else if (id == R.id.wireway_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_down);
				tower_progress.setBackgroundResource(R.drawable.progress_up);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_up);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_up);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_up);
			}else if (id == R.id.ground_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_down);
				tower_progress.setBackgroundResource(R.drawable.progress_up);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_up);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_up);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_up);
			}else if (id == R.id.cable_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_down);
				tower_progress.setBackgroundResource(R.drawable.progress_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_cable_up);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_up);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_up);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_up);
			}else if (id == R.id.fittings_progress) {
				newContent = new TowerProgressFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				fittings_progress.setBackgroundResource(R.drawable.pro_fittings_down);
				tower_progress.setBackgroundResource(R.drawable.progress_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
				reunite_progress.setBackgroundResource(R.drawable.pro_reunite_up);
				wireway_progress.setBackgroundResource(R.drawable.pro_wireway_up);
				ground_progress.setBackgroundResource(R.drawable.pro_ground_up);
				cable_progress.setBackgroundResource(R.drawable.pro_cable_up);
				porcelain_progress.setBackgroundResource(R.drawable.pro_porcelain_up);
			}

			if (newContent != null)
				switchFragment(newContent);

		}
	};

	public void setActivity(ContractActivity mActivity) {
		this.mActivity = mActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof ContractActivity) {
			ContractActivity fca = (ContractActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
