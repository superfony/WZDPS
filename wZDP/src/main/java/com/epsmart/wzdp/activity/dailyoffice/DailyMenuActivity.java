package com.epsmart.wzdp.activity.dailyoffice;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.view.CircleImageView;
import com.epsmart.wzdp.view.CircleLayoutTwo;

/**
 * @author fony 日常办公
 */
public class DailyMenuActivity extends DailyCommonAct {
	private String TAG = DailyMenuActivity.class.getName();
	private CircleImageView prowork;
	
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
		tview.setBackgroundResource(R.drawable.daily_title);

		setContentView(R.layout.activity_daily);

		CircleLayoutTwo circletwo = (CircleLayoutTwo) findViewById(R.id.daily_layout);


		String NOTICE = getIntent().getStringExtra("office");
		CircleImageView notice = (CircleImageView) circletwo
				.findViewById(R.id.pronotice_image);
		if ("notice".equals(NOTICE)) {
//			notice.setBackgroundResource(R.drawable.icon_noticexz);
		}
		notice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if (!PermissHelp.isPermiss("036")) {
//					PermissHelp.showToast(activity);
//					return;
//				}
				Intent intent = new Intent();
				intent.setClass(DailyMenuActivity.this, DailyActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});

		String WORK = getIntent().getStringExtra("office");
		prowork = (CircleImageView) circletwo.findViewById(R.id.prowork_image);
		if ("work".equals(WORK)) {
			prowork.setBackgroundResource(R.drawable.icon_workxz);
		}
		prowork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (!PermissHelp.isPermiss("028")) {
					PermissHelp.showToast(activity);
					return;
				}
				Intent intent = new Intent();
				intent.setClass(DailyMenuActivity.this, DailyActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});

		String PROONLINE = getIntent().getStringExtra("office");
		CircleImageView proonline = (CircleImageView) circletwo
				.findViewById(R.id.proonline_image);
		if ("online".equals(PROONLINE)) {
//			notice.setBackgroundResource(R.drawable.icon_noticexz);
		}
		proonline.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
//				if (!PermissHelp.isPermiss("035")) {
//					PermissHelp.showToast(activity);
//					return;
//				}
				Intent intent = new Intent();
				intent.setClass(DailyMenuActivity.this, DailyActivity.class);
				intent.putExtra("tag", "2");
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
