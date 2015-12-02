package com.epsmart.wzdp.activity.scene.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.ViewBuildSer;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;

/**
 * 现场服务-》申请服务列表（新建）
 */
public class ServiceTopListFragment extends SceneFragmemt {


	//private Activity activity;
	private View view;
	private String req;
	// 初始化分页标签
	private ViewBuildSer viewBuild;
	String TAG = ServiceTopListFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;

	@Override
	public void onAttach(Activity activity) {
	//	this.activity = activity;
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
		viewBuild = new ViewBuildSer();
		// 列表分页控件初始化设置
		if (null == paginationWidget)
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
	}

	/**
	 * 设置查询条件
	 * 
	 * @param condition
	 */
	public void processQueryCondition(QueryCondition condition) {

		requestPram.param = getArguments().getString("reqParam");//
		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.servicelistDownload;
		requestPram.password = "";
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
						if (!PermissHelp.isPermiss("024")
								|| !PermissHelp.isPermiss("025")
								|| !PermissHelp.isPermiss("026")) {
							PermissHelp.showToast(activity);
							return;
						}
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size()+1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position-1);
						Field PurchaseOrder = workOrder.fields
								.get("purchaseorder");
//						Field POitem = workOrder.fields.get("poitem");
						Field ServiceNO = workOrder.fields.get("serviceno");
						Field ServiceState = workOrder.fields.get("servicestate");
						Field equipmentname = workOrder.fields.get("equipmentname");
						
//						if (ServiceState == null) {
//							Toast.makeText(activity, "缺少必要参数，无法进行操作！",
//									Toast.LENGTH_LONG).show();
//							return;
//						}
						String state = ServiceState.fieldContent;
						String persionType = PermissHelp.getUserType("000");
						if (state.equals("待审核") && persionType.equals("6")// 审核
								|| state.equals("已审核")&& persionType.equals("6")// 修改
								||persionType.equals("4")){
							TopAftermarketEvaluationFragment topafter = new TopAftermarketEvaluationFragment();
							Bundle bundle = new Bundle();
							bundle.putString(
									"reqParam",
									RequestXmlHelp.getCommonXML(RequestXmlHelp
											.getReqXML("purchaseorder",
													PurchaseOrder.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"serviceno",
													ServiceNO.fieldContent))
											.append(RequestXmlHelp.getReqXML(
													"servicestate",
													ServiceState.fieldContent))
													.append(RequestXmlHelp.getReqXML(
												"equipmentname",
												equipmentname.fieldContent))
											.append(RequestXmlHelp.getReqXML(
													"user_type", PermissHelp
															.getUserType("000")))));
							bundle.putString(
									"reqP",
									RequestXmlHelp
											.getReqXML("purchaseorder",
													PurchaseOrder.fieldContent)
											.append(RequestXmlHelp.getReqXML(
													"serviceno",
													ServiceNO.fieldContent))
											.append(RequestXmlHelp.getReqXML(
													"servicestate",
													ServiceState.fieldContent))
											.toString());
							bundle.putString("state", ServiceState.fieldContent);
							
							topafter.setArguments(bundle);
							Common.replaceRightFragment(activity, topafter,
									false, R.id.content);
						}

					}
				});
		paginationWidget.setServiceName(RequestParamConfig.selectNowWork);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initPage(savedInstanceState);
		add_lay = (RelativeLayout) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.add_lay);
		add_btn = (Button) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.add_btn);
		if (PermissHelp.isAdd("028")) {
			add_lay.setVisibility(View.VISIBLE);
			add_btn.setOnClickListener(addListener);
			add_lay.setOnClickListener(addListener);	
		}
		
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.service_cs);
	}
   
	OnClickListener addListener=new OnClickListener() {

		@Override
		public void onClick(View v) {
			TopAftermarketEvaluationFragment topafter = new TopAftermarketEvaluationFragment();
			Bundle bundle = new Bundle();
			bundle.putString("reqParam", RequestXmlHelp
					.getCommonXML(new StringBuffer(getArguments()
							.getString("reqP")
							+ RequestXmlHelp.getReqXML("user_type",
									PermissHelp.getUserType("000")))));
			bundle.putString(
					"reqP",
					getArguments().getString("reqP")
							+ RequestXmlHelp.getReqXML("user_type",
									PermissHelp.getUserType("000")));
			bundle.putString("state", "新建");
			topafter.setArguments(bundle);
			Common.replaceRightFragment(activity, topafter, false,
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
