package com.epsmart.wzdp.activity.dailyoffice.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import Decoder.BASE64Decoder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.RequestParamConfig;
import com.epsmart.wzdp.activity.dailyoffice.DailyActivity;
import com.epsmart.wzdp.activity.dailyoffice.DailyCommonAct;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReply;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReplyAdapter;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReplyHandler;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReplyPro;
import com.epsmart.wzdp.activity.dailyoffice.fragment.handler.UIHelper;
import com.epsmart.wzdp.bean.RequestPram;
import com.epsmart.wzdp.common.StringUtils;
import com.epsmart.wzdp.http.BaseHttpModule;
import com.epsmart.wzdp.http.ModuleResponseProcessor;
import com.epsmart.wzdp.http.request.BaseRequest.RequestType;
import com.epsmart.wzdp.http.request.RequestAction;
import com.epsmart.wzdp.http.response.model.StatusEntity;
import com.epsmart.wzdp.http.xml.handler.DefaultSaxHandler;
import com.epsmart.wzdp.view.PullToRefreshListView;

public class DailyReplyThemeAct extends DailyCommonAct {

	private DailyReplyAdapter replyadapter;
	private List<DailyReply> listData = new ArrayList<DailyReply>();
	private PullToRefreshListView lvNews;
	private View dyReply_footer;
	private int dyReplySumData;
	private TextView dyReply_foot_more;
	private ProgressBar dyReply_foot_progress;
	private int PAGE_SIZE = 30;
	private int action;
	public String req = null;
	EditText replay_contact = null;
	private List<DailyReply> list = new ArrayList<DailyReply>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.daily_reply);
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
			 Log.i("", "....................>>onkeydown");
			 Intent intent=new Intent(activity,DailyActivity.class);
				activity.setResult(1,intent);
				activity.finish();
		 }
		return super.onKeyDown(keyCode, event);
	}




	public void Init() {
		ImageView post_img = (ImageView) findViewById(R.id.daily_post_img);// 发帖人头像
		TextView post_name = (TextView) findViewById(R.id.daily_post_name);// 发帖人姓名
		TextView post_theme = (TextView) findViewById(R.id.daily_post_theme);// 发帖主题
		TextView post_content = (TextView) findViewById(R.id.daily_post_content);// 发帖内容
		TextView post_time = (TextView) findViewById(R.id.daily_post_time);// 发帖时间
		LinearLayout detail_lay = (LinearLayout) findViewById(R.id.detail_lay);// 详情布局
		replay_contact = (EditText) findViewById(R.id.daily_replay_contact);// 回复内容
		Button comment_btn = (Button) findViewById(R.id.daily_comment_btn);// 评论按钮

		String img = getIntent().getStringExtra("subpersionimg");
		if (TextUtils.isEmpty(img)) {
			post_img.setBackgroundResource(R.drawable.wm_icon_admin_normal);
		} else {
			Bitmap bm = stringToBitmap(img);
			post_img.setImageBitmap(parseRound(bm, 60));
		}
		post_name.setText(getIntent().getStringExtra("subpersionname"));
		post_theme.setText(getIntent().getStringExtra("subtitle"));
		post_time.setText(getIntent().getStringExtra("subtime"));
		post_content.setText(getIntent().getStringExtra("subcontent"));

		comment_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(arg0.getWindowToken(), 0);
				req = replay_contact.getText().toString();
				 RequestPram requestPram = new RequestPram();
				 loadDataComment(requestPram,req);
				 replay_contact.getText().clear(); 
			}
		});
		
		detail_lay.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.putExtra("persion", getIntent().getStringExtra("subpersionname"));
				intent.putExtra("theme", getIntent().getStringExtra("subtitle"));
				intent.putExtra("time", getIntent().getStringExtra("subtime"));
				intent.putExtra("content", getIntent().getStringExtra("subcontent"));
				intent.putExtra("subper", getIntent().getStringExtra("subper"));
				intent.setClass(DailyReplyThemeAct.this, DailyOnlineDeAct.class);
				startActivity(intent);
				
			}
		});

	}

	/*
	 * 评论请求
	 */
	public void loadDataComment(RequestPram requestPram,String req) {
		RequestAction requestAction = new RequestAction();
		requestAction.reset();
		showModuleProgressDialog("提示", "数据请求中请稍后...");
		 requestPram.param = req;
		requestPram.bizId = 1004;
		requestPram.password = getIntent().getStringExtra("SUBID");
		requestPram.pluginId = 119;
		requestPram.userName = appContext.user.getUid();
		requestPram.methodName = RequestParamConfig.submitReplay;
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new DefaultSaxHandler(),
				new ProcessResponseP(), RequestType.THRIFT);
	}

	/**
	 * 字符串转图片格式
	 * 
	 * @return
	 */
	public Bitmap stringToBitmap(String imagStr) {
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = null;
		try {
			bytes = decoder.decodeBuffer(imagStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(bytes==null){
			return null;
		}
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

	}

	public Bitmap parseRound(Bitmap bitmap, int pixels) {
		Bitmap output = null;
		output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	public void loadData(int pageNub) {
		requestAction = new RequestAction();
		RequestPram requestPram = new RequestPram();
		requestPram.param = getIntent().getStringExtra("SUBID");
		requestPram.methodName = RequestParamConfig.getReplayList;
		requestPram.pluginId = pageNub;
		requestPram.userName = appContext.user.getUid();
		requestAction.setReqPram(requestPram);
		httpModule.executeRequest(requestAction, new DailyReplyHandler(),
				new ProcessResponse(), RequestType.THRIFT);
	}

	class ProcessResponse implements ModuleResponseProcessor {
		@Override
		public void processResponse(BaseHttpModule httpModule, Object parseObj) {
			Message msg = new Message();
			if (parseObj instanceof DailyReplyPro) {

				list = ((DailyReplyPro) parseObj).list;
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
			System.out.println("..........parseOb>>>>>>>>>>>>>>>"+parseObj);
			if (parseObj instanceof StatusEntity) {
				pHandler.obtainMessage(1, parseObj).sendToTarget();
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
			default:
				break;
			}
		}
	}
	
	private void initNewsListView() {
		replyadapter = new DailyReplyAdapter(this, listData,
				R.layout.daily_reply_pull);
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
	}

	private void handleLvData(int what, Object obj, int actiontype) {
		switch (actiontype) {
		case UIHelper.LISTVIEW_ACTION_INIT:// 数据初始化
		case UIHelper.LISTVIEW_ACTION_REFRESH:// 下来刷新
			listData.clear();// 先清除原有数据
			listData.addAll((Collection<? extends com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReply>) obj);
			break;
		case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
			break;
		case UIHelper.LISTVIEW_ACTION_SCROLL: // 加载更多
			dyReplySumData += what;
			if (listData.size() > 0) {
				listData.addAll((Collection<? extends com.epsmart.wzdp.activity.dailyoffice.fragment.handler.DailyReply>) obj);
			}
			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
