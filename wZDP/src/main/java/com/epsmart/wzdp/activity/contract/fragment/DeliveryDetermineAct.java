package com.epsmart.wzdp.activity.contract.fragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.dailyoffice.DailyCommonAct;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.UIHelper;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.RequestXmlHelp;
import com.epsmart.wzdp.common.StringUtils;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.view.PullToRefreshListView;
import com.epsmart.wzdp.widget.time.JudgeDate;
import com.epsmart.wzdp.widget.time.ScreenInfo;
import com.epsmart.wzdp.widget.time.WheelMain;

/*
 *   施工单位 处理发货计划流程 确认或者拒绝
 */

public class DeliveryDetermineAct extends DailyCommonAct {

	private DeliveryDeterAdapter replyadapter;
	private List<DeliveryDeter> listData = new ArrayList<DeliveryDeter>();
	private PullToRefreshListView lvNews;
	private View dyReply_footer;
	private int dyReplySumData;
	private TextView dyReply_foot_more;
	private ProgressBar dyReply_foot_progress;
	private int PAGE_SIZE = 30;
	private int action;
	public String req = null;
	EditText replay_contact = null;
	private List<DeliveryDeter> list = new ArrayList<DeliveryDeter>();
	private TextView ganth,sydw,vendor,tax,zhongl,jhzczl;
	private String ganth1,sydw1,vendor1,tax1,zhongl1,jhzczl1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delivery_determine);
		Init();
		initNewsListView();
		if (listData.isEmpty()) {
			action = UIHelper.LISTVIEW_ACTION_INIT;
			loadData(0);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if(keyCode==KeyEvent.KEYCODE_BACK){
		 }
		return super.onKeyDown(keyCode, event);
	}

	public void Init() {
		ganth = (TextView) findViewById(R.id.ganth);
		sydw = (TextView) findViewById(R.id.sydw);
		vendor = (TextView) findViewById(R.id.vendor);
		tax = (TextView) findViewById(R.id.tax);
		zhongl = (TextView) findViewById(R.id.zhongl);
		jhzczl = (TextView) findViewById(R.id.jhzczl);

	}

	public void loadData(int pageNub) {
		requestAction = new RequestAction();
		RequestPram requestPram = new RequestPram();
		requestPram.param = getIntent().getStringExtra("reqParam");
		requestPram.methodName = RequestParamConfig.DelplanDownload;
		requestPram.pluginId = pageNub;
		requestPram.userName = appContext.user.getUid();
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new DeliveryDeterHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			Message msg = new Message();
				if(parseObj.toString() !=null){
				ganth1 = ((DeliveryDeterPro) parseObj).ganth;
				sydw1 =  ((DeliveryDeterPro) parseObj).sydw;
				vendor1 = ((DeliveryDeterPro) parseObj).vendor;
				tax1 = ((DeliveryDeterPro) parseObj).tax;
				zhongl1 = ((DeliveryDeterPro) parseObj).zhongl;
				jhzczl1 = ((DeliveryDeterPro) parseObj).jhzczl;
				list = ((DeliveryDeterPro) parseObj).list;
				msg.what = list.size();
				msg.obj = list;
				msg.arg1 = action;
				mHandler.sendMessage(msg);
			}
			if (parseObj instanceof StatusEntity) {
				msg.what = -1;
				msg.obj = "服务异常";
				mHandler.sendMessage(msg);
			}
		}
	}

	protected FlowHandler mHandler = new FlowHandler();

	public class FlowHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what >= 0) { // 解析的返回数据的总记录数据
				// 返回的数据需要装配adapter
				handleLvData(msg.what, msg.obj, msg.arg1);
				if (msg.what < PAGE_SIZE) {
					lvNews.setTag(UIHelper.LISTVIEW_DATA_FULL);
					replyadapter.notifyDataSetChanged();
					dyReply_foot_more.setText(R.string.load_full);// 已全部加载
				} else if (msg.what == PAGE_SIZE) {
					lvNews.setTag(UIHelper.LISTVIEW_DATA_MORE);
					replyadapter.notifyDataSetChanged();
					dyReply_foot_more.setText(R.string.load_more);// 更多
					// 特殊处理-热门动弹不能翻页
				}
				
				ganth.setText(ganth1);
				sydw.setText(sydw1);
				vendor.setText(vendor1);
				tax.setText(tax1);
				zhongl.setText(zhongl1);
				jhzczl.setText(jhzczl1);

			} else if (msg.what == -1) {
				// 有异常--显示加载出错 & 弹出错误消息
				lvNews.setTag(UIHelper.LISTVIEW_DATA_MORE);
				dyReply_foot_more.setText(R.string.load_error);
				// ((AppException) msg.obj).makeToast(Main.this);
			}
			// 没有任何数据返回的情况 msg.what==0 的时候
			if (replyadapter.getCount() == 0) {
				lvNews.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
				dyReply_foot_more.setText(R.string.load_empty);// 暂无数据
				ganth.setText(ganth1);
				sydw.setText(sydw1);
				vendor.setText(vendor1);
				tax.setText(tax1);
				zhongl.setText(zhongl1);
				jhzczl.setText(jhzczl1);
			}
			// 隐藏进度条
			dyReply_foot_progress.setVisibility(ProgressBar.GONE);

			if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) { // 刷新的操作
				lvNews.onRefreshComplete(getString(R.string.pull_to_refresh_update)// 最近更新
						+ new Date().toLocaleString());
				lvNews.setSelection(0);
			} else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
				lvNews.onRefreshComplete();
				lvNews.setSelection(0);
			}
		}
	}
	
	class ProcessResponseP implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			if (parseObj instanceof StatusEntity) {
				pHandler.obtainMessage(1, parseObj).sendToTarget();
			}else{

			}
		}
	}
	
	/** 处理网络异常等信息 **/
	private BaseHandler pHandler = new BaseHandler();

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
				listData.clear();
					action = UIHelper.LISTVIEW_ACTION_INIT;
					loadData(0);
				
				break;
			case 2:// 确认返回的
				StatusEntity see = (StatusEntity) msg.obj;
				Toast.makeText(activity, see.message + "", Toast.LENGTH_LONG)
						.show();
				loadData(0);
				break;
			default:
				break;
			}
		}
	}
	
	private void initNewsListView() {
		replyadapter = new DeliveryDeterAdapter(this, listData,
				R.layout.delivery_deter_pull);
		dyReply_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		dyReply_foot_more = (TextView) dyReply_footer
				.findViewById(R.id.listview_foot_more);
		dyReply_foot_progress = (ProgressBar) dyReply_footer
				.findViewById(R.id.listview_foot_progress);
		lvNews = (PullToRefreshListView) findViewById(R.id.list_act);
		lvNews.addFooterView(dyReply_footer);// 添加底部视图 必须在setAdapter前
		lvNews.setAdapter(replyadapter);
		lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvNews.onScrollStateChanged(view, scrollState);
				// 数据为空--不用继续下面代码了
				if (listData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(dyReply_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvNews.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvNews.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					dyReply_foot_more.setText(R.string.load_ing);
					dyReply_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex = dyReplySumData / PAGE_SIZE;
					action = UIHelper.LISTVIEW_ACTION_SCROLL;
					loadData(pageIndex);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvNews.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvNews.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				action = UIHelper.LISTVIEW_ACTION_REFRESH;
				loadData(0);

			}
		});
		
		replyadapter.setOnDelClick(replyadapter.new OnDelClickListener(){
			@Override
			public void onClick(View v) {
			String num=v.getTag().toString();
			switch (v.getId()){
				case R.id.delet_btn:
					dialog( num) ;
					break;
				case R.id.jjue_btn:
					jjueDialog(num);
					break;

			}


		}
	});
		
	}
	
	// 确认弹出框
		private void dialog(final String num) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage("是否确认");
			
			builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.dismiss();
				}
			}).setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					RequestPram requestPram=new RequestPram();
					requestPram.param = RequestXmlHelp.getCommonXML(new StringBuffer(getIntent().getStringExtra("reqP"))
							.append(RequestXmlHelp.getReqXML("type", "1")));
					sureSubmit(requestPram,num);
				}
			}).create().show();
			
		}

	 // 拒绝弹出框
	 Dialog dialog;
	private void jjueDialog(final String num){

		LayoutInflater inflater = LayoutInflater.from(activity);
		RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.jujue_dialog, null);

		dialog = new Dialog(activity);
		dialog.setTitle("   拒绝操作");
		dialog.setCancelable(false);
		dialog.show();
		dialog.getWindow().setContentView(layout);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		InputMethodManager imm = (InputMethodManager)
				getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软

		Button exit_cancel = (Button) layout.findViewById(R.id.exit_cancel);
		Button submit_btn = (Button) layout.findViewById(R.id.drop_out);

		final EditText reasion_et=(EditText)layout.findViewById(R.id.jujue_reasion);
		final EditText time_et=(EditText)layout.findViewById(R.id.jujue_time);
		time_et.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectDateNew(v);
			}
		});

		submit_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				RequestPram requestPram=new RequestPram();
				String param=getIntent().getStringExtra("reqP");
				String denialreasion=reasion_et.getText().toString();
				String plantime=time_et.getText().toString();
				// 添加 reasion   time//TODO
				StringBuffer stringBuffer=	new StringBuffer(param)
						.append(RequestXmlHelp.getReqXML("yy", denialreasion))
						.append(RequestXmlHelp.getReqXML("clsj", plantime))
				.append(RequestXmlHelp.getReqXML("type", "0"));
				requestPram.param = RequestXmlHelp.getCommonXML(stringBuffer);
				sureSubmit(requestPram, num);
			}
		});
		exit_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

	}




	private void selectDateNew(View v) {
		final EditText et = (EditText) v;
		et.setInputType(InputType.TYPE_NULL);

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:ss");
		LayoutInflater inflater = LayoutInflater.from(activity);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(activity);
		final WheelMain wheelMain = new WheelMain(timepickerview, false);
		wheelMain.screenheight = screenInfo.getHeight();
		// 获取当前时间
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String time = dateFormat.format(curDate);

		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd hh:ss")) {
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		new AlertDialog.Builder(activity).setTitle("选择时间")
				.setView(timepickerview)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						et.setText(wheelMain.getTime());
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();

	}

		/* 处理表单提交返回消息 */
		class DeleteSub implements ModuleResponseProcessor {
			@Override
			public void processResponse(BaseHttpModule httpModule, Object parseObj) {
				if (parseObj instanceof StatusEntity) {
					pHandler.obtainMessage(2, parseObj).sendToTarget();
				}
			}
		}
		
		protected void sureSubmit(RequestPram requestPram,String num) {
			RequestAction requestAction = new RequestAction();
			requestAction.reset();
			requestPram.bizId = 1004;
			requestPram.password = num;
			requestPram.pluginId = 119;
			requestPram.userName = appContext.user.getUid();
			requestPram.methodName = RequestParamConfig.deldeterupload;



			// type: 0 拒绝  1：确认
			requestAction.setReqPram(requestPram);
			subdelsub(requestPram);
		}
		
		
		protected void subdelsub(RequestPram reqPram) {
			showModuleProgressDialog("提示", "数据请求中请稍后...");
			RequestAction requestAction = new RequestAction();
			requestAction.reset();
			requestAction.setReqPram(reqPram);
			httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
					new DeleteSub(), RequestType.THRIFT);
		}

	private void handleLvData(int what, Object obj, int actiontype) {
		switch (actiontype) {
		case UIHelper.LISTVIEW_ACTION_INIT:// 数据初始化
		case UIHelper.LISTVIEW_ACTION_REFRESH:// 下来刷新
			listData.clear();// 先清除原有数据
			listData.addAll((Collection<? extends com.epsmart.wzdp.activity.contract.fragment.DeliveryDeter>) obj);
			break;
		case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			break;
		case UIHelper.LISTVIEW_ACTION_SCROLL: // 加载更多
			dyReplySumData += what;
			if (listData.size() > 0) {
				listData.addAll((Collection<? extends com.epsmart.wzdp.activity.contract.fragment.DeliveryDeter>) obj);
			}
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
