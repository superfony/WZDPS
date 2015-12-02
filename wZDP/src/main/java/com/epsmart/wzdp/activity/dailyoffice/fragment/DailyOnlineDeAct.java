package com.epsmart.wzdp.activity.dailyoffice.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.dailyoffice.DailyCommonAct;

/*
 * 在线交流详情页面
 */
public class DailyOnlineDeAct extends DailyCommonAct {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_online_detail);
		
		TextView persion = (TextView) findViewById(R.id.persion);
		TextView time = (TextView) findViewById(R.id.time);
		TextView theme = (TextView) findViewById(R.id.theme);
		TextView content = (TextView) findViewById(R.id.content);
		TextView assign_per = (TextView) findViewById(R.id.assign_per);
		
		
		persion.setText(":"+getIntent().getStringExtra("persion"));
		theme.setText(":"+getIntent().getStringExtra("theme"));
		time.setText(":"+getIntent().getStringExtra("time"));
		content.setText(":"+getIntent().getStringExtra("content"));	
		String persions = getIntent().getStringExtra("subper");
//		String[] per = persions.split(",");
		if(!TextUtils.isEmpty(persions) ){
			persions = ":"+persions;
			assign_per.setText(persions);
		}else{
			assign_per.setText(persions);
		}
	}
	
	

}
