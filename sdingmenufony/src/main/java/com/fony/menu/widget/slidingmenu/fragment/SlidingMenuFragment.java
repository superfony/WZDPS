package com.fony.menu.widget.slidingmenu.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fony.menu.widget.slidingmenu.BaseSdingActivity;
/**
 * 
 * @author fony
 *
 */

public class SlidingMenuFragment extends Fragment {
	protected BaseSdingActivity mActivity = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mActivity = (BaseSdingActivity) getActivity();
	}

}