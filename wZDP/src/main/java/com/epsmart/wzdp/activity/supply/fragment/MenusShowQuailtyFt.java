package com.epsmart.wzdp.activity.supply.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.common.Common;

/**
 * 
 */
@SuppressLint("NewApi")
public class MenusShowQuailtyFt extends SupplyFragmemt {
	private  View view;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		 view = inflater.inflate(R.layout.menu_show_quality, container,
				false);
		
		Common.replaceRightFragment(activity,
				new QtyTopInfo(), false,
				R.id.top_container);
		
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
