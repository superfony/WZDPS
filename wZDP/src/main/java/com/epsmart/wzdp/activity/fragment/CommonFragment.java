package com.epsmart.wzdp.activity.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.ActivityUtils;
import com.epsmart.wzdp.activity.AppContext;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.search.QueryDialog;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.supply.bean.BasicEntity;
import com.epsmart.wzdp.activity.supply.bean.DataRow;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.BaseHttpModule.RequestListener;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.HttpException;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.Response;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.http.xml.parser.BaseXmlParser;

/**
 * 
 * @author fony
 * 
 */

@SuppressLint("NewApi") public class CommonFragment extends Fragment  {
	protected CommonActivity activity;
	protected BaseHttpModule httpModule;
	/** 分页控件 */
	protected PaginationWidget<WorkOrder> paginationWidget = null;
	/** 表格布局 */
	protected RelativeLayout all_id;
	protected LinearLayout text_lay;
	protected LinearLayout table_lay;
	protected LinearLayout btn_lay;
	/** 提交、取消按钮 */
	protected Button submit_btn;
	protected Button cancel_btn;
	protected FillTableHelp fillHelp;
	protected FillTableHelpNew fillHelpNew;
	protected RequestPram requestPram;
	protected  QueryDialog queryDialog;
	protected RelativeLayout add_lay;// 添加按钮
	protected Button add_btn;// 添加按钮
	protected RelativeLayout search_lay;// 查询按钮
	protected Button search_btn;// 查询按钮
	protected RelativeLayout present_lay;//提交布局
	protected Button present_btn;//提交按钮
	protected RelativeLayout question_lay;
	protected Button question_btn;//提问题按钮
	protected TextView query_tv;
	protected RelativeLayout query_lay;
	//protected AppContext app_context;
	protected QueryDialogListener listener;
	protected AppContext appContext;

	public enum WZDPTYPE{
		
		PRODUCTION,// 生产过程监控
		QUALITY,//质量监督
		POINT,// 关键点见证
		SCENEPRO,// 节点信息维护
		WORK,//工作日志
		SCENEAFT,//物资收货验收
		SCENEAFTS,//供应进度维护2级
		SCENSERVICE,//现场服务
		SubstanceaNalysisAct,//合同在线统计报表
		ProductionFromAct,//物资供应综合分析报表
		SettleCharAct,//合同结算情况统计报表
		ContractLineCharAct,//项目进度情况统计报表
	}

	private ArrayList<String> txtShowList = new ArrayList<String>();

	private String TAG = CommonFragment.class.getName();

	public CommonFragment() {

	}
	
	

	
	

	@Override
	public void onAttach(Activity activity) {
		this.activity = (CommonActivity) activity;
		appContext=(AppContext)this.activity.getApplication();
		init();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		requestPram = new RequestPram();
		search_lay = (RelativeLayout) getActivity().getActionBar()
				.getCustomView().findViewById(R.id.search_lay);
		search_btn = (Button) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.search_btn);
		present_lay = (RelativeLayout) getActivity().getActionBar()
				.getCustomView().findViewById(R.id.present_lay);
		present_btn = (Button) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.present_btn);
		question_lay = (RelativeLayout) getActivity().getActionBar()
				.getCustomView().findViewById(R.id.question_lay);
		question_btn = (Button) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.question_btn);
		query_lay=(RelativeLayout) getActivity().getActionBar()
				.getCustomView().findViewById(R.id.query_lay);
		query_tv=(TextView)getActivity().getActionBar().getCustomView()
				.findViewById(R.id.query_tv);
	}
	
	protected OnClickListener searchLis=new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			queryDialog = new QueryDialog(activity, listener);// TODO
			queryDialog.show(v);
		}
	};
	protected OnClickListener clickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.present_btn) {
				submitMethod(requestPram);
			} else if (id == R.id.question_btn) {
				cancelMethod();
			} else {

			}

		}
	};

	// 提交
	protected void submitMethod(RequestPram reqPram) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestAction.setReqPram(reqPram);
		httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}
	protected void subdelsub(RequestPram reqPram) {
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestAction.setReqPram(reqPram);
		httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
				new DeleteSub(), RequestType.THRIFT);
	}
	

	protected void cancelMethod() {
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
	
	/* 处理表单提交返回消息 */
	class DeleteSub implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				mHandler.obtainMessage(2, parseObj).sendToTarget();
			}
		}
	}

	private void init() {
		if (httpModule == null) {
			httpModule = new BaseHttpModule(activity);
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
			Log.i("", "..........onFail with requestLis");
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

	protected BaseXmlParser getXmlParser(Response response) {
		response.setResponseAsString(response.getResponseAsString().replace(
				"&", "#"));
		return httpModule.getBaseXmlParser(response,
				httpModule.getParseHandler());
	}

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
				Toast.makeText(activity, (String) msg.obj + "",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				StatusEntity se = (StatusEntity) msg.obj;
				Toast.makeText(activity, se.message + "", Toast.LENGTH_LONG)
						.show();
            activity.getSupportFragmentManager().popBackStack();
				break;
				
			case 2:// 删除的操作
				StatusEntity see = (StatusEntity) msg.obj;
				Toast.makeText(activity, see.message + "", Toast.LENGTH_LONG)
						.show();
				deltoback();
				break;
				
			default:
				break;
			}
		}
	}

	protected void deltoback(){}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}


	protected void addDataRow(ViewGroup group, DataRow dataRow, int row,
			ArrayList<String> listTable) {
		int size = dataRow.columns.size();
		if (size < 1)
			return;

		LinearLayout rowL = new LinearLayout(activity);
		rowL.setOrientation(LinearLayout.HORIZONTAL);
		Field field = null;
		for (int i = 0; i < size; i++) {
			field = dataRow.columns.get(i);
			if ("0".equals(field.fieldView)) {// 0-隐藏
				continue;
			}
			if ("1".equals(field.fieldView)) {// 1 文本显示在表格外
				// addTextView(rowL, field, row, i);
				txtShowList.add(field.fieldEnName);
			} else {
				listTable.add(field.fieldEnName);// 其它添加到表格显示
			}
		}
	}

	// 动态显示加载表格数据
	protected ArrayList<String> fillContent(BasicEntity entity) {
		int rowCount = entity.rows.size();
		ArrayList<String> listTable = new ArrayList<String>();
		for (int i = 0; i < rowCount; i++) {
			addDataRow(null, entity.rows.get(i), i, listTable);
		}
		initTextLay(entity, txtShowList);
		return listTable;

	}

	protected void addTextView(ViewGroup view, Field field, int row, int col) {
		TextView title;
		TextView value;
		int titleId = row * 10 + col;
		title = new TextView(activity);
		title.setId(titleId);
		title.setTextColor(getResources().getColor(android.R.color.black));
		title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		title.setText(field.fieldChName + ":");
		title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

		int valueId = titleId + 1;
		value = new TextView(activity);
		value.setId(valueId);
		value.setTextColor(getResources().getColor(android.R.color.black));
		value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);

		value.setText((null == field.fieldContent || "null"
				.equals(field.fieldContent)) ? "" : field.fieldContent);

		if (col != 0) {
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = ActivityUtils.dipToPixels(activity, 20);
			view.addView(title, lp);
		} else {
			view.addView(title);
		}
		view.addView(value);
	}

	/** 表格外数据 */
	protected void initTextLay(BasicEntity entity, ArrayList<String> list) {
		int n = list.size();
		if (n < 1)
			return;
		LinearLayout layT = new LinearLayout(activity);
		final LinearLayout layShowT = new LinearLayout(activity);
		layShowT.setVisibility(View.GONE);
		LinearLayout.LayoutParams param_title_lay = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 80);
		
		LinearLayout.LayoutParams param_title_jc = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout.LayoutParams param_title_table = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		LinearLayout.LayoutParams param_button = new LinearLayout.LayoutParams(
				40, 40);
		LinearLayout.LayoutParams linelay = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 1);
		linelay.rightMargin = 22;
		linelay.leftMargin = 22;
		layT.setOrientation(LinearLayout.HORIZONTAL);
		layShowT.setOrientation(LinearLayout.VERTICAL);
		ImageView lineImg = new ImageView(activity);
		lineImg.setBackgroundResource(R.drawable.rec_boder_lie);
		layShowT.addView(lineImg, linelay);
		TextView titleInfo = new TextView(activity);
		titleInfo.setText("基础信息");
		titleInfo.setTextSize(20);
		titleInfo.setTextColor(getResources()
				.getColor(R.color.table_line_title));

		final Button button = new Button(activity);
		layT.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (layShowT.isShown()) {
					button.setBackgroundResource(R.drawable.btn_down);
					layShowT.setVisibility(View.GONE);
				} else {
					button.setBackgroundResource(R.drawable.btn_up);
					layShowT.setVisibility(View.VISIBLE);
				}
			}
		});
		button.setBackgroundResource(R.drawable.btn_down);
		param_title_jc.gravity = Gravity.CENTER_VERTICAL;
		layT.addView(titleInfo, param_title_jc);
		param_button.gravity = Gravity.CENTER_VERTICAL;
		param_button.rightMargin = 20;
		param_button.leftMargin = 860;
		layT.addView(button, param_button);
		param_title_lay.topMargin = 30;
		text_lay.addView(layT, param_title_lay);

		for (int i = 0; i < n; i++) {
			TextView title;
			TextView value;
			LinearLayout lay = new LinearLayout(activity);

			lay.setOrientation(LinearLayout.HORIZONTAL);
			for (int j = i; j < i + 2; j++) {
				if (j > n - 1)
					continue;
				Field field = entity.fields.get(list.get(j));
				int titleId = i * 10 + j;
				title = new TextView(activity);
				title.setId(titleId);
				title.setTextColor(getResources().getColor(
						R.color.listitem_gray));
				title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				title.setText(field.fieldChName + ":");
				title.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
				int valueId = titleId + 1;
				value = new TextView(activity);
				value.setId(valueId);
				value.setTextColor(getResources().getColor(
						R.color.listitem_gray));
				value.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
				value.setText((null == field.fieldContent || "null"
						.equals(field.fieldContent)) ? "" : field.fieldContent);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.leftMargin = ActivityUtils.dipToPixels(activity, 30);
				lay.addView(title, lp);
				lay.addView(value);

			}
			i++;
			layShowT.addView(lay);
			lineImg = new ImageView(activity);
			lineImg.setBackgroundResource(R.drawable.rec_boder_lie);
			layShowT.addView(lineImg, linelay);

		}
		text_lay.addView(layShowT);
		titleInfo = new TextView(activity);
		titleInfo.setText("表单信息");
		titleInfo.setTextSize(20);
		titleInfo.setTextColor(getResources()
				.getColor(R.color.table_line_title));
		param_title_table.bottomMargin=10;
		text_lay.addView(titleInfo, param_title_table);
		
	
	}

	/** 初始化表格数据 */
	protected void initTable(BasicEntity entity) {
		if (null == entity || entity.rows.size() < 1)
			return;
		fillHelp = new FillTableHelp(activity, table_lay);
		fillHelp.setBtn_lay(btn_lay);
		((com.epsmart.wzdp.activity.CommonActivity) activity)
				.setFillHelp(fillHelp);
		// 表格布局
		fillHelp.fillTable(entity, fillContent(entity));
		fillHelp.setTxtShowList(txtShowList);

	}
	
	/** 生产过程监控 监理师分配组和人员  */
	protected void initTableGroup(BasicEntity entity) {
		if (null == entity || entity.rows.size() < 1)
			return;
		fillHelp = new FillTableHelp(activity, table_lay);
		fillHelp.setBtn_lay(btn_lay);
		((com.epsmart.wzdp.activity.CommonActivity) activity)
				.setFillHelp(fillHelp);
		// 表格布局
		fillHelp.fillTableGroup(entity, fillContent(entity));

	}
	/** 生产过程监控 监理师分配组和人员  */
	protected void initTableGroupNew(ArrayList<BasicEntity> entityList,boolean isGroup) {
		if (null == entityList || entityList.size() < 1)
			return;
		fillHelpNew = new FillTableHelpNew(activity, table_lay);
		fillHelpNew.setBtn_lay(btn_lay);
		((com.epsmart.wzdp.activity.CommonActivity) activity)
				.setFillHelpNew(fillHelpNew);
		// 表格布局
		fillHelpNew.setGroup(isGroup);
		fillHelpNew.fillTableNew(entityList);

	}
	
	protected void initTableNew(ArrayList<BasicEntity> entityList) {
		if (null == entityList || entityList.size()<1)
			return;
		fillHelpNew = new FillTableHelpNew(activity, table_lay);// 绘制表格布局
		fillHelpNew.setBtn_lay(btn_lay);// 提交按钮布局
		((com.epsmart.wzdp.activity.CommonActivity) activity)
				.setFillHelpNew(fillHelpNew);
		// 表格布局
		fillHelpNew.fillTableNew(entityList);

	}

}
