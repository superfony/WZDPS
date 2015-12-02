package com.epsmart.wzdp.activity.achar;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.achar.service.CharResponse;
import com.epsmart.wzdp.activity.fragment.CommonFragment.WZDPTYPE;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.search.QueryProjectDialogAchar;
import com.epsmart.wzdp.activity.ui.IndicatorFragmentActivity;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 项目进度情况统计表格
 */

public class ContractLineCharAct extends IndicatorFragmentActivity {
	protected QueryProjectDialogAchar queryProDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.contract_cs);
		
		showLinelay = new ScrollView(activity);
		showLinelay.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		((ScrollView) showLinelay).setFillViewport(true);

		search_lay.setOnClickListener(searchLisp);
		search_btn.setOnClickListener(searchLisp);
		initNet();
		loadData(new RequestPram());

	}

	
	@Override
	protected void onStart() {
		super.onStart();
		app_context.wzdpType=WZDPTYPE.ContractLineCharAct;
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
			requestPram.methodName=RequestParamConfig.projectmilestone;
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
	protected OnClickListener searchLisp=new OnClickListener() {
		@Override
		public void onClick(View v) {
			queryProDialog = new QueryProjectDialogAchar(activity, listener);// TODO
			queryProDialog.show(v);
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

}
