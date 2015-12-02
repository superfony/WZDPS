package com.epsmart.wzdp.activity.achar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.achar.service.CommonCharActivity;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.search.QueryDialogAchar;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;

/**
 * 网络请求公共的
 * @author fony
 * 
 */

public class CommonBarAct extends CommonCharActivity {

	protected BaseHttpModule httpModule;
	protected CommonBarAct activity;
	protected RelativeLayout search_lay;// 查询按钮
	protected Button search_btn;// 查询按钮
	protected QueryDialogAchar queryDialog;
	protected QueryDialogListener listener;
	protected AppContext app_context;
	protected PaginationWidget<WorkOrder> paginationWidget = null;
	protected RequestPram requestPram;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		initSearchBtn();
	}
	
	protected void initSearchBtn(){
		
		app_context = (AppContext) activity.getApplication();
		search_lay = (RelativeLayout) getActionBar()
				.getCustomView().findViewById(R.id.search_lay);
		search_btn = (Button) getActionBar().getCustomView()
				.findViewById(R.id.search_btn);
	}
	
	
	
	protected OnClickListener searchLis=new OnClickListener() {
		@Override
		public void onClick(View v) {
			queryDialog = new QueryDialogAchar(activity, listener);// TODO
			queryDialog.show(v);
		}
	};

	protected void initNet() {
		if (httpModule == null) {
			httpModule = new BaseHttpModule(this);
			httpModule.init();
			httpModule.setRequestListener(requestListener);
			httpModule.setServiceNameSpace(RequestParamConfig.serviceNameSpace);
			httpModule.setServiceUrl(RequestParamConfig.ServerUrl);
		}
	}

	protected ProgressDialog progressDialog;
	/* 处理网络错误 */
	protected RequestListener requestListener = new RequestListener() {
		@Override
		public void onSuccess(Response response) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			httpModule.processResponse(httpModule, response,
					getXmlParser(response), httpModule.getResponseProcess());
		}

		@Override
		public void onFail(Exception e) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			String msg = "未知错误";
			if (e instanceof HttpException) {
				HttpException he = (HttpException) e;
				msg = he.getMessage();
			}

			Message obtainMessage = mHandler.obtainMessage(0);
			obtainMessage.obj = msg;
			obtainMessage.sendToTarget();
		}
	};

	private BaseHandler mHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				Toast.makeText(activity, (String) msg.obj + "",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				StatusEntity se = (StatusEntity) msg.obj;
				Toast.makeText(activity, se.message + "", Toast.LENGTH_LONG)
						.show();
				break;
			default:
				break;

			}
		}
	}

	protected BaseXmlParser getXmlParser(Response response) {
		response.setResponseAsString(response.getResponseAsString().replace(
				"&", "#"));
		return httpModule.getBaseXmlParser(response,
				httpModule.getParseHandler());
	}

	public void showModuleProgressDialog(String title, String msg) {
		progressDialog = ProgressDialog.show(this, title, msg, true);
	}

	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	@Override
	protected void onStart() {
		search_lay.setVisibility(View.VISIBLE);
		super.onStart();
	}
	
	
	@Override
	public void onPause() {
		search_lay.setVisibility(View.INVISIBLE);
		super.onPause();
	}
	
	
}
