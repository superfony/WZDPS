package com.epsmart.wzdp.activity.dailyoffice.fragment;

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
import com.epsmart.wzdp.activity.fragment.CommonFragment;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.pagination.UIHelper;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.DailyViewBuildNotice;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 通知公告模块
 */
public class ItemNoticeFragment extends CommonFragment {
	private View view;
	// 初始化分页标签
	private DailyViewBuildNotice viewBuildNotice;
	String TAG = ItemNoticeFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;

	@Override
	public void onAttach(Activity activity) {
		queryCondition = new QueryCondition();
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.dailypull_fragment_item, container, false);
		return view;
	}

	/**
	 * 初始化分页控件
	 * 
	 * @param savedInstanceState
	 */
	public void initPage(Bundle savedInstanceState) {

		if (null == paginationWidget) {
			viewBuildNotice = new DailyViewBuildNotice();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.init(activity,
					activity.findViewById(R.id.contentRoot), viewBuildNotice);
			paginationWidget.setRequestType(RequestType.THRIFT);// TODO
			initPaginationWidget(paginationWidget);
			paginationWidget.loadPaginationData();
		} else {
			ArrayList<WorkOrder> arraylist = (ArrayList<WorkOrder>) paginationWidget.tableBodyAdapter
					.getDataCache();
			RequestAction requestAction = paginationWidget.requestAction;
			Object tag = paginationWidget.lv_page_body.getTag();
			viewBuildNotice = new DailyViewBuildNotice();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.initArray(activity,
					activity.findViewById(R.id.contentRoot), viewBuildNotice,
					arraylist);
			paginationWidget.setRequestType(RequestType.THRIFT);
			initPaginationWidget(paginationWidget);
			paginationWidget.requestAction = requestAction;
			paginationWidget.lv_page_body.setTag(tag);
		}
	}
	
	public void oo(){
		if(paginationWidget!=null){
			PaginationWidget.action = UIHelper.LISTVIEW_ACTION_REFRESH;
			paginationWidget.requestAction.reset();
			paginationWidget.requestAction.pageBean.setCurrentPage(1);
			paginationWidget.loadPaginationData();
		}else{
			viewBuildNotice = new DailyViewBuildNotice(); 
		paginationWidget = new Pagination();
		PaginationWidget.action = UIHelper.LISTVIEW_ACTION_INIT;
		paginationWidget.setFootView(activity.getLayoutInflater().inflate(
				R.layout.listview_footer, null));
		paginationWidget.init(activity,
				activity.findViewById(R.id.contentRoot), viewBuildNotice);
		paginationWidget.setRequestType(RequestType.THRIFT);// TODO
		initPaginationWidget(paginationWidget);
		paginationWidget.loadPaginationData();
		}
	}
	
	/**
	 * 设置查询条件
	 * 
	 * @param condition
	 */
	public void processQueryCondition(QueryCondition condition) {

		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.getNoticeList;
		requestPram.password = "password";
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

//						if (!PermissHelp.isPermiss("020")) {
//							PermissHelp.showToast(activity);
//							return;
//						}
						if (position == 0
								|| paginationWidget.getPageBodyDatas().size() + 1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position - 1);
						Field noticeid = workOrder.fields.get("noticeid");
						String noticeId = noticeid.fieldContent;
						
						Intent intent = new Intent();
						intent.putExtra("noticeId", noticeId);
						intent.setClass(getActivity(), DailyNoticeAct.class);
						startActivityForResult(intent, 1);
					}
				});
		paginationWidget.setServiceName(RequestParamConfig.getNoticeList);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initPage(savedInstanceState);
		activity.setSmShow();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.notice_title);

	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
}
