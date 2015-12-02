package com.epsmart.wzdp.activity.scene.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.supply.service.SupplyService;
import com.epsmart.wzdp.service.CommonService;

public class LeftFragment extends SceneFragmemt{

	private View view;
	private CommonService service;
	private CommonActivity activity;
	private TextView menu0Tv, menu1Tv, menu2Tv;
	private int tag = 0;
	
	@Override
	public void onAttach(Activity activity) {
		this.activity=(CommonActivity)activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_scene_left, container, false);
		service = new SupplyService((CommonActivity) getActivity(), view);
		initLeftMenuListener();
		return view;
	}
	
	private void initLeftMenuListener(){
		TextView menu0 = (TextView) view.findViewById(R.id.tv_menu0);
		//Constants.supplyLeftTextView = menu0;
		service.setLeftTextViewBackgroundColor(menu0,0);
		for (int i = 0; i < 3; i++) {
			TextView menu = (TextView) view.findViewWithTag(i+"");
			menu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					getActivity().setTitle(((TextView)v).getText());
					service.setLeftTextViewBackgroundColor(v,Integer.parseInt(v.getTag().toString()));
					LeftFragment.this.onMenuSelected(activity,Integer.parseInt(v.getTag().toString()));
				}
			});
		}
	}
	
   
}
