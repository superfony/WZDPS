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
import com.epsmart.wzdp.view.CircleLayoutTwo;

/**
 * 合同管理
 * @author fony
 *
 */

public class ContractMenuActivity extends Activity {
	private String TAG = ContractMenuActivity.class.getName();

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
		tview.setBackgroundResource(R.drawable.contract_project);

		setContentView(R.layout.activity_contract);
		CircleLayoutTwo circletwo = (CircleLayoutTwo) findViewById(R.id.contract_layout);
		
		CircleImageView towerPlan = (CircleImageView) circletwo
				.findViewById(R.id.plan_image);// 生产计划
		towerPlan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuActivity.this, ContractMenuPlanActivity.class);
				startActivity(intent);
			}
		});
		
		CircleImageView towerWeekly = (CircleImageView) circletwo
				.findViewById(R.id.progress_image);// 生成进度
		towerWeekly.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuActivity.this, ContractMenuProgressActivity.class);
				startActivity(intent);
			}
		});
		
		CircleImageView Delivery = (CircleImageView) circletwo
				.findViewById(R.id.delivery_image);// 发货计划
		Delivery.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuActivity.this, ContractDelDepActivity.class);
				intent.putExtra("tag", "0");
				startActivity(intent);
			}
		});
		
		CircleImageView Departure = (CircleImageView) circletwo
				.findViewById(R.id.departure_image);// 起运通知
		Departure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuActivity.this, ContractDelDepActivity.class);
				intent.putExtra("tag", "1");
				startActivity(intent);
			}
		});
		
		CircleImageView confirm = (CircleImageView) circletwo
				.findViewById(R.id.confirm_image);// 到货确认
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(ContractMenuActivity.this, ContractDelDepActivity.class);
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
