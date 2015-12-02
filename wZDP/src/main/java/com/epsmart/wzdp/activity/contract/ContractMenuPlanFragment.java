package com.epsmart.wzdp.activity.contract;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.contract.fragment.TowerPlanFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

public class ContractMenuPlanFragment extends SlidingMenuFragment {
	private View contextView = null;
	private LayoutInflater inflater = null;
	public  ImageView tower_plan, porcelain_plan, reunite_plan,
	wireway_plan, ground_plan, cable_plan,fittings_plan;
	private Fragment newContent = null;
	private String types;

	public void setNewContent(Fragment newContent) {
		this.newContent = newContent;
		types = newContent.getArguments().getString("types");
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceSta) {

		this.inflater = inflater;
		contextView = inflater.inflate(R.layout.slide_menu_contractplan, container,false);
		tower_plan = (ImageView) contextView.findViewById(R.id.tower_plan);
		porcelain_plan = (ImageView) contextView.findViewById(R.id.porcelain_plan);
		reunite_plan = (ImageView) contextView.findViewById(R.id.reunite_plan);
		wireway_plan = (ImageView) contextView.findViewById(R.id.wireway_plan);
		ground_plan = (ImageView) contextView.findViewById(R.id.ground_plan);
		cable_plan = (ImageView) contextView.findViewById(R.id.cable_plan);
		fittings_plan = (ImageView) contextView.findViewById(R.id.fittings_plan);

		if ("tower".equals(types)) {
			tower_plan.setBackgroundResource(R.drawable.plan_down);// 铁塔
		} else if ("tower".equals(types)) {
			porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_down);// 瓷
		}else if ("tower".equals(types)) {
			reunite_plan.setBackgroundResource(R.drawable.plan_reunite_down);// 复合
		}else if ("tower".equals(types)) {
			wireway_plan.setBackgroundResource(R.drawable.plan_wireway_down);// 导线
		}else if ("tower".equals(types)) {
			ground_plan.setBackgroundResource(R.drawable.plan_ground_down);// 地线
		}else if ("tower".equals(types)) {
			cable_plan.setBackgroundResource(R.drawable.plan_cable_down);// 光缆
		}else if ("tower".equals(types)) {
			fittings_plan.setBackgroundResource(R.drawable.plan_fittings_down);// 金具
		}

		tower_plan.setOnClickListener(click_image);
		porcelain_plan.setOnClickListener(click_image);
		reunite_plan.setOnClickListener(click_image);
		wireway_plan.setOnClickListener(click_image);
		ground_plan.setOnClickListener(click_image);
		cable_plan.setOnClickListener(click_image);
		fittings_plan.setOnClickListener(click_image);
		return contextView;
	}

	OnClickListener click_image = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Bundle bundle = new Bundle();
			int id = v.getId();
			if (id == R.id.tower_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				tower_plan.setBackgroundResource(R.drawable.plan_down);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_up);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_up);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_up);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_up);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_up);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_up);
			} else if (id == R.id.porcelain_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_down);
				tower_plan.setBackgroundResource(R.drawable.plan_up);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_up);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_up);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_up);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_up);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_up);
			}else if (id == R.id.reunite_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_down);
				tower_plan.setBackgroundResource(R.drawable.plan_up);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_up);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_up);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_up);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_up);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_up);
			}else if (id == R.id.wireway_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_down);
				tower_plan.setBackgroundResource(R.drawable.plan_up);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_up);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_up);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_up);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_up);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_up);
			}
			else if (id == R.id.ground_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_down);
				tower_plan.setBackgroundResource(R.drawable.plan_up);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_up);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_up);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_up);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_up);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_up);
			}else if (id == R.id.cable_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_down);
				tower_plan.setBackgroundResource(R.drawable.plan_up);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_up);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_up);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_up);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_up);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_up);
			}else if (id == R.id.fittings_plan) {
				newContent = new TowerPlanFragment();
				bundle.putString("types", types);
				newContent.setArguments(bundle);
				fittings_plan.setBackgroundResource(R.drawable.plan_fittings_down);
				tower_plan.setBackgroundResource(R.drawable.plan_up);
				reunite_plan.setBackgroundResource(R.drawable.plan_reunite_up);
				wireway_plan.setBackgroundResource(R.drawable.plan_wireway_up);
				ground_plan.setBackgroundResource(R.drawable.plan_ground_up);
				cable_plan.setBackgroundResource(R.drawable.plan_cable_up);
				porcelain_plan.setBackgroundResource(R.drawable.plan_porcelain_up);
			}

			if (newContent != null)
				switchFragment(newContent);

		}
	};

	public void setActivity(ContractPlanActivity mActivity) {
		this.mActivity = mActivity;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof ContractPlanActivity) {
			ContractPlanActivity fca = (ContractPlanActivity) getActivity();
			fca.switchContent(fragment);
		}
	}

}
