package com.epsmart.wzdp.activity.movedapprove;

import android.content.Intent;
import android.os.Bundle;

import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.scene.fragment.ItemProServiceFragment;
import com.epsmart.wzdp.activity.supply.fragment.ItemFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;

/**
 * 移动审批
 */
public class MovedActivity extends CommonActivity {
	String TAG = MovedActivity.class.getName();
	private int modelFlag = 0;
	private MovedMenuFragment smf;

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
		smf = new MovedMenuFragment();
		smf.setActivity(MovedActivity.this);
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
				mContent = new ItemFragment();// 质量监督问题审批
				break;
			case 1:
				mContent = new ItemProServiceFragment();// 项目现场服务审批
				break;
			// case 2:
			// mContent = new PointFragment();//物资需求计划审批
			// break;
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
