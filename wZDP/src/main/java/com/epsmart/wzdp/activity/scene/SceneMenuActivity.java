package com.epsmart.wzdp.activity.scene;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.view.CircleImageView;
import com.epsmart.wzdp.view.CircleLayoutTwo;

/**
 * @author fony 供应商现场管理
 */
public class SceneMenuActivity extends Activity {
	private String TAG = SceneMenuActivity.class.getName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		View view = actionBar.getCustomView();
		ImageView tview = (ImageView) view.findViewById(R.id.title_image);
		tview.setBackgroundResource(R.drawable.scene_project);

		setContentView(R.layout.activity_scene);
//		if (!PermissHelp.isPermiss("003")) {
//			PermissHelp.showToast(this);
//			return;
//		}
		CircleLayoutTwo circletwo = (CircleLayoutTwo) findViewById(R.id.scene_layout);
		
		CircleImageView itemProInfo = (CircleImageView) circletwo
				.findViewById(R.id.proinfo_image);
		itemProInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(SceneMenuActivity.this, SceneActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});
		
		CircleImageView itemProCheck = (CircleImageView) circletwo
				.findViewById(R.id.procheck_image);
		itemProCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(SceneMenuActivity.this, SceneActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});
		
		CircleImageView itemProService = (CircleImageView) circletwo
				.findViewById(R.id.proservice_image);
		itemProService.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(SceneMenuActivity.this, SceneActivity.class);
				intent.putExtra("tag", "2");
				startActivity(intent);
			}
		});
		
//		CircleImageView itemProWork = (CircleImageView) circletwo
//				.findViewById(R.id.prowork_image);
//		itemProWork.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent();
//				intent.setClass(SceneMenuActivity.this, SceneActivity.class);
//				intent.putExtra("tag", "3");
//				startActivity(intent);
//			}
//		});
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
