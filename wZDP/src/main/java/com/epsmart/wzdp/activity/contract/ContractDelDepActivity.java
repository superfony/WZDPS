package com.epsmart.wzdp.activity.contract;

import android.content.Intent;
import android.os.Bundle;

import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.contract.fragment.ConfirmListFragment;
import com.epsmart.wzdp.activity.contract.fragment.DeliveryListFragment;
import com.epsmart.wzdp.activity.contract.fragment.DepartureListFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 发货起运
 */
public class ContractDelDepActivity extends CommonActivity {
	String TAG = ContractDelDepActivity.class.getName();
	private int modelFlag = 0;
	private ContractMenuDeliveryFragment smf;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		modelFlag = Integer.parseInt(getIntent().getStringExtra("tag"));
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume(){
		super.onResume();
	}

	public SlidingMenuFragment addSlidingMenuFragment() {
		smf = new ContractMenuDeliveryFragment();
		smf.setActivity(ContractDelDepActivity.this);
		smf.setNewContent(mContent);
		return smf;
	}

	/**
	 * 装配第一个需要显示的Fragmet
	 */
	protected void initSdingBar(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			mContent = getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");
		}

		if (mContent == null) {
			switch (modelFlag) {
			case 0:
				mContent = new DeliveryListFragment();// 发货计划
				break;
			case 1:
				mContent = new DepartureListFragment();// 起运通知
				break;
			case 2:
				mContent = new ConfirmListFragment();// 到货确认
				break;
			default:
				break;
			}
		}
		super.initSdingBar(savedInstanceState);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (fillHelp != null)
			fillHelp.onCammerResult(requestCode, resultCode, data, tag);

		if (fillHelpNew != null)
			fillHelpNew.onCammerResult(requestCode, resultCode, data, tag);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
