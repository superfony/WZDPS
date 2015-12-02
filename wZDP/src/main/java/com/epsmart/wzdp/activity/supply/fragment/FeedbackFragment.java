package com.epsmart.wzdp.activity.supply.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
/*
 * 待反馈列表下的表单的Fragment
 * author mat
 */
public class FeedbackFragment extends SupplyFragmemt {

	private static final String CommonActivity = null;
	private Activity activity;
	private View view;
	protected WorkOrder workOrder;
	private BasicResponse response;
	private BasicResponseNew responseNew;

	// 初始化分页标签

	 String TAG = FeedbackFragment.class.getName();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.supply_feedback, container, false);
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
	
		present_btn.setOnClickListener(clickLis);

		String state = getArguments().getString("state");
		if (PermissHelp.getUserType("000").equals("5")) {// 监理公司管理人员
			if (state.equals("待审核")) {
				present_btn.setText("审核");
			} else if (state.equals("已审核")) {
				present_btn.setText("修改");
			}
		} else if (PermissHelp.getUserType("000").equals("2")) {// 监造师
			if (state.equals("新建")) {
				present_btn.setText("新建");
			} else if (state.equals("待审核")) {
				present_btn.setText("修改");
			}
		}
//		else if (PermissHelp.getUserType("000").equals("6")) {// 监造师
//				present_btn.setVisibility(view.GONE);;
//		} 
		else{
			present_btn.setText("提交");
		}

		if (response == null) {
			loadData(requestPram);
		} else {
			initTable(response.entity);
		}

	}
	
	@Override
	public void onResume() {
		super.onResume();
		present_lay.setVisibility(View.VISIBLE);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	protected RequestAction requestAction = null;

	// 数据请求
	public void loadData(RequestPram requestPram) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestAction = new RequestAction();
		requestAction.reset();

		requestPram.bizId = 1004;
		requestPram.password = "password";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.qualityIssueDownload;
		requestPram.param = getArguments().getString("reqParam");
		requestAction.setReqPram(requestPram);
		

		httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			
			if (parseObj instanceof BasicResponseNew) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			}else if (parseObj instanceof BasicResponse) {
				mHandler.obtainMessage(1, parseObj).sendToTarget();
			}
		}

	}

	// 设置请求参数
	protected void submitMethod(RequestPram requestPram) {
		/** getSubmitMsg：获取提交 */
		requestPram.bizId = 1004;
		requestPram.password = "";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.qualityIssueUpload;
		requestPram.param = fillHelpNew
				.getparams(getArguments().getString("reqP")+RequestXmlHelp.getReqXML("user_type", PermissHelp.getUserType("000")));
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
				onSuccessNew((BasicResponseNew) msg.obj);// 新表单 模块化显示
			}else if(msg.what==1){
				onSuccess((BasicResponse) msg.obj);// old表单的样式
			}
		}
	}

	private void onSuccess(BasicResponse response) {
		this.response = response;
		if (null == response.entity || response.entity.rows.size() < 1)
			return;
		initTable(response.entity);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	
	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;
		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1)
			return;
		initTableNew(response.basicEntityList);
	}
	
	@Override
	public void onPause() {
		present_lay.setVisibility(View.GONE);
		super.onPause();
	}
}

