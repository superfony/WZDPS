package com.epsmart.wzdp.activity.contract;

import android.content.Intent;
import android.os.Bundle;

import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.contract.fragment.TowerProgressFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/*
 * 特高压项目（生产进度）
 */
public class ContractActivity extends CommonActivity {
	private int modelFlag = 0;
	private ContractMenuFragment cmf;
	private String TAG = ContractActivity.class.getName();

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
		cmf = new ContractMenuFragment();
		cmf.setActivity(ContractActivity.this);
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
				mContent = new TowerProgressFragment();// 铁塔生产进度
				bundle.putString("types", "tower");
				mContent.setArguments(bundle);
				break;
			case 1:
				mContent = new TowerProgressFragment();// 瓷绝缘子生产进度
				bundle.putString("types", "porcelain");
				mContent.setArguments(bundle);
				break;
			case 2:
				mContent = new TowerProgressFragment();// 复合绝缘子生产进度
				bundle.putString("types", "reunite");
				mContent.setArguments(bundle);
				break;
			case 3:
				mContent = new TowerProgressFragment();// 导线生产进度
				bundle.putString("types", "wireway");
				mContent.setArguments(bundle);
				break;
			case 4:
				mContent = new TowerProgressFragment();// 地线生产进度
				bundle.putString("types", "ground");
				mContent.setArguments(bundle);
				break;
			case 5:
				mContent = new TowerProgressFragment();// 光缆生产进度
				bundle.putString("types", "cable");
				mContent.setArguments(bundle);
				break;
			case 6:
				mContent = new TowerProgressFragment();// 金具生产进度
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
