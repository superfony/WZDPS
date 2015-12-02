package com.epsmart.wzdp.activity.movedapprove;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.view.CircleImageView;
import com.epsmart.wzdp.view.CircleLayoutTwo;

/**
 * 移动审批 
 */
public class MovedMenuActivity extends MovedCommonAct {
	private String TAG = MovedMenuActivity.class.getName();

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
		tview.setBackgroundResource(R.drawable.move_title);

		setContentView(R.layout.moved_activity);
		CircleLayoutTwo circletwo = (CircleLayoutTwo) findViewById(R.id.supply_layout);
		
		String MOVE = getIntent().getStringExtra("move");

		CircleImageView mquality = (CircleImageView) circletwo
				.findViewById(R.id.mquality_image);
		if ("quality".equals(MOVE)) {
			mquality.setBackgroundResource(R.drawable.icon_qualityxz);
		}
		mquality.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("013")) {
					 PermissHelp.showToast(activity);
					return;
				}
				Toast.makeText(getApplicationContext(), "质量监督问题审批",
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(MovedMenuActivity.this, MovedActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});

		CircleImageView mservice = (CircleImageView) circletwo
				.findViewById(R.id.mservice_image);//项目现场服务审批
		if ("service".equals(MOVE)) {
			mservice.setBackgroundResource(R.drawable.icon_servicexz);
		}
		mservice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("025")) {
					 PermissHelp.showToast(activity);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(MovedMenuActivity.this, MovedActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});

//		CircleImageView mrequest = (CircleImageView) circletwo
//				.findViewById(R.id.mrequest_image);//物资需求计划审批
//		mrequest.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				Toast.makeText(getApplicationContext(), "没有权限",
//						Toast.LENGTH_SHORT).show();
//				// Intent intent = new Intent();
//				// intent.setClass(MovedMenuActivity.this, MovedActivity.class);
//				// intent.putExtra("tag", "2");
//				// startActivity(intent);
//			}
//		});

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
