package com.epsmart.wzdp.activity.more;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;

public class OffLineCommonAct extends Activity {

	protected BaseHttpModule httpModule;
	protected OffLineCommonAct activity;
	protected RequestPram requestPram;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		initNet();
//		initActionBar();
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
	
		/* 处理表单提交返回消息 */
		class ProcessResponse implements ModuleResponseProcessor {
			@Override
			public void processResponse(BaseHttpModule httpModule, Object parseObj) {
				if (parseObj instanceof StatusEntity) {
					mHandler.obtainMessage(1, parseObj).sendToTarget();
				}
			}
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
}
