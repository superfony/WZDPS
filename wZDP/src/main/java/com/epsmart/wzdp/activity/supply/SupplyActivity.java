package com.epsmart.wzdp.activity.supply;

import android.content.Intent;
import android.os.Bundle;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.supply.fragment.EquipmentFragment;
import com.epsmart.wzdp.activity.supply.fragment.ItemFragment;
import com.epsmart.wzdp.activity.supply.fragment.PointFragment;
import com.epsmart.wzdp.activity.supply.fragment.ProductionFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * @author fony 供应商现场管理  生产过程监控
 */
public class SupplyActivity extends CommonActivity {
	String TAG = SupplyActivity.class.getName();
	private int modelFlag = 0;
	private SupplyMenuFragment smf;

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
		smf = new SupplyMenuFragment();
		smf.setActivity(SupplyActivity.this);
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
				mContent = new ProductionFragment();
				break;
			case 1:
				mContent = new ItemFragment();
				break;
			case 2:
				mContent = new PointFragment();
				break;
			case 3:
				mContent = new EquipmentFragment();
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
