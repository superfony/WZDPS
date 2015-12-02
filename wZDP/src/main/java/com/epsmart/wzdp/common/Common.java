package com.epsmart.wzdp.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.epsmart.wzdp.R;
public class Common {
	

	public static void replaceRightFragment(FragmentActivity activity,Fragment fragment,boolean popBackStack,int layoutFragment) {
		if(popBackStack)
			activity.getFragmentManager().popBackStack();
		
		FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager()
				.beginTransaction();
		
		//fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
		fragmentTransaction.replace(layoutFragment, fragment);
		fragmentTransaction.addToBackStack(null); 
		fragmentTransaction.commit();
	}
	
	
	public static void sideBarClickableSetting(FragmentActivity activity,boolean clickable) {
		FragmentManager fragmentManager = activity.getSupportFragmentManager();
		Fragment leftFragment = fragmentManager
				.findFragmentById(R.id.fragment_left);
		View leftView = leftFragment.getView();
		for (int i = 0; i < 6; i++) {
			TextView tv = (TextView) leftView.findViewWithTag(i + "");
			if (i == 4)
				continue;
			tv.setClickable(clickable);
		}
	}
	
	
}
