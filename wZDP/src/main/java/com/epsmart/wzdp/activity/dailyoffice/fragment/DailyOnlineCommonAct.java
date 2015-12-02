package com.epsmart.wzdp.activity.dailyoffice.fragment;

import android.app.ActionBar;
import android.app.ExpandableListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.dailyoffice.DailyActivity;
import com.epsmart.wzdp.activity.fragment.FillTableHelpNew;
import com.epsmart.wzdp.activity.search.QueryDialog;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;

// 在线交流 FragmentActivity 管理类

public class DailyOnlineCommonAct extends ExpandableListActivity {
	protected DailyOnlineCommonAct activity;
	protected BaseHttpModule httpModule;
	protected AppContext appContext;
	protected RelativeLayout search_lay;// 提交布局
	protected Button search_btn;// 提交按钮
	protected RequestPram requestPram;
	protected QueryDialog queryDialog;
	protected ProgressDialog progressDialog;
	protected FillTableHelpNew fillHelpNew;
	protected RequestAction requestAction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		appContext = (AppContext) getApplication();
		initActionBar();
		initNet();
	}

	public void initActionBar() {
		Log.i("", "...........initActionBar................>>");
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(R.layout.actionbar_main);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		View view = actionBar.getCustomView();

	}

	protected void initNet() {
		if (httpModule == null) {
			httpModule = new BaseHttpModule(this);
			httpModule.init();
			httpModule.setRequestListener(requestListener);
			httpModule.setServiceNameSpace(RequestParamConfig.serviceNameSpace);
			httpModule.setServiceUrl(RequestParamConfig.ServerUrl);
		}
	}

	protected OnClickListener clickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.search_btn) {
				submitMethod(requestPram);
			} else {
			}

		}
	};

	// 提交
	protected void submitMethod(RequestPram reqPram) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestAction.setReqPram(reqPram);
		httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	protected void cancelMethod() {
	}

	public void showModuleProgressDialog(String title, String msg) {
		progressDialog = ProgressDialog.show(activity, title, msg, true);
	}

	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

	}

	/* 处理表单提交返回消息 */
	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

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

	protected BaseXmlParser getXmlParser(Response response) {
		response.setResponseAsString(response.getResponseAsString().replace(
				"&", "#"));
		return httpModule.getBaseXmlParser(response,
				httpModule.getParseHandler());
	}

	/** 处理网络异常等信息 **/
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
				// 这里进行返回列表进行刷新的操作
				Intent intent=new Intent(activity,DailyActivity.class);
				activity.setResult(1,intent);
				activity.finish();
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
