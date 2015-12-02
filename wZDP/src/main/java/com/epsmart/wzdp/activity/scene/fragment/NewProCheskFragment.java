package com.epsmart.wzdp.activity.scene.fragment;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.fragment.CommonFragment.WZDPTYPE;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.pagination.PaginationWidget.OnItemClickListener;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.ViewBuildProse;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;

public class NewProCheskFragment extends SceneFragmemt {
	// private Activity activity;
	private View view;
	// 初始化分页标签
	private ViewBuildProse viewBuild;
	String TAG = ItemProServiceFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;
	private TextView titles;
	private ImageView title_image;


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

		if (null == paginationWidget) {
			viewBuild = new ViewBuildProse();
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
			viewBuild = new ViewBuildProse();
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
		requestPram.param = getArguments().getString("reqParam");
		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.materialsListReq2;
		requestPram.password = "password33";
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

						if (!PermissHelp.isPermiss("021")) {
							PermissHelp.showToast(activity);
							return;
						}
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size() + 1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position - 1);
						Field PurchaseOrder = workOrder.fields
								.get("purchaseorder");

						if (PurchaseOrder == null ) {//|| poitem == null
							Toast.makeText(
									activity,
									"缺少必要参数：PurchaseOrder=" + PurchaseOrder,
									Toast.LENGTH_LONG).show();
							return;
						}
						Bundle bundle = new Bundle();
						bundle.putString("reqParam", RequestXmlHelp
								.getCommonXML(RequestXmlHelp.getReqXML(
										"purchaseorder",
										PurchaseOrder.fieldContent)));
						bundle.putString(
								"reqP",
								RequestXmlHelp
										.getReqXML("purchaseorder",
												PurchaseOrder.fieldContent)
										.toString());
						ServiceTopListFragment sertoplist = new ServiceTopListFragment();
						sertoplist.setArguments(bundle);
						activity.setSmGone();
						Common.replaceRightFragment(activity, sertoplist,
								false, R.id.content);
					}
				});
		paginationWidget.setServiceName(RequestParamConfig.materialsListReq2);
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
		appContext.wzdpType = WZDPTYPE.SCENSERVICE;
//		search_lay.setVisibility(View.VISIBLE);
//		search_lay.setOnClickListener(searchLis);
//		search_btn.setOnClickListener(searchLis);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String XMMC = getArguments().getString("xmmc");
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setVisibility(view.INVISIBLE);
		titles = (TextView) view.findViewById(R.id.titles);
		titles.setVisibility(view.VISIBLE);
		titles.setText(XMMC);
	}

	@Override
	public void onPause() {
		search_lay.setVisibility(View.INVISIBLE);
		titles.setVisibility(view.GONE);
		title_image.setVisibility(view.VISIBLE);
		super.onPause();
	}

}
