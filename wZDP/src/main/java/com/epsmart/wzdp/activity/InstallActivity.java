package com.epsmart.wzdp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.common.CommonRegex;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.util.Pub_method;

public class InstallActivity extends Activity {
	private EditText oldpwd, newpwd, reapwd;
	private Button exit_cancel, drop_out;
	protected int systemID;
	private String userpwd;
	private String userpwdMd5;
	private String newPwd;
	private String newPwdMd5;
	private String reaPwd;
	private String userName;
	protected BaseHttpModule httpModule;
	private Activity activity;
	private ProgressDialog pd;
	private AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.install_lay);
		activity = this;
		appContext = (AppContext) getApplication();
		oldpwd = (EditText) findViewById(R.id.oldpwd);
		newpwd = (EditText) findViewById(R.id.newpwd);
		reapwd = (EditText) findViewById(R.id.reapwd);
		drop_out = (Button) findViewById(R.id.drop_out);
		exit_cancel = (Button) findViewById(R.id.exit_cancel);
		exit_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InstallActivity.this.finish();
			}
		});

		drop_out.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				progressDialog = new ProgressDialog(activity).show(activity,
						"提示", "请求中。。。");
				userpwd = oldpwd.getText().toString().trim();
				userpwdMd5 = Pub_method.Md5(userpwd).toLowerCase();
				newPwd = newpwd.getText().toString().trim();
				newPwdMd5 = Pub_method.Md5(newPwd).toLowerCase();
				reaPwd = reapwd.getText().toString().trim();
				if (userpwd.equals("") || newPwd.equals("")
						|| reaPwd.equals("")) {
					Toast.makeText(InstallActivity.this,
							"旧密码、新密码、确认密码不能为空,请输入!", Toast.LENGTH_LONG).show();
					return;
				}
				if (newPwd.equals(userpwd)) {
					Toast.makeText(InstallActivity.this, "新旧密码不能相同，请重新输入!",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!newPwd.equals(reaPwd)) {
					Toast.makeText(InstallActivity.this, "新密码与确认密码不匹配，请重新输入!",
							Toast.LENGTH_LONG).show();

					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						sendLogin(userpwdMd5, newPwdMd5, 1004, userName);
					}
				}).start();
			}
		});

	}

	private void sendLogin(String userpwdMd5, String newPwdMd5, int systemID,
			String userName) {
		RequestAction requestAction = new RequestAction();

		requestAction.putParam("systemID", "1004");
		requestAction.putParam("userName", (appContext.user.getUid()) + "");
		requestAction.putParam("userpwdMd5", userpwdMd5);
		requestAction.putParam("newPwdMd5", newPwdMd5);
		init(requestAction);
	}

	private void init(RequestAction requestAction) {

		httpModule = new BaseHttpModule(activity);
		httpModule.init();
		httpModule.setRequestListener(requestListener);
		httpModule
				.setServiceNameSpace(RequestParamConfig.servicenamespacelogin);
		httpModule.setServiceUrl(RequestParamConfig.loginUrl);
		requestAction.isPageBeanEnable = false;
		requestAction.serviceName = RequestParamConfig.userUpload;
		httpModule.executeRequest(requestAction, new DefaultSaxHandler(), null,
				RequestType.WEBSERVICE);
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Toast.makeText(activity, "修改失败！", Toast.LENGTH_LONG).show();
		}
	};

	protected ProgressDialog progressDialog;
	/* 处理网络错误 */
	protected RequestListener requestListener = new RequestListener() {
		@Override
		public void onSuccess(Response response) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			String ras = response.getResponseAsString();
			try {
				if (ras == null) {
					mHandler.obtainMessage(0, "返回数据库为空！").sendToTarget();
					return;
				}
				String result = CommonRegex.getMiddleValue("result", ras);
				if ("0".equals(result)) {
					mHandler.obtainMessage(0).sendToTarget();
				} else {
					mHandler.obtainMessage(1).sendToTarget();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public void showModuleProgressDialog(String title, String msg) {
		progressDialog = ProgressDialog.show(activity, title, msg, true);
	}

	protected void closeDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/** 处理网络异常等信息 **/
	private BaseHandler mHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				Toast.makeText(activity, "更改成功！", Toast.LENGTH_LONG).show();
				installsetting(newPwd);
				break;
			case 1:

				break;
			default:
				break;
			}
		}

		private void installsetting(String pwd) {
			PerferenceModel.getPM(InstallActivity.this).insertPreference(
					"userpwd", pwd);
			InstallActivity.this.finish();
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	public void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}
