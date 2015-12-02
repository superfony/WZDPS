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
 * 计划排产
 */

public class ContractMenuPlanActivity extends Activity {
	private String TAG = ContractMenuPlanActivity.class.getName();

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
		tview.setBackgroundResource(R.drawable.title_plans);// 排产计划

		setContentView(R.layout.activity_contractplan);
		CircleLayoutThree circlethree = (CircleLayoutThree) findViewById(R.id.contract_layout);

		CircleImageView tower = (CircleImageView) circlethree
				.findViewById(R.id.tower_pl);// 铁塔排产计划
		tower.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});

		CircleImageView Porcelain = (CircleImageView) circlethree
				.findViewById(R.id.Porcelain_pl);// 瓷绝缘子排产计划
		Porcelain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});

		CircleImageView composite = (CircleImageView) circlethree
				.findViewById(R.id.composite_pl);// 复合绝缘子排产计划
		composite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
				intent.putExtra("tag", "2");
				startActivity(intent);
			}
		});

		CircleImageView wireway = (CircleImageView) circlethree
				.findViewById(R.id.wireway_pl);// 导线排产计划
		wireway.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
				intent.putExtra("tag", "3");
				startActivity(intent);
			}
		});

		CircleImageView ground = (CircleImageView) circlethree
				.findViewById(R.id.ground_pl);// 地线排产计划
		ground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
				intent.putExtra("tag", "4");
				startActivity(intent);
			}
		});

		CircleImageView cable = (CircleImageView) circlethree
				.findViewById(R.id.cable_pl);// 光缆排产计划
		cable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
				intent.putExtra("tag", "5");
				startActivity(intent);
			}
		});

		CircleImageView fittings = (CircleImageView) circlethree
				.findViewById(R.id.fittings_pl);// 金具排产计划
		fittings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuPlanActivity.this,
						ContractPlanActivity.class);
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
