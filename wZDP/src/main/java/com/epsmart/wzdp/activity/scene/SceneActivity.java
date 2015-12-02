package com.epsmart.wzdp.activity.scene;

import android.content.Intent;
import android.os.Bundle;

import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.scene.fragment.ItemProCheckFragment;
import com.epsmart.wzdp.activity.scene.fragment.ItemProInfoFragment;
import com.epsmart.wzdp.activity.scene.fragment.ItemProServiceFragment;
import com.epsmart.wzdp.activity.scene.fragment.ItemProWorkFragment;
import com.fony.menu.widget.slidingmenu.fragment.SlidingMenuFragment;
/**
 * 项目现场管理界面
 * @author hqq
 *
 */
public class SceneActivity extends CommonActivity {
	private int modelFlag = 0;
	private SceneMenuFragment smf;
	private String TAG = SceneActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		modelFlag=Integer.parseInt(getIntent().getStringExtra("tag"));
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	

	public SlidingMenuFragment addSlidingMenuFragment() {
		smf = new SceneMenuFragment();
		smf.setActivity(SceneActivity.this);
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
				mContent = new ItemProInfoFragment();
				break;
			case 1:
				mContent = new ItemProCheckFragment();
				break;
			case 2:
				mContent = new ItemProServiceFragment();
				break;
			case 3:
				mContent = new ItemProWorkFragment();
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
