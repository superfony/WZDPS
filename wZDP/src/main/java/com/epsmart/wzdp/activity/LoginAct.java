package com.epsmart.wzdp.activity;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.bean.User;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.util.Pub_method;
import com.epsmart.wzdp.view.SwitchButton;

public class LoginAct extends Activity {
	protected ProgressDialog pDialog;
	private SwitchButton switch_btn;
	private String username;
	private String userpwd;
	private String userpwdMd5;
	private EditText user_text, text_pwd;
	protected int systemID;
	protected String deviceID;
	private Activity activity;
	protected BaseHttpModule httpModule;
	protected User user;
	private AppContext appContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		activity = this;
		appContext = (AppContext) getApplication();
		// 网络连接判断
		appContext.isOnline = appContext.isNetworkConnected();// 网络可用true
		if (!appContext.isOnline)
			Toast.makeText(activity, "当前网络不可用，进入离线模式！", Toast.LENGTH_LONG)
					.show();
		
		//UpdateManager.getUpdateManager().checkAppUpdate(this, false);// 检查是否更新

		user_text = (EditText) findViewById(R.id.user_text);
		text_pwd = (EditText) findViewById(R.id.text_pwd);
		switch_btn = (SwitchButton) findViewById(R.id.switch_btn);
		ImageView loginBt = (ImageView) findViewById(R.id.button_login);

		String state = PerferenceModel.getPM(LoginAct.this).getValue("state",
				"");// 记录开关状态
		if (state != null && state.equals("rember")) {
			switch_btn.setChecked(true);
			username = PerferenceModel.getPM(LoginAct.this).getValue(
					"username", "");
			userpwd = PerferenceModel.getPM(LoginAct.this).getValue("userpwd",
					"");
			user_text.setText(username);
			text_pwd.setText(userpwd);
		}
		switch_btn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
			}
		});
		loginBt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showModuleProgressDialog("提示", "登录中请稍后......");
				username = user_text.getText().toString().trim();
				userpwd = text_pwd.getText().toString().trim();
				userpwdMd5 = Pub_method.Md5(userpwd).toLowerCase();
				deviceID = Pub_method.getDeviceID(LoginAct.this);
				System.out.println("devices.....deviceID=" + deviceID);
				Log.d("deviceid", "........." + deviceID);
				if (username.equals("") || userpwd.equals("")) {
					toast(activity, "用户名、密码不能为空,请输入!");
					closeDialog();
					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						Looper.prepare();
						sendLogin(username, userpwdMd5, deviceID, 1004);
					}
				}).start();
			}
		});
	}

	private void sendLogin(String userName, String userpwdMd5, String deviceID,
			int systemID) {
		// Client client = new Client();
		// rbp = client.getLoginInfo(userName, userpwdMd5, deviceID, systemID);
		user = new User();
		user.setFace(username);
		user.setPwd(userpwd);
		appContext.user=user;
		RequestAction requestAction = new RequestAction();
		requestAction.putParam("systemID", "1004");
		requestAction.putParam("deviceID", deviceID);
		requestAction.putParam("userName", userName);
		requestAction.putParam("userpwdMd5", userpwdMd5);
		init(requestAction);

	}

	/**
	 * 登录成功后的处理
	 */

	private void loginsetting(User user) {

		if (switch_btn.isChecked())// 检测用户名密码
		{
			PerferenceModel.getPM(LoginAct.this).insertPreference("username",
					user.getFace());
			PerferenceModel.getPM(LoginAct.this).insertPreference("userpwd",
					user.getPwd());
			PerferenceModel.getPM(LoginAct.this).insertPreference("deviceID",
					deviceID);
			PerferenceModel.getPM(LoginAct.this).insertPreference("systemID",
					Integer.toString(systemID));
			PerferenceModel.getPM(LoginAct.this).insertPreference("state",
					"rember");
		} else {
			PerferenceModel.getPM(LoginAct.this)
					.insertPreference("userpwd", "");
			PerferenceModel.getPM(LoginAct.this).insertPreference("deviceID",
					"");
			PerferenceModel.getPM(LoginAct.this).insertPreference("systemID",
					"");
			PerferenceModel.getPM(LoginAct.this).insertPreference("state", "");
		}
		Intent intent = new Intent();
		intent.setClass(LoginAct.this, MainActivity.class);
		startActivity(intent);
		LoginAct.this.finish();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			closeDialog();
			Toast.makeText(activity, "登录失败！", Toast.LENGTH_LONG).show();
		}
	};

	@Override
	public void onBackPressed() {

		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

	public void toast(Activity activity, String msg) {
		Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
	}

	// webservice登录方式
	private void init(RequestAction requestAction) {
		httpModule = new BaseHttpModule(activity);
		httpModule.init();
		httpModule.setRequestListener(requestListener);
		httpModule
				.setServiceNameSpace(RequestParamConfig.servicenamespacelogin);
		httpModule.setServiceUrl(RequestParamConfig.loginUrl);
		requestAction.isPageBeanEnable = false;
		requestAction.serviceName = RequestParamConfig.loginname;
		if (((AppContext) activity.getApplication()).isOnline){
			httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
					null, RequestType.WEBSERVICE);
		}else {
			String username = PerferenceModel.getPM(LoginAct.this).getValue(
					"username", "");
			String pwd = PerferenceModel.getPM(LoginAct.this).getValue(
					"userpwd", "");
			if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd)) {
				if (user.getFace().equals(username)
						&& user.getPwd().equals(pwd)) {

					String uid = PerferenceModel.getPM(LoginAct.this).getValue(
							"uuid_my", "");
					mHandler.obtainMessage(1, uid).sendToTarget();
				} else {
             String msg= "用户名和密码校验失败，请连接网络后重新登录！";
					mHandler.obtainMessage(3,msg).sendToTarget();
					
				}
			} else {
				 String msg= "首次登录或密码修改后请连接网络进行初始登录！";
					mHandler.obtainMessage(3,msg).sendToTarget();
			}
		}
	}

	protected ProgressDialog progressDialog;
	/* 处理网络错误 */
	protected RequestListener requestListener = new RequestListener() {
		@Override
		public void onSuccess(Response response) {
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			// 处理返回的数据 解析json数据
			try {
				if (response.getResponseAsString() == null) {
					mHandler.obtainMessage(0, "返回数据为空！").sendToTarget();
					return;
				}
				JSONObject jsonObj = response.asJSONObject();
				Log.w("......jsonObj..>",jsonObj.toString());
				String success = jsonObj.get("success").toString();
				if ("false".equals(success)) {
					String msg=jsonObj.getString("fault").toString();
					
					mHandler.obtainMessage(3,msg).sendToTarget();
				} else {
					String uid = jsonObj.getJSONObject("userInfo").getString(
							"name");
					mHandler.obtainMessage(1, uid).sendToTarget();
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
			if (progressDialog != null && progressDialog.isShowing())
				progressDialog.dismiss();
			switch (msg.what) {
			case 0:
				Toast.makeText(activity, (String) msg.obj + "",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				String uuid = (String) msg.obj;
				appContext.user.setUid(Integer.parseInt(uuid));
				PerferenceModel.getPM(LoginAct.this).insertPreference(
						"uuid_my", uuid);
				loginsetting(appContext.user);
				break;
			case 3:
				String error=(String)msg.obj;
				Toast.makeText(LoginAct.this, error,
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	}
}
