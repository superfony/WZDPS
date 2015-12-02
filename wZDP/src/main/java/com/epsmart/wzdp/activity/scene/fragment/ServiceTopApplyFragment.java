package com.epsmart.wzdp.activity.scene.fragment;

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
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandler;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
/**
 * 
 * @author hqq
 *
 */
public class ServiceTopApplyFragment extends SceneFragmemt{
	
	private static final String CommonActivity = null;
	private Activity activity;
	private View view;
	protected RequestAction requestAction = null;
	protected WorkOrder workOrder;
	private String TAG = ServiceTopApplyFragment.class.getName();
	@Override
	public void onAttach(Activity activity) {
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.service_top_apply, container, false);
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
		if(response == null){
			loadData(requestPram);
		}else{
			initTable(response.entity);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}


    /**数据请求*/
	public void loadData(RequestPram param) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestAction = new RequestAction();
		requestAction.reset();
		//getTableShow：从中间件获取表单
		param.methodName = "getTableShow";
		requestAction.setReqPram(param);
		requestAction.putParam("txt", "showTable_log.txt");//从本地获取表单（webservice）
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
	
	
	//设置请求参数
	protected void submitMethod(RequestPram requestPram) {
		//getSubmitMsg：从中间获取提交车成功数据
		requestPram.methodName="getSubmitMsg";
		requestPram.param=fillHelp.getparams();
		super.submitMethod(requestPram);
		
	}

	@Override
	protected void cancelMethod() {
		super.cancelMethod();
	}



	private FlowHandler mHandler = new FlowHandler();
	private BasicResponse response;

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
		initTable( response.entity);
	}

	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	/*private View view;
	private String TAG = ServiceTopApplyFragment.class.getName();
	
	@Override
	public void onAttach(Activity activity) {
		Log.i(TAG, "....MenuOneQuft..onAttach.>>");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "....MenuOneQuft..onCreate.>>");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);
		}
		  view = inflater.inflate(R.layout.service_top_apply,
				container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(TAG, "......onActivityCreated.>>");
		super.onActivityCreated(savedInstanceState);
	}
	


	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.i(TAG, "......onSaveInstanceState.>>");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.i(TAG, "......onViewCreated.>>");
		super.onViewCreated(view, savedInstanceState);
	}*/
}
