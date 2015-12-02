package com.epsmart.wzdp.activity.achar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationSet;
import android.widget.ImageView;

import com.epsmart.wzdp.R;

/**
 * 在线统计功能
 * 
 * @author fony
 * 
 */
public class DoStatisticsAct extends Activity {
	private AnimationSet mAnimationSet;
	private String type;

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
		tview.setBackgroundResource(R.drawable.online_statistics);

		setContentView(R.layout.online_analysis);
		ImageView signing = (ImageView) findViewById(R.id.online_signing);

		signing.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DoStatisticsAct.this,
						SubstanceaNalysisActNew.class);
				intent.putExtra("title", "合同签订统计报表");
				DoStatisticsAct.this.startActivity(intent);
			}
		});

		ImageView material = (ImageView) findViewById(R.id.online_material);
		material.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DoStatisticsAct.this, ProductionFromAct.class);
				intent.putExtra("title", "物资供应综合分析报表");
				DoStatisticsAct.this.startActivity(intent);

			}
		});

		ImageView settlements = (ImageView) findViewById(R.id.online_settlements);
		settlements.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DoStatisticsAct.this, SettleCharAct.class);
				intent.putExtra("title", "合同结算情况统计报表");
				DoStatisticsAct.this.startActivity(intent);
			}
		});

		ImageView schedule = (ImageView) findViewById(R.id.online_schedule);
		schedule.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DoStatisticsAct.this, ContractLineCharAct.class);
				intent.putExtra("title", "项目进度情况统计报表");
				DoStatisticsAct.this.startActivity(intent);
			}
		});

		ImageView plan = (ImageView) findViewById(R.id.online_plan);
		plan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				type = "plan";
				System.out.println(",...................??????" + type);
				Intent intent = new Intent();
				intent.putExtra("type", type);
				intent.setClass(DoStatisticsAct.this, PlanListAct.class);// 铁塔排产计划统计分析数据
				DoStatisticsAct.this.startActivity(intent);
			}
		});

		ImageView weekly = (ImageView) findViewById(R.id.online_progress);
		weekly.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				type = "report";
				Intent intent = new Intent();
				intent.putExtra("type", type);
				intent.setClass(DoStatisticsAct.this, PlanListAct
				// ProgressListAct
						.class);// 铁塔生产进度周报信息统计数据
				DoStatisticsAct.this.startActivity(intent);
			}
		});

		// 测试
		// test();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
