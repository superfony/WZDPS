package com.epsmart.wzdp.activity.supply.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SupplyFragmemt;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.ViewBuildFeed;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;

/**
 * 质量问题列表
 * 
 * @author fony
 * 
 */

public class QtyTopList extends SupplyFragmemt {
	private View view;
	//private Activity activity;
	 String TAG = QtyTopList.class.getName();
	private String req;
	private String materialtype;
	// 初始化分页标签
	private ViewBuildFeed viewBuildfeed;

	@Override
	public void onAttach(Activity activity) {
		//this.activity = activity;
		queryCondition = new QueryCondition();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.pull_fragment_item, container,
				false);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * 初始化分页控件
	 * 
	 * @param savedInstanceState
	 */
	public void initPage(Bundle savedInstanceState) {
		viewBuildfeed = new ViewBuildFeed();
		materialtype=getArguments().getString("materialtype");
		viewBuildfeed.setMaterialtype(materialtype);
		// 列表分页控件初始化设置
		if (null == paginationWidget) 
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
				R.layout.listview_footer, null));
			paginationWidget.init(activity,
					activity.findViewById(R.id.contentRoot), viewBuildfeed);
			paginationWidget.getHttpModule().setServiceNameSpace(
					RequestParamConfig.serviceNameSpace);
			paginationWidget.getHttpModule().setServiceUrl(
					RequestParamConfig.ServerUrl);
			paginationWidget.setRequestType(RequestType.THRIFT);// TODO
			initPaginationWidget(paginationWidget);
			paginationWidget.loadPaginationData();
		


	}

	/** 查询条件 */
	protected QueryCondition queryCondition;

	protected void initPaginationWidget(
			final PaginationWidget<WorkOrder> paginationWidget) {
		processQueryCondition(queryCondition);//
		paginationWidget.setPageSize(5);
		paginationWidget
				.setPageBodyOnItemClickListener(paginationWidget.new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// 缺少删除的操作
						if (!PermissHelp.isPermiss("011")
								|| !PermissHelp.isPermiss("012")
								|| !PermissHelp.isPermiss("013")
								|| !PermissHelp.isPermiss("014")) {
							PermissHelp.showToast(activity);
							return;
						}
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size()+1 == position) {
							
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position-1);

						Field purchaseorder = workOrder.fields
								.get("purchaseorder");
						Field eqorder = workOrder.fields.get("eqorder");
						Field Issueno = workOrder.fields.get("issueno");
						Field Releaseflag = workOrder.fields.get("releaseflag");// 状态
						

						if (eqorder == null || Issueno == null|| Releaseflag == null) {
							Toast.makeText(activity, "缺少必要参数，无法进行操作！",
									Toast.LENGTH_LONG).show();
							return;
						}
						FeedbackFragment feedfragment = new FeedbackFragment();
						Bundle bundle = new Bundle();
						bundle.putString(
								"reqParam",
								RequestXmlHelp.getCommonXML(RequestXmlHelp
										.getReqXML("purchaseorder",
												purchaseorder.fieldContent)
										.append(RequestXmlHelp.getReqXML(
												"eqorder", eqorder.fieldContent))
										.append(RequestXmlHelp
												.getReqXML("issueno",
														Issueno.fieldContent))
										.append(RequestXmlHelp.getReqXML(
												"releaseflag",
												Releaseflag.fieldContent))
										.append(RequestXmlHelp.getReqXML(
												"user_type",
												PermissHelp.getUserType("000")))));
						bundle.putString(
								"reqP",
								RequestXmlHelp
										.getReqXML("purchaseorder",
												purchaseorder.fieldContent)
										.append(RequestXmlHelp.getReqXML(
												"eqorder", eqorder.fieldContent))
										.append(RequestXmlHelp
												.getReqXML("issueno",
														Issueno.fieldContent))
										.append(RequestXmlHelp.getReqXML(
												"releaseflag",
												Releaseflag.fieldContent))
										.toString());
						bundle.putString("state",Releaseflag.fieldContent.toString());

						feedfragment.setArguments(bundle);
						Common.replaceRightFragment(activity, feedfragment,
								false, R.id.content);
					}
				});
		paginationWidget.setServiceName(RequestParamConfig.issuelistDownload);
	}

	/**
	 * 设置查询条件
	 * 
	 * @param condition
	 */
	public void processQueryCondition(QueryCondition condition) {
		requestPram.param = getArguments().getString("reqParam");
		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.issuelistDownload;
		requestPram.password = "password456";
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		paginationWidget.requestAction.setReqPram(requestPram);
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initPage(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		add_lay = (RelativeLayout) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.add_lay);
		add_btn = (Button) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.add_btn);
		if (PermissHelp.isAdd("011")) {
			add_lay.setVisibility(View.VISIBLE);
			add_lay.setOnClickListener(addOnLister);
			add_btn.setOnClickListener(addOnLister);
		}
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.supply_cs);
	}

	OnClickListener addOnLister = new OnClickListener() {
		@Override
		public void onClick(View v) {
			FeedbackFragment qtytop = new FeedbackFragment();
			Bundle bundle = new Bundle();
			bundle.putString("reqParam", RequestXmlHelp
					.getCommonXML(new StringBuffer(getArguments().getString(
							"reqP")
							+ RequestXmlHelp.getReqXML("user_type",
									PermissHelp.getUserType("000")))));
			bundle.putString("reqP", getArguments().getString("reqP"));
			bundle.putString("state","新建");
			qtytop.setArguments(bundle);
			Common.replaceRightFragment(activity, qtytop, false,
					R.id.content);
		}
	};

	@Override
	public void onPause() {
		if (add_lay != null)
			add_lay.setVisibility(View.GONE);
		super.onPause();
	}
	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

}