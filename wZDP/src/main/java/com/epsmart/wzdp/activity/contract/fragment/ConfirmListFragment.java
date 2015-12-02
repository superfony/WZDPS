package com.epsmart.wzdp.activity.contract.fragment;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
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
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/*
 * 特高压项目 列表
 */
public class ConfirmListFragment extends ContractFragment{
	// private Activity activity;
	private View view;
	// 初始化分页标签
	private ViewBuildPlan viewBuild;
	// 申明PopupWindow对象引用
	private String TAG = ConfirmListFragment.class.getName();

	@Override
	public void onAttach(Activity activity) {
		// this.activity = activity;
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
		
		// 列表分页控件初始化设置
		if (null == paginationWidget) {
			viewBuild = new ViewBuildPlan();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.init(activity,
					activity.findViewById(R.id.contentRoot), viewBuild);
			paginationWidget.getHttpModule().setServiceNameSpace(
					RequestParamConfig.serviceNameSpace);
			paginationWidget.getHttpModule().setServiceUrl(
					RequestParamConfig.ServerUrl);
			paginationWidget.setRequestType(RequestType.THRIFT);// TODO
			initPaginationWidget(paginationWidget);
			paginationWidget.loadPaginationData();
		} else {
			ArrayList<WorkOrder> arraylist=	(ArrayList<WorkOrder>) paginationWidget.tableBodyAdapter.getDataCache();
			RequestAction requestAction=paginationWidget.requestAction;
			Object tag=paginationWidget.lv_page_body.getTag();
			viewBuild = new ViewBuildPlan();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.initArray(activity,
					activity.findViewById(R.id.contentRoot), viewBuild,arraylist);
			paginationWidget.getHttpModule().setServiceNameSpace(
					RequestParamConfig.serviceNameSpace);
			paginationWidget.getHttpModule().setServiceUrl(
					RequestParamConfig.ServerUrl);
			paginationWidget.setRequestType(RequestType.THRIFT);
			initPaginationWidget(paginationWidget);
			paginationWidget.requestAction=requestAction;
			paginationWidget.lv_page_body.setTag(tag);
		}

	}

	/** 查询条件 */
	protected QueryCondition queryCondition;

	protected void initPaginationWidget(
			final PaginationWidget<WorkOrder> paginationWidget) {
		processQueryCondition(queryCondition);
		paginationWidget.setPageSize(5);
		paginationWidget
				.setPageBodyOnItemClickListener(paginationWidget.new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
//						if (!PermissHelp.isPermiss("006")) {
//							PermissHelp.showToast(activity);
//							return;
//						}
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size() + 1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position - 1);
						Field pspid = workOrder.fields.get("pspid");
						ConfirmMaListFragment cmfragment = new ConfirmMaListFragment();
						Bundle bundle = new Bundle();
						bundle.putString("reqParam",RequestXmlHelp.getCommonXML(RequestXmlHelp
										.getReqXML("pspid",
												pspid.fieldContent)
										.append(RequestXmlHelp.getReqXML(
												"user_type",
												PermissHelp.getUserType("000")))
												));
						cmfragment.setArguments(bundle);
							activity.setSmGone();
							Common.replaceRightFragment(activity, cmfragment,
									false, R.id.content);
					}
				});
		paginationWidget.setServiceName(RequestParamConfig.ehvProjectList);
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
		 requestPram.userName=appContext.user.getUid();
		paginationWidget.requestAction.setReqPram(requestPram);
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
//		search_lay.setVisibility(View.VISIBLE);
//		search_lay.setOnClickListener(searchLis);
//		search_btn.setOnClickListener(searchLis);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.confirm_title);

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onPause() {
//		search_lay.setVisibility(View.INVISIBLE);
		super.onPause();
	}

	

}