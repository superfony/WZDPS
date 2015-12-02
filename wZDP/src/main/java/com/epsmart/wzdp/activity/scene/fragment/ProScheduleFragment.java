package com.epsmart.wzdp.activity.scene.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.MainActivity;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.SceneFragmemt;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.ViewBuildSche;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.common.PermissHelp;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;

public class ProScheduleFragment extends SceneFragmemt {
//	private Activity activity;
	private View view;
	// 初始化分页标签
	private ViewBuildSche viewBuild;
	String TAG = ProScheduleFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;
	private TextView titles;
	private ImageView title_image;
	
	@Override
	public void onAttach(Activity activity) {
		//this.activity = activity;
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
		viewBuild = new ViewBuildSche();
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
		requestPram.bizId=1004;
		requestPram.param = getArguments().getString("reqParam");
		requestPram.methodName=RequestParamConfig.materialslistReq;
		requestPram.password="password";
		requestPram.pluginId=119;
		requestPram.userName=appContext.user.getUid();
      	paginationWidget.requestAction.setReqPram(requestPram);

	}
	
	public void processQueryCondition1(QueryCondition condition) {
		requestPram.bizId=1004;
		requestPram.methodName=RequestParamConfig.materialslistReq;
		requestPram.password="password";
		requestPram.pluginId=119;
		requestPram.userName=appContext.user.getUid();
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
						
						if(!PermissHelp.isPermiss("020")){
							PermissHelp.showToast(activity);
							return;
						}
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size()+1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position-1);
						Field purchaseorder=workOrder.fields.get("purchaseorder");
						Field poitem=workOrder.fields.get("poitem");
						Field post1=workOrder.fields.get("post1");
						String post = post1.fieldContent;
						
						ProCheckFragment	procheck=new ProCheckFragment();
						Bundle bundle=new Bundle();
						bundle.putString("reqParam", RequestXmlHelp.getCommonXML(RequestXmlHelp.getReqXML("purchaseorder", purchaseorder.fieldContent).append(RequestXmlHelp.getReqXML("poitem", poitem.fieldContent))));
						bundle.putString("reqP", RequestXmlHelp.getReqXML("purchaseorder", purchaseorder.fieldContent).append(RequestXmlHelp.getReqXML("poitem", poitem.fieldContent)).toString());
//						bundle.putString("reqParam", RequestXmlHelp.getCommonXML(RequestXmlHelp.getReqXML("xmbm", XMBM.fieldContent)));
//						bundle.putString("reqP", RequestXmlHelp.getReqXML("purchaseorder", purchaseorder.fieldContent).append(RequestXmlHelp.getReqXML("poitem", poitem.fieldContent)).append(RequestXmlHelp.getReqXML("xmbm", XMBM.fieldContent)).toString());
						bundle.putString("post", post);
						procheck.setArguments(bundle);
						Common.replaceRightFragment(activity, procheck,
								false, R.id.content);
					}
				});
		paginationWidget.setServiceName(RequestParamConfig.materialslistReq);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initPage(savedInstanceState);
		appContext.wzdpType = WZDPTYPE.SCENEAFTS;
		search_lay.setVisibility(View.VISIBLE);
		String xmbm=getArguments().getString("xmbm");
		activity.setPspid(xmbm);
		search_lay.setOnClickListener(searchLis);
		search_btn.setOnClickListener(searchLis);
		
	}
	
	{
		listener = new QueryDialogListener() {
			@Override
			public void doQuery(String req) {
				requestPram.param = req;
				paginationWidget.getRequestAction().reset();
				processQueryCondition1(null);
				paginationWidget.loadPaginationData();
			}
		};
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		String XMMC = getArguments().getString("xmmc");
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setVisibility(View.INVISIBLE);
		titles = (TextView) view.findViewById(R.id.titles);
		titles.setText(XMMC);
		titles.setVisibility(View.VISIBLE);

	}

	@Override
	public void onPause() {
		search_lay.setVisibility(View.INVISIBLE);
		titles.setVisibility(View.INVISIBLE);
		title_image.setVisibility(View.VISIBLE);
		super.onPause();
	}

}
