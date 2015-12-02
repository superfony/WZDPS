package com.epsmart.wzdp.activity.contract.fragment;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.ContractFragment;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.ViewBuildPlan;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/*
 * 铁塔排产计划（特高压项目列表）
 */
public class TowerPlanFragment extends ContractFragment {
	// private Activity activity;
	private View view;
	// 初始化分页标签
	private ViewBuildPlan viewBuild;
	String TAG = TowerPlanFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;
	protected String types;

	@Override
	public void onAttach(Activity activity) {
		queryCondition = new QueryCondition();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pull_fragment_item, container, false);
		return view;
	}

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

						 if (!PermissHelp.isPermiss("033")) {
						 PermissHelp.showToast(activity);
						 return;
						 }
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size() + 1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position - 1);
						Field pspid = workOrder.fields.get("pspid");
						String PSPID = pspid.fieldContent;
						activity.setSmGone();
						Intent intent = new Intent();
						intent.putExtra("PSPID", PSPID);
						
						if("tower".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "1"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "1"))));
						}else if("porcelain".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "2"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "2"))));
						}else if("reunite".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "3"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "3"))));
						}else if("wireway".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "4"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "4"))));
						}else if("ground".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "5"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "5"))));
						}else if("cable".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "6"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "6"))));
						}else if("fittings".equals(types)){
							intent.putExtra("reqParam", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
											pspid.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"version", ""))
													.append(RequestXmlHelp.getReqXML(
															"type", "7"))));
							intent.putExtra("reqPq", RequestXmlHelp
									.getCommonXML(RequestXmlHelp.getReqXML("pspid",
									pspid.fieldContent)
									.append(RequestXmlHelp.getReqXML(
											"version", "7"))));
						}
						intent.putExtra("types",types);
						intent.setClass(getActivity(), HosoolListTt.class);
						startActivity(intent);
						
					}
				});
		paginationWidget.setServiceName(RequestParamConfig.ehvProjectList);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initPage(savedInstanceState);
		activity.setSmShow();
		types = getArguments().getString("types");
//		search_lay.setVisibility(View.VISIBLE);
//		search_lay.setOnClickListener(searchLis);
//		search_btn.setOnClickListener(searchLis);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		add_lay = (RelativeLayout) getActivity().getActionBar().getCustomView()
//				.findViewById(R.id.add_lay);
//		add_btn = (Button) getActivity().getActionBar().getCustomView()
//				.findViewById(R.id.add_btn);
////		if (PermissHelp.isAdd("028")) {
//			add_lay.setVisibility(View.VISIBLE);
//			add_lay.setOnClickListener(addOnLister);
//			add_btn.setOnClickListener(addOnLister);
////		}
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.title_plans);
		
	}
	/*OnClickListener addOnLister = new OnClickListener() {	
		@Override
		public void onClick(View v) {
			activity.setSmGone();
//			Common.replaceRightFragment(activity, new NewWitnessFG(), false,
//					R.id.content);
		}
	};*/
	@Override
	public void onPause() {
		search_lay.setVisibility(View.INVISIBLE);
		super.onPause();
	}

}
