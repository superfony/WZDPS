package com.epsmart.wzdp.activity.contract;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.view.CircleImageView;
import com.epsmart.wzdp.view.CircleLayoutThree;

/**
 * 生产进度
 */

public class ContractMenuProgressActivity extends Activity {
	private String TAG = ContractMenuProgressActivity.class.getName();

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
		 tview.setBackgroundResource(R.drawable.title_progress);//生产进度

		setContentView(R.layout.activity_contractprogress);
		CircleLayoutThree circlethree = (CircleLayoutThree) findViewById(R.id.contract_layout);

		CircleImageView tower = (CircleImageView) circlethree
				.findViewById(R.id.tower_pro);// 铁塔生产进度
		tower.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});

		CircleImageView Porcelain = (CircleImageView) circlethree
				.findViewById(R.id.Porcelain_pro);// 瓷绝缘子生产进度
		Porcelain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});

		CircleImageView composite = (CircleImageView) circlethree
				.findViewById(R.id.composite_pro);// 复合绝缘子生产进度
		composite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "2");
				startActivity(intent);
			}
		});

		CircleImageView wireway = (CircleImageView) circlethree
				.findViewById(R.id.wireway_pro);// 导线生产进度
		wireway.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "3");
				startActivity(intent);
			}
		});

		CircleImageView ground = (CircleImageView) circlethree
				.findViewById(R.id.ground_pro);// 地线生产进度
		ground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "4");
				startActivity(intent);
			}
		});

		CircleImageView cable = (CircleImageView) circlethree
				.findViewById(R.id.cable_pro);// 光缆生产进度
		cable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "5");
				startActivity(intent);
			}
		});

		CircleImageView fittings = (CircleImageView) circlethree
				.findViewById(R.id.fittings_pro);// 金具生产进度
		fittings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuProgressActivity.this,
						ContractActivity.class);
				intent.putExtra("tag", "6");
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
