package com.epsmart.wzdp.activity.dailyoffice.fragment;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.dailyoffice.DailyActivity;
import com.epsmart.wzdp.activity.dailyoffice.DailyCommonAct;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyDerailHandler;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyDetail;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 通知详情
 */
public class DailyNoticeAct extends DailyCommonAct {

	private TextView details, details_content;
	private TextView noticeWeb;
	private ImageView detail_down;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.daily_details);
		init();
		loadData();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode==KeyEvent.KEYCODE_BACK){
			 Intent intent=new Intent(activity,DailyActivity.class);
				activity.setResult(1,intent);
				activity.finish();
		 }
		return super.onKeyDown(keyCode, event);
	}
	
	private void init() {
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.notice_title);

		details = (TextView) findViewById(R.id.details);
		details_content = (TextView) findViewById(R.id.details_content);
		noticeWeb = (TextView) findViewById(R.id.web_down);
		detail_down = (ImageView)findViewById(R.id.detail_down);

	}

	private void loadData() {
		requestAction = new RequestAction();
		RequestPram requestPram = new RequestPram();
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestPram.methodName = RequestParamConfig.getNoticeDetail;
		requestPram.password = getIntent().getStringExtra("noticeId");
		requestPram.userName = appContext.user.getUid();
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new DailyDerailHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}
	
	//protected ProgressDialog progressDialog=null;

	public void showModuleProgressDialog(String title, String msg) {
		progressDialog = ProgressDialog.show(this.activity, title, msg, true);
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
			closeDialog();
			if (parseObj instanceof DailyDetail) {
				DailyDetail resp = ((DailyDetail) parseObj);
				mHandler.obtainMessage(0, resp).sendToTarget();
			}
		}
	}

	private BaseHandler mHandler = new BaseHandler();

	private class BaseHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			final DailyDetail resp = ((DailyDetail) (msg.obj));
			details.setText(resp.noticetitle);
			String content = resp.noticedetail;
			if(!TextUtils.isEmpty(content) && content.contains("*#*")){
				content = content.replace("*#*", "*#*      ");
				content = content.replace("*#*", "\n\r");
				content = "      "+content;
				details_content.setText(content);
			}else{
				details_content.setText(content);
			}
			if(!TextUtils.isEmpty(resp.fileurl)){
				detail_down.setVisibility(View.VISIBLE);
			noticeWeb.setText("http://" + RequestParamConfig.IP +":"+RequestParamConfig.PORT+"/"
					+ resp.fileurl);
			}else{
				noticeWeb.setVisibility(View.INVISIBLE);
			}
		}
	}
	
}
