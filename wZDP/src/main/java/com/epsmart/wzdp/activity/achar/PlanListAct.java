package com.epsmart.wzdp.activity.achar;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.scene.fragment.ItemProCheckFragment;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.search.QueryPlanDialog;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.ViewBuildPlan;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/*
 * 铁塔排产计划（特高压项目列表）
 */
public class PlanListAct extends PlanCommonAct {
	private View view;
	// 初始化分页标签
	private ViewBuildPlan viewBuild;
	String TAG = ItemProCheckFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;
	protected QueryPlanDialog queryPlanDialog;

	/**
	 * 初始化分页控件
	 * 
	 * @param savedInstanceState
	 */
	public void initPage(Bundle savedInstanceState) {

		if (null == paginationWidget) {
			viewBuild = new ViewBuildPlan();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.init(activity,
					activity.findViewById(R.id.contentRoot), viewBuild);
			paginationWidget.setRequestType(RequestType.THRIFT);// TODO
			initPaginationWidget(paginationWidget);
			paginationWidget.loadPaginationData();
		} else {
			ArrayList<WorkOrder> arraylist = (ArrayList<WorkOrder>) paginationWidget.tableBodyAdapter
					.getDataCache();
			RequestAction requestAction = paginationWidget.requestAction;
			Object tag = paginationWidget.lv_page_body.getTag();
			viewBuild = new ViewBuildPlan();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.initArray(activity,
					activity.findViewById(R.id.contentRoot), viewBuild,
					arraylist);
			paginationWidget.setRequestType(RequestType.THRIFT);
			initPaginationWidget(paginationWidget);
			paginationWidget.requestAction = requestAction;
			paginationWidget.lv_page_body.setTag(tag);
		}
	}

	/**
	 * 设置查询条件
	 * 
	 * @param condition
	 */
	public void processQueryCondition(QueryCondition condition) {
		requestPram=new RequestPram();
		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.ehvProjectList;
		requestPram.password = PermissHelp.getUserType("000");
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		paginationWidget.requestAction.setReqPram(requestPram);

	}

	protected void initPaginationWidget(
			final PaginationWidget<WorkOrder> paginationWidget) {
		processQueryCondition(queryCondition);
		paginationWidget.setPageSize(5);
		paginationWidget
				.setPageBodyOnItemClickListener(paginationWidget.new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						// if (!PermissHelp.isPermiss("020")) {
						// PermissHelp.showToast(activity);
						// return;
						// }
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size() + 1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position - 1);
						Field pspid = workOrder.fields.get("pspid");
						String PSPID = pspid.fieldContent;
						Intent intent = new Intent();
//						intent.putExtra("reqParam", RequestXmlHelp
//								.getCommonXML(RequestXmlHelp.getReqXML("pspid",
//										pspid.fieldContent)));
						 intent.putExtra("PSPID", PSPID);
						 String type = getIntent().getStringExtra("type");
						 if(type.equals("plan")){
							 intent.setClass(activity, ScheduleAct.class);
						 }else{
							 intent.setClass(activity, WeeklyAct.class);
						 }
						 
						startActivity(intent);

					}
				});
		paginationWidget.setServiceName(RequestParamConfig.ehvProjectList);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pull_fragment_item);
		queryCondition = new QueryCondition();
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.hight_pressure);
		initPage(savedInstanceState);
//		search_lay.setVisibility(View.VISIBLE);
//		search_lay.setOnClickListener(searchLisp);// 查询
//		search_btn.setOnClickListener(searchLisp);

	}

	protected OnClickListener searchLisp = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			((PlanCommonAct) activity).setVers(getIntent().getStringExtra(
//					"reqParam"));
//			queryPlanDialog = new QueryPlanDialog(activity, listener,
//					httpModule);// TODO
//			queryPlanDialog.show(v);
		}
	};

	{
		listener = new QueryDialogListener() {
			@Override
			public void doQuery(String req) {
				requestPram.param = req;
				paginationWidget.getRequestAction().reset();
				processQueryCondition(null);
				paginationWidget.loadPaginationData();
			}
		};
	}

	@Override
	public void onPause() {
		search_lay.setVisibility(View.INVISIBLE);
		super.onPause();
	}
}
