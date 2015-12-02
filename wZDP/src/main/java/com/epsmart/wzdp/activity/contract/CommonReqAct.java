package com.epsmart.wzdp.activity.contract;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.search.QueryDialogAchar;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;

/**
 * 网络请求公共的
 */

public class CommonReqAct extends Activity {

	protected BaseHttpModule httpModule;
	protected CommonReqAct activity;
	protected RelativeLayout search_lay;// 查询按钮
	protected Button search_btn;// 查询按钮
	protected QueryDialogAchar queryDialog;
	protected QueryDialogListener listener;
	protected AppContext app_context;
	protected PaginationWidget<WorkOrder> paginationWidget = null;
	protected RequestPram requestPram;
	protected AppContext appContext;
	protected RequestAction requestAction;

	public enum WZDPTYPES {

		tower_pro,// 铁塔生产进度
		porcelain_pro,// 瓷绝缘子生产进度
		reunite_pro,// 复合绝缘子生产进度
		wireway_pro,// 导线生产进度
		ground_pro,// 地线生产进度
		cable_pro,// 光缆生产进度
		fittings_pro,// 金具生产进度
		tower_plan,// 铁塔排产计划
		porcelain_plan,// 瓷绝缘子排产计划
		reunite_plan,// 复合绝缘子排产计划
		wireway_plan,// 导线排产计划
		ground_plan,// 地线排产计划
		cable_plan,// 光缆排产计划
		fittings_plan// 金具排产计划
	}

	private String vers;

	public String getVers() {
		return vers;
	}

	public void setVers(String vers) {
		this.vers = vers;
	}

	private String pspid;

	public String getPspid() {
		return pspid;
	}

	public void setPspid(String pspid) {
		this.pspid = pspid;
	}
	private String types;

	/**
	 * @return the types
	 */
	public String getTypes() {
		return types;
	}

	/**
	 * @param types the types to set
	 */
	public void setTypes(String types) {
		this.types = types;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		appContext = (AppContext) getApplication();
		// initSearchBtn();
		initActionBar();
	}

	protected void initSearchBtn() {

	}

	public void initActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		View view = actionBar.getCustomView();
		RelativeLayout exitBt = (RelativeLayout) view
				.findViewById(R.id.add_lay);

	}

	protected OnClickListener searchLis = new OnClickListener() {
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
			search_lay.setVisibility(View.VISIBLE);
		super.onPause();
	}

}
