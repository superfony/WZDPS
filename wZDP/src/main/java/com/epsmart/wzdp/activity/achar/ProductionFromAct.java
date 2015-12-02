package com.epsmart.wzdp.activity.achar;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.achar.service.CharResponse;
import com.epsmart.wzdp.activity.fragment.CommonFragment.WZDPTYPE;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.search.QuerySuppliesDialogAchar;
import com.epsmart.wzdp.activity.ui.IndicatorFragmentActivity;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 物资供应综合分析
 * 
 * @author fony
 * 
 * 
 */

public class ProductionFromAct extends IndicatorFragmentActivity {
	protected QuerySuppliesDialogAchar querySupDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.production_cs);
		
		ScrollView sv = new ScrollView(activity);
		sv.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		sv.setFillViewport(true);

		LinearLayout lay = new LinearLayout(activity);
		lay.setOrientation(LinearLayout.VERTICAL);
		lay.setBackgroundColor(Color.WHITE);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		sv.addView(lay, params);
		//setContentView(sv);

		search_lay.setOnClickListener(searchLiss);
		search_btn.setOnClickListener(searchLiss);
		showLinelay = new LinearLayout(activity);
		((LinearLayout) showLinelay).setOrientation(LinearLayout.VERTICAL);
		lay.addView(showLinelay, params);
		
		
		initNet();
		loadData(new RequestPram());

	}
	
	@Override
	protected void onStart() {
		super.onStart();
		app_context.wzdpType=WZDPTYPE.ProductionFromAct;
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	/** 数据请求 */
	public void loadData(RequestPram requestPram) {
		    requestAction = new RequestAction();
			requestAction.reset();
			requestPram.bizId=1004;
			requestPram.password="password";
			requestPram.pluginId=119;
			requestPram.userName=appContext.user.getUid();
			
			requestPram.methodName=RequestParamConfig.materialsupply;
			
			requestAction.setReqPram(requestPram);
			super.loadData(requestPram);
	}
	{
		listener = new QueryDialogListener() {
			@Override
			public void doQuery(String req) {
				RequestPram	requestPram=new RequestPram(); 
				requestPram.param = req;
				loadData(requestPram);
				
			}
		};
	}
	protected OnClickListener searchLiss=new OnClickListener() {
		@Override
		public void onClick(View v) {
			querySupDialog = new QuerySuppliesDialogAchar(activity, listener);// TODO
			querySupDialog.show(v);
		}
	};
	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof CharResponse) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			}
		}
	}


OnClickListener itemsOnClick=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};
}
