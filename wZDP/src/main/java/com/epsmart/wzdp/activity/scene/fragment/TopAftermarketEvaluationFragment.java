package com.epsmart.wzdp.activity.scene.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandler;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
/**
 *申请服务表单信息
 *
 */
public class TopAftermarketEvaluationFragment extends SceneFragmemt{

	private static final String CommonActivity = null;
//	private Activity activity;
	private View view;
	protected RequestAction requestAction = null;
	protected WorkOrder workOrder;
	private BasicResponseNew responseNew;
	private TextView titles;
	private ImageView title_image;
	 String TAG = TopAftermarketEvaluationFragment.class.getName();
	@Override
	public void onAttach(Activity activity) {
	//	this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.top_aftermarket_evaluation, container, false);
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
		present_btn.setOnClickListener(clickLis);
		ActionBar actionBar = activity.getActionBar();
		View view1 = actionBar.getCustomView();
		title_image = (ImageView) view1.findViewById(R.id.title_image);
		title_image.setVisibility(view.GONE);
		titles = (TextView) view1.findViewById(R.id.titles);
//		titles.setText(projectname1);
		String state=getArguments().getString("state");
		
		if(PermissHelp.getUserType("000").equals("5")){
		if(state.equals("待审核")){
//			tview.setText("现场服务联系单");
			present_btn.setText("审核");
//			tview.setText("现场服务联系单");
		}else if(state.equals("已审核")){
			present_btn.setText("修改");
//			tview.setText("现场服务联系单");
		}
		}else if(PermissHelp.getUserType("000").equals("4")){
			 if(state.equals("待审核")){
				 present_btn.setText("修改");
			}else if(state.equals("已审核")){
				present_btn.setVisibility(View.VISIBLE);
				present_btn.setText("确认完成");
//				tview.setText("现场服务评价单");
				question_lay.setVisibility(View.VISIBLE);
				question_btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						cancelsMethod();
					}
				});
			}else if(state.equals("已完成")){
				present_btn.setText("评价");
			}else if(state.equals("已评价")){
				present_btn.setVisibility(View.INVISIBLE);
			}else if(state.equals("新建")){
				present_btn.setText("新建");
			}
			
		}
		
		present_btn.setOnClickListener(clickLis);
		
		if(response == null){
			loadData(requestPram);
		}else{
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


    /**数据请求*/
	public void loadData(RequestPram param) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		requestAction = new RequestAction();
		requestAction.reset();
		
		requestPram.bizId=1004;
		requestPram.password="password";
		requestPram.pluginId=119;
		requestPram.userName=appContext.user.getUid();
		
		requestPram.methodName=RequestParamConfig.serviceDownload;
		
		requestPram.param=getArguments().getString("reqParam");
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
	
	
	//设置请求参数
	protected void submitMethod(RequestPram requestPram) {
	
		requestPram.bizId=1004;
		requestPram.password="password12";
		requestPram.pluginId=119;
		requestPram.userName=appContext.user.getUid();
		requestPram.methodName =RequestParamConfig.serviceRequireUpload;
		requestPram.param = fillHelpNew.getparams(getArguments().getString("reqP")
				+RequestXmlHelp.getReqXML("user_type", PermissHelp.getUserType("000")));
		super.submitMethod(requestPram);
	}

	
	protected void cancelsMethod() {
		TopQuestionFeedBackFragment topquest = new TopQuestionFeedBackFragment();
		Bundle bundle = new Bundle();
		bundle.putString("reqParam",getArguments().getString("reqParam"));
		bundle.putString("reqP",getArguments().getString("reqP"));
		topquest.setArguments(bundle);
		Common.replaceRightFragment(activity, topquest, false,
				R.id.content);
	}



	private FlowHandler mHandler = new FlowHandler();
	private BasicResponse response;

	private class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			closeDialog();
			if (msg.what == 0) {
				onSuccessNew((BasicResponseNew) msg.obj);
			}else if (msg.what == 1) {
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

	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;
		
		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1)
			return;
		String title=response.basicEntityList.get(1).fields.get("projectname").fieldContent;
		titles.setText(title);
		initTableNew(response.basicEntityList);
	}
	
	@Override
	public void onPause() {
		present_lay.setVisibility(View.GONE);
		question_lay.setVisibility(View.GONE);
//		title_image.setVisibility(view.VISIBLE);
//		titles.setVisibility(view.GONE);
		super.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
