package com.epsmart.wzdp.activity.scene.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;

/*
 * 工作日志 表单显示
 */
public class ProWorkFragment extends SceneFragmemt {

	private static final String CommonActivity = null;
	private Activity activity;
	private View view;
	protected WorkOrder workOrder;
	private BasicResponse response;
	private BasicResponseNew responseNew;
	private String TAG = ProWorkFragment.class.getName();

	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_table, container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		text_lay = (LinearLayout) view.findViewById(R.id.show_text_lay);
		table_lay = (LinearLayout) view.findViewById(R.id.show_input_lay);
		btn_lay = (LinearLayout) view.findViewById(R.id.show_submit_lay);
		submit_btn = (Button) view.findViewById(R.id.submit_button);
		cancel_btn = (Button) view.findViewById(R.id.cancel_button);
		submit_btn.setOnClickListener(clickLis);
		cancel_btn.setOnClickListener(clickLis);
		present_lay.setVisibility(View.VISIBLE);
		present_btn.setOnClickListener(clickLis);
		if (response == null) {
			loadData(requestPram);
		} else {
			initTable(response.entity);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.work_diary);

	}

	protected RequestAction requestAction = null;

	/** 数据请求 */
	public void loadData(RequestPram param) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.dairyReceiveReq;
		requestPram.param = getArguments().getString("reqParam");
		requestAction.setReqPram(requestPram);

		// httpModule.executeRequest(requestAction, new BasicResponseHandler(),
		// new ProcessResponse(), RequestType.THRIFT);

		httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof BasicResponseNew) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			} else if (parseObj instanceof BasicResponse) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			}
		}
	}

	// 设置请求参数
	protected void submitMethod(RequestPram requestPram) {
		requestPram.bizId = 1004;
		requestPram.password = "password12";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.dairyReceiveUpload;
		requestPram.param = fillHelpNew.getparams(getArguments().getString(
				"reqP"));

		super.submitMethod(requestPram);

	}

	@Override
	protected void cancelMethod() {
		super.cancelMethod();
	}

	private FlowHandler mHandler = new FlowHandler();

	private class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			closeDialog();
			if (msg.what == 0) {
				onSuccessNew((BasicResponseNew) msg.obj);
			} else if (msg.what == 1) {
				onSuccess((BasicResponse) msg.obj);
			}
		}
	}

	private void onSuccess(BasicResponse response) {
		this.response = response;
		if (null == response.entity || response.entity.rows.size() < 1)
			return;
		initTable(response.entity);
	}

	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;
		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1)
			return;
		initTableNew(response.basicEntityList);
	}

	@Override
	public void onResume() {
		super.onResume();
		present_lay.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPause() {
		present_lay.setVisibility(View.GONE);
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
