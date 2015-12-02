package com.epsmart.wzdp.activity.dailyoffice.fragment;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.fragment.CommonFragment;
import com.epsmart.wzdp.activity.pagination.PaginationWidget;
import com.epsmart.wzdp.activity.pagination.UIHelper;
import com.epsmart.wzdp.activity.search.QueryDialogListener;
import com.epsmart.wzdp.activity.supply.bean.Field;
import com.epsmart.wzdp.bean.DailyViewBuildOnline;
import com.epsmart.wzdp.bean.Pagination;
import com.epsmart.wzdp.bean.QueryCondition;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.bean.WorkOrder;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;

/**
 * 在线交流
 */
public class ItemOnlineFragment extends CommonFragment {
	private View view;
	// 初始化分页标签
	private DailyViewBuildOnline viewBuildOnline;
	String TAG = ItemOnlineFragment.class.getName();
	/** 查询条件 */
	protected QueryCondition queryCondition;
	private Field subid;
	private String SUBID;
	private String subname;
	private String subimg;
	private String subdate;
	private String subTitle;
	private String subContent;
	private String subper;
	

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

	
	public void oo(){
		if(paginationWidget!=null){
			PaginationWidget.action = UIHelper.LISTVIEW_ACTION_REFRESH;
			paginationWidget.requestAction.reset();
			paginationWidget.requestAction.pageBean.setCurrentPage(1);
			paginationWidget.loadPaginationData();
		}else{
		viewBuildOnline = new DailyViewBuildOnline(); 
		paginationWidget = new Pagination();
		PaginationWidget.action = UIHelper.LISTVIEW_ACTION_INIT;
		paginationWidget.setFootView(activity.getLayoutInflater().inflate(
				R.layout.listview_footer, null));
		paginationWidget.init(activity,
				activity.findViewById(R.id.contentRoot), viewBuildOnline);
		paginationWidget.setRequestType(RequestType.THRIFT);// TODO
		initPaginationWidget(paginationWidget);
		paginationWidget.loadPaginationData();
		}
	}
	protected void deltoback(){
		oo();
	}
	
	/**
	 * 初始化分页控件
	 * @param savedInstanceState
	 */
	public void initPage(Bundle savedInstanceState) {

		if (null == paginationWidget) {
			viewBuildOnline = new DailyViewBuildOnline(); 
			viewBuildOnline.setUserid(appContext.user.getUid()+"");
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.init(activity,
					activity.findViewById(R.id.contentRoot), viewBuildOnline);
			paginationWidget.setRequestType(RequestType.THRIFT);// TODO
			initPaginationWidget(paginationWidget);
			paginationWidget.loadPaginationData();
		} else {
			ArrayList<WorkOrder> arraylist = (ArrayList<WorkOrder>) paginationWidget.tableBodyAdapter
					.getDataCache();
			RequestAction requestAction = paginationWidget.requestAction;
			Object tag = paginationWidget.lv_page_body.getTag();
			viewBuildOnline = new DailyViewBuildOnline();
			paginationWidget = new Pagination();
			paginationWidget.setFootView(activity.getLayoutInflater().inflate(
					R.layout.listview_footer, null));
			paginationWidget.initArray(activity,
					activity.findViewById(R.id.contentRoot), viewBuildOnline,
					arraylist);
			paginationWidget.setRequestType(RequestType.THRIFT);
			initPaginationWidget(paginationWidget);
			paginationWidget.requestAction = requestAction;
			paginationWidget.lv_page_body.setTag(tag);
		}
		viewBuildOnline.setOnDelClick(viewBuildOnline.new OnDelClickListener(){
			@Override
			public void onClick(View v) {
				String subid=v.getTag().toString();
				 dialog( subid) ;
			}
		});
	}

	/**
	 * 设置查询条件
	 * 
	 * @param condition
	 */
	public void processQueryCondition(QueryCondition condition) {

		requestPram.bizId = 1004;
		requestPram.methodName = RequestParamConfig.getSubList;
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

						if (position == 0
								|| paginationWidget.getPageBodyDatas().size() + 1 == position) {
							return;
						}
						WorkOrder workOrder = paginationWidget
								.getPageBodyDatas().get(position - 1);
						subid = workOrder.fields.get("subid");
						SUBID = subid.fieldContent;
						Field subpersionname = workOrder.fields.get("subpersionname");
						subname = subpersionname.fieldContent;
						Field subpersionimg = workOrder.fields.get("subpersionimg");
						subimg = subpersionimg.fieldContent;
						Field subtime = workOrder.fields.get("subtime");
						subdate = subtime.fieldContent;
						Field subtitle = workOrder.fields.get("subtitle");
						subTitle = subtitle.fieldContent;
						Field subcontent = workOrder.fields.get("subcontent");
						subContent = subcontent.fieldContent;
						Field subpersions = workOrder.fields.get("subpersions");
						subper = subpersions.fieldContent;

						Intent intent = new Intent();
						intent.putExtra("subpersionname", subname);
						intent.putExtra("subpersionimg", subimg);
						intent.putExtra("subtime", subdate);
						intent.putExtra("subtitle", subTitle);
						intent.putExtra("SUBID", SUBID);
						intent.putExtra("subcontent", subContent);
						intent.putExtra("subper", subper);
						
						intent.setClass(getActivity(), DailyReplyThemeAct.class);
						startActivityForResult(intent, 1);
					}
				});
		
		

		paginationWidget.setServiceName(RequestParamConfig.getSubList);
	}
	
	// 推出弹出框
	private void dialog(final String subid) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage("是否删除");
		
		builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}
		}).setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				deleteSub(new RequestPram(),subid);
			}
		}).create().show();
		
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void deleteSub(RequestPram requestPram,String subid) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		requestPram.bizId = 1004;
		requestPram.password = subid;
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.delSubject;
		requestAction.setReqPram(requestPram);
		super.subdelsub(requestPram);
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
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ActionBar actionBar = activity.getActionBar();
		View view = actionBar.getCustomView();
		ImageView title_image = (ImageView) view.findViewById(R.id.title_image);
		title_image.setBackgroundResource(R.drawable.daily_online_title);
		
		add_lay = (RelativeLayout) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.add_lay);
		add_btn = (Button) getActivity().getActionBar().getCustomView()
				.findViewById(R.id.add_btn);
		add_btn.setBackgroundResource(R.drawable.daily_new_item);
		add_btn.setOnTouchListener(new Button.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event) {
			    if(event.getAction() == MotionEvent.ACTION_DOWN){   
			                    v.setBackgroundResource(R.drawable.daily_new_itemchange);   
			                }   
			                else if(event.getAction() == MotionEvent.ACTION_UP){   
			                    v.setBackgroundResource(R.drawable.daily_new_item); 
			                } 
			    return false;
			   }
			  });
			add_lay.setVisibility(View.VISIBLE);
			add_lay.setOnClickListener(addOnLister);
			add_btn.setOnClickListener(addOnLister);
	}

	OnClickListener addOnLister = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getActivity(),DailyOnlineAct.class);
			startActivityForResult(intent, 1);
		}
	};
	@Override
	public void onPause() {
		if (add_lay != null)
			add_lay.setVisibility(View.GONE);
		super.onPause();
	}

}
