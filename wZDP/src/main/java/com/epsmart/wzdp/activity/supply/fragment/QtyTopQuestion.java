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
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandler;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 质量问题报告Fragment
 * 
 */

public class QtyTopQuestion extends SupplyFragmemt {
	private View view;
	private Activity activity;
	private BasicResponse response;
	protected WorkOrder workOrder;
	private String TAG = QtyTopQuestion.class.getName();
	
	@Override
	public void onAttach(Activity activity) {
		// this.activity = activity;

		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.qty_top_question, container, false);
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
		if (response == null) {
			loadData(requestPram);
		} else {
			initTable(response.entity);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	protected RequestAction requestAction = null;

	public void loadData(RequestPram param) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestAction = new RequestAction();
		requestAction.reset();

		requestPram.bizId=1004;
		requestPram.password="password";
		requestPram.pluginId=119;
		requestPram.userName=appContext.user.getUid();
		
		requestPram.methodName=RequestParamConfig.qualityIssueDownload;
		//bundle.putString("reqParam", RequestXmlHelp.getCommonXML(RequestXmlHelp.getReqXML("purchaseorder", purchaseorder.fieldContent).append(RequestXmlHelp.getReqXML("poitem", poitem.fieldContent)).append(RequestXmlHelp.getReqXML("issueno", Issueno.fieldContent)).append(RequestXmlHelp.getReqXML("releaseflag", Releaseflag.fieldContent)).append(RequestXmlHelp.getReqXML("user_type", "5"))));
		requestPram.param=RequestXmlHelp.getCommonXML(new StringBuffer(getArguments().getString("reqP")+RequestXmlHelp.getReqXML("user_type", "5")));
		requestAction.setReqPram(requestPram);
		
		httpModule.executeRequest(requestAction, new BasicResponseHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof BasicResponse) {
				mHandler.obtainMessage(0, parseObj).sendToTarget();
			}
		}

	}

	// 设置请求数据   新建状态 0 
	protected void submitMethod(RequestPram requestPram) {
		requestPram.bizId=1004;
		requestPram.password="password12";
		requestPram.pluginId=119;
		requestPram.userName=appContext.user.getUid();
		requestPram.methodName =RequestParamConfig.qualityIssueUpload;
		requestPram.param = fillHelp.getparams(getArguments().getString("reqP"));
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}