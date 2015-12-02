package com.epsmart.wzdp.activity.contract;

import android.content.Intent;
import android.os.Bundle;

import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.contract.fragment.TowerPlanFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/*
 * 特高压项目（排产计划）
 */
public class ContractPlanActivity extends CommonActivity {
	private int modelFlag = 0;
	private ContractMenuPlanFragment cmf;
	private String TAG = ContractPlanActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		modelFlag = Integer.parseInt(getIntent().getStringExtra("tag"));
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public SlidingMenuFragment addSlidingMenuFragment() {
		cmf = new ContractMenuPlanFragment();
		cmf.setActivity(ContractPlanActivity.this);
		cmf.setNewContent(mContent);
		return cmf;
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
			Bundle bundle = new Bundle();
			switch (modelFlag) {
			case 0:
				mContent = new TowerPlanFragment();// 铁塔排产计划
				bundle.putString("types", "tower");
				mContent.setArguments(bundle);
				break;
			case 1:
				mContent = new TowerPlanFragment();// 瓷绝缘子排产计划
				bundle.putString("types", "porcelain");
				mContent.setArguments(bundle);
				break;
			case 2:
				mContent = new TowerPlanFragment();// 复合绝缘子排产计划
				bundle.putString("types", "reunite");
				mContent.setArguments(bundle);
				break;
			case 3:
				mContent = new TowerPlanFragment();// 导线排产计划
				bundle.putString("types", "wireway");
				mContent.setArguments(bundle);
				break;
			case 4:
				mContent = new TowerPlanFragment();// 地线排产计划
				bundle.putString("types", "ground");
				mContent.setArguments(bundle);
				break;
			case 5:
				mContent = new TowerPlanFragment();// 光缆排产计划
				bundle.putString("types", "cable");
				mContent.setArguments(bundle);
				break;
			case 6:
				mContent = new TowerPlanFragment();// 金具排产计划
				bundle.putString("types", "fittings");
				mContent.setArguments(bundle);
				break;
			default:
				break;
			}
		}
		super.initSdingBar(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (fillHelp != null)
			fillHelp.onCammerResult(requestCode, resultCode, data, tag);

		if (fillHelpNew != null)
			fillHelpNew.onCammerResult(requestCode, resultCode, data, tag);
		super.onActivityResult(requestCode, resultCode, data);
	}

}
