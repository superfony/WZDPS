package com.epsmart.wzdp.activity.fragment;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.supply.fragment.ItemFragment;
import com.epsmart.wzdp.activity.supply.fragment.PointFragment;
import com.epsmart.wzdp.activity.supply.fragment.ProductionFragment;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class ContractFragment extends CommonFragment implements
		OnLeftMenuSelectedListener {

	@Override 
	public void onMenuSelected(FragmentActivity activity, int position) {
		 clearStack();
		CommonActivity.state=position;
		switch (position) {
		case 0:
			// 铁塔排产计划
//			if(!PermissHelp.isPermiss("003")){
//				PermissHelp.showToast(activity);
//				return;
//			}
			Common.replaceRightFragment(activity, new ProductionFragment(), false,R.id.fragment_right_container);
			break;
		case 1:
			// 铁塔生产进度周报
//			if(!PermissHelp.isPermiss("004")){
//				PermissHelp.showToast(activity);
//				return;
//			}
			Common.replaceRightFragment(activity, new ItemFragment(), false,R.id.fragment_right_container);
			break;
		default:
			break;
		
	}
		
	}

	@Override
	public void onSupplyTopMenuSelected(FragmentActivity activity, int position) {

	}
	/* 清除栈空间*/
	public void clearStack(){
		 int backStackCount = getFragmentManager().getBackStackEntryCount();
			Log.i("SupplyFragment", ".......clearStack..........>>="+backStackCount);
	        for(int i = 0; i < backStackCount; i++) {    
	            getFragmentManager().popBackStack();
	        }
	}
	
	/* 栈底保留两个*/
	public void clearStackOther(){
		 int backStackCount = getFragmentManager().getBackStackEntryCount();
			Log.i("SupplyFragment", ".....clearStackOther.......>>="+backStackCount);
	        for(int i = 2; i < backStackCount; i++) {    
	            getFragmentManager().popBackStack();
	        }
	}
	
	
}
