package com.epsmart.wzdp.activity.supply.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.BasicResponse;
import com.epsmart.wzdp.activity.supply.bean.BasicResponseNew;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.activity.supply.fragment.parser.BasicResponseHandlerNew;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/*
 * 生成过程监控 表单显示
 */

@SuppressLint("NewApi")
public class PDFragment extends SupplyFragmemt {
	private static final String CommonActivity = null;
	private Activity activity;
	private View view;
	private WorkOrder workOrder;
	private BasicResponseNew responseNew;
	private BasicResponse response;
	private LinearLayout time_scroll_lay;
	private TextView logo;

	public static String[] transformer = new String[] { "parts", "fueltank",
			"ironcore", "coil", "insulation", "bodyassemble", "bodydry","assemble","outtest", "quality", "packaging", "shipment" }; // 变压器
	public static String[] electricity = new String[] { "parts", "fueltank",
			"ironcore", "coil", "insulation", "bodyassemble", "bodydry","assemble","outtest", "quality", "packaging", "shipment" }; // 电抗器
	public static String[] circuitBreaker = new String[] { "parts", "bodyinstall", "controlerassemble", "frameassemble",
			"assemble","outtest", "packaging", "shipment" }; // 断路器
	public static String[] combination = new String[] { "parts", "shell", "breakerassemble", "switchassemble", 
		    "arrester", "line", "semimanufacture", "circuit", "fyqjc", "assemble", "outtest", "packaging", "shipment" }; // 组合电器
	public static String[] isolation = new String[] { "parts", "ctxtzp", "zdzuz", "ddzp", "djzzp", 
		    "cdjgzp", "outtest", "packaging", "shipment"}; // 隔离开关

	// 初始化分页标签

	private String TAG = PDFragment.class.getName();

	@Override
	public void onAttach(Activity activity) {

		this.activity = activity;

		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater
				.inflate(R.layout.fragment_supply_info, container, false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Log.i("", ".......onViewCreated....");
		time_scroll_lay = (LinearLayout) view
				.findViewById(R.id.time_scroll_lay);
		logo = (TextView)view.findViewById(R.id.logo);
		text_lay = (LinearLayout) view.findViewById(R.id.show_text_lay);
		table_lay = (LinearLayout) view.findViewById(R.id.show_input_lay);
		btn_lay = (LinearLayout) view.findViewById(R.id.show_submit_lay);
		submit_btn = (Button) view.findViewById(R.id.submit_button);
		cancel_btn = (Button) view.findViewById(R.id.cancel_button);
		submit_btn.setOnClickListener(clickLis);
		cancel_btn.setOnClickListener(clickLis);
		// present_lay.setVisibility(View.VISIBLE);
		if (PermissHelp.getUserType("000").equals("5")) {
			present_btn.setVisibility(View.INVISIBLE);
		}else{
			present_btn.setText("提交");
			present_btn.setOnClickListener(clickLis);
		}
		if (response == null)
			loadData(requestPram);
		else {
			// if (PermissHelp.getUserType("000").equals("1")) {
			// initTableGroup(response.entity);
			// } else
			// initTable(response.entity);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		 ActionBar actionBar = activity.getActionBar();
		 View view = actionBar.getCustomView();
		 ImageView title_image = (ImageView)view.findViewById(R.id.title_image);
		 title_image.setBackgroundResource(R.drawable.product_cs);

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
		requestPram.methodName = RequestParamConfig.procedureDownload;
		requestPram.param = getArguments().getString("reqParam");
		requestAction.setReqPram(requestPram);
		
		httpModule.executeRequest(requestAction, new BasicResponseHandlerNew(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	// 设置请求参数
	protected void submitMethod(RequestPram requestPram) {
		requestPram.bizId = 1004;
		requestPram.password = "password12";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.procedureUpload;
		requestPram.param = fillHelpNew.getparams(getArguments().getString("reqP"));

		super.submitMethod(requestPram);
	}

	@Override
	protected void cancelMethod() {
		super.cancelMethod();
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
		if (PermissHelp.getUserType("000").equals("1")) {
			initTableGroup(response.entity);
		} else
			initTable(response.entity);
	}

	private void onSuccessNew(BasicResponseNew response) {
		this.responseNew = response;
		if (null == response.basicEntityList
				|| response.basicEntityList.size() < 1){
			return;}
		if (PermissHelp.getUserType("000").equals("1")) {
			initTableGroupNew(response.basicEntityList, true);
			time_scroll_lay.setVisibility(View.GONE);
		} else {
			logo.setVisibility(view.VISIBLE);
			initTableNew(response.basicEntityList);
			showTimeScroll(response.basicEntityList.get(5), time_scroll_lay);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		present_lay.setVisibility(View.GONE);
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		present_lay.setVisibility(View.VISIBLE);
	}

	/* 显示时间轴 */
	private void showTimeScroll(BasicEntity entity, LinearLayout time_scroll_lay) {

		HorizontalScrollView hsv = new HorizontalScrollView(activity);
		hsv.setBackgroundResource(R.drawable.time_scroll_bg);
		hsv.setHorizontalScrollBarEnabled(false);

		HorizontalScrollView.LayoutParams lp = new HorizontalScrollView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		time_scroll_lay.addView(hsv, lp);
		LinearLayout lay = new LinearLayout(activity);
		lay.setOrientation(LinearLayout.HORIZONTAL);
		hsv.addView(lay, lp);

		Field field = entity.fields.get("materialtype");
		String showScrollName[] = null;
		if (field == null)
			return;

		String showtype = field.fieldContent.trim();
		if (showtype.equals("变压器")) {
			showScrollName = transformer;
		} else if (showtype.equals("电抗器")) {
			showScrollName = electricity;
		} else if (showtype.equals("断路器")) {
			showScrollName = circuitBreaker;
		} else if (showtype.equals("组合电器")) {
			showScrollName = combination;
		}else if (showtype.equals("隔离开关")) {
			showScrollName = isolation;
		} else {
			showScrollName = new String[0];
		}

		Field fieldnext = null;
		String statenext = null;
		int n = 0;
		for (int i = 0; i < showScrollName.length; i++) {
			field = entity.fields.get(showScrollName[i]);
			if (field == null)
				continue;

			if (i + 1 < showScrollName.length) {
				fieldnext = entity.fields.get(showScrollName[i + 1]);
				if (fieldnext == null)
					continue;
				statenext = fieldnext.fieldContent;
			}

			String title = field.fieldChName;
			String state = field.fieldContent.trim();
			if (state != null && state.contains("#")) {
				state = state.split("#")[0];
			}
			if (TextUtils.isEmpty(state))
				state = "未开始";
//			if (TextUtils.isEmpty(statenext))
//				statenext = "未开始";
			if (state.equals("已完成"))
				state = "1";
			if (state.equals("进行中"))
				state = "2";
			if (state.equals("未开始"))
				state = "0";

			LinearLayout linlay = new LinearLayout(activity);
			linlay.setOrientation(LinearLayout.VERTICAL);
			TextView tv = new TextView(activity);
			tv.setText(title);
			ImageView iv = new ImageView(activity);
			LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			linlay.addView(tv, lps);
			lps.gravity = Gravity.CENTER_HORIZONTAL;
			linlay.addView(iv, lps);
			lay.addView(linlay);

			LinearLayout linarrow = new LinearLayout(activity);
			linarrow.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams lparrow = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ImageView iv_arrow = new ImageView(activity);

			linarrow.addView(iv_arrow);
			lparrow.gravity = Gravity.BOTTOM;
			lay.addView(linarrow, lparrow);

			if ("1".equals(state)) {
				iv.setBackgroundResource(R.drawable.complete);
				iv_arrow.setBackgroundResource(R.drawable.arrow_y);
				tv.setTextColor(Color.GREEN);
			} else if ("2".equals(state)) {
				iv.setBackgroundResource(R.drawable.doing);
				iv_arrow.setBackgroundResource(R.drawable.arrow_no);
				tv.setTextColor(Color.RED);
			} else if ("0".equals(state)) {
				iv.setBackgroundResource(R.drawable.wait);
				iv_arrow.setBackgroundResource(R.drawable.arrow_no);
				tv.setTextColor(Color.GRAY);
			}
			if (i == showScrollName.length - 1)
				iv_arrow.setVisibility(View.INVISIBLE);
		}
	}

}
